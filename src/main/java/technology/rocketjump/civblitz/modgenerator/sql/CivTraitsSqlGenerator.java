package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.Patch;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;
import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.ActOfGod;

import java.util.function.Predicate;

import static technology.rocketjump.civblitz.model.CardCategory.CivilizationAbility;
import static technology.rocketjump.civblitz.model.CardCategory.Power;

@Component
public class CivTraitsSqlGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		final String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();
		// For attaching extra modifiers, beyond the regular civ trait.
		final String uniqueCivTraitName = "TRAIT_CIVILIZATION_" + modName;
		final StringBuilder sqlBuilder = new StringBuilder();

		if (civInfo.selectedCards.stream().anyMatch(needsDedicatedAbility)) {
			registerNewCivTrait(sqlBuilder, uniqueCivTraitName, modName);
		}

		for (CardCategory cardCategory : CardCategory.mainCategories) {
			if (!cardCategory.equals(CardCategory.LeaderAbility)) {
				final Card card = civInfo.getCard(cardCategory);
				addCivTraitLine(sqlBuilder, card.getTraitType(), modName, card.getPatchSQL());
				if (card.getGrantsTraitType().isPresent()) {
					addCivTraitLine(sqlBuilder, card.getGrantsTraitType().get(), modName, card.getPatchSQL());
				}

				if (cardCategory.equals(CivilizationAbility)) {
					for (ActOfGod actOfGod : modHeader.actsOfGod) {
						actOfGod.applyToCivTrait(card.getTraitType(), modName, sqlBuilder);
					}
				}
			}
		}

		civInfo.selectedCards.stream()
				.filter(card -> card.getCardCategory().equals(Power))
				.forEach(powerCard -> {
					sqlBuilder.append("-- ").append(powerCard.getIdentifier()).append("\n");
					if (powerCard.getGrantsTraitType().isPresent()) {
						addCivTraitLine(sqlBuilder, powerCard.getGrantsTraitType().get(), modName, powerCard.getPatchSQL());
					}
					powerCard.getModifierIds().forEach(modifierId ->
							addTraitModifierLine(sqlBuilder, uniqueCivTraitName, modifierId)
					);
				});

		return sqlBuilder.toString();
	}

	private static final Predicate<Card> needsDedicatedAbility = (card) ->
			card.getCardCategory().equals(Power) ||
					(card.getPatchSQL() != null && card.getPatchSQL().needsDedicatedAbility());

	private void addTraitModifierLine(StringBuilder sqlBuilder, String civAbilityTraitType, String modifierId) {
		sqlBuilder.append("INSERT OR REPLACE INTO TraitModifiers (TraitType, ModifierId) VALUES ('")
				.append(civAbilityTraitType).append("', '").append(modifierId).append("');\n");
	}

	private void addCivTraitLine(StringBuilder sqlBuilder, String traitType, String modName, Patch patch) {
		sqlBuilder.append("INSERT OR REPLACE INTO CivilizationTraits (TraitType, CivilizationType) VALUES ('")
				.append(traitType).append("', 'CIVILIZATION_IMP_").append(modName).append("');\n");
		if (patch != null) {
			sqlBuilder.append(
					"--------------------------------------------------------------------------------------------------------------------------\n-- ")
					.append(traitType).append(" fix.\n");
			ST template = new ST(patch.getSqlTemplate());
			template.add("modName", modName);
			sqlBuilder.append(template.render());
			sqlBuilder.append(
					"\n--------------------------------------------------------------------------------------------------------------------------\n");
		}
	}

	private void registerNewCivTrait(StringBuilder sqlBuilder, String civTraitType, String modName) {
		sqlBuilder.append(ST.format("""
				-- Unique ability for attaching extra modifiers.
				INSERT OR REPLACE INTO Types (Type, Kind) VALUES ('<%1>', 'KIND_TRAIT');
				INSERT OR REPLACE INTO Traits (TraitType, InternalOnly) VALUES ('<%1>', 1);
				INSERT OR REPLACE INTO CivilizationTraits (TraitType, CivilizationType) VALUES ('<%1>', '<%2>');
				
				""", civTraitType, "CIVILIZATION_IMP_" + modName));
	}

	@Override
	public String getFilename() {
		return "CivTraits.sql";
	}
}

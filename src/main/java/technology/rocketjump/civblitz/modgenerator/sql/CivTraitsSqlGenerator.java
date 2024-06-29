package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;
import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.ActOfGod;

import static technology.rocketjump.civblitz.model.CardCategory.CivilizationAbility;
import static technology.rocketjump.civblitz.model.CardCategory.Power;

@Component
public class CivTraitsSqlGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		final String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();
		final StringBuilder sqlBuilder = new StringBuilder();

		for (CardCategory cardCategory : CardCategory.mainCategories) {
			if (!cardCategory.equals(CardCategory.LeaderAbility)) {
				final Card card = civInfo.getCard(cardCategory);
				addCivTraitLine(sqlBuilder, card.getTraitType(), modName);
				if (card.getGrantsTraitType().isPresent()) {
					addCivTraitLine(sqlBuilder, card.getGrantsTraitType().get(), modName);
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
						addCivTraitLine(sqlBuilder, powerCard.getGrantsTraitType().get(), modName);
					}
					if (!powerCard.getModifierIds().isEmpty()) {
						registerNewCivTrait(sqlBuilder, powerCard.getTraitType(), modName);
						powerCard.getModifierIds().forEach(modifierId ->
								addTraitModifierLine(sqlBuilder, powerCard.getTraitType(), modifierId)
						);
					}
				});

		return sqlBuilder.toString();
	}

	private void addCivTraitLine(StringBuilder sqlBuilder, String traitType, String modName) {
		sqlBuilder.append("INSERT OR REPLACE INTO CivilizationTraits (TraitType, CivilizationType) VALUES ('")
				.append(traitType).append("', 'CIVILIZATION_IMP_").append(modName).append("');\n");
	}

	private static final ST NEW_TRAIT_TEMPLATE = new ST("""
			INSERT OR IGNORE INTO Types (Type, Kind) VALUES ('<type>', 'KIND_TRAIT');
			INSERT OR IGNORE INTO Traits (TraitType, Name, Description)
			VALUES ('<type>', 'LOC_<type>_NAME', 'LOC_<type>_DESCRIPTION');
			INSERT OR REPLACE INTO CivilizationTraits (TraitType, CivilizationType) VALUES ('<type>', '<civType>');
			
			""");

	private void registerNewCivTrait(StringBuilder sqlBuilder, String civTraitType, String modName) {
		sqlBuilder.append(new ST(NEW_TRAIT_TEMPLATE)
				.add("type", civTraitType)
				.add("civType", "CIVILIZATION_IMP_" + modName)
				.render());
	}

	private void addTraitModifierLine(StringBuilder sqlBuilder, String civAbilityTraitType, String modifierId) {
		sqlBuilder.append("INSERT OR REPLACE INTO TraitModifiers (TraitType, ModifierId) VALUES ('")
				.append(civAbilityTraitType).append("', '").append(modifierId).append("');\n");
	}

	@Override
	public String getFilename() {
		return "CivTraits.sql";
	}
}

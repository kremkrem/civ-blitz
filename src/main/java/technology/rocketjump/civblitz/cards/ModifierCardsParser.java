package technology.rocketjump.civblitz.cards;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static technology.rocketjump.civblitz.matches.objectives.ObjectiveDefinitionParser.toIdentifier;
import static technology.rocketjump.civblitz.model.CardRarity.Common;

@Component
public class ModifierCardsParser {

	private final Logger logger = LoggerFactory.getLogger(ModifierCardsParser.class);

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public ModifierCardsParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	public void parsePowerCards(String modifierCardsCsv) throws IOException {
		try (Reader input = new StringReader(modifierCardsCsv)) {
			CSVParser parsed = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				parsePowerCard(record).ifPresent(sourceDataRepo::add);
			}
		}
	}

	public void parseUpgradeCards(String modifierCardsCsv) throws IOException {
		try (Reader input = new StringReader(modifierCardsCsv)) {
			CSVParser parsed = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				parseUpgradeCard(record).ifPresent(sourceDataRepo::add);
			}
		}
	}

	private Optional<Card> parseSharedCardData(CSVRecord record) {
		String id = record.get("Id");
		String name = record.get("Name");
		String active = record.get("IsActive");
		CardRarity rarity = EnumUtils.getEnum(CardRarity.class, record.get("Rarity"));
		if (rarity == null) {
			logger.error("Did not recognise rarity: {} for card {}", record.get("Rarity"), name);
			return Optional.empty();
		} else if (rarity.equals(Common)) {
			logger.error("Upgraded cards must be higher rarity than common");
			return Optional.empty();
		}
		if (name.isBlank() || "FALSE".equalsIgnoreCase(active)) {
			return Optional.empty();
		}
		if (id.isEmpty()) {
			id = toIdentifier(name);
		}
		Card card = new Card();
		card.setIdentifier(id);
		card.setEnhancedCardName(name);
		card.setEnhancedCardDescription(record.get("Description"));
		card.setRarity(rarity);
		return Optional.of(card);
	}

	private Optional<Card> parsePowerCard(CSVRecord record) {
		return parseSharedCardData(record).map(data -> {
			String belongsTo = record.get("BelongsTo");
			Card civAbilityCard = sourceDataRepo.civAbilityCardByFriendlyName.get(belongsTo);
			if (civAbilityCard == null) {
				logger.error("Can not find civ ability by friendly name {}", belongsTo);
				return null;
			}

			String addsTraitType = record.get("AddsTraitType");
			String addsModifierIds = record.get("AddsModifierIds");
			List<String> modifierIds = List.of(addsModifierIds.split("\\|"));
			if (addsTraitType.isBlank() && modifierIds.isEmpty()) {
				logger.error("No trait type or modifier IDs granted by power {}", data.getEnhancedCardName());
				return null;
			}

			Card powerCard = new Card(civAbilityCard);
			powerCard.setIdentifier(data.getIdentifier());
			powerCard.setBaseCardDescription("");
			powerCard.setEnhancedCardName(data.getEnhancedCardName());
			powerCard.setEnhancedCardDescription(data.getEnhancedCardDescription());
			powerCard.setCardCategory(CardCategory.Power);
			powerCard.setSuperCategory(SuperCategory.Power);
			powerCard.setRarity(data.getRarity());
			if (!addsTraitType.isBlank()) {
				powerCard.setGrantsTraitType(Optional.of(addsTraitType));
			}
			powerCard.getModifierIds().clear();
			powerCard.getModifierIds().addAll(modifierIds.stream().filter(m -> !m.isBlank()).toList());
			if (!powerCard.getModifierIds().isEmpty()) {
				powerCard.setTraitType("TRAIT_IMP_" + powerCard.getIdentifier());
			}
			String localizationGlue = record.get("LocalizationGlue");
			String nameSql = "INSERT OR IGNORE INTO LocalizedText(Tag, Language, Text) VALUES ('LOC_"
					+ powerCard.getTraitType()
					+ "_NAME', 'en_US', '" + data.getEnhancedCardName() + "');";
			String descSql = "";
			if (localizationGlue != null && !localizationGlue.isBlank()) {
				String[] terms = localizationGlue.split(",");
				String sourceLocalization = terms[0].trim();
				boolean isValid = !sourceLocalization.isBlank() && terms.length >= 2;
				for (int i = 1; i < terms.length; i++) {
					try {
						 Integer.parseInt(terms[i]);
					} catch (NumberFormatException e) {
						isValid = false;
					}
				}

				if (isValid) {
					descSql = "INSERT OR IGNORE INTO LocalizedText(Tag, Language, Text)\n"
							+ "SELECT 'LOC_" + powerCard.getTraitType() + "_DESCRIPTION', Language, "
							+ "TRIM(group_concat(Part, ' '))\n"
							+ "FROM LocalizedTextSplit\n"
							+ "WHERE Tag = '" + sourceLocalization
							+ "' AND Idx IN (" + Arrays.stream(terms).skip(1).collect(Collectors.joining(", "))
							+ ")\n"
							+ "GROUP BY Language;";
				}
			}
			powerCard.setLocalizationSQL(nameSql + "\n" + descSql);

			return powerCard;
		});
	}

	private Optional<Card> parseUpgradeCard(CSVRecord record) {
		return parseSharedCardData(record).map(data -> {
			String upgradesCard = record.get("UpgradesCard");
			Card baseCard = sourceDataRepo.getBaseCardByTraitType(upgradesCard);
			if (baseCard == null) {
				logger.error("Can not find base card {} for upgrade {}", upgradesCard, data.getEnhancedCardName());
				return null;
			}
			if (data.getRarity().equals(Common)) {
				logger.error("Upgrade cards must be higher rarity than common");
				return null;
			}

			String identifier = data.getRarity().name() + "_" + upgradesCard;
			if (sourceDataRepo.getByIdentifier(identifier) != null) {
				logger.error("Duplicate card with identifier {}", identifier);
				return null;
			}

			String gameplaySql = record.get("SQL");
			String localizationSql = record.get("LocalisationSQL");

			Card upgradedCard = new Card(baseCard);
			upgradedCard.setIdentifier(identifier);
			upgradedCard.setEnhancedCardName(data.getEnhancedCardName());
			upgradedCard.setEnhancedCardDescription(data.getEnhancedCardDescription());
			upgradedCard.setSuperCategory(SuperCategory.Upgrade);
			upgradedCard.setRarity(data.getRarity());
			upgradedCard.setGameplaySQL(String.join(
					"\n--\n",
					Stream.of(baseCard.getGameplaySQL(), gameplaySql)
							.filter(str -> str != null && !str.isBlank())
							.toList()));
			upgradedCard.setLocalizationSQL(localizationSql);

			return upgradedCard;
		});
	}
}

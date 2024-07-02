package technology.rocketjump.civblitz.modgenerator.sql;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;
import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.ActOfGod;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class LocaleGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		return getLocalizedTextSplitSql(civInfo.selectedCards.stream()) + getCivLocale(civInfo);
	}

	@Override
	public String getFileContents(ModHeader modHeader, List<ModdedCivInfo> civs) {
		StringBuilder builder = new StringBuilder();
		builder.append(getLocalizedTextSplitSql(civs.stream().flatMap(civInfo -> civInfo.selectedCards.stream())));
		for (ModdedCivInfo civ : civs) {
			builder.append(getCivLocale(civ));
		}
		for (ActOfGod actOfGod : modHeader.actsOfGod) {
			actOfGod.applyLocalisationChanges(builder);
		}
		return builder.toString();
	}

	@Override
	public String getFilename() {
		return "Locale.sql";
	}

	public static final String LOCALIZED_TEXT_SPLIT_SQL = """
			CREATE TEMP TABLE IF NOT EXISTS LocalizedTextSplit AS
			WITH RECURSIVE Split AS (SELECT Tag, Language, Text AS Part, Text AS Remainder, 0 AS Idx
			                         FROM LocalizedText
			                         WHERE Tag LIKE 'LOC_%_DESCRIPTION'
			                         UNION ALL
			                         SELECT Tag,
			                                Language,
			                                TRIM(SUBSTR(Remainder, 1, INSTR(Remainder, '.') + INSTR(Remainder, '。'))) AS Part,
			                                SUBSTR(Remainder, INSTR(Remainder, '.') + INSTR(Remainder, '。') + 1) AS Remainder,
			                                idx + 1                                                              AS Idx
			                         FROM Split
			                         WHERE INSTR(Remainder, '.') + INSTR(Remainder, '。') > 0)
			SELECT *
			FROM Split
			ORDER BY Tag, Language, Idx;
			
			""";

	private String getLocalizedTextSplitSql(Stream<Card> cards) {
		return cards.map(Card::getLocalizationSQL)
				.filter(Objects::nonNull)
				.anyMatch(sql -> sql.contains("LocalizedTextSplit")) ? LOCALIZED_TEXT_SPLIT_SQL : "";
	}

	private String getCivLocale(ModdedCivInfo civInfo) {
		StringBuilder sqlBuilder = new StringBuilder();

		String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();

		Card civCard = civInfo.getCard(CardCategory.CivilizationAbility);
		Card leaderCard = civInfo.getCard(CardCategory.LeaderAbility);

		sqlBuilder.append("""
						INSERT OR REPLACE INTO LocalizedText
						(Tag, Language, Text)
						VALUES
						('LOC_LEADER_IMP_""")
				.append(modName)
				.append("', 'en_US', '")
				.append(leaderCard.getBaseCardName())
				.append(" (")
				.append(civCard.getBaseCardName())
				.append(")');");

		for (Card card : civInfo.selectedCards) {
			if (!StringUtils.isEmpty(card.getLocalizationSQL())) {
				sqlBuilder.append("\n\n-- ")
						.append(card.getIdentifier())
						.append("\n")
						.append(card.getLocalizationSQL());
			}
		}

		return sqlBuilder.toString();
	}
}

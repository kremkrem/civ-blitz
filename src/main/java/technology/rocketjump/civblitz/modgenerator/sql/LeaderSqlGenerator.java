package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModData;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;
import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.ActOfGod;

import java.util.List;

@Component
public class LeaderSqlGenerator extends BlitzFileGenerator {

	private final SourceDataRepo sourceDataRepo;

	public LeaderSqlGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModData modData, ModdedCivInfo civInfo) {
		StringBuilder sqlBuilder = new StringBuilder();

		String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();

		Card leaderCard = civInfo.getCard(CardCategory.LeaderAbility);
		String leaderType = leaderCard.getLeaderType().orElseThrow();

		List<String> bgLeaders =
				sourceDataRepo.allLeadersByCivType.get(civInfo.getCard(CardCategory.CivilizationAbility)
						.getCivilizationType());
		String bgLeaderType =
				(bgLeaders == null || bgLeaders.isEmpty()) ? leaderType : bgLeaders.get(0).replace("LEADER_", "");

		// TODO replace leaders insert with VALUES statement

		sqlBuilder.append(ST.format("""

				INSERT INTO Types (Type, Kind)
				VALUES('LEADER_IMP_<%1>', 'KIND_LEADER');

				INSERT INTO Leaders
				(LeaderType, Name, InheritFrom, SceneLayers, Sex, SameSexPercentage)
				SELECT 'LEADER_IMP_<%1>', 'LOC_LEADER_IMP_<%1>', Leaders.InheritFrom, Leaders.SceneLayers, Leaders.Sex, Leaders.SameSexPercentage
				FROM Leaders
				WHERE Leaders.LeaderType = '<%2>';

				INSERT INTO DuplicateLeaders
				(LeaderType, OtherLeaderType)
				VALUES ('<%2>', 'LEADER_IMP_<%1>');

				--------------------------------------------------------------------------------------------------------------------------
				-- DiplomacyInfo
				--------------------------------------------------------------------------------------------------------------------------
				INSERT INTO DiplomacyInfo (Type, BackgroundImage)
				VALUES ('LEADER_IMP_<%1>', '<%3>');
				--------------------------------------------------------------------------------------------------------------------------
				-- LeaderTraits
				--------------------------------------------------------------------------------------------------------------------------
				""", modName, leaderType, bgLeaderType));

		addLeaderTrait(sqlBuilder, leaderCard.getTraitType(), modName);

		if (civInfo.getCard(CardCategory.CivilizationAbility).getTraitType().equals("TRAIT_CIVILIZATION_MAORI_MANA")) {
			sqlBuilder.append("INSERT OR REPLACE INTO Leaders_XP2 (LeaderType, OceanStart) ")
					.append("VALUES ('LEADER_IMP_").append(modName).append("', 1);\n");
		}

		for (ActOfGod actOfGod : modData.actsOfGod()) {
			actOfGod.applyToLeaderTrait(leaderCard.getTraitType(), modName, sqlBuilder);
		}

		if (leaderCard.getGrantsTraitType().isPresent()) {
			// NOTE(sztodwa): applying fixes to "Grants" traits should be implemented.
			addLeaderTrait(sqlBuilder, leaderCard.getGrantsTraitType().get(), modName);
		}

		sqlBuilder.append(
				"""
				--------------------------------------------------------------------------------------------------------------------------
				-- LeaderQuotes
				--------------------------------------------------------------------------------------------------------------------------
				INSERT INTO LeaderQuotes (LeaderType, Quote)
				SELECT 'LEADER_IMP_""").append(modName).append("""
				', Quote
				FROM LeaderQuotes
				WHERE LeaderType = '""").append(leaderType).append("""
				';
				--------------------------------------------------------------------------------------------------------------------------
				-- HistoricalAgendas
				--------------------------------------------------------------------------------------------------------------------------
				INSERT INTO HistoricalAgendas (LeaderType, AgendaType)
				SELECT 'LEADER_IMP_""").append(modName).append("""
				', AgendaType
				FROM HistoricalAgendas
				WHERE LeaderType = '""").append(leaderType).append("""
				';
				--------------------------------------------------------------------------------------------------------------------------
				-- AgendaPreferredLeaders
				--------------------------------------------------------------------------------------------------------------------------
				INSERT INTO AgendaPreferredLeaders (LeaderType, AgendaType)
				SELECT 'LEADER_IMP_""").append(modName).append("""
				', AgendaType
				FROM AgendaPreferredLeaders
				WHERE LeaderType = '""").append(leaderType).append("""
				';
				--------------------------------------------------------------------------------------------------------------------------
				-- FavoredReligions
				--------------------------------------------------------------------------------------------------------------------------
				INSERT OR REPLACE INTO FavoredReligions (LeaderType, ReligionType)
				SELECT 'LEADER_IMP_""").append(modName).append("""
				', ReligionType
				FROM FavoredReligions
				WHERE LeaderType = '""").append(leaderType).append("""
				';
				--==========================================================================================================================
				-- LEADERS: LOADING INFO
				--==========================================================================================================================
				INSERT INTO LoadingInfo (LeaderType, BackgroundImage, ForegroundImage, LeaderText)
				""");
				if (leaderType.contains("_ALT")) {
					sqlBuilder.append("SELECT 'LEADER_IMP_").append(modName).append("""
							', BackgroundImage, ForegroundImage, LeaderText
							FROM LoadingInfo
							WHERE LeaderType = '""").append(leaderType).append("';");
				} else {
					sqlBuilder.append("VALUES ('LEADER_IMP_").append(modName).append("', '")
							.append(leaderType).append("_BACKGROUND', '")
							.append(leaderType).append("_NEUTRAL', 'LOC_LOADING_INFO_")
							.append(leaderType).append("');");
				}

		return sqlBuilder.toString();
	}

	private void addLeaderTrait(StringBuilder sqlBuilder, String traitType, String modName) {
		sqlBuilder.append("INSERT INTO LeaderTraits (LeaderType, TraitType)\n" +
				"VALUES ('LEADER_IMP_").append(modName).append("', '").append(traitType).append("');\n");
	}

	@Override
	public String getFileContents(ModData modData, List<ModdedCivInfo> civs) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.getFileContents(modData, civs));
		for (ActOfGod actOfGod : modData.actsOfGod()) {
			actOfGod.applyGlobalChanges(builder);
		}
		return builder.toString();
	}

	@Override
	public String getFilename() {
		return "Leader.sql";
	}
}

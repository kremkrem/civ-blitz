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

import java.util.List;

@Component
public class LeaderSqlGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		StringBuilder sqlBuilder = new StringBuilder();

		String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();

		Card leaderCard = civInfo.getCard(CardCategory.LeaderAbility);
		String leaderType = leaderCard.getLeaderType().orElseThrow();

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
				""",
				modName,
				civInfo.getCard(CardCategory.LeaderAbility).getLeaderType().orElseThrow(),
				civInfo.getCard(CardCategory.LeaderAbility).getLeaderType().orElseThrow().replace("LEADER_", "")));

		if (leaderCard.getPatchSQL() != null && leaderCard.getPatchSQL().needsDedicatedAbility()) {
			sqlBuilder.append(ST.format("""
				-- Unique ability for attaching extra modifiers.
				INSERT OR REPLACE INTO Types (Type, Kind) VALUES ('TRAIT_<%1>', 'KIND_TRAIT');
				INSERT OR REPLACE INTO Traits (TraitType, InternalOnly) VALUES ('TRAIT_<%1>', 1);
				INSERT OR REPLACE INTO LeaderTraits (LeaderType, TraitType) VALUES ('<%1>', 'TRAIT_<%1>');
				
				""", "LEADER_IMP_" + modName));
		}

		addLeaderTrait(sqlBuilder, leaderCard.getTraitType(), modName, leaderCard.getPatchSQL());

		if (civInfo.getCard(CardCategory.CivilizationAbility).getTraitType().equals("TRAIT_CIVILIZATION_MAORI_MANA")) {
			sqlBuilder.append("INSERT OR REPLACE INTO Leaders_XP2 (LeaderType, OceanStart) ")
					.append("VALUES ('LEADER_IMP_").append(modName).append("', 1);\n");
		}

		for (ActOfGod actOfGod : modHeader.actsOfGod) {
			actOfGod.applyToLeaderTrait(leaderCard.getTraitType(), modName, sqlBuilder);
		}

		if (leaderCard.getGrantsTraitType().isPresent()) {
			// NOTE(sztodwa): applying fixes to "Grants" traits should be implemented.
			addLeaderTrait(sqlBuilder, leaderCard.getGrantsTraitType().get(), modName, null);
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

	private void addLeaderTrait(StringBuilder sqlBuilder, String traitType, String modName, Patch patch) {
		sqlBuilder.append("INSERT INTO LeaderTraits (LeaderType, TraitType)\n" +
				"VALUES ('LEADER_IMP_").append(modName).append("', '").append(traitType).append("');\n");
		if (patch != null) {
			sqlBuilder.append(
							"--------------------------------------------------------------------------------------------------------------------------\n-- ")
					.append(traitType).append(" fix.\n");
			ST template = new ST(patch.getSqlTemplate());
			template.add("modName", modName);
			sqlBuilder.append(template.render()).append("\n");
		}
	}

	@Override
	public String getFileContents(ModHeader modHeader, List<ModdedCivInfo> civs) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.getFileContents(modHeader, civs));
		for (ActOfGod actOfGod : modHeader.actsOfGod) {
			actOfGod.applyGlobalChanges(builder);
		}
		return builder.toString();
	}

	@Override
	public String getFilename() {
		return "Leader.sql";
	}
}

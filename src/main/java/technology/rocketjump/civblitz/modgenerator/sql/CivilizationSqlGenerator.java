package technology.rocketjump.civblitz.modgenerator.sql;

import org.apache.commons.csv.CSVRecord;
import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModData;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import static technology.rocketjump.civblitz.model.CardCategory.CivilizationAbility;

@Component
public class CivilizationSqlGenerator extends BlitzFileGenerator {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public CivilizationSqlGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModData modData, ModdedCivInfo civInfo) {
		Card civAbilityCard = civInfo.getCard(CivilizationAbility);
		StringBuilder civSql = new StringBuilder(getCivilizationSql(
				ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase(),
				civAbilityCard.getCivilizationType(),
				civInfo.startBiasCivType
		));
		String civModName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();
		for (Card card : civInfo.selectedCards) {
			if (!StringUtils.isEmpty(card.getGameplaySQL())) {
				ST template = new ST(card.getGameplaySQL());
				template.add("modName", civModName);
				civSql.append("\n\n-- ")
						.append(card.getIdentifier())
						.append("\n")
						.append(template.render())
						.append("\n");
			}
		}
		return civSql.toString();
	}

	private String getCivilizationSql(String modName, String namesCivType, String startBiasCivType) {
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("INSERT OR REPLACE INTO Types (Type, Kind)\n" +
				"VALUES\t('CIVILIZATION_IMP_").append(modName).append("""
				',\t'KIND_CIVILIZATION');

				""");

		CSVRecord civRecord = sourceDataRepo.civilizationCsvRecordsByCivType.get(namesCivType);
		String ethnicity = civRecord.get("Ethnicity");
		if (ethnicity == null) {
			ethnicity = "";
		}

		sqlBuilder.append("""
						INSERT OR REPLACE INTO Civilizations
						(CivilizationType, Name, Description, Adjective, StartingCivilizationLevelType, RandomCityNameDepth, Ethnicity)
						VALUES
						('CIVILIZATION_IMP_""")
				.append(modName)
				.append("', " + "'")
				.append(civRecord.get("Name"))
				.append("', '")
				.append(civRecord.get("Description"))
				.append("', '")
				.append(civRecord.get("Adjective"))
				.append("', 'CIVILIZATION_LEVEL_FULL_CIV', 10, '")
				.append(ethnicity)
				.append("');\n\n");

		// TODO replace CivLeaders insert with a VALUES statement

		String capitalName = sourceDataRepo.capitalNamesByCivType.get(namesCivType);

		sqlBuilder.append("""

						INSERT OR REPLACE INTO CivilizationLeaders
						(CivilizationType, LeaderType, CapitalName)
						VALUES ('CIVILIZATION_IMP_""")
				.append(modName)
				.append("', 'LEADER_IMP_")
				.append(modName)
				.append("', '")
				.append(capitalName)
				.append("');\n\n");

		// Names aren't as important, see how fallback works

		sqlBuilder.append("""

				INSERT OR REPLACE INTO CityNames (CivilizationType, CityName)
				SELECT 'CIVILIZATION_IMP_""").append(modName).append("""
				', CityNames.CityName
				from CityNames
				where CityNames.CivilizationType = '""").append(namesCivType).append("""
				';

				INSERT OR REPLACE INTO CivilizationCitizenNames
				(CivilizationType, CitizenName,\tFemale, Modern)
				SELECT 'CIVILIZATION_IMP_""").append(modName).append("""
				', CivilizationCitizenNames.CitizenName, CivilizationCitizenNames.Female, CivilizationCitizenNames.Modern
				FROM CivilizationCitizenNames
				WHERE CivilizationCitizenNames.CivilizationType = '""").append(namesCivType).append("""
				';

				INSERT OR REPLACE INTO CivilizationInfo
				(CivilizationType, Header, Caption, SortIndex)
				SELECT 'CIVILIZATION_IMP_""").append(modName).append("""
				', CivilizationInfo.Header, CivilizationInfo.Caption, CivilizationInfo.SortIndex
				FROM CivilizationInfo
				WHERE CivilizationInfo.CivilizationType = '""").append(namesCivType).append("';\n\n");

		// TODO collate start biases and replace with VALUES insert

		sqlBuilder.append("""
				INSERT OR REPLACE INTO StartBiasFeatures
				(CivilizationType, FeatureType, Tier)
				SELECT 'CIVILIZATION_IMP_""").append(modName).append("""
				', StartBiasFeatures.FeatureType, StartBiasFeatures.Tier
				FROM StartBiasFeatures
				WHERE StartBiasFeatures.CivilizationType = '""").append(startBiasCivType).append("""
				';

				INSERT OR REPLACE INTO StartBiasResources
				(CivilizationType, ResourceType, Tier)
				SELECT 'CIVILIZATION_IMP_""").append(modName).append("""
				', StartBiasResources.ResourceType, StartBiasResources.Tier
				FROM StartBiasResources
				WHERE StartBiasResources.CivilizationType = '""").append(startBiasCivType).append("""
				';

				INSERT OR REPLACE INTO StartBiasRivers
				(CivilizationType, Tier)
				SELECT 'CIVILIZATION_IMP_""").append(modName).append("""
				', StartBiasRivers.Tier
				FROM StartBiasRivers
				WHERE StartBiasRivers.CivilizationType = '""").append(startBiasCivType).append("""
				';

				INSERT OR REPLACE INTO StartBiasTerrains
				(CivilizationType, TerrainType, Tier)
				SELECT 'CIVILIZATION_IMP_""").append(modName).append("""
				', StartBiasTerrains.TerrainType, StartBiasTerrains.Tier
				FROM StartBiasTerrains
				WHERE StartBiasTerrains.CivilizationType = '""").append(startBiasCivType).append("';\n");

		return sqlBuilder.toString();
	}

	@Override
	public String getFilename() {
		return "Civilization.sql";
	}
}

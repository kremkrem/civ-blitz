package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModData;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

@Component
public class GeographySqlGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModData modData, ModdedCivInfo civInfo) {
		return getGeographySql(ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase(),
				civInfo.getCard(CardCategory.CivilizationAbility).getCivilizationType());
	}

	public String getGeographySql(String modName, String geographyCivType) {
		return "INSERT OR REPLACE INTO NamedRiverCivilizations (NamedRiverType, CivilizationType)\n" +
				"SELECT NamedRiverCivilizations.NamedRiverType, 'CIVILIZATION_IMP_"
				+ modName.toUpperCase() + "'\n" +
				"FROM NamedRiverCivilizations\n" +
				"WHERE NamedRiverCivilizations.CivilizationType = '"
				+ geographyCivType + "';\n" +
				"\n" +
				"INSERT OR REPLACE INTO NamedMountainCivilizations (NamedMountainType, CivilizationType)\n" +
				"SELECT NamedMountainCivilizations.NamedMountainType, 'CIVILIZATION_IMP_"
				+ modName.toUpperCase() + "'\n" +
				"FROM NamedMountainCivilizations\n" +
				"WHERE NamedMountainCivilizations.CivilizationType = '"
				+ geographyCivType + "';\n" +
				"\n" +
				"INSERT OR REPLACE INTO NamedVolcanoCivilizations (NamedVolcanoType, CivilizationType)\n" +
				"SELECT NamedVolcanoCivilizations.NamedVolcanoType, 'CIVILIZATION_IMP_"
				+ modName.toUpperCase() + "'\n" +
				"FROM NamedVolcanoCivilizations\n" +
				"WHERE NamedVolcanoCivilizations.CivilizationType = '"
				+ geographyCivType + "';\n" +
				"\n" +
				"INSERT OR REPLACE INTO NamedDesertCivilizations (NamedDesertType, CivilizationType)\n" +
				"SELECT NamedDesertCivilizations.NamedDesertType, 'CIVILIZATION_IMP_"
				+ modName.toUpperCase() + "'\n" +
				"FROM NamedDesertCivilizations\n" +
				"WHERE NamedDesertCivilizations.CivilizationType = '"
				+ geographyCivType + "';\n" +
				"\n" +
				"INSERT OR REPLACE INTO NamedLakeCivilizations (NamedLakeType, CivilizationType)\n" +
				"SELECT NamedLakeCivilizations.NamedLakeType, 'CIVILIZATION_IMP_"
				+ modName.toUpperCase() + "'\n" +
				"FROM NamedLakeCivilizations\n" +
				"WHERE NamedLakeCivilizations.CivilizationType = '"
				+ geographyCivType + "';\n" +
				"\n" +
				"INSERT OR REPLACE INTO NamedSeaCivilizations (NamedSeaType, CivilizationType)\n" +
				"SELECT NamedSeaCivilizations.NamedSeaType, 'CIVILIZATION_IMP_"
				+ modName.toUpperCase() + "'\n" +
				"FROM NamedSeaCivilizations\n" +
				"WHERE NamedSeaCivilizations.CivilizationType = '"
				+ geographyCivType + "';\n";
	}

	@Override
	public String getFilename() {
		return "Geography.sql";
	}
}

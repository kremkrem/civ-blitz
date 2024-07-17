package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.IconAtlasEntry;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModData;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

@Component
public class IconsSqlGenerator extends BlitzFileGenerator {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public IconsSqlGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModData modData, ModdedCivInfo civInfo) {
		String name = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();

		IconAtlasEntry civIconEntry =
				sourceDataRepo.getIconAtlasEntry(civInfo.getCard(CardCategory.CivilizationAbility)
				.getCivilizationType());
		IconAtlasEntry leaderIconEntry = sourceDataRepo.getIconAtlasEntry(civInfo.getCard(CardCategory.LeaderAbility)
				.getLeaderType()
				.orElseThrow());

		return "INSERT OR REPLACE INTO IconDefinitions\n" +
				"(Name, Atlas,  'Index')\n" +
				"VALUES\t('ICON_CIVILIZATION_IMP_"
				+ name
				+ "', '"
				+ civIconEntry.atlasName
				+ "', "
				+ civIconEntry.index
				+ "),\n" +
				"\t\t('ICON_LEADER_IMP_"
				+ name
				+ "', '"
				+ leaderIconEntry.atlasName
				+ "', "
				+ leaderIconEntry.index
				+ ");\n";
	}

	@Override
	public String getFilename() {
		return "Icons.sql";
	}
}

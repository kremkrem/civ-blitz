package technology.rocketjump.civblitz.modgenerator.artdef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.LeaderArtData;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;
import java.util.Optional;

@Component
public class LeadersArtDefGenerator extends BlitzFileGenerator {
	SourceDataRepo sourceDataRepo;

	@Autowired
	LeadersArtDefGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		return getFileContents(modHeader, List.of(civInfo));
	}

	@Override
	public String getFilename() {
		return "ArtDefs/Leaders.artdef";
	}

	@Override
	public String getFileContents(ModHeader modHeader, List<ModdedCivInfo> civs) {
		StringBuilder builder = new StringBuilder("""
				<?xml version="1.0" encoding="UTF-8" ?>
				<AssetObjects..ArtDefSet>
					<m_Version>
						<major>4</major>
						<minor>0</minor>
						<build>762</build>
						<revision>531</revision>
					</m_Version>
					<m_TemplateName text="Leaders"/>
					<m_RootCollections>
						<Element>
							<m_CollectionName text="Leaders"/>
							<m_ReplaceMergedCollectionElements>false</m_ReplaceMergedCollectionElements>
				""");
		for (ModdedCivInfo civInfo : civs) {
			builder.append(getLeaderElementXml(civInfo));
		}
		builder.append("""
						</Element>
					</m_RootCollections>
				</AssetObjects..ArtDefSet>""");
		return builder.toString();
	}

	private String getLeaderElementXml(ModdedCivInfo civInfo) {
		Optional<String> leaderType = civInfo.getCard(CardCategory.LeaderAbility).getLeaderType();
		if (leaderType.isPresent()) {
			String moddedCivName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();
			LeaderArtData data = sourceDataRepo.leaderArtDataByLeaderType.get(leaderType.get());
			return new ST("""
								<Element>
									<m_Fields>
										<m_Values>
											<Element class="AssetObjects..BLPEntryValue">
												<m_EntryName text="$LeaderEntryName$"/>
												<m_XLPClass text="Leader"/>
												<m_XLPPath text="$LeaderXLPPath$"/>
												<m_BLPPackage text="$LeaderBLPPackage$"/>
												<m_LibraryName text="Leader"/>
												<m_ParamName text="Leader_BLP_Entry"/>
											</Element>
											<Element class="AssetObjects..BLPEntryValue">
												<m_EntryName text="$LightrigEntryName$"/>
												<m_XLPClass text="LeaderLighting"/>
												<m_XLPPath text="Leader_LightRigs.xlp"/>
												<m_BLPPackage text="leaders/light_rigs"/>
												<m_LibraryName text="LeaderLighting"/>
												<m_ParamName text="Leader_Lightrig_BLP_Entry"/>
											</Element>
											<Element class="AssetObjects..BLPEntryValue">
												<m_EntryName text="$ColorKeyEntryName$"/>
												<m_XLPClass text="ColorKey"/>
												<m_XLPPath text="ColorKeys.xlp"/>
												<m_BLPPackage text="ColorKeys"/>
												<m_LibraryName text="ColorKey"/>
												<m_ParamName text="Leader_ColorKey_BLP_Entry"/>
											</Element>
											<Element class="AssetObjects..BLPEntryValue">
												<m_EntryName text=""/>
												<m_XLPClass text="Leader"/>
												<m_XLPPath text=""/>
												<m_BLPPackage text=""/>
												<m_LibraryName text="Leader"/>
												<m_ParamName text="Leader_Background_BLP_Entry"/>
											</Element>
											<Element class="AssetObjects..StringValue">
												<m_Value text=""/>
												<m_ParamName text="Leader_Background_Animation_State"/>
											</Element>
											<Element class="AssetObjects..StringValue">
												<m_Value text="$Audio$"/>
												<m_ParamName text="Audio"/>
											</Element>
										</m_Values>
									</m_Fields>
									<m_ChildCollections/>
									<m_Name text="LEADER_IMP_$modName$"/>
									<m_AppendMergedParameterCollections>false</m_AppendMergedParameterCollections>
								</Element>
					""", '$', '$')
					.add("LeaderEntryName", data.getLeaderEntry().getEntryName())
					.add("LeaderXLPPath", data.getLeaderEntry().getXLPPath())
					.add("LeaderBLPPackage", data.getLeaderEntry().getBLPPackage())
					.add("LightrigEntryName", data.getLightrigEntry().getEntryName())
					.add("ColorKeyEntryName", data.getColorKeyEntry().getEntryName())
					.add("Audio", data.getAudioEntry())
					.add("modName", moddedCivName)
					.render();
		} else {
			return "";
		}
	}
}

package technology.rocketjump.civblitz.modgenerator.artdef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;
import java.util.Optional;

@Component
public class FallbackLeadersArtDefGenerator extends BlitzFileGenerator {
	private final SourceDataRepo sourceDataRepo;

	@Autowired
	FallbackLeadersArtDefGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		return getFileContents(modHeader, List.of(civInfo));
	}

	@Override
	public String getFileContents(ModHeader modHeader, List<ModdedCivInfo> civs) {
		StringBuilder builder = new StringBuilder("""
				<AssetObjects..ArtDefSet>
					<m_Version>
						<major>4</major>
						<minor>0</minor>
						<build>762</build>
						<revision>531</revision>
					</m_Version>
					<m_TemplateName text="LeaderFallback"/>
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

	@Override
	public String getFilename() {
		return "ArtDefs/FallbackLeaders.artdef";
	}

	private String getLeaderElementXml(ModdedCivInfo civInfo) {
		Optional<String> leaderType = civInfo.getCard(CardCategory.LeaderAbility).getLeaderType();
		if (leaderType.isPresent()) {
			String moddedCivName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();
			String fallbackLeader = sourceDataRepo.fallbackLeaderByLeaderType.get(leaderType.get());
			return new ST("""
								<Element>
									<m_Fields>
										<m_Values/>
									</m_Fields>
									<m_ChildCollections>
										<Element>
											<m_CollectionName text="Animations"/>
											<m_ReplaceMergedCollectionElements>false</m_ReplaceMergedCollectionElements>
											<Element>
												<m_Fields>
													<m_Values>
														<Element class="AssetObjects..BLPEntryValue">
															<m_EntryName text="$fallbackEntryName$"/>
															<m_XLPClass text="LeaderFallback"/>
															<m_XLPPath text="leaderfallbackimages.xlp"/>
															<m_BLPPackage text="LeaderFallbackImages"/>
															<m_LibraryName text="LeaderFallback"/>
															<m_ParamName text="BLP Entry"/>
														</Element>
													</m_Values>
												</m_Fields>
												<m_ChildCollections/>
												<m_Name text="DEFAULT"/>
												<m_AppendMergedParameterCollections>false</m_AppendMergedParameterCollections>
											</Element>
										</Element>
									</m_ChildCollections>
									<m_Name text="LEADER_IMP_$modName$"/>
									<m_AppendMergedParameterCollections>false</m_AppendMergedParameterCollections>
								</Element>
					""", '$', '$')
					.add("fallbackEntryName", fallbackLeader)
					.add("modName", moddedCivName)
					.render();
		} else {
			return "";
		}
	}
}

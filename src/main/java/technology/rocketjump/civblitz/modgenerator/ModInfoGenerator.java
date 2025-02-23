package technology.rocketjump.civblitz.modgenerator;

import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.time.Instant;
import java.util.List;

@Component
public class ModInfoGenerator extends BlitzFileGenerator {

	private String modName = "UNSET";

	@Override
	public String getFileContents(ModData modData, ModdedCivInfo civInfo) {
		modName = modData.header().modName;
		return getModInfoContent(modData.header());
	}

	@Override
	public String getFileContents(ModData modData, List<ModdedCivInfo> civs) {
		return getFileContents(modData, (ModdedCivInfo) null);
	}

	public String getModInfoContent(ModHeader modHeader) {

		return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
				"<Mod id=\"" + modHeader.id + "\" version=\"1\">\n" +
				"  <Properties>\n" +
				"    <Name>Civ-Blitz " + modHeader.prettyModName + "</Name>\n" +
				"    <Description>" + modHeader.modDescription + "</Description>\n" +
				"    <Created>" + Instant.now().getEpochSecond() + "</Created>\n" +
				"    <Teaser>" + modHeader.modDescription + "</Teaser>\n" +
				"""
				     <Authors>Zsinj, SztoDwa</Authors>
				     <CompatibleVersions>2.0</CompatibleVersions>
				  </Properties>
				  <Dependencies>
					<Mod id="4873eb62-8ccc-4574-b784-dda455e74e68" title="Expansion: Gathering Storm" />
				  </Dependencies>
				  <References>
					<Mod id="E3F53C61-371C-440B-96CE-077D318B36C0" title="DLC: Australia Civilization Pack" />
					<Mod id="02A8BDDE-67EA-4D38-9540-26E685E3156E" title="DLC: Aztec Civilization Pack" />
					<Mod id="1F367231-A040-4793-BDBB-088816853683" title="DLC: Khmer and Indonesia Civilization Pack" />
					<Mod id="643EA320-8E1A-4CF1-A01C-00D88DDD131A" title="DLC: Nubia Civilization Pack" />
					<Mod id="E2749E9A-8056-45CD-901B-C368C8E83DEB" title="DLC: Persia and Macedon Civilization Pack" />
					<Mod id="3809975F-263F-40A2-A747-8BFB171D821A" title="DLC: Poland Civilization Pack" />
					<Mod id="2F6E858A-28EF-46B3-BEAC-B985E52E9BC1" title="DLC: Vikings Content" />
					<Mod id="8424840C-92EF-4426-A9B4-B4E0CB818049" title="LOC_BABYLON_MOD_TITLE" />
					<Mod id="A1100FC4-70F2-4129-AC27-2A65A685ED08" title="LOC_BYZANTIUM_GAUL_MOD_TITLE" />
					<Mod id="CE5876CD-6900-46D1-9C9C-8DBA1F28872E" title="LOC_CATHERINE_DE_MEDICI_MOD_TITLE" />
					<Mod id="1B394FE9-23DC-4868-8F0A-5220CB8FB427" title="LOC_ETHIOPIA_MOD_TITLE" />
					<Mod id="1B28771A-C749-434B-9053-D1380C553DE9" title="LOC_EXPANSION1_MOD_TITLE" />
					<Mod id="9DE86512-DE1A-400D-8C0A-AB46EBBF76B9" title="LOC_GRANCOLOMBIA_MAYA_MOD_TITLE" />
					<Mod id="A3F42CD4-6C3E-4F5A-BC81-BE29E0C0B87C" title="LOC_KUBLAIKHAN_VIETNAM_MOD_TITLE" />
					<Mod id="FFDF4E79-DEE2-47BB-919B-F5739106627A" title="LOC_PORTUGAL_MOD_TITLE" />
					<Mod id="113D9459-0A3B-4FCB-A49C-483F40303575" title="LOC_TEDDY_ROOSEVELT_MOD_TITLE" />
					<Mod id="9ED63236-617C-45A6-BB70-8CB6B0BE8ED2" title="LOC_JULIUS_CAESAR_MOD_TITLE" />
					<Mod id="7A66DB58-B354-4061-8C80-95B638DD6F6C" title="LOC_GREAT_NEGOTIATORS_MOD_TITLE" />
					<Mod id="F48213B4-56F5-45DD-92F7-AC78E49BDA49" title="LOC_GREAT_WARLORDS_MOD_TITLE"/>
					<Mod id="7D27831B-BAA6-4A8B-A39C-94243BAD3F47" title="LOC_RULERS_OF_CHINA_MOD_TITLE"/>
					<Mod id="82AE6F24-930F-4640-833C-FCDFD4845757" title="LOC_RULERS_OF_THE_SAHARA_MOD_TITLE"/>
					<Mod id="249D9276-0832-48E4-B370-14531FA4E33C" title="LOC_GREAT_BUILDERS_MOD_TITLE"/>
					<Mod id="258EF3CA-890B-4863-8A52-982822EFF7BD" title="LOC_RULERS_OF_ENGLAND_MOD_TITLE"/>
				  </References>
				  <FrontEndActions>
					<UpdateDatabase id="Update_DB_Frontend">
					  <File>Configuration.sql</File>
					</UpdateDatabase>
					<UpdateColors id="Colors">
					  <File>Colors.sql</File>
					</UpdateColors>
					<UpdateText id="Text">
					  <File>Locale.sql</File>
					</UpdateText>
					<UpdateIcons id="Icons">
					  <File>Icons.sql</File>
					</UpdateIcons>
				  </FrontEndActions>
				  <InGameActions>
					<UpdateDatabase id="Update_InGame_DB">
					  <File>Civilization.sql</File>
					  <File>CivTraits.sql</File>
					  <File>Geography.sql</File>
					  <File>Leader.sql</File>
					</UpdateDatabase>
					<ReplaceUIScript id="LeaderScene_layeredBg">
					  <Properties>
					    <LuaContext>LeaderScene</LuaContext>
					    <LuaReplace>lua/LeaderScene_layeredBg.lua</LuaReplace>
					  </Properties>
					</ReplaceUIScript>
					<ImportFiles id="ImportUI">
					  <File>lua/LeaderScene_layeredBg.lua</File>
					</ImportFiles>
					<UpdateColors id="Colors">
					  <File>Colors.sql</File>
					</UpdateColors>
					<UpdateText id="Text">
					  <File>Locale.sql</File>
					</UpdateText>
					<UpdateIcons id="Icons">
					  <File>Icons.sql</File>
					</UpdateIcons>
					<UpdateArt id="ArtUpdate">
					  <Properties>
						<LoadOrder>9000</LoadOrder>
					  </Properties>
					  <File>Art.dep</File>
					  <File>DLC_Indones_Khmer.dep</File>
					  <File>DLC_Maced_Pers.dep</File>
					  <File>DLC_Nubia_Amanit.dep</File>
					  <File>Exp1.dep</File>
					</UpdateArt>
				  </InGameActions>
				  <Files>
				  	<File>Art.dep</File>
					<File>Civilization.sql</File>
					<File>CivTraits.sql</File>
					<File>Colors.sql</File>
					<File>Configuration.sql</File>
					<File>Geography.sql</File>
					<File>Icons.sql</File>
					<File>Leader.sql</File>
					<File>Locale.sql</File>
					<File>ArtDefs/Civilizations.artdef</File>
					<File>ArtDefs/Cultures.artdef</File>
					<File>ArtDefs/Landmarks.artdef</File>
					<File>ArtDefs/FallbackLeaders.artdef</File>
					<File>ArtDefs/Leaders.artdef</File>
					<File>DLC_Indones_Khmer.dep</File>
					<File>DLC_Maced_Pers.dep</File>
					<File>DLC_Nubia_Amanit.dep</File>
					<File>Exp1.dep</File>
					<File>lua/LeaderScene_layeredBg.lua</File>
				  </Files>
				</Mod>""";
	}

	@Override
	public String getFilename() {
		return "CivBlitz" + ModgenStringUtils.NormalizeString(modName) + ".modinfo";
	}
}

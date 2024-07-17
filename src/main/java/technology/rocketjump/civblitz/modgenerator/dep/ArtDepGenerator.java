package technology.rocketjump.civblitz.modgenerator.dep;

import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModData;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;
import java.util.UUID;

@Component
public class ArtDepGenerator extends BlitzFileGenerator {
	@Override
	public String getFileContents(ModData modData, ModdedCivInfo civInfo) {
		// Using ModHeader as an identifying header for this GameDependency.
		String depName = modData.header().modName + "Art";
		ModHeader fileHeader = new ModHeader(depName, "", UUID.nameUUIDFromBytes(depName.getBytes()));
		return new ST("""
				<?xml version="1.0" encoding="UTF-8"?>
				<AssetObjects..GameDependencyData>
				  <ID>
				    <name text="CivBlitz$modName$"/>
				    <id text="$UUID$"/>
				  </ID>
				  <RequiredGameArtIDs/>
				  <SystemDependencies>
				    <Element>
				      <ConsumerName text="Audio"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				        <Element text="Leaders.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Civilizations"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Cultures"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Farms"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				        <Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="TileBase"/>
				        <Element text="CityBuildings"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Features"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				        <Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Improvements"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="IndirectGrid"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Landmarks"/>
				      <ArtDefDependencyPaths>
				        <Element text="Civilizations.artdef"/>
				        <Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="CityBuildings"/>
				        <Element text="TileBase"/>
				        <Element text="RouteDecalMaterial"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="LeaderFallback"/>
				      <ArtDefDependencyPaths>
				        <Element text="FallbackLeaders.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="LeaderFallback"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="LeaderLighting"/>
				      <ArtDefDependencyPaths/>
				      <LibraryDependencies>
				        <Element text="LeaderLighting"/>
				        <Element text="ColorKey"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Leaders"/>
				      <ArtDefDependencyPaths>
				        <Element text="Leaders.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="Leader"/>
				        <Element text="LeaderLighting"/>
				        <Element text="ColorKey"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Resources"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="StrategicView_Properties"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="StrategicView_Route"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="StrategicView_Route"/>
				        <Element text="StrategicView_DirectedAsset"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="StrategicView_Sprite"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="StrategicView_Sprite"/>
				        <Element text="StrategicView_DirectedAsset"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="StrategicView_TerrainType"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="StrategicView_TerrainBlend"/>
				        <Element text="StrategicView_TerrainBlendCorners"/>
				        <Element text="StrategicView_TerrainType"/>
				        <Element text="StrategicView_DirectedAsset"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="StrategicView_TerrainBlend"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="StrategicView_TerrainBlend"/>
				        <Element text="StrategicView_DirectedAsset"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="StrategicView_TerrainBlendCorners"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="StrategicView_TerrainBlendCorners"/>
				        <Element text="StrategicView_DirectedAsset"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="StrategicView_Translate"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Terrain"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="TerrainAsset"/>
				        <Element text="TerrainElement"/>
				        <Element text="TerrainMaterial"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Terrains"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Units"/>
				      <ArtDefDependencyPaths>
				        <Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="Unit"/>
				        <Element text="VFX"/>
				        <Element text="Light"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="UnitSimulation"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="VFX"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="VFX"/>
				        <Element text="Light"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="WorldView_Translate"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Civilizations.artdef"/>
				      	<Element text="Cultures.artdef"/>
				      	<Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>false</LoadsLibraries>
				    </Element>
				  </SystemDependencies>
				  <ArtDefDependencies>
				    <Element>
				      <ArtDefPath text="Civilizations.artdef"/>
				      <ArtDefDependencyPaths/>
				    </Element>
				    <Element>
				      <ArtDefPath text="Cultures.artdef"/>
				      <ArtDefDependencyPaths/>
				    </Element>
				    <Element>
				      <ArtDefPath text="Landmarks.artdef"/>
				      <ArtDefDependencyPaths>
				        <Element text="Civilizations.artdef"/>
				        <Element text="Cultures.artdef"/>
				        <Element text="Landmarks.artdef"/>
				      </ArtDefDependencyPaths>
				    </Element>
				    <Element>
				      <ArtDefPath text="Leaders.artdef"/>
				      <ArtDefDependencyPaths/>
				    </Element>
				    <Element>
				      <ArtDefPath text="FallbackLeaders.artdef"/>
				      <ArtDefDependencyPaths/>
				    </Element>
				  </ArtDefDependencies>
				</AssetObjects..GameDependencyData>
				""", '$', '$')
				.add("modName", fileHeader.modName)
				.add("UUID", fileHeader.id.toString())
				.render();
	}

	@Override
	public String getFilename() {
		return "Art.dep";
	}

	@Override
	public String getFileContents(ModData modData, List<ModdedCivInfo> civs) {
		return getFileContents(modData, (ModdedCivInfo) null);
	}
}

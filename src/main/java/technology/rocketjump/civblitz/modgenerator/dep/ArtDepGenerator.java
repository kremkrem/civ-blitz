package technology.rocketjump.civblitz.modgenerator.dep;

import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;
import java.util.UUID;

@Component
public class ArtDepGenerator extends BlitzFileGenerator {
	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		// Using ModHeader as an identifying header for this GameDependency.
		String depName = modHeader.modName + "Art";
		ModHeader fileHeader = new ModHeader(depName, "", UUID.nameUUIDFromBytes(depName.getBytes()), List.of());
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
				      	<Element text="Cultures.artdef"/>
				        <Element text="Leaders.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Cultures"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Cultures.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Improvements"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Cultures.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Landmarks"/>
				      <ArtDefDependencyPaths>
				        <Element text="Cultures.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="CityBuildings"/>
				        <Element text="TileBase"/>
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
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="Units"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Cultures.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies>
				        <Element text="Unit"/>
				      </LibraryDependencies>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				    <Element>
				      <ConsumerName text="WorldView_Translate"/>
				      <ArtDefDependencyPaths>
				      	<Element text="Cultures.artdef"/>
				      </ArtDefDependencyPaths>
				      <LibraryDependencies/>
				      <LoadsLibraries>true</LoadsLibraries>
				    </Element>
				  </SystemDependencies>
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
	public String getFileContents(ModHeader modHeader, List<ModdedCivInfo> civs) {
		return getFileContents(modHeader, (ModdedCivInfo) null);
	}
}

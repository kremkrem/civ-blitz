package technology.rocketjump.civblitz.modgenerator.artdef;

import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

@Component
public class LeadersArtDefGenerator extends BlitzFileGenerator {
	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		return "";
	}

	@Override
	public String getFilename() {
		return "ArtDefs/Leader.artdef";
	}
}

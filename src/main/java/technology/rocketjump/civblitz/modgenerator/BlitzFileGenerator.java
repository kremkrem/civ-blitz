package technology.rocketjump.civblitz.modgenerator;

import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;

public abstract class BlitzFileGenerator {

	public abstract String getFileContents(ModData modData, ModdedCivInfo civInfo);

	public abstract String getFilename();

	public String getFileContents(ModData modData, List<ModdedCivInfo> civs) {
		StringBuilder builder = new StringBuilder();
		for (ModdedCivInfo civ : civs) {
			builder.append(getFileContents(modData, civ)).append("\n\n");
		}
		return builder.toString();
	}

}

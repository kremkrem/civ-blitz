package technology.rocketjump.civblitz.modgenerator.model;

import technology.rocketjump.civblitz.modgenerator.ModgenStringUtils;

import java.util.UUID;

public class ModHeader {

	public final String prettyModName;
	public final String modName;
	public final String modDescription;
	public final UUID id;

	public ModHeader(String modName, String modDescription, UUID id) {
		this.prettyModName = modName;
		this.modName = ModgenStringUtils.NormalizeString(modName);
		this.modDescription = modDescription;
		this.id = id;
	}

}

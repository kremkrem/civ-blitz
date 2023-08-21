package technology.rocketjump.civblitz.modgenerator.model;

import technology.rocketjump.civblitz.modgenerator.ModgenStringUtils;
import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.ActOfGod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModHeader {

	public final String prettyModName;
	public final String modName;
	public final String modDescription;
	public final UUID id;
	public final List<ActOfGod> actsOfGod = new ArrayList<>();

	public ModHeader(String modName, String modDescription, UUID id, List<ActOfGod> actsOfGod) {
		this.prettyModName = modName;
		this.modName = ModgenStringUtils.NormalizeString(modName);
		this.modDescription = modDescription;
		this.id = id;
		this.actsOfGod.addAll(actsOfGod);
	}

}

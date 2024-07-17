package technology.rocketjump.civblitz.modgenerator;

import technology.rocketjump.civblitz.mapgen.MapSettings;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.ActOfGod;

import javax.annotation.Nullable;
import java.util.List;

public record ModData(ModHeader header, List<ActOfGod> actsOfGod, @Nullable MapSettings mapSettings) {
	public ModData(ModHeader header) {
		this(header, List.of());
	}

	public ModData(ModHeader header, List<ActOfGod> actsOfGod) {
		this(header, List.copyOf(actsOfGod), null);
	}
}

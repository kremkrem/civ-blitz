package technology.rocketjump.civblitz.matches.guilds;

import technology.rocketjump.civblitz.mapgen.StartEra;

import java.util.Map;
import java.util.Objects;

public record GuildDefinition(String guildName,
							  String guildId,
							  String description,
							  String category,
							  boolean active,
							  Map<StartEra, Boolean> availableInEra) {

	public static final GuildDefinition NULL_GUILD = new GuildDefinition("Unknown", "UNKNOWN",
			"This guild is missing", "Unknown", false, Map.of());

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GuildDefinition that = (GuildDefinition) o;
		return guildId.equals(that.guildId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(guildId);
	}
}

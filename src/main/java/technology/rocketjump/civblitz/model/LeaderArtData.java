package technology.rocketjump.civblitz.model;

import technology.rocketjump.civblitz.modgenerator.artdef.xml.BLPEntryValue;

public record LeaderArtData(BLPEntryValue leaderEntry,
							BLPEntryValue lightrigEntry,
							BLPEntryValue colorKeyEntry,
							String audioEntry) {
}

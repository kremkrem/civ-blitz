package technology.rocketjump.civblitz.model;

import technology.rocketjump.civblitz.modgenerator.artdef.xml.BLPEntryValue;

public record LandmarkData(String collection,
						   String name,
						   String subjectName,
						   String era,
						   BLPEntryValue asset,
						   String entryName,
						   boolean flatten,
						   String traitType) {
}

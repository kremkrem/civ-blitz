package technology.rocketjump.civblitz.model;

public class LeaderArtData {
	public static class BLPEntry {
		final private String entryName;
		final private String XLPPath;
		final private String BLPPackage;

		public BLPEntry(String entryName, String XLPPath, String BLPPackage) {
			this.entryName = entryName;
			this.XLPPath = XLPPath;
			this.BLPPackage = BLPPackage;
		}

		public String getEntryName() {
			return entryName;
		}

		public String getXLPPath() {
			return XLPPath;
		}

		public String getBLPPackage() {
			return BLPPackage;
		}
	}
	final private BLPEntry leaderEntry;
	final private BLPEntry lightrigEntry;
	final private BLPEntry colorKeyEntry;
	final private String audioEntry;

	public LeaderArtData(BLPEntry leaderEntry, BLPEntry lightrigEntry, BLPEntry colorKeyEntry, String audioEntry) {
		this.leaderEntry = leaderEntry;
		this.lightrigEntry = lightrigEntry;
		this.colorKeyEntry = colorKeyEntry;
		this.audioEntry = audioEntry;
	}

	public BLPEntry getLeaderEntry() {
		return leaderEntry;
	}

	public BLPEntry getLightrigEntry() {
		return lightrigEntry;
	}

	public BLPEntry getColorKeyEntry() {
		return colorKeyEntry;
	}

	public String getAudioEntry() {
		return audioEntry;
	}
}

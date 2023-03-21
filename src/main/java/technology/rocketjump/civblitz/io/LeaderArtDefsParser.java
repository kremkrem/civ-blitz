package technology.rocketjump.civblitz.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.LeaderArtData;
import technology.rocketjump.civblitz.model.SourceDataRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@Component
public class LeaderArtDefsParser {
	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public LeaderArtDefsParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	public void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(input);
			for (CSVRecord record : parsed.getRecords()) {
				sourceDataRepo.leaderArtDataByLeaderType.put(record.get("LeaderType"),
						new LeaderArtData(
								new LeaderArtData.BLPEntry(record.get("LeaderEntryName"), record.get("LeaderXLPPath"),
										record.get("LeaderBLPPackage")),
								new LeaderArtData.BLPEntry(record.get("LightrigEntryName"), record.get(
										"LightrigXLPPath"), ""),
								new LeaderArtData.BLPEntry(record.get("ColorKeyEntryName"), "", ""),
								record.get("Audio")));
			}
		}
	}
}

package technology.rocketjump.civblitz.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.LeaderArtData;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.BLPEntryValue;

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
						new LeaderArtData(new BLPEntryValue("Leader_BLP_Entry",
								record.get("LeaderEntryName"),
								"Leader",
								record.get("LeaderXLPPath"),
								record.get("LeaderBLPPackage"),
								"Leader"),
								new BLPEntryValue("Leader_Lightrig_BLP_Entry",
										record.get("LightrigEntryName"),
										"LeaderLighting",
										record.get("LightrigXLPPath"),
										"leaders/light_rigs",
										"LeaderLighting"),
								new BLPEntryValue("Leader_ColorKey_BLP_Entry",
										record.get("ColorKeyEntryName"),
										"ColorKey",
										"ColorKeys.xlp",
										"ColorKeys",
										"ColorKey"),
								record.get("Audio")));
			}
		}
	}
}

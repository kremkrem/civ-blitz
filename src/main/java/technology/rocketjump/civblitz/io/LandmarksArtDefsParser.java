package technology.rocketjump.civblitz.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.LandmarkData;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.BLPEntryValue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@Component
public class LandmarksArtDefsParser {
	private final SourceDataRepo sourceDataRepo;


	@Autowired
	public LandmarksArtDefsParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	public void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(input);
			sourceDataRepo.landmarkArtdefs.addAll(parsed.getRecords()
					.stream()
					.map(record -> new LandmarkData(record.get("Collection"),
							record.get("Name"),
							record.get("SubjectName"),
							record.get("Era"),
							new BLPEntryValue("Asset",
									record.get("Asset"),
									"TileBase",
									record.get("XLPPath"),
									record.get("BLPPackage"),
									"TileBase"),
							record.get("EntryName"),
							Boolean.parseBoolean(record.get("Flatten")),
							record.get("TraitType")))
					.toList());
		}
	}
}

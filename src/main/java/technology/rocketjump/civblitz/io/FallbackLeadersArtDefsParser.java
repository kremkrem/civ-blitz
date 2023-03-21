package technology.rocketjump.civblitz.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.SourceDataRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@Component
public class FallbackLeadersArtDefsParser {
	SourceDataRepo sourceDataRepo;

	@Autowired
	FallbackLeadersArtDefsParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(input);
			for (CSVRecord record : parsed.getRecords()) {
				sourceDataRepo.fallbackLeaderByLeaderType.put(record.get("LeaderType"), record.get("FallbackLeader"));
			}
		}
	}
}

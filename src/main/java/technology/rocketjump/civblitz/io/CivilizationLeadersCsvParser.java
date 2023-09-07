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
import java.util.ArrayList;

@Component
public class CivilizationLeadersCsvParser {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public CivilizationLeadersCsvParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	public void parse(String ciCsvContent) throws IOException {
		try (Reader input = new StringReader(ciCsvContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String leaderType = record.get("LeaderType");
				String civType = record.get("CivilizationType");
				String capitalName = record.get("CapitalName");

				sourceDataRepo.capitalNamesByCivType.put(civType, capitalName);
				sourceDataRepo.allLeadersByCivType.compute(civType, (k, l) -> {
					if (l == null) {
						l = new ArrayList<>();
					}
					l.add(leaderType);
					return l;
				});
			}
		}
	}
}

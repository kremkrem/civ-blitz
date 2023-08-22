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
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CivilizationCulturesParser {
	SourceDataRepo sourceDataRepo;

	@Autowired
	public CivilizationCulturesParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(input);
			List<CSVRecord> records = parsed.getRecords();
			sourceDataRepo.civilizationToCultures.putAll(records.stream()
					.filter(r -> "Culture".equals(r.get("CultureType")))
					.collect(Collectors.groupingBy(r -> r.get("CivilizationType"),
							Collectors.mapping(r -> r.get("Culture"), Collectors.toList()))));
			sourceDataRepo.civilizationToUnitCultures.putAll(records.stream()
					.filter(r -> "UnitCulture".equals(r.get("CultureType")))
					.collect(Collectors.groupingBy(r -> r.get("CivilizationType"),
							Collectors.mapping(r -> r.get("Culture"), Collectors.toList()))));
		}
	}
}

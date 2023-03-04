package technology.rocketjump.civblitz.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.Patch;
import technology.rocketjump.civblitz.model.SourceDataRepo;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

@Component
public class CardPatchParser {
	private final SourceDataRepo sourceDataRepo;

	private final ResourceLoader resourceLoader;

	@Autowired
	public CardPatchParser(SourceDataRepo sourceDataRepo, ResourceLoader resourceLoader) {
		this.sourceDataRepo = sourceDataRepo;
		this.resourceLoader = resourceLoader;
	}

	public void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(input);
			for (CSVRecord record : parsed.getRecords()) {
				String civTraitType = record.get("TraitType");
				String sqlFileName = record.get("SqlFile");
				boolean needsDedicatedAbility = Boolean.parseBoolean(record.get("NeedsDedicatedAbility"));
				Card card = sourceDataRepo.getBaseCardByTraitType(civTraitType);
				Resource sqlFile = resourceLoader.getResource("classpath:sql/" + sqlFileName);
				try (InputStream is = sqlFile.getInputStream()) {
					String sqlTemplate = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
					Patch patch = new Patch();
					patch.setSqlTemplate(sqlTemplate);
					patch.setNeedsDedicatedAbility(needsDedicatedAbility);
					card.setPatchSQL(patch);
				}
			}
		}
	}
}

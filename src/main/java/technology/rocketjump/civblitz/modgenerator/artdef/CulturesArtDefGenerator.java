package technology.rocketjump.civblitz.modgenerator.artdef;

import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.*;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class CulturesArtDefGenerator extends ArtDefGenerator {
	private final SourceDataRepo sourceDataRepo;

	public CulturesArtDefGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFilename() {
		return "ArtDefs/Cultures.artdef";
	}

	@Override
	protected String getTemplateName() {
		return "Cultures";
	}

	@Override
	protected List<Collection> getRootCollections(ModHeader modHeader, List<ModdedCivInfo> civs) {
		return List.of(
				new Collection("Culture", culturesFor(civs, sourceDataRepo.civilizationToCultures)),
				new Collection("UnitCulture", culturesFor(civs, sourceDataRepo.civilizationToUnitCultures))
		);
	}

	private List<CivElement> culturesFor(List<ModdedCivInfo> civs, Map<String, List<String>> cultureMap) {
		Map<String, List<ModdedCivInfo>> cultureToCivs = civs.stream()
				.flatMap(civ -> cultureMap.getOrDefault(civ.getCard(CardCategory.CivilizationAbility)
						.getCivilizationType(), List.of()).stream().map(culture -> Map.entry(culture, civ)))
				.collect(Collectors.groupingBy(Map.Entry::getKey,
						Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
		cultureToCivs.remove(null);
		return cultureToCivs.entrySet()
				.stream()
				.map(entry -> new CivElement(
						entry.getKey(),
						List.of(new CollectionValue("Civilizations",
								"INVALID",
								"ARTDEF_REF",
								civsAsArtDefReference(entry.getValue()),
								true)),
						List.of(),
						true))
				.toList();
	}

	private List<XmlNode> civsAsArtDefReference(List<ModdedCivInfo> civs) {
		AtomicInteger i = new AtomicInteger(1);
		return civs.stream()
				.map(civ -> (XmlNode) (new ArtDefReferenceValue(
						"Civilizations" + indexStr(i.getAndIncrement()),
						civ.getCivDBName(),
						"Civilization",
						"Civilizations.artdef",
						true,
						"Civilizations")))
				.toList();
	}

	private static String indexStr(int index) {
		return String.format("%03d", index);
	}
}

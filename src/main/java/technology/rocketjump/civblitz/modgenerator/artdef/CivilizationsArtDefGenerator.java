package technology.rocketjump.civblitz.modgenerator.artdef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.ArtDefGenerator;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.CivElement;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.Collection;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.StringValue;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CivilizationsArtDefGenerator extends ArtDefGenerator {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public CivilizationsArtDefGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFilename() {
		return "ArtDefs/Civilizations.artdef";
	}

	@Override
	protected String getTemplateName() {
		return "Civilizations";
	}

	@Override
	protected List<Collection> getRootCollections(ModHeader modHeader, List<ModdedCivInfo> civs) {
		return List.of(new Collection("Civilization",
				civs.stream()
						.map(civ -> new CivElement(civ.getCivDBName(),
								List.of(),
								List.of(new Collection("Audio",
										List.of(new CivElement("Entry",
												List.of(new StringValue("XrefName",
														sourceDataRepo.civilizationToCivilizationArtdef.get(civ.getCard(
																		CardCategory.CivilizationAbility)
																.getCivilizationType()))),
												List.of()))))))
						.collect(Collectors.toList())));
	}
}

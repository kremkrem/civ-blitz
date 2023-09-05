package technology.rocketjump.civblitz.modgenerator.artdef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.ArtDefGenerator;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.BLPEntryValue;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.CivElement;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.Collection;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;

@Component
public class FallbackLeadersArtDefGenerator extends ArtDefGenerator {
	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public FallbackLeadersArtDefGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFilename() {
		return "ArtDefs/FallbackLeaders.artdef";
	}

	@Override
	protected String getTemplateName() {
		return "LeaderFallback";
	}

	@Override
	protected List<Collection> getRootCollections(ModHeader modHeader, List<ModdedCivInfo> civs) {
		return List.of(new Collection("Leaders",
				civs.stream()
						.filter(civ -> civ.getCard(CardCategory.LeaderAbility)
								.getLeaderType()
								.filter(sourceDataRepo.fallbackLeaderByLeaderType::containsKey)
								.isPresent())
						.map(civ -> new CivElement(civ.getLeaderDBName(),
								List.of(),
								List.of(new Collection("Animations",
										List.of(new CivElement("DEFAULT",
												List.of(new BLPEntryValue("BLP Entry",
														sourceDataRepo.fallbackLeaderByLeaderType.get(civ.getCard(
																CardCategory.LeaderAbility).getLeaderType().get()),
														"LeaderFallback",
														"leaderfallbackimages.xlp",
														"LeaderFallbackImages",
														"LeaderFallback")),
												List.of()))))))
						.toList()));
	}
}

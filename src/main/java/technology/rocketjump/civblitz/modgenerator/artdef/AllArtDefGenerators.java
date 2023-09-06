package technology.rocketjump.civblitz.modgenerator.artdef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;

import java.util.List;

@Component
public class AllArtDefGenerators {
	@Autowired
	private LeadersArtDefGenerator leadersArtDefGenerator;

	@Autowired
	private FallbackLeadersArtDefGenerator fallbackLeadersArtDefGenerator;

	@Autowired
	private CulturesArtDefGenerator culturesArtDefGenerator;

	@Autowired
	private CivilizationsArtDefGenerator civilizationsArtDefGenerator;

	@Autowired
	private LandmarksArtDefGenerator landmarksArtDefGenerator;

	public List<BlitzFileGenerator> getAll() {
		return List.of(leadersArtDefGenerator,
				fallbackLeadersArtDefGenerator,
				culturesArtDefGenerator,
				civilizationsArtDefGenerator,
				landmarksArtDefGenerator);
	}
}

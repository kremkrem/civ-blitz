package technology.rocketjump.civblitz.modgenerator.artdef;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class LandmarksArtDefGeneratorTest {

	@Autowired
	LandmarksArtDefGenerator generator;

	@Autowired
	SourceDataRepo sourceDataRepo;

	@Autowired
	ModHeaderGenerator modHeaderGenerator;

	@Test
	void getsOutput() {
		List<Card> cards = List.of(
				sourceDataRepo.getByCategory(CardCategory.CivilizationAbility, Optional.empty()).get(0),
				sourceDataRepo.getByCategory(CardCategory.LeaderAbility, Optional.empty()).get(0),
				sourceDataRepo.getByCategory(CardCategory.UniqueUnit, Optional.empty()).get(0),
				sourceDataRepo.getByCategory(CardCategory.UniqueInfrastructure, Optional.empty()).get(0)
		);
		ModHeader modHeader = modHeaderGenerator.createFor(cards);
		ModdedCivInfo moddedCivInfo = new ModdedCivInfo(cards, "None");
		System.out.println(generator.getFileContents(modHeader, moddedCivInfo));
	}

}
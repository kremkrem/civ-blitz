package technology.rocketjump.civblitz.modgenerator.artdef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.LeaderArtData;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.*;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;
import java.util.Objects;

@Component
public class LeadersArtDefGenerator extends ArtDefGenerator {

	private final SourceDataRepo sourceDataRepo;

	private static final BLPEntryValue leaderBgEntry =
			new BLPEntryValue("Leader_Background_BLP_Entry", "Leader", "Leader");
	private static final StringValue leaderBgAnimationStateEntry =
			new StringValue("Leader_Background_Animation_State", "");

	@Autowired
	public LeadersArtDefGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFilename() {
		return "ArtDefs/Leaders.artdef";
	}

	@Override
	protected String getTemplateName() {
		return "Leaders";
	}

	@Override
	protected List<Collection> getRootCollections(ModHeader modHeader, List<ModdedCivInfo> civs) {
		return List.of(new Collection("Leaders",
				civs.stream().map(civ -> civ.getCard(CardCategory.LeaderAbility).getLeaderType().map(leaderType -> {
					LeaderArtData leaderArtData = sourceDataRepo.leaderArtDataByLeaderType.get(leaderType);
					return new CivElement(civ.getLeaderDBName(),
							List.of(leaderArtData.leaderEntry(),
									leaderArtData.lightrigEntry(),
									leaderArtData.colorKeyEntry(),
									leaderBgEntry,
									leaderBgAnimationStateEntry,
									new StringValue("Audio", leaderArtData.audioEntry())),
							List.of());
				}).orElse(null)).filter(Objects::nonNull).toList()));
	}
}

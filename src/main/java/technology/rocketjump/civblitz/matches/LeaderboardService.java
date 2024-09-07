package technology.rocketjump.civblitz.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import technology.rocketjump.civblitz.codegen.tables.pojos.Player;
import technology.rocketjump.civblitz.codegen.tables.pojos.SecretObjective;
import technology.rocketjump.civblitz.matches.objectives.AllObjectivesService;
import technology.rocketjump.civblitz.matches.objectives.ObjectiveDefinition;
import technology.rocketjump.civblitz.matches.objectives.ObjectiveDefinitionRepo;
import technology.rocketjump.civblitz.matches.objectives.PublicObjectiveWithClaimants;
import technology.rocketjump.civblitz.model.MatchWithPlayers;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static technology.rocketjump.civblitz.matches.objectives.ObjectiveDefinition.NULL_OBJECTIVE;

@Service
public class LeaderboardService {
	public record Entry(@NotNull Integer objectiveScore, @Nullable Integer finalScore) {}

	private final AllObjectivesService allObjectivesService;
	private final ObjectiveDefinitionRepo objectiveDefinitionRepo;

	@Autowired
	public LeaderboardService(AllObjectivesService allObjectivesService, ObjectiveDefinitionRepo objectiveDefinitionRepo) {
		this.allObjectivesService = allObjectivesService;
		this.objectiveDefinitionRepo = objectiveDefinitionRepo;
	}

	public Map<String, Entry> getLeaderboard(MatchWithPlayers match) {
		Map<String, Entry> scoreboard = new LinkedHashMap<>();

		List<PublicObjectiveWithClaimants> publicObjectives = allObjectivesService.getPublicObjectives(match.getMatchId());
		List<SecretObjective> secretObjectives = allObjectivesService.getAllSecretObjectives(match, fakePlayer);

		match.signups.forEach(signup -> {
			int objectiveScore = 0;

			for (PublicObjectiveWithClaimants objective : publicObjectives) {
				if (objective.getClaimedByPlayerIds().contains(signup.getPlayerId())) {
					ObjectiveDefinition objectiveDefinition = objectiveDefinitionRepo.getById(objective.getObjective()).orElse(NULL_OBJECTIVE);
					Integer stars = objectiveDefinition.getStars(match.getStartEra());
					if (stars != null) {
						objectiveScore += stars;
					}
				}
			}
			for (SecretObjective secretObjective : secretObjectives) {
				if (secretObjective.getPlayerId().equals(signup.getPlayerId()) && secretObjective.getClaimed()) {
					ObjectiveDefinition objectiveDefinition = objectiveDefinitionRepo.getById(secretObjective.getObjective()).orElse(NULL_OBJECTIVE);
					Integer stars = objectiveDefinition.getStars(match.getStartEra());
					if (stars != null) {
						objectiveScore += stars;
					}
				}
			}
			final Integer finalScore = Optional.ofNullable(signup.getFinalPointsAwarded()).map(d ->
					(int) Math.round(d)).orElse(null);

			scoreboard.put(signup.getPlayerId(), new Entry(objectiveScore, finalScore));
		});
		return scoreboard;
	}

	private static final Player fakePlayer = new Player();
	static {
		fakePlayer.setIsAdmin(true);
	}
}

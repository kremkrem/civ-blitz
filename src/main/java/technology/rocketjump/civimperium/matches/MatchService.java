package technology.rocketjump.civimperium.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civimperium.cards.CollectionService;
import technology.rocketjump.civimperium.codegen.tables.pojos.Match;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static technology.rocketjump.civimperium.matches.MatchState.DRAFT;
import static technology.rocketjump.civimperium.matches.MatchState.SIGNUPS;

@Service
public class MatchService {

	private final AdjectiveNounNameGenerator adjectiveNounNameGenerator;
	private final MatchRepo matchRepo;
	private final CollectionService collectionService;
	private final SourceDataRepo sourceDataRepo;
	private Random random = new Random();

	@Autowired
	public MatchService(AdjectiveNounNameGenerator adjectiveNounNameGenerator, MatchRepo matchRepo,
						CollectionService collectionService, SourceDataRepo sourceDataRepo) {
		this.adjectiveNounNameGenerator = adjectiveNounNameGenerator;
		this.matchRepo = matchRepo;
		this.collectionService = collectionService;
		this.sourceDataRepo = sourceDataRepo;
	}


	public Match create(String matchName, String timeslot) {
		if (matchName == null || matchName.isBlank()) {
			Optional<Match> existingMatchWithSameName;
			do {
				matchName = adjectiveNounNameGenerator.generateName(random);
				existingMatchWithSameName = matchRepo.getMatchByName(matchName);
			} while (existingMatchWithSameName.isPresent());
		} else if (matchRepo.getMatchByName(matchName).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "A match with this name already exists");
		}

		return matchRepo.createMatch(matchName, timeslot);
	}

	public List<MatchWithPlayers> getUncompletedMatches() {
		return matchRepo.getUncompletedMatches();
	}

	public synchronized void signup(int matchId, Player player) {
		Optional<MatchWithPlayers> matchById = matchRepo.getMatchById(matchId);
		if (matchById.isPresent()) {
			matchRepo.signup(matchById.get(), player);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	public void resign(int matchId, Player player) {
		Optional<MatchWithPlayers> matchById = matchRepo.getMatchById(matchId);
		if (matchById.isPresent()) {
			if (!matchById.get().getMatchState().equals(SIGNUPS)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not resign from an in-progress match, only during signups");
			}
			matchRepo.resign(matchById.get(), player);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	public Optional<MatchWithPlayers> getById(int matchId) {
		return matchRepo.getMatchById(matchId);
	}

	public Match update(Match match, String name, String timeslot) {
		match.setMatchName(name);
		match.setTimeslot(timeslot);
		matchRepo.update(match);
		return match;
	}

	public Match switchState(Match match, MatchState newState, Map<String, Object> payload) {
		MatchState currentState = match.getMatchState();
		if (currentState.equals(SIGNUPS) && newState.equals(DRAFT)) {
			proceedToDraft(match, payload);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not yet implemented, transition from " + currentState + " to " + newState);
		}
		return match;
	}

	private void proceedToDraft(Match match, Map<String, Object> payload) {
		List<String> selectedPlayerIds = (List<String>) payload.get("playerIds");
		// At this stage, may only be removing signups
		MatchWithPlayers matchWithPlayers = matchRepo.getMatchById(match.getMatchId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		for (MatchSignupWithPlayer signup : matchWithPlayers.signups) {
			if (!selectedPlayerIds.contains(signup.getPlayerId())) {
				resign(match.getMatchId(), signup.getPlayer());
			}
		}


		match.setMatchState(DRAFT);
		matchRepo.update(match);
	}

	public synchronized MatchSignupWithPlayer addCardToMatchDeck(MatchWithPlayers match, Player player, String cardTraitType) {
		if (!match.getMatchState().equals(DRAFT)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can only add to deck during draft phase");
		}

		List<CollectionCard> playerCollection = collectionService.getCollection(player);
		CollectionCard cardInCollection = playerCollection.stream().filter(c -> c.getTraitType().equals(cardTraitType)).findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card does not exist in player's collection"));
		if (cardInCollection.getQuantity() < 1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card has no quantity in player's collection");
		}

		MatchSignupWithPlayer matchSignup = match.signups.stream().filter(signup -> signup.getPlayerId().equals(player.getPlayerId())).findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player is not part of specified match"));

		String existingCardSelection = matchSignup.getCard(cardInCollection.getCardCategory());
		if (existingCardSelection != null) {
			removeCardFromMatchDeck(match, player, existingCardSelection);
		}

		matchSignup.setCard(cardInCollection);
		matchRepo.updateSignup(matchSignup);
		collectionService.removeFromCollection(cardInCollection, player);
		return matchSignup;
	}

	public synchronized MatchSignupWithPlayer removeCardFromMatchDeck(MatchWithPlayers match, Player player, String cardTraitType) {
		if (!match.getMatchState().equals(DRAFT)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can only remove from deck during draft phase");
		}

		MatchSignupWithPlayer matchSignup = match.signups.stream().filter(signup -> signup.getPlayerId().equals(player.getPlayerId())).findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player is not part of specified match"));

		Card card = sourceDataRepo.getByTraitType(cardTraitType);
		if (matchSignup.getCard(card.getCardCategory()) == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card is not already part of selected deck");
		}

		matchSignup.removeCard(card);
		matchRepo.updateSignup(matchSignup);
		collectionService.addToCollection(card, player);
		return matchSignup;
	}
}

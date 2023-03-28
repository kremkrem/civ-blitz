package technology.rocketjump.civblitz.cards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.ArrayMap;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import technology.rocketjump.civblitz.model.*;
import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.*;

import java.util.*;

@Component
public class ActOfGodCardsParser {

	private final Logger logger = LoggerFactory.getLogger(ActOfGodCardsParser.class);

	private final String googleApiKey;
	private final SourceDataRepo sourceDataRepo;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	public ActOfGodCardsParser(@Value("${google.api.key}") String googleApiKey, SourceDataRepo sourceDataRepo) {
		this.googleApiKey = googleApiKey;
		this.sourceDataRepo = sourceDataRepo;
	}

	public void readFromGoogleSheet() throws JsonProcessingException {
		String requestUrl = "https://sheets.googleapis.com/v4/spreadsheets/" +
				"1PNS3od_8Kh3LrH48WLQveBhVQ7CvDSwmOlwErP1Q8SM/values/Inducements!A1:H300?key=" + googleApiKey;

		ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			JsonNode root = objectMapper.readTree(response.getBody());

			Iterator<JsonNode> iterator = root.get("values").elements();

			JsonNode headers = iterator.next();
			Map<ColumnHeader, Integer> columnIndicies = new LinkedHashMap<>();
			for (int cursor = 0; cursor < headers.size(); cursor++) {
				ColumnHeader columnHeader = EnumUtils.getEnum(ColumnHeader.class, headers.get(cursor).asText());
				if (columnHeader != null) {
					columnIndicies.put(columnHeader, cursor);
				}
			}
			RowToCardParser parser = new RowToCardParser(sourceDataRepo, columnIndicies, logger);

			while (iterator.hasNext()) {
				JsonNode row = iterator.next();
				if (row.isEmpty()) {
					continue;
				}
				parser.parse(row).ifPresent(sourceDataRepo::add);
			}
		} else {
			throw new RuntimeException("Could not load inducement cards from google sheet, " + response.getBody());
		}
	}

	private enum ColumnHeader {
		ID,
		Name,
		Rarity,
		Description,
		Type,
		Table,
		StartEras,
		BelongsTo,
	}

	private static class RowToCardParser {

		private final SourceDataRepo sourceDataRepo;
		private final ImmutableMap<ColumnHeader, Integer> columnIndicies;
		private final Logger logger;
		private final Map<String, ActOfGod> actMap = new ArrayMap<>();
		public RowToCardParser(SourceDataRepo sourceDataRepo, Map<ColumnHeader, Integer> columnIndicies, Logger logger) {
			this.sourceDataRepo = sourceDataRepo;
			this.columnIndicies = ImmutableMap.copyOf(columnIndicies);
			this.logger = logger;
			for (ActOfGod act : List.of(new AntiquesRoadshow(), new BeachHouse(), new BorderForce(), new ChoppingMall(),
					new ClanLeague(), new Contractors(), new Cowvalry(), new Curfew(), new Dictatorship(),
					new DoubleMoneyRound(), new DungeonCrawler(), new EveryoneMC2(), new FlexibleGovernment(),
					new GodEmperor(), new GodImmortal(), new GrandmasterMonk(), new GreatBigConvoy(), new HeadStart(),
					new HeroesLegends(), new HypatiasBlessing(), new ImmortalDifficulty(), new InvestmentCapital(),
					new Kutm(), new LabourShortage(), new LabourSurplus(), new LeapOfFaith(), new LostAtSea(),
					new MistOfWar(), new MonumentsToTheGods(), new MyGrain(), new NavySteals(), new NewtonianMechanic(),
					new PeaceMode(), new QOLImprovements(), new RentalMarket(), new RoadTrip(), new SettleMettle(),
					new ShuffleThePack(), new Subordination(), new ThreeForFree(), new TradeFederation(),
					new TroubleByTheOldMill(), new UrbanStrategy(), new Veganism(), new VikingMode(),
					new WhatWeDoInTheShadows(), new WoodenSwords(), new YouShallNotPass(), new ZombieDefense())) {
				actMap.put(act.getID(), act);
			}
		}

		public Optional<Card> parse(JsonNode row) {
			String identifier = getColumn(row, ColumnHeader.ID);
			String name = getColumn(row, ColumnHeader.Name);
			if (identifier.isEmpty()) {
				identifier = name.toUpperCase().replace(' ', '_');
			}
			ActOfGod act = actMap.get(identifier);
			if (act == null) {
				logger.error("Could not find implementation for act of god with ID: " + identifier);
				return Optional.empty();
			}
			CardRarity rarity = EnumUtils.getEnum(CardRarity.class, getColumn(row, ColumnHeader.Rarity));
			if (rarity == null) {
				logger.error(
						"Did not recognise rarity: " + getColumn(row, ColumnHeader.Rarity) + " for act of god " + name);
				return Optional.empty();
			}

			Card card = new Card();
			card.setIdentifier(identifier);
			card.setCardCategory(CardCategory.ActOfGod);
			card.setSuperCategory(SuperCategory.ActOfGod);
			card.setBaseCardName(name);
			card.setEnhancedCardName(name);
			card.setEnhancedCardDescription(getColumn(row, ColumnHeader.Description));
			Card civAbilityCard = sourceDataRepo.civAbilityCardByFriendlyName.get(getColumn(row, ColumnHeader.BelongsTo));
			if (civAbilityCard == null) {
				card.setCivilizationFriendlyName("Civ Blitz Original");
				card.setCivilizationType("imperium");
			} else {
				card.setCivilizationFriendlyName(civAbilityCard.getCivilizationFriendlyName());
				card.setCivilizationType(civAbilityCard.getCivilizationType());
			}
			card.setRarity(rarity);
			card.setMediaName("ActOfGod/" + identifier);
			card.setActOfGod(Optional.of(act));

			return Optional.of(card);
		}

		private String getColumn(JsonNode row, ColumnHeader columnHeader) {
			JsonNode node = row.get(columnIndicies.getOrDefault(columnHeader, -1));
			if (node == null) {
				return Strings.EMPTY;
			} else {
				return node.asText();
			}
		}
	}
}

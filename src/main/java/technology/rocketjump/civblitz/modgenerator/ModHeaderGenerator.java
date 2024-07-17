package technology.rocketjump.civblitz.modgenerator;

import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.CardRarity;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static technology.rocketjump.civblitz.model.CardCategory.ActOfGod;
import static technology.rocketjump.civblitz.model.CardCategory.CivilizationAbility;
import static technology.rocketjump.civblitz.model.CardCategory.LeaderAbility;
import static technology.rocketjump.civblitz.model.CardCategory.UniqueInfrastructure;
import static technology.rocketjump.civblitz.model.CardCategory.UniqueUnit;
import static technology.rocketjump.civblitz.model.CardCategory.mainCategories;

@Component
public class ModHeaderGenerator {

	public ModHeader createFor(String matchName) {

		String description = "This mod consists of a selection of new civs to support the Civ Blitz match " + matchName;

		String modName = StringUtils.replace(matchName, " ", "_");
		modName = StringUtils.replace(modName, "/", "_");

		return new ModHeader(modName, description, UUID.nameUUIDFromBytes(modName.getBytes()));
	}

	public ModHeader createFor(List<Card> selectedCards) {
		String name = buildName(selectedCards);

		StringBuilder descriptionBuilder = new StringBuilder("This mod consists of a new civ using the " +
				find(selectedCards, CivilizationAbility).getCivilizationFriendlyName() + " civ ability, " +
				find(selectedCards, LeaderAbility).getBaseCardName() + " as the leader, the " +
				find(selectedCards, UniqueInfrastructure).getBaseCardName() + " unique infrastructure (from " +
				find(selectedCards, UniqueInfrastructure).getCivilizationFriendlyName() + "), and the " +
				find(selectedCards, UniqueUnit).getBaseCardName() + " unique unit (from " +
				find(selectedCards, UniqueUnit).getCivilizationFriendlyName() + ").");
		List<Card> actOfGodCards = selectedCards.stream().filter(c -> c.getCardCategory() == ActOfGod).toList();
		if (!actOfGodCards.isEmpty()) {
			descriptionBuilder.append("\nThe mod also applies the following global modifiers (aka \"Acts of God\"):");
			for (Card c : actOfGodCards) {
				descriptionBuilder.append("\n* ").append(c.getBaseCardName());
			}
		}
		return new ModHeader(name, descriptionBuilder.toString(), UUID.nameUUIDFromBytes(name.getBytes()));
	}

	public static String buildName(List<Card> selectedCards) {
		StringBuilder nameBuilder = new StringBuilder();
		selectedCards = selectedCards.stream().filter(c -> mainCategories.contains(c.getCardCategory()))
				.collect(Collectors.toList());
		selectedCards.sort(Comparator.comparing(Card::getCardCategory));
		for (Card card : selectedCards) {
			if (!card.getRarity().equals(CardRarity.Common)) {
				nameBuilder.append(card.getRarity().name().charAt(0));
			}
			if (card.getCardCategory().equals(LeaderAbility)) {
				nameBuilder.append(getShortLeaderName(card.getBaseCardName()));
			} else {
				nameBuilder.append(getShortName(card.getBaseCardName()));
			}
		}
		return nameBuilder.toString();
	}

	private static String getShortName(String name) {
		StringBuilder shortNameBuilder = new StringBuilder();
		for (int cursor = 0; cursor < name.length(); cursor++) {
			char c = name.charAt(cursor);
			if (isLetter(c)) {
				shortNameBuilder.append(c);
				if (shortNameBuilder.length() >= 4) {
					break;
				}
			}
		}

		return shortNameBuilder.toString();
	}

	private static String getShortLeaderName(String name) {
		// Get rid of all diacritics. I'm looking at you, Joao.
		String normalized_name = ModgenStringUtils.NormalizeStringCutJunk(name);
		return normalized_name.substring(0, 3) + normalized_name.substring(normalized_name.length() - 1);
	}

	private static boolean isLetter(char c) {
		// only want A - Z, not unicode characters
		return (c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z');
	}

	private Card find(List<Card> selectedCards, CardCategory category) {
		return selectedCards.stream().filter(c -> c.getCardCategory().equals(category)).findAny().orElse(new Card());
	}
}

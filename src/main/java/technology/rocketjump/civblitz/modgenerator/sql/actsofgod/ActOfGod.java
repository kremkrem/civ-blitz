package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import technology.rocketjump.civblitz.model.Card;

import java.util.Collection;
import java.util.List;

import static technology.rocketjump.civblitz.model.CardCategory.ActOfGod;

public interface ActOfGod {

	String getID();

	void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder);

	void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder);

	void applyGlobalChanges(StringBuilder sqlBuilder);

	void applyLocalisationChanges(StringBuilder sqlBuilder);

	static List<ActOfGod> fromCards(Collection<Card> cards) {
		return cards.stream()
				.filter(c -> c.getCardCategory() == ActOfGod)
				.flatMap(c -> c.getActOfGod().stream())
				.toList();
	}
}

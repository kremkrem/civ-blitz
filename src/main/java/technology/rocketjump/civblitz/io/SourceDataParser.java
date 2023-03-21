package technology.rocketjump.civblitz.io;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.cards.ModifierCardsParser;
import technology.rocketjump.civblitz.matches.guilds.GuildDefinitionParser;
import technology.rocketjump.civblitz.matches.objectives.ObjectiveDefinitionParser;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.IconAtlasEntry;
import technology.rocketjump.civblitz.model.SourceDataRepo;

import java.io.IOException;

@Component
public class SourceDataParser {

	@Autowired
	public SourceDataParser(LeaderTraitsParser leaderTraitsParser, CivTraitsParser civTraitsParser,
							SubtypesParser subtypesParser, SourceDataRepo sourceDataRepo, IconParser iconParser,
							PlayersParser playersParser, CivilizationsCsvParser civilizationsCsvParser,
							CivilizationLeadersCsvParser civilizationLeadersCsvParser,
							DlcCsvParser dlcParser, ModifiersCsvParser modifiersParser,
							ObjectiveDefinitionParser objectiveDefinitionParser,
							GuildDefinitionParser guildDefinitionParser,
							ModifierCardsParser modifierCardsParser,
							CardPatchParser cardPatchParser,
							LeaderArtDefsParser leaderArtDefsParser,
							FallbackLeadersArtDefsParser fallbackLeadersArtDefsParser,
							@Qualifier("leaderTraits") String leaderTraitsContent,
							@Qualifier("civTraits") String civTraitsContent,
							@Qualifier("subtypes") String subtypesContent,
							@Qualifier("CivIcons") String civIconsContent,
							@Qualifier("LeaderIcons") String leaderIconsContent,
							@Qualifier("Players") String playersContent,
							@Qualifier("Civilizations") String civCsvContent,
							@Qualifier("CivilizationLeaders") String civLeadersCsvContent,
							@Qualifier("CivilizationDLC") String civDlcContent,
							@Qualifier("LeaderDLC") String leaderDlcContent,
							@Qualifier("TraitModifiers") String traitModifiersContent,
							@Qualifier("CardPatches") String cardPatchesContent,
							@Qualifier("LeaderArtDefs") String leaderArtDefsContent,
							@Qualifier("FallbackLeaderArtDefs") String fallbackLeaderArtDefsContent,
							@Value("#{new Boolean('${multiplayer-flag}')}") boolean multiplayerFlag) throws IOException {
		leaderTraitsParser.parse(leaderTraitsContent);
		civTraitsParser.parse(civTraitsContent);
		subtypesParser.parse(subtypesContent);
		iconParser.parse(civIconsContent);
		iconParser.parse(leaderIconsContent);
		playersParser.parse(playersContent);
		civilizationsCsvParser.parse(civCsvContent);
		civilizationLeadersCsvParser.parse(civLeadersCsvContent);
		dlcParser.parse(civDlcContent, leaderDlcContent);
		modifiersParser.parse(traitModifiersContent);
		cardPatchParser.parse(cardPatchesContent);
		leaderArtDefsParser.parse(leaderArtDefsContent);
		fallbackLeadersArtDefsParser.parse(fallbackLeaderArtDefsContent);

		if (multiplayerFlag) {
			objectiveDefinitionParser.readFromGoogleSheet();
			guildDefinitionParser.readFromGoogleSheet();
		}

		sourceDataRepo.removeGrantedCards();
		System.out.println("Base cards parsed: " + sourceDataRepo.getAll().size());

		verifyIconAtlasEntries(sourceDataRepo);
		verifyAllCivRecordEntries(sourceDataRepo);

		if (multiplayerFlag) {
			modifierCardsParser.readFromGoogleSheet();
		}

		System.out.println("All cards parsed: " + sourceDataRepo.getAll().size());
	}

	private void verifyIconAtlasEntries(SourceDataRepo sourceDataRepo) {
		for (Card card : sourceDataRepo.getAll()) {
			if (card.getCardCategory().equals(CardCategory.LeaderAbility)) {
				IconAtlasEntry iconAtlasEntry = sourceDataRepo.getIconAtlasEntry(card.getLeaderType().get());
				if (iconAtlasEntry == null) {
					throw new RuntimeException("Could not find icon atlas entry for " + card.getLeaderType().get());
				}
			} else if (card.getCardCategory().equals(CardCategory.CivilizationAbility)) {
				IconAtlasEntry iconAtlasEntry = sourceDataRepo.getIconAtlasEntry(card.getCivilizationType());
				if (iconAtlasEntry == null) {
					throw new RuntimeException("Could not find icon atlas entry for " + card.getCivilizationType());
				}
			}
		}
	}

	private void verifyAllCivRecordEntries(SourceDataRepo sourceDataRepo) {
		for (Card card : sourceDataRepo.getAll()) {
			if (card.getCardCategory().equals(CardCategory.CivilizationAbility)) {
				String civType = card.getCivilizationType();

				CSVRecord record = sourceDataRepo.civilizationCsvRecordsByCivType.get(civType);
				if (record == null) {
					throw new RuntimeException("No Civ csv record for " + civType);
				}

				String capitalName = sourceDataRepo.capitalNamesByCivType.get(civType);
				if (capitalName == null) {
					throw new RuntimeException("No capital name for " + civType);
				}
			}
		}
	}

}

package technology.rocketjump.civblitz.modgenerator.sql;

import com.google.common.collect.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModData;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ConfigurationSqlGenerator extends BlitzFileGenerator {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public ConfigurationSqlGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModData modData, ModdedCivInfo civInfo) {
		String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();

		Card civAbilityCard = civInfo.getCard(CardCategory.CivilizationAbility);
		String civType = civAbilityCard.getCivilizationType();
		String civName = sourceDataRepo.civNameByCivType.get(civType);
		String civIcon = sourceDataRepo.civIconByCivType.get(civType);

		String civAbilityName = sourceDataRepo.civAbilityNameByCivType.get(civType);
		String civAbilityDesc = sourceDataRepo.civAbilityDescByCivType.get(civType);
		String civAbilityIcon = sourceDataRepo.civAbilityIconByCivType.get(civType);

		Card leaderAbilityCard = civInfo.getCard(CardCategory.LeaderAbility);
		String leaderType = leaderAbilityCard.getLeaderType().orElseThrow();
		String leaderTrait = leaderAbilityCard.getTraitType();
		String leaderIcon = sourceDataRepo.leaderIconByLeaderType.get(leaderType);
		String leaderAbilityIcon = sourceDataRepo.leaderAbilityIconByLeaderType.get(leaderType);

		String locLeaderTraitName = sourceDataRepo.leaderTraitNameByTraitType.getOrDefault(leaderTrait, "");
		String locLeaderTraitDesc = sourceDataRepo.leaderTraitDescByTraitType.getOrDefault(leaderTrait, "");

		String portrait = sourceDataRepo.portraitsByLeaderType.getOrDefault(leaderType, "");
		String portraitBackground = sourceDataRepo.portraitBackgroundsByLeaderType.getOrDefault(leaderType,"");

		return """
				INSERT OR REPLACE INTO Players
				(Domain,
				 CivilizationType,
				 Portrait,
				 PortraitBackground,
				 LeaderType,
				 LeaderName,
				 LeaderIcon,
				 LeaderAbilityName,
				 LeaderAbilityDescription,
				 LeaderAbilityIcon,
				 CivilizationName,
				 CivilizationIcon,
				 CivilizationAbilityName,
				 CivilizationAbilityDescription,
				 CivilizationAbilityIcon)
				VALUES
				('Players:Expansion2_Players',
				 'CIVILIZATION_IMP_""" + modName + "',\n"
				+ " '" + portrait + "',\n"
				+ " '" + portraitBackground + "',\n"
				+ " 'LEADER_IMP_" + modName + "',\n"
				+ " 'LOC_LEADER_IMP_" + modName + "',\n"
				+ " '" + leaderIcon + "',\n"
				+ " '" + locLeaderTraitName + "',\n"
				+ " '" + locLeaderTraitDesc + "',\n"
				+ " '" + leaderAbilityIcon + "',\n"
				+ " '" + civName + "',\n"
				+ " '" + civIcon + "',\n"
				+ " '" + civAbilityName + "',\n"
				+ " '" + civAbilityDesc + "',\n"
				+ " '" + civAbilityIcon + "'\n);\n\n"
				+ Streams.mapWithIndex(
				civInfo.selectedCards.stream()
						.filter(card -> !card.getCardCategory().equals(CardCategory.ActOfGod))
						.sorted(Comparator.comparing(Card::getCardCategory))
						.flatMap(card -> Stream.of(
										Optional.ofNullable((card.getCardCategory().equals(CardCategory.UniqueUnit)
												|| card.getCardCategory().equals(CardCategory.UniqueInfrastructure))
												? new CardRepresentation(
												card.getTraitType(), card.getCivilizationType(), false)
												: null),
										card.getGrantsTraitType()
												.map(grantType -> new CardRepresentation(
														grantType,
														card.getCivilizationType(),
														false)),
										card.getModifierIds()
												.stream()
												.findAny()
												.map(x -> card.getCardCategory().equals(CardCategory.Power)
														? new CardRepresentation(
														card.getTraitType(),
														card.getCivilizationType(),
														true)
														: null))
								.flatMap(Optional::stream)),
				(cardRep, idx) -> cardRep.playerItemSQL(modName, (int) (10 + idx * 10), sourceDataRepo)).collect(
				Collectors.joining("\n\n"));
	}

	@Override
	public String getFilename() {
		return "Configuration.sql";
	}

	private record CardRepresentation(String traitType, String civType, boolean original) {
		private static final ST NEW_TRAIT_TEMPLATE = new ST("""
				INSERT OR REPLACE INTO PlayerItems
				(Domain, CivilizationType, LeaderType, Type, Icon, Name, Description, SortIndex)
				SELECT 'Players:Expansion2_Players', 'CIVILIZATION_IMP_<modName>', 'LEADER_IMP_<modName>',
				'<traitType>', CivilizationIcon, 'LOC_<traitType>_NAME', 'LOC_<traitType>_DESCRIPTION', <sortIdx>
				FROM Players
				WHERE Domain = 'Players:Expansion2_Players' AND CivilizationType = '<civType>'
				LIMIT 1;
				""");

		private static final ST COPY_EXISTING_TRAIT_TEMPLATE = new ST("""
				INSERT OR REPLACE INTO PlayerItems
				(Domain, CivilizationType, LeaderType, Type, Icon, Name, Description, SortIndex)
				SELECT 'Players:Expansion2_Players', 'CIVILIZATION_IMP_<modName>', 'LEADER_IMP_<modName>',
				PlayerItems.Type, PlayerItems.Icon, PlayerItems.Name, PlayerItems.Description, <sortIdx>
				FROM PlayerItems
				WHERE Domain = 'Players:Expansion2_Players' AND CivilizationType = '<civType>'
				AND Type = '<subType>';
				""");

		String playerItemSQL(String modName, int sortIdx, SourceDataRepo sourceDataRepo) {
			if (original) {
				return new ST(NEW_TRAIT_TEMPLATE)
						.add("modName", modName)
						.add("traitType", traitType)
						.add("civType", civType)
						.add("sortIdx", sortIdx)
						.render();
			}
			String subtype = sourceDataRepo.getSubtypeByTraitType(traitType);
			return new ST(COPY_EXISTING_TRAIT_TEMPLATE)
					.add("modName", modName)
					.add("civType", civType)
					.add("sortIdx", sortIdx)
					.add("subType", subtype)
					.render();
		}
	}
}

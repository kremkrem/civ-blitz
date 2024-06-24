package technology.rocketjump.civblitz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class ResourceFiles {

	@Value("classpath:csv/LeaderTraits.csv")
	private Resource leaderTraitsFile;

	@Bean(name = "leaderTraits")
	public String leaderTraits() {
		try (InputStream is = leaderTraitsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/CivTraits.csv")
	private Resource civTraitsFile;

	@Bean(name = "civTraits")
	public String civTraits() {
		try (InputStream is = civTraitsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/subtypes.csv")
	private Resource subtypesFile;

	@Bean(name = "subtypes")
	public String subtypes() {
		try (InputStream is = subtypesFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/CivIcons.csv")
	private Resource civIconsFile;

	@Bean(name = "CivIcons")
	public String civIcons() {
		try (InputStream is = civIconsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/LeaderIcons.csv")
	private Resource leaderIconsFile;

	@Bean(name = "LeaderIcons")
	public String leaderIcons() {
		try (InputStream is = leaderIconsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/Players.csv")
	private Resource playersFile;

	@Bean(name = "Players")
	public String players() {
		try (InputStream is = playersFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/Civilizations.csv")
	private Resource civilizationsFile;

	@Bean(name = "Civilizations")
	public String civilizations() {
		try (InputStream is = civilizationsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/CivilizationLeaders.csv")
	private Resource civilizationLeadersFile;

	@Bean(name = "CivilizationLeaders")
	public String civilizationLeaders() {
		try (InputStream is = civilizationLeadersFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/CivilizationsDLC.csv")
	private Resource civilizationDlcFile;

	@Bean(name = "CivilizationDLC")
	public String civilizationDLC() {
		try (InputStream is = civilizationDlcFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/LeadersDLC.csv")
	private Resource leaderDlcFile;

	@Bean(name = "LeaderDLC")
	public String leaderDLC() {
		try (InputStream is = leaderDlcFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:adjective_noun/adjectives.txt")
	private Resource adjectivesFile;

	@Bean(name = "adjectives")
	public String adjectives() {
		try (InputStream is = adjectivesFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:adjective_noun/nouns.txt")
	private Resource nounsFile;

	@Bean(name = "nouns")
	public String nouns() {
		try (InputStream is = nounsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/TraitModifiers.csv")
	private Resource traitModifiersFile;

	@Bean(name = "TraitModifiers")
	public String TraitModifiers() {
		try (InputStream is = traitModifiersFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/CardPatches.csv")
	private Resource cardPatchesFile;

	@Bean(name = "CardPatches")
	public String CardPatches() {
		try (InputStream is = cardPatchesFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/LeaderArtDefs.csv")
	private Resource leaderArtDefsFile;

	@Bean(name = "LeaderArtDefs")
	public String LeaderArtDefs() {
		try (InputStream is = leaderArtDefsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/FallbackLeadersArtDefs.csv")
	private Resource fallbackLeadersArtDefsFile;

	@Bean(name = "FallbackLeaderArtDefs")
	public String FallbackLeaderArtDefs() {
		try (InputStream is = fallbackLeadersArtDefsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/CivilizationsCulture.csv")
	private Resource civilizationsCultureFile;

	@Bean(name = "CivilizationsCulture")
	public String CivilizationsCulture() {
		try (InputStream is = civilizationsCultureFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/LandmarksArtDefs.csv")
	private Resource landmarksArtDefsFile;

	@Bean(name = "LandmarksArtDefs")
	public String LandmarksArtDefs() {
		try (InputStream is = landmarksArtDefsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/PowerCards.csv")
	private Resource powerCardsFile;

	@Bean(name = "PowerCards")
	public String PowerCards() {
		try (InputStream is = powerCardsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/UpgradeCards.csv")
	private Resource upgradeCardsFile;

	@Bean(name = "UpgradeCards")
	public String UpgradeCards() {
		try (InputStream is = upgradeCardsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

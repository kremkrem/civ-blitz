package technology.rocketjump.civblitz.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technology.rocketjump.civblitz.auth.JwtService;
import technology.rocketjump.civblitz.codegen.tables.pojos.Player;
import technology.rocketjump.civblitz.discord.DiscordAccessToken;
import technology.rocketjump.civblitz.discord.DiscordHttpClient;
import technology.rocketjump.civblitz.discord.DiscordUserInfo;
import technology.rocketjump.civblitz.players.PlayerService;

import static technology.rocketjump.civblitz.auth.SecurityConfiguration.isProduction;

@RestController
@RequestMapping("/api/login")
public class LoginController {

	private final Environment environment;
	private final DiscordHttpClient discordHttpClient;
	private final JwtService jwtService;
	private final PlayerService playerService;
	private final String discordClientId;

	@Autowired
	public LoginController(
			Environment environment,
			DiscordHttpClient discordHttpClient,
			JwtService jwtService,
			PlayerService playerService,
			@Value("${spring.security.oauth2.client.registration.discord.client-id}") String discordClientId) {
		this.environment = environment;
		this.discordHttpClient = discordHttpClient;
		this.jwtService = jwtService;
		this.playerService = playerService;
		this.discordClientId = discordClientId;
	}


	@GetMapping("/discord")
	public ResponseEntity<String> discordOauthCode(@RequestParam(name = "code") String oauthCode,
												   @RequestParam(name = "state") String stateParam,
												   @CookieValue("state") String stateCookie) throws JsonProcessingException {
		if (isProduction(environment)) {
			if (!stateParam.equals(stateCookie)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("State sent to Discord does not match cookie");
			}
		}

		DiscordAccessToken token = discordHttpClient.getToken(oauthCode);
		DiscordUserInfo discordUser = discordHttpClient.getCurrentUserInfo(token);
		Player player = playerService.getPlayer(discordUser.getId(), discordUser.getUsername(), discordUser.getAvatar());
		String jwt = jwtService.create(token, player);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", "/?token=" + jwt);

		return ResponseEntity.status(HttpStatus.FOUND).headers(responseHeaders).build();
	}

	@GetMapping("/discord-client-id")
	public ResponseEntity<String> discordClientId() {
		return ResponseEntity.status(HttpStatus.OK).body("\"" + discordClientId + "\"");
	}

}

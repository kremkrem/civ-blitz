package technology.rocketjump.civblitz.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/features")
public class FeaturesController {
	@Value("#{new Boolean('${multiplayer-flag}')}")
	private boolean multiplayerFlag;

	@GetMapping()
	public Map<String, Boolean> getFeatureFlags() {
		return Map.of(
				"multiplayer",  multiplayerFlag
		);
	}
}

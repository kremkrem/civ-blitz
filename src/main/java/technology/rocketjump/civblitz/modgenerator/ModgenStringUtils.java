package technology.rocketjump.civblitz.modgenerator;

import java.text.Normalizer;

public class ModgenStringUtils {
	public static String NormalizeString(String str, String replacement_char) {
		return Normalizer.normalize(str, Normalizer.Form.NFKD).replaceAll("[^\\p{Alnum}]", replacement_char);
	}

	public static String NormalizeString(String str) {
		return NormalizeString(str, "_");
	}

	public static String NormalizeStringCutJunk(String str) {
		return NormalizeString(str, "");
	}
}

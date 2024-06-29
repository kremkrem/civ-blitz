package technology.rocketjump.civblitz.modgenerator;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.artdef.AllArtDefGenerators;
import technology.rocketjump.civblitz.modgenerator.dep.ArtDepGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;
import technology.rocketjump.civblitz.modgenerator.sql.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class CompleteModGenerator {

	private final ModHeaderGenerator modHeaderGenerator;
	private final ResourceLoader resourceLoader;

	private final List<BlitzFileGenerator> fileGeneratorList = new ArrayList<>();
	private static final List<String> staticFileList = List.of("lua/LeaderScene_layeredBg.lua");

	@Autowired
	public CompleteModGenerator(ResourceLoader resourceLoader,
								ModHeaderGenerator modHeaderGenerator,
								ModInfoGenerator modInfoGenerator,
								CivilizationSqlGenerator civilizationSqlGenerator,
								CivTraitsSqlGenerator civTraitsSqlGenerator,
								ColorsSqlGenerator colorsSqlGenerator,
								ConfigurationSqlGenerator configurationSqlGenerator,
								GeographySqlGenerator geographySqlGenerator,
								IconsSqlGenerator iconsSqlGenerator,
								LeaderSqlGenerator leaderSqlGenerator,
								LocaleGenerator localeGenerator,
								ArtDepGenerator artDepGenerator,
								AllArtDefGenerators allArtDefGenerators) {
		this.resourceLoader = resourceLoader;
		this.modHeaderGenerator = modHeaderGenerator;

		fileGeneratorList.add(civilizationSqlGenerator);
		fileGeneratorList.add(civTraitsSqlGenerator);
		fileGeneratorList.add(colorsSqlGenerator);
		fileGeneratorList.add(configurationSqlGenerator);
		fileGeneratorList.add(geographySqlGenerator);
		fileGeneratorList.add(iconsSqlGenerator);
		fileGeneratorList.add(modInfoGenerator);
		fileGeneratorList.add(leaderSqlGenerator);
		fileGeneratorList.add(localeGenerator);
		fileGeneratorList.add(artDepGenerator);
		fileGeneratorList.addAll(allArtDefGenerators.getAll());
	}

	public byte[] generateMod(String matchName, List<ModdedCivInfo> moddedCivs) throws IOException {
		for (ModdedCivInfo civInfo : moddedCivs) {
			if (civInfo.selectedCards.size() != 4) {
				throw new IllegalArgumentException(getClass().getSimpleName() + " must be passed a map of 4 cards");
			}
			for (CardCategory cardCategory : CardCategory.mainCategories) {
				if (civInfo.selectedCards.stream().noneMatch(c -> c.getCardCategory().equals(cardCategory))) {
					throw new IllegalArgumentException(
							getClass().getSimpleName() + " must be passed one card in each category");
				}
			}
		}

		ModHeader header = modHeaderGenerator.createFor(matchName);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

		for (BlitzFileGenerator generator : fileGeneratorList) {
			byte[] contentBytes = generator.getFileContents(header, moddedCivs).getBytes();
			zipOutputStream.putNextEntry(new ZipEntry(generator.getFilename()));
			zipOutputStream.write(contentBytes, 0, contentBytes.length);
		}

		zipOutputStream.finish();
		zipOutputStream.flush();
		IOUtils.closeQuietly(zipOutputStream);
		IOUtils.closeQuietly(bufferedOutputStream);
		IOUtils.closeQuietly(byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

	public byte[] generateMod(ModdedCivInfo civInfo) throws IOException {
		if (civInfo.selectedCards.size() < 4) {
			throw new IllegalArgumentException(
					getClass().getSimpleName() + " must be passed a map of at least 4 cards");
		}
		for (CardCategory cardCategory : CardCategory.mainCategories) {
			if (civInfo.selectedCards.stream().noneMatch(c -> c.getCardCategory().equals(cardCategory))) {
				throw new IllegalArgumentException(
						getClass().getSimpleName() + " must be passed one card in each category");
			}
		}

		ModHeader header = modHeaderGenerator.createFor(civInfo.selectedCards);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

		for (BlitzFileGenerator generator : fileGeneratorList) {
			byte[] contentBytes = generator.getFileContents(header, civInfo).getBytes();
			zipOutputStream.putNextEntry(new ZipEntry(generator.getFilename()));
			zipOutputStream.write(contentBytes, 0, contentBytes.length);
		}
		for (String filename : staticFileList) {
			Resource staticFile = resourceLoader.getResource("classpath:" + filename);
			try (InputStream is = staticFile.getInputStream()) {
				byte[] contentBytes = is.readAllBytes();
				zipOutputStream.putNextEntry(new ZipEntry(filename));
				zipOutputStream.write(contentBytes, 0, contentBytes.length);
			}
		}

		zipOutputStream.finish();
		zipOutputStream.flush();
		IOUtils.closeQuietly(zipOutputStream);
		IOUtils.closeQuietly(bufferedOutputStream);
		IOUtils.closeQuietly(byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

}

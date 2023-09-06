package technology.rocketjump.civblitz.modgenerator.artdef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.LandmarkData;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.artdef.xml.*;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LandmarksArtDefGenerator extends ArtDefGenerator {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public LandmarksArtDefGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}


	@Override
	public String getFilename() {
		return "ArtDefs/Landmarks.artdef";
	}

	@Override
	protected String getTemplateName() {
		return "Landmarks";
	}

	@Override
	protected List<Collection> getRootCollections(ModHeader modHeader, List<ModdedCivInfo> civs) {
		return List.of(
				new Collection("Districts", districts()),
				new Collection("Landmarks", landmarks()),
				new Collection("ResourceTags"),
				new Collection("Globals"),
				new Collection("TerrainTags"));
	}

	private static final List<AssetObject> districtValues = List.of(
			new ArtDefReferenceValue("DistrictGenerator"),
			new RGBValue("TintColor"),
			new BoolValue("IsAlignedToCoast", false),
			new BoolValue("FlattenTerrain", true),
			new BoolValue("bUseCityScale", false),
			new BoolValue("DistrictDamagePillagesBuildings", false),
			new StringValue("ProceduralPlacementMode", "NONE"));

	private static final List<AssetObject> cityCenterValues = List.of(
			new ArtDefReferenceValue(
					"DistrictGenerator",
					"Gen_CityCenter",
					"Generator",
					"CityGenerators.artdef"),
			new RGBValue("TintColor"),
			new BoolValue("IsAlignedToCoast", false),
			new BoolValue("FlattenTerrain", true),
			new BoolValue("bUseCityScale", false),
			new BoolValue("DistrictDamagePillagesBuildings", true),
			new StringValue("ProceduralPlacementMode", "NONE"));


	private static ArtDefReferenceValue makeEra(String era) {
		return new ArtDefReferenceValue("Tag_Era", era, "ArtEra", "Eras.artdef", true, "Eras");
	}

	private static final ArtDefReferenceValue defaultCulture =
			new ArtDefReferenceValue("Tag_Culture", "DEFAULT", "Culture", "Cultures.artdef", true, "Cultures");
	private static final ArtDefReferenceValue anyAppeal =
			new ArtDefReferenceValue("Tag_Appeal", "ANY", "AppealTags", "Appeal.artdef", true, "Appeal");

	private List<CivElement> districts() {
		Map<String, List<LandmarkData>> districtToEntries = sourceDataRepo.landmarkArtdefs.stream()
				.filter(landmark -> !"Eras".equals(landmark.collection()))
				.collect(Collectors.groupingBy(LandmarkData::name));
		return districtToEntries.entrySet().stream().map(entry -> new CivElement(
				entry.getKey(),
				"DISTRICT_CITY_CENTER".equals(entry.getKey()) ? cityCenterValues : districtValues,
				List.of(new Collection(
						"BaseVariants",
						entry.getValue()
								.stream()
								.filter(landmark -> "BaseVariants".equals(landmark.collection()))
								.map(landmark -> new CivElement(
										landmark.entryName() + " CIV_IMP",
										List.of(
												new ArtDefReferenceValue(
														"Set_HeroBuildings",
														landmark.subjectName(),
														"BuildingSets",
														"Landmarks.artdef",
														true,
														"Landmarks"),
												makeEra(landmark.era()),
												defaultCulture,
												anyAppeal,
												landmark.asset(),
												new StringValue("SelectionRule", ""),
												new IntValue("Priority", 0),
												new StringValue("Placement", "INHERIT")),
										List.of()))
								.toList()), new Collection(
						"BuildingVariants",
						entry.getValue()
								.stream()
								.filter(landmark -> "BuildingVariants".equals(landmark.collection()))
								.map(landmark -> new CivElement(
										landmark.entryName() + " CIV_IMP",
										List.of(
												new ArtDefReferenceValue(
														"Tag_HeroBuilding",
														landmark.subjectName(),
														"Building",
														"Buildings.artdef",
														true,
														"Buildings"),

												makeEra(landmark.era()),
												defaultCulture,
												anyAppeal,
												landmark.asset(),
												new StringValue("SelectionRule", ""),
												new IntValue("Priority", 0)),
										List.of()))
								.toList())))).toList();
	}

	private List<CivElement> landmarks() {
		Map<String, List<LandmarkData>> grouped = sourceDataRepo.landmarkArtdefs.stream()
				.filter(landmark -> "Eras".equals(landmark.collection()))
				.collect(Collectors.groupingBy(LandmarkData::name));
		return grouped.entrySet().stream().map(entry -> new CivElement(
				entry.getKey(),
				List.of(
						new BoolValue("FlattenTerrain", entry.getValue().get(0).flatten()),
						new StringValue(
								"RotationType",
								entry.getKey().contains("POLDER") ? "COASTAL" : "ONLY_FIRST_60")),
				List.of(new Collection(
						"Eras",
						entry.getValue()
								.stream()
								.map(landmark -> new CivElement(
										landmark.entryName() + " CIV_IMP",
										List.of(
												makeEra(landmark.era()),
												landmark.asset(),
												defaultCulture,
												anyAppeal,
												new StringValue("SelectionRule", ""),
												new FloatValue("Priority", 0.f)),
										List.of()))
								.toList())))).toList();
	}
}

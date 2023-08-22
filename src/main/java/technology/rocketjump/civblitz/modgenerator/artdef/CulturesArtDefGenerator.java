package technology.rocketjump.civblitz.modgenerator.artdef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CulturesArtDefGenerator extends BlitzFileGenerator {
	private final SourceDataRepo sourceDataRepo;

	@Autowired
	CulturesArtDefGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		return getFileContents(modHeader, List.of(civInfo));
	}

	@Override
	public String getFilename() {
		return "ArtDefs/Cultures.artdef";
	}

	@Override
	public String getFileContents(ModHeader modHeader, List<ModdedCivInfo> civs) {
		StringBuilder builder = new StringBuilder(ArtDefUtils.PREAMBLE + """
					<m_TemplateName text="Cultures"/>
					<m_RootCollections>
						<Element>
							<m_CollectionName text="Culture"/>
							<m_ReplaceMergedCollectionElements>false</m_ReplaceMergedCollectionElements>
				""");
		appendCivCultures(builder, civs, sourceDataRepo.civilizationToCultures);
		builder.append("""
						</Element>
						<Element>
							<m_CollectionName text="UnitCulture"/>
							<m_ReplaceMergedCollectionElements>false</m_ReplaceMergedCollectionElements>
				""");
		appendCivCultures(builder, civs, sourceDataRepo.civilizationToUnitCultures);
		builder.append("""
						</Element>
					</m_RootCollections>
				""" + ArtDefUtils.FOOTER);

		return builder.toString();
	}

	private void appendCivCultures(StringBuilder builder, List<ModdedCivInfo> civs, Map<String, List<String>> cultureMap) {
		Map<String, List<ModdedCivInfo>> cultureToCivs = civs.stream()
				.flatMap(civ -> cultureMap.getOrDefault(civ.getCard(CardCategory.CivilizationAbility)
						.getCivilizationType(), List.of()).stream().map(culture -> Map.entry(culture, civ)))
				.collect(Collectors.groupingBy(Map.Entry::getKey,
						Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
		cultureToCivs.remove(null);
		cultureToCivs.forEach((culture, civTypes) -> {
			builder.append("""
								<Element>
									<m_Fields>
										<m_Values>
											<Element class="AssetObjects..CollectionValue">
												<m_eObjectType>INVALID</m_eObjectType>
												<m_eValueType>ARTDEF_REF</m_eValueType>
												<m_Values>
					""");
			builder.append(civTypes.stream().map(civ -> new ST("""
															<Element class="AssetObjects..ArtDefReferenceValue">
																<m_ElementName text="LEADER_IMP_$modName$"/>
																<m_RootCollectionName text="Civilization"/>
																<m_ArtDefPath text="Civilizations.artdef"/>
																<m_CollectionIsLocked>true</m_CollectionIsLocked>
																<m_TemplateName text=""/>
																<m_ParamName text="Civ$randStr$"/>
															</Element>
							""", '$', '$')
							.add("modName", ModHeaderGenerator.buildName(civ.selectedCards).toUpperCase())
							.add("randStr", UUID.randomUUID().toString().replaceAll("-", "")).render())
					.collect(Collectors.joining()));
			builder.append(new ST("""
												</m_Values>
												<m_ParamName text="Civilizations"/>
												<m_AppendMergedParameterCollections>true</m_AppendMergedParameterCollections>
											</Element>
										</m_Values>
									</m_Fields>
									<m_ChildCollections />
									<m_Name text="$culture$"/>
									<m_AppendMergedParameterCollections>true</m_AppendMergedParameterCollections>
								</Element>
					""", '$', '$').add("culture", culture).render());
		});
	}
}

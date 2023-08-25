package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import java.util.List;

public class ArtDefReferenceValue extends AssetObject {
	public final String elementName;
	public final String rootCollectionName;
	public final String artDefPath;
	public final boolean collectionIsLocked;
	public final String templateName;

	public ArtDefReferenceValue(String paramName,
								String elementName,
								String rootCollectionName,
								String artDefPath,
								boolean collectionIsLocked,
								String templateName) {
		super(paramName);
		this.elementName = elementName;
		this.rootCollectionName = rootCollectionName;
		this.artDefPath = artDefPath;
		this.collectionIsLocked = collectionIsLocked;
		this.templateName = templateName;
	}

	public ArtDefReferenceValue(String paramName, String elementName, String rootCollectionName, String artDefPath) {
		this(paramName, elementName, rootCollectionName, artDefPath, true, "");
	}

	@Override
	protected List<ElementEntry> getEntries() {
		return List.of(
				new ElementEntry("m_ElementName", elementName),
				new ElementEntry("m_RootCollectionName", rootCollectionName),
				new ElementEntry("m_ArtDefPath", artDefPath),
				new TextNodeEntry("m_CollectionIsLocked", Boolean.toString(collectionIsLocked)),
				new ElementEntry("m_TemplateName", templateName)
		);
	}

	@Override
	protected String getAssetObjectClass() {
		return "AssetObjects..ArtDefReferenceValue";
	}
}

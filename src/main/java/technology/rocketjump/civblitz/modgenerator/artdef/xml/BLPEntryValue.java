package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import java.util.List;

public class BLPEntryValue extends AssetObject {

	public final String name;
	public final String xlpClass;
	public final String xlpPath;
	public final String blpPackage;
	public final String libraryName;

	public BLPEntryValue(String paramName,
						 String name,
						 String xlpClass,
						 String xlpPath,
						 String blpPackage,
						 String libraryName) {
		super(paramName);
		this.name = name;
		this.xlpClass = xlpClass;
		this.xlpPath = xlpPath;
		this.blpPackage = blpPackage;
		this.libraryName = libraryName;
	}

	@Override
	protected List<ElementEntry> getEntries() {
		return List.of(
				new ElementEntry("m_EntryName", name),
				new ElementEntry("m_XLPClass", xlpClass),
				new ElementEntry("m_XLPPath", xlpPath),
				new ElementEntry("m_BLPPackage", blpPackage),
				new ElementEntry("m_LibraryName", libraryName)
		);
	}

	@Override
	protected String getAssetObjectClass() {
		return "AssetObjects..BLPEntryValue";
	}
}

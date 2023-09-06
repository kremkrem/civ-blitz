package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import java.util.List;

public class BoolValue extends AssetObject{

	private final boolean bValue;

	public BoolValue(String paramName, boolean bValue) {
		super(paramName);
		this.bValue = bValue;
	}

	@Override
	protected List<XmlNode> getEntries() {
		return List.of(new TextNodeEntry("m_bValue", Boolean.toString(bValue)));
	}

	@Override
	protected String getAssetObjectClass() {
		return "AssetObjects..BoolValue";
	}
}

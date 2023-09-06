package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import java.util.List;

public class IntValue extends AssetObject{
	private final int nValue;

	public IntValue(String paramName, int nValue) {
		super(paramName);
		this.nValue = nValue;
	}

	@Override
	protected List<XmlNode> getEntries() {
		return List.of(new TextNodeEntry("m_nValue", Integer.toString(nValue)));
	}

	@Override
	protected String getAssetObjectClass() {
		return "AssetObjects..IntValue";
	}
}

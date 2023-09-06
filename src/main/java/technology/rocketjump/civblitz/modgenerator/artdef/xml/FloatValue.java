package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import java.util.List;

public class FloatValue extends AssetObject {

	private final float fValue;

	public FloatValue(String paramName, float fValue) {
		super(paramName);
		this.fValue = fValue;
	}

	@Override
	protected List<XmlNode> getEntries() {
		return List.of(new TextNodeEntry("m_fValue", String.format("%.6f", fValue)));
	}

	@Override
	protected String getAssetObjectClass() {
		return "AssetObjects..FloatValue";
	}
}

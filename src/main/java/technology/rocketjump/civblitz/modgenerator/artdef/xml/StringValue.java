package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import java.util.List;

public class StringValue extends AssetObject{

	public final String value;

	public StringValue(String paramName, String value) {
		super(paramName);
		this.value = value;
	}

	@Override
	protected List<XmlNode> getEntries() {
		return List.of(new TextEntry("m_Value", value));
	}

	@Override
	protected String getAssetObjectClass() {
		return "AssetObjects..StringValue";
	}
}

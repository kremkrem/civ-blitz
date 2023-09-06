package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import java.util.List;

public class RGBValue extends AssetObject{

	private final float r, g, b;

	public RGBValue(String paramName, float r, float g, float b) {
		super(paramName);
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public RGBValue(String paramName) {
		this(paramName, 255.f, 255.f, 255.f);
	}

	@Override
	protected List<XmlNode> getEntries() {
		return List.of(
				new TextNodeEntry("m_r", toStr(r)),
				new TextNodeEntry("m_g", toStr(g)),
				new TextNodeEntry("m_b", toStr(b))
		);
	}

	@Override
	protected String getAssetObjectClass() {
		return "AssetObjects..RGBValue";
	}

	private String toStr(float f) {
		return String.format("%.6f", f);
	}
}

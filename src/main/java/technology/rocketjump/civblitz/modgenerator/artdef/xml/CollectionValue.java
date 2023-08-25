package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.List;

public class CollectionValue extends AssetObject{

	public final String eObjectType;
	public final String eValueType;
	public final List<XmlNode> values;
	public final boolean appendMergedParameterCollections;

	public CollectionValue(String paramName,
						   String eObjectType,
						   String eValueType,
						   List<XmlNode> values,
						   boolean appendMergedParameterCollections) {
		super(paramName);
		this.eObjectType = eObjectType;
		this.eValueType = eValueType;
		this.values = Collections.unmodifiableList(values);
		this.appendMergedParameterCollections = appendMergedParameterCollections;
	}

	@Override
	protected List<XmlNode> getEntries() {
		return List.of(
				new TextNodeEntry("m_eObjectType", eObjectType),
				new TextNodeEntry("m_eValueType", eValueType),
				new CollectionEntry("m_Values", values)
				//new TextNodeEntry("m_AppendMergedParameterCollections", appendMergedParameterCollections)
		);
	}

	@Override
	protected String getAssetObjectClass() {
		return "AssetObjects..CollectionValue";
	}

	@Override
	public Element toElement(Document document) {
		Element e = super.toElement(document);
		e.appendChild(new TextNodeEntry("m_AppendMergedParameterCollections",
				Boolean.toString(appendMergedParameterCollections)).toElement(document));

		return e;
	}
}

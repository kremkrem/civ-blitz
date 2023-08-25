package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.List;

public class CivElement implements XmlNode{
	public final String name;
	public final List<AssetObject> fields;
	public final List<Collection> childCollections;
	public final boolean appendMergedParameterCollections;

	public CivElement(String name,
					  List<AssetObject> fields,
					  List<Collection> childCollections,
					  boolean appendMergedParameterCollections) {
		this.name = name;
		this.fields = Collections.unmodifiableList(fields);
		this.childCollections = Collections.unmodifiableList(childCollections);
		this.appendMergedParameterCollections = appendMergedParameterCollections;
	}

	public CivElement(String name, List<AssetObject> fields, List<Collection> childCollections) {
		this(name, fields, childCollections, false);
	}

	public CivElement(String name) {
		this(name, List.of(), List.of());
	}

	@Override
	public Element toElement(Document document) {
		Element e = document.createElement("Element");

		Element valuesElement = document.createElement("m_Values");
		for (AssetObject field : fields) {
			valuesElement.appendChild(field.toElement(document));
		}
		Element fieldsElement = document.createElement("m_Fields");
		fieldsElement.appendChild(valuesElement);
		e.appendChild(fieldsElement);

		Element childCollectionsElement = document.createElement("m_ChildCollections");
		for (Collection collection : childCollections) {
			childCollectionsElement.appendChild(collection.toElement(document));
		}
		e.appendChild(childCollectionsElement);

		Element nameElement = document.createElement("m_Name");
		nameElement.setAttribute("text", name);
		e.appendChild(nameElement);

		Element isAppendElement = document.createElement("m_AppendMergedParameterCollections");
		isAppendElement.appendChild(document.createTextNode(Boolean.toString(appendMergedParameterCollections)));
		e.appendChild(isAppendElement);

		return e;
	}
}

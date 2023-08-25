package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.List;

public class Collection implements XmlNode{

	public final String name;
	public final boolean replaceMergedCollectionElements;
	public final List<CivElement> civElements;

	public Collection(String name, boolean replaceMergedCollectionElements, List<CivElement> civElements) {
		this.name = name;
		this.replaceMergedCollectionElements = replaceMergedCollectionElements;
		this.civElements = Collections.unmodifiableList(civElements);
	}

	public Collection(String name, List<CivElement> civElements) {
		this(name, false, civElements);
	}

	public Collection(String name) {
		this(name, List.of());
	}

	@Override
	public Element getDomElement(Document document) {
		Element e = document.createElement("Element");

		Element collectionName = document.createElement("m_CollectionName");
		collectionName.setAttribute("text", name);
		e.appendChild(collectionName);

		Element isReplace = document.createElement("m_ReplaceMergedCollectionElements");
		isReplace.appendChild(document.createTextNode(Boolean.toString(replaceMergedCollectionElements)));
		e.appendChild(isReplace);

		for (CivElement civElement : civElements) {
			e.appendChild(civElement.getDomElement(document));
		}

		return e;
	}
}

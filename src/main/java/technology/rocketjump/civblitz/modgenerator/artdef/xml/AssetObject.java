package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

abstract class AssetObject implements XmlNode {

	public final String paramName;

	public AssetObject(String paramName) {
		this.paramName = paramName;
	}

	protected record TextEntry(String tag, String text) implements XmlNode {

		@Override
		public Element toElement(Document document) {
			Element e = document.createElement(tag);
			e.setAttribute("text", text);
			return e;
		}
	}

	protected record TextNodeEntry(String tag, String text) implements XmlNode {
		@Override
		public Element toElement(Document document) {
			Element e = document.createElement(tag);
			e.appendChild(document.createTextNode(text));
			return e;
		}
	}

	protected record CollectionEntry(String tag, List<XmlNode> entries) implements XmlNode {
		@Override
		public Element toElement(Document document) {
			Element e = document.createElement(tag);
			for (XmlNode node : entries) {
				e.appendChild(node.toElement(document));
			}
			return e;
		}
	}

	protected abstract List<XmlNode> getEntries();

	protected abstract String getAssetObjectClass();

	@Override
	public Element toElement(Document document) {
		Element e = document.createElement("Element");
		e.setAttribute("class", getAssetObjectClass());

		for (XmlNode entry : getEntries()) {
			e.appendChild(entry.toElement(document));
		}
		e.appendChild(new TextEntry("m_ParamName", paramName).toElement(document));

		return e;
	}
}

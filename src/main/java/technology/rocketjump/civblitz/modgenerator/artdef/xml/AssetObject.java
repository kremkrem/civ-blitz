package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Objects;

abstract class AssetObject implements XmlNode {

	public final String paramName;

	public AssetObject(String paramName) {
		this.paramName = paramName;
	}

	protected static class ElementEntry {
		public final String tag;
		public final String text;

		protected ElementEntry(String tag, String text) {
			this.tag = tag;
			this.text = text;
		}

		public Element toElement(Document document) {
				Element e = document.createElement(tag);
				e.setAttribute("text", text);
				return e;
			}
		}

	protected static class TextNodeEntry extends ElementEntry {

		protected TextNodeEntry(String tag, String text) {
			super(tag, text);
		}

		@Override
		public Element toElement(Document document) {
			Element e = document.createElement(tag);
			e.appendChild(document.createTextNode(text));
			return e;
		}
	}

	protected abstract List<ElementEntry> getEntries();

	protected abstract String getAssetObjectClass();

	@Override
	public Element getDomElement(Document document) {
		Element e = document.createElement("Element");
		e.setAttribute("class", getAssetObjectClass());

		for (ElementEntry entry : getEntries()) {
			e.appendChild(entry.toElement(document));
		}
		e.appendChild(new ElementEntry("m_ParamName", paramName).toElement(document));

		return e;
	}
}

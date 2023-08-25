package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import technology.rocketjump.civblitz.modgenerator.BlitzFileGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;

public abstract class ArtDefGenerator extends BlitzFileGenerator {
	@Autowired
	XmlBuildersProvider xmlBuildersProvider;

	@Override
	public String getFileContents(ModHeader modHeader, List<ModdedCivInfo> civs) {
		Document document = xmlBuildersProvider.getDocumentBuilder().newDocument();
		Element root = document.createElement("AssetObjects..ArtDefSet");

		root.appendChild(version(document));
		Element templateName = document.createElement("m_TemplateName");
		templateName.setAttribute("text", getTemplateName());
		root.appendChild(templateName);
		for (Collection collection : getRootCollections()) {
			root.appendChild(collection.getDomElement(document));
		}

		DOMSource domSource = new DOMSource(document);
		try {
			Transformer transformer = xmlBuildersProvider.getTransformer();
			StringWriter writer = new StringWriter();
			StreamResult streamResult = new StreamResult(writer);
			transformer.transform(domSource, streamResult);
			return writer.toString();
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		return getFileContents(modHeader, List.of(civInfo));
	}

	protected String getTemplateName() {
		return "";
	}

	protected abstract List<Collection> getRootCollections();

	private Element version(Document document) {
		Element v = document.createElement("m_Version");

		Element major = document.createElement("major");
		major.appendChild(document.createTextNode("4"));
		v.appendChild(major);

		Element minor = document.createElement("minor");
		minor.appendChild(document.createTextNode("0"));
		v.appendChild(minor);

		Element build = document.createElement("build");
		build.appendChild(document.createTextNode("762"));
		v.appendChild(build);

		Element revision = document.createElement("revision");
		revision.appendChild(document.createTextNode("531"));
		v.appendChild(revision);

		return v;
	}
}

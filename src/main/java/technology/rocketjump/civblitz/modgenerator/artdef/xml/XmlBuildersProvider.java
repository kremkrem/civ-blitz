package technology.rocketjump.civblitz.modgenerator.artdef.xml;

import org.springframework.stereotype.Component;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

@Component
public class XmlBuildersProvider {
	private final DocumentBuilder documentBuilder;
	private final Transformer transformer;

	public XmlBuildersProvider() {
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		} catch (ParserConfigurationException | TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public DocumentBuilder getDocumentBuilder() {
		return documentBuilder;
	}

	public Transformer getTransformer() {
		return transformer;
	}
}

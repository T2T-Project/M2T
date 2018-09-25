package ma.emi.est.mde.xmi.visitors;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ma.emi.est.mde.xmi.abstraction.Model;

public abstract class Parser {
	
	private FileInputStream source;
	private Model model;
	private String UMLVersion;
	private String xmiNsURI;
	private String umlNsURI;
	protected static Parser parser = null;

	public void setSource(FileInputStream s) {
		this.source = s;
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public FileInputStream getSource() {
		return source;
	}

	public String getUMLVersion() {
		return UMLVersion;
	}

	public void setUMLVersion(String uMLVersion) {
		UMLVersion = uMLVersion;
	}
	
	public abstract Object parse() throws Exception;

}

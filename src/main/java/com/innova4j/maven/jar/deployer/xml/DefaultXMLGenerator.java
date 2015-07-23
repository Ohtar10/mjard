package com.innova4j.maven.jar.deployer.xml;

import java.nio.file.Path;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.innova4j.maven.jar.deployer.xml.domain.Model;
import com.innova4j.maven.jar.deployer.xml.domain.ObjectFactory;

/**
 * The Class DefaultXMLGenerator.
 *
 * @author Luis Eduardo Ferro Diez - Innova4j
 */
public class DefaultXMLGenerator implements XMLGenerator {

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(XMLGenerator.class.getSimpleName());
	
	/** The jaxb context. */
	private JAXBContext jaxbContext;
	
	/**
	 * Instantiates a new default xml generator.
	 */
	public DefaultXMLGenerator() {
		try {
			jaxbContext = JAXBContext.newInstance(Model.class);
		} catch (JAXBException e) {
			LOG.error(e);
		}
		
	}
	
	/**
	 * Generate dependency pom.
	 *
	 * @param project the project
	 * @param destination the destination
	 */
	@Override
	public void generateDependencyPom(Model project, Path destination) {
		try {						
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(new ObjectFactory().createProject(project), destination.toFile());
		} catch (JAXBException e) {
			LOG.error(e);
		}

	}
}

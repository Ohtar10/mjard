package org.mjard.maven.jar.deployer.xml;

import java.nio.file.Path;

import org.mjard.maven.jar.deployer.xml.domain.Model;

/**
 * The Interface XMLGenerator.
 *
 * @author Luis Eduardo Ferro Diez - Innova4j
 */
public interface XMLGenerator {

	/**
	 * Generate dependency pom.
	 *
	 * @param project the project
	 * @param destination the destination
	 */
	void generateDependencyPom(Model project, Path destination);
	
}

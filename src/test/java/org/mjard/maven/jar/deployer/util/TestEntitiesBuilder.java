package org.mjard.maven.jar.deployer.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * The Class TestEntitiesBuilder.
 *
 * @author Luis Eduardo Ferro Diez
 */
public class TestEntitiesBuilder {

	/**
	 * Gets the test jar files.
	 *
	 * @return the test jar files
	 */
	public static List<Path> getTestDefaultJarFiles(){
		List<Path> testJars = new ArrayList<>();
		testJars.add(Paths.get("adf-controller-api.jar"));
		testJars.add(Paths.get("adf-controller-rt-common.jar"));
		testJars.add(Paths.get("adf-controller.jar"));
		testJars.add(Paths.get("adf-desktop-integration.jar"));
		testJars.add(Paths.get("adf-desktop-integration-model-api.jar"));
		testJars.add(Paths.get("javax.mail.jar"));
		testJars.add(Paths.get("groovy-all-1.6.3.jar"));
		return testJars;
	}
	
	/**
	 * Gets the test default maven jar files.
	 *
	 * @return the test default maven jar files
	 */
	public static List<Path> getTestDefaultMavenJarFiles(){
		List<Path> testJars = new ArrayList<>();
		testJars.add(Paths.get("adf-controller-api-11.1.1.jar"));
		testJars.add(Paths.get("adf-controller-rt-common-11.1.1.jar"));
		testJars.add(Paths.get("adf-controller-11.1.1.jar"));
		testJars.add(Paths.get("adf-desktop-integration-11.1.1.jar"));
		testJars.add(Paths.get("adf-desktop-integration-model-api-11.1.1.jar"));
		testJars.add(Paths.get("javax.mail-11.1.1.7.0.jar"));
		testJars.add(Paths.get("groovy-all-1.6.3.jar"));
		return testJars;
	}
}

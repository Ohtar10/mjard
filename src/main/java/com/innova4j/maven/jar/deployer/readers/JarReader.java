package com.innova4j.maven.jar.deployer.readers;

import java.nio.file.Path;
import java.util.Set;

/**
 * Jar Reader to obtain all the jars in a certain directory
 * and the child of him.
 * @author Luis Eduardo Ferro Diez - Innova4j Development Team
 *
 */
public interface JarReader {

	/**
	 * Starting from the given root Path, it will
	 * gather all the paths to the jar files inside
	 * the given directory and his children.
	 * @param root
	 * @return
	 */
	Set<Path> getJarsFromRootPath(Path root);
}

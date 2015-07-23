package org.mjd.deployers;

import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The Interface MavenJarDeployer.
 *
 * @author Luis Eduardo Ferro Diez
 */
public interface JarDeployer {

	Logger LOG = Logger.getLogger(JarDeployer.class.getSimpleName());
	/**
	 * Deploy jars.
	 * This version export the dependencies in maven format
	 * in a separate given directory based on the implementation.
	 * 
	 * Is made default to do nothing to avoid having the implementations
	 * of this interface to implement a method that may not be suitable for
	 * his purposes.
	 * 
	 * @param groupId the group id
	 * @param masterVersion the master version
	 * @param outputDir the output dir
	 * @param jarList the jar list
	 */
	int deployJars(String groupId, String masterVersion, Path outputDir, Set<Path> jarList);
}

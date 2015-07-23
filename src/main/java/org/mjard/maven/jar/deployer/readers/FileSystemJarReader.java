package org.mjard.maven.jar.deployer.readers;

import static org.mjard.maven.jar.deployer.utils.LogUtils.logEndProcess;
import static org.mjard.maven.jar.deployer.utils.LogUtils.logExceptionInProgress;
import static org.mjard.maven.jar.deployer.utils.LogUtils.logJarFound;
import static org.mjard.maven.jar.deployer.utils.LogUtils.logStartProcess;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * The Class FileSystemJarReader.
 *
 * @author Luis Eduardo Ferro Diez
 */
public class FileSystemJarReader implements JarReader {

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger("");
	
	/** The jar matcher. */
	private PathMatcher jarMatcher = FileSystems.getDefault().getPathMatcher("glob:"+"**.jar");
	/** The jars in path. */
	private Set<Path> jarsInPath;
	
	/**
	 * Gets the jars from root path.
	 *
	 * @param root the root
	 * @return the jars from root path
	 */
	@Override
	public Set<Path> getJarsFromRootPath(Path root) {		
		jarsInPath = new HashSet<>();
		try {
			Files.walkFileTree(root, new JarsFileVisitor());
		} catch (IOException e) {
			LOG.error(logExceptionInProgress("Getting jars from: "+root.toString(), e.getMessage()));
		}
		return jarsInPath;
	}

	/**
	 * File Visitor implementation to walk the directory tree
	 * based on the received root path and locate the jar
	 * files within.
	 * 
	 * @author Luis Eduardo Ferro - Innova4j
	 *
	 */
	private class JarsFileVisitor extends SimpleFileVisitor<Path>{
		
		/** The Constant ACTION. */
		private static final String ACTION = "look for files in: {0}";
		
		/**
		 * Pre visit directory.
		 *
		 * @param dir the dir
		 * @param attrs the attrs
		 * @return the file visit result
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			LOG.info(logStartProcess(MessageFormat.format(ACTION, dir.toString())));
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Visit file failed.
		 *
		 * @param file the file
		 * @param exc the exc
		 * @return the file visit result
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			LOG.error(logExceptionInProgress(MessageFormat.format(ACTION, file.toString())), exc);
			return super.visitFileFailed(file, exc);
		}

		/**
		 * Post visit directory.
		 *
		 * @param dir the dir
		 * @param exc the exc
		 * @return the file visit result
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			LOG.info(logEndProcess(MessageFormat.format(ACTION, dir.toString())));
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Visit file.
		 *
		 * @param file the file
		 * @param attrs the attrs
		 * @return the file visit result
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			if(jarMatcher.matches(file)){
				LOG.info(logJarFound(file.toString()));
				jarsInPath.add(file);
			}
			return FileVisitResult.CONTINUE;
		}
		
	}
}

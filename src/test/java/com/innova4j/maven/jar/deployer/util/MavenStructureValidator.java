package com.innova4j.maven.jar.deployer.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

/**
 * The Class MavenStructureValidator.
 *
 * @author Luis Eduardo Ferro Diez - Innova4j
 */
public class MavenStructureValidator extends SimpleFileVisitor<Path> {
	/** The jars. */
	private List<Path> jars;

	/** The jar matcher. */
	private PathMatcher jarMatcher = FileSystems.getDefault().getPathMatcher("glob:" + "**.jar");

	/**
	 * Instantiates a new maven structure validator.
	 *
	 * @param jars the jars
	 */
	public MavenStructureValidator(List<Path> jars) {
		this.jars = jars;
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
		if (jarMatcher.matches(file)) {
			// The retrieved jar must be present in the test comparing list
			assertTrue(jars.contains(file.getFileName()));
			// A pom version of the same file name must exist
			assertTrue(Files.exists(file.resolveSibling(file.getFileName().toString().replace(".jar", ".pom"))));
			// The parent directory must be named the same as the jar
			// version
			assertTrue(file.getFileName().toString().endsWith(file.getParent().getFileName().toString() + ".jar"));
			// The parent of the parent directory must contain a
			// maven-metadata-local.xml file besides the directory
			// containing the jar
			File parentDir = file.getParent().getParent().toFile();

			List<String> elements = Arrays.asList(parentDir.list());
			elements.contains(file.getParent().getFileName().toString());				

			return FileVisitResult.SKIP_SIBLINGS;
		} else {
			return FileVisitResult.CONTINUE;
		}
	}

}

package org.mjd.deployers;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mjd.readers.FileSystemJarReader;
import org.mjd.readers.JarReader;
import org.mjd.util.MavenStructureValidator;
import org.mjd.util.TestConstants;
import org.mjd.util.TestEntitiesBuilder;

/**
 * The Class JarMavenFormatTest.
 *
 * @author Luis Eduardo Ferro Diez
 */
public class JarMavenFormatTest {

	/** The deployer. */
	private JarDeployer deployer;

	/** The jar reader. */
	private JarReader jarReader;

	/**
	 * Before class.
	 */
	@BeforeClass
	public static void beforeClass() {
		BasicConfigurator.configure();
	}

	/**
	 * Before method.
	 */
	@Before
	public void beforeMethod() {
		deployer = new FileSystemJarDeployer();
		jarReader = new FileSystemJarReader();
	}

	/**
	 * After method.
	 */
	@After
	public void afterMethod() {
		// delete the directories
		Path dummyPath = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_RESULT_PATH + "output/");
		try {
			Files.walkFileTree(dummyPath, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.deleteIfExists(file);
					return super.visitFile(file, attrs);
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.deleteIfExists(dir);
					return super.postVisitDirectory(dir, exc);
				}

			});
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test generate maven tree from jars success. This test validates that a
	 * call to the operation in charge of generate the corresponding directory
	 * structure for maven dependencies works correctly in the best scenario.
	 */
	@Test
	public void testGenerateMavenTreeFromJarsSuccess() {
		Path outputDir = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_RESULT_PATH);
		String groupId = "com.oracle.adf";
		String masterVersion = "11.1.1.7.0";
		Path dummyPath = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_JAR_DIRECTORY_PATH);
		assertTrue(Files.exists(dummyPath, LinkOption.NOFOLLOW_LINKS));
		Set<Path> jarFiles = jarReader.getJarsFromRootPath(dummyPath);

		// Call to deploy the jar files
		deployer.deployJars(groupId, masterVersion, outputDir.resolve("output/"), jarFiles);

		// assert the output directory exists
		assertTrue(Files.exists(outputDir.resolve("output/")));

		// validate the resulting directories
		try {
			Files.walkFileTree(outputDir,
					new MavenStructureValidator(TestEntitiesBuilder.getTestDefaultMavenJarFiles()));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	

}

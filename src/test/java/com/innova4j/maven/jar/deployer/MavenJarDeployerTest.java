package com.innova4j.maven.jar.deployer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.After;
import org.junit.Test;

import com.innova4j.maven.jar.deployer.util.MavenStructureValidator;
import com.innova4j.maven.jar.deployer.util.TestConstants;
import com.innova4j.maven.jar.deployer.util.TestEntitiesBuilder;


/**
 * The Class MavenJarDeployerTest.
 *
 * @author Luis Eduardo Ferro Diez - Innova4j
 */
public class MavenJarDeployerTest {

	
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
			//Do Nothing
		}
	}
	
	
	/**
	 * Test deploy jars.
	 * Tests the main program calling the
	 * help.
	 * The help text will be on stdout only not the log file, this test
	 * only exercise that portion of code plus the log by file output.
	 */
	@Test
	public void testDeployJarsHelp(){
		try {
			Path log = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_RESULT_PATH+"mjd.log");
			MavenJarDeployer.main(new String[]{"-h","-l",log.toString()});			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test deploy jars unrecognized params.
	 * In this test, the program is called
	 * with wrong or unrecognized args hence
	 * a message indicating the problem has to
	 * be raised suggesting use the -h command. 
	 */
	@Test
	public void testDeployJarsUnrecognizedParams(){
		try {			
			MavenJarDeployer.main(new String[]{"-pepe","hola mundo"});
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test deploy jars no params.
	 * In this test, the program is called with no arguments
	 * the expected output is the same message as the help
	 * plus a message indicating that a no args called
	 * is not allowed.
	 */
	@Test
	public void testDeployJarsNoParams(){
		try {			
			MavenJarDeployer.main(null);			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test deploy jars success.
	 * In this test, the program is called with all the necessary
	 * arguments hence a success answer is expected and the
	 * output must be valid. 
	 */
	@Test
	public void testDeployJarsSuccess(){
		try {
			Path log = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_RESULT_PATH+"mjd.log");
			Path jarPath = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_JAR_DIRECTORY_PATH);
			Path outputPath = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_RESULT_PATH + "output/");
			String groupId = "com.oracle.adf";
			String masterVersion = "11.1.1.7.0";
			MavenJarDeployer.main(new String[]{"-groupId",groupId,"-masterVersion",masterVersion,"-d",jarPath.toString(),"-o",outputPath.toString(),"-l",log.toString()});
			assertTrue(Files.exists(log));
			assertTrue(Files.deleteIfExists(log));
			// validate the resulting directories
			try {
				Files.walkFileTree(outputPath,
						new MavenStructureValidator(TestEntitiesBuilder.getTestDefaultMavenJarFiles()));
			} catch (IOException e) {
				fail(e.getMessage());
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	/**
	 * Test deploy jars multiple sources success.
	 * In this test, it is given more than one
	 * directory of jars so the program must walk them
	 * all finding the jars.
	 */
	@Test
	public void testDeployJarsMultipleSourcesSuccess(){
		try {
			Path log = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_RESULT_PATH+"mjd.log");
			Path jarPath1 = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_JAR_DIRECTORY_PATH+"oracle.adf.controller_11.1.1/");
			Path jarPath2 = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_JAR_DIRECTORY_PATH+"oracle.adf.desktopintegration.model_11.1.1/");
			Path outputPath = Paths.get(System.getProperty("user.dir") + TestConstants.DUMMY_RESULT_PATH + "output/");
			String groupId = "com.oracle.adf";
			String masterVersion = "11.1.1.7.0";
			MavenJarDeployer.main(new String[]{"-groupId",groupId,"-masterVersion",masterVersion,"-d",jarPath1.toString()+", "+jarPath2.toString(),"-o",outputPath.toString(),"-l",log.toString()});
			assertTrue(Files.exists(log));
			assertTrue(Files.deleteIfExists(log));
			// validate the resulting directories
			try {
				Files.walkFileTree(outputPath,
						new MavenStructureValidator(TestEntitiesBuilder.getTestDefaultMavenJarFiles()));
			} catch (IOException e) {
				fail(e.getMessage());
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
		
}

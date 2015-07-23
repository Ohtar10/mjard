package com.innova4j.maven.jar.deployer.readers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.innova4j.maven.jar.deployer.readers.FileSystemJarReader;
import com.innova4j.maven.jar.deployer.readers.JarReader;
import com.innova4j.maven.jar.deployer.util.TestConstants;
import com.innova4j.maven.jar.deployer.util.TestEntitiesBuilder;

/**
 * Class that validates the operations to gather jar files
 * within a given root directory.
 * @author Luis Eduardo Ferro - Innova4j Dev Team
 *
 */
public class JarReaderTest {

	private JarReader jarReader;
	
	@BeforeClass
	public static void beforeClass(){
		BasicConfigurator.configure();
	}
	
	@Before
	public void beforeMethod(){		
		jarReader = new FileSystemJarReader();
	}
	/**
	 * This test validates that in a given dummy root path
	 * it can gather all jars inside this and his child 
	 * directories.
	 * 
	 * Inside the dummy directory there are files and sub directories
	 * not related with jar files hence this should be ignored
	 */
	@Test
	public void testGatherJarFiles(){
		List<Path> comparingResult = TestEntitiesBuilder.getTestDefaultJarFiles();
		Path dummyPath = Paths.get(System.getProperty("user.dir")+TestConstants.DUMMY_JAR_DIRECTORY_PATH);
		assertTrue(Files.exists(dummyPath, LinkOption.NOFOLLOW_LINKS));
		Set<Path> jarFiles = jarReader.getJarsFromRootPath(dummyPath);
		assertNotNull(jarFiles);
		assertFalse(jarFiles.isEmpty());		
		assertEquals(comparingResult.size(), jarFiles.size());
		
		//check that all elements are present in booth lists
		//as the result of the operation will obtain full paths for all the jar files
		//map to only get the filenames.
		List<Path> pathNames = jarFiles.stream().map(p -> p.getFileName()).collect(Collectors.toList());
		
		assertTrue(comparingResult.containsAll(pathNames));
	}
	
}

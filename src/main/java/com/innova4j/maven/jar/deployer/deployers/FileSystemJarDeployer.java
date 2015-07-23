package com.innova4j.maven.jar.deployer.deployers;

import static com.innova4j.maven.jar.deployer.utils.Constants.EXAMPLE_POM_DESCRIPTION;
import static com.innova4j.maven.jar.deployer.utils.Constants.GENERATED_POM_DESCRIPTION;
import static com.innova4j.maven.jar.deployer.utils.Constants.MODEL_VERSION;
import static com.innova4j.maven.jar.deployer.utils.LogUtils.logEndProcess;
import static com.innova4j.maven.jar.deployer.utils.LogUtils.logJarAlreadyDeployedWarn;
import static com.innova4j.maven.jar.deployer.utils.LogUtils.logStartProcess;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.innova4j.maven.jar.deployer.xml.DefaultXMLGenerator;
import com.innova4j.maven.jar.deployer.xml.XMLGenerator;
import com.innova4j.maven.jar.deployer.xml.domain.Dependency;
import com.innova4j.maven.jar.deployer.xml.domain.Model;
import com.innova4j.maven.jar.deployer.xml.domain.Model.Dependencies;


/**
 * The Class FileSystemJarDeployer.
 *
 * @author Luis Eduardo Ferro Diez - Innova4j
 */
public class FileSystemJarDeployer implements JarDeployer {

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(FileSystemJarDeployer.class.getSimpleName());

	/** The Constant ACTION. */
	private static final String ACTION = "export jars in maven format";
	
	/** The Constant ACTION_EXP_JAR. */
	private static final String ACTION_EXP_JAR = "export {0} dependency to maven format";
	
	/** The Constant JAR_FILE_VERSION_PATTERN. */
	private static final String JAR_FILE_VERSION_PATTERN = ".+[_/-]([\\d\\.-]+)\\.jar$";

	/** The Constant PARENT_DIR_VERSION_PATTERN. */
	private static final String PARENT_DIR_VERSION_PATTERN = ".+[_/-]([\\d\\.-]+)$";

	/** The Constant JAR_FILE_NAME_PATTERN. */
	private static final String JAR_FILE_NAME_PATTERN = "([\\-_][\\d\\.]+)?.jar$";

	/** The jar file version pattern. */
	private Pattern jarFileVersionPattern;

	/** The parent dir version pattern. */
	private Pattern parentDirVersionPattern;

	/** The version lookup. */
	private BiFunction<String, Pattern, String> versionLookup;

	/** The xml generator. */
	private XMLGenerator xmlGenerator;
	/**
	 * Instantiates a new file system jar deployer.
	 */
	public FileSystemJarDeployer() {
		xmlGenerator = new DefaultXMLGenerator();
		jarFileVersionPattern = Pattern.compile(JAR_FILE_VERSION_PATTERN);
		parentDirVersionPattern = Pattern.compile(PARENT_DIR_VERSION_PATTERN);
		versionLookup = (path, pattern) -> {
			Matcher matcher = pattern.matcher(path);
			if (matcher.matches()) {
				return matcher.group(1);
			}
			return null;
		};
	}

	/**
	 * Deploy jars.
	 *
	 * @param groupId the group id
	 * @param masterVersion the master version
	 * @param outputDir the output dir
	 * @param jarSet the jar list
	 */
	@Override
	public int deployJars(String groupId, String masterVersion, Path outputDir, Set<Path> jarSet) {
		final AtomicInteger deployedJars = new AtomicInteger(0);
		try {
			LOG.info(logStartProcess(ACTION));			
			// To guarantee that there will no file conflicts, clean the
			// output directory before proceeding
			if(Files.exists(outputDir)){
				Files.walkFileTree(outputDir, new SimpleFileVisitor<Path>() {

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
			}
			
			// Create the directories again
			Files.createDirectories(outputDir);
			
			//Prepare a example pom with the usage of all the generated
			//dependencies
			Path examplePom = outputDir.resolve("example_pom.xml");
			Model examplePomModel = new Model();
			Dependencies dependencies = new Dependencies();
			examplePomModel.setGroupId(groupId);
			examplePomModel.setArtifactId("example-pom");
			examplePomModel.setVersion(masterVersion);
			examplePomModel.setName(EXAMPLE_POM_DESCRIPTION);
			examplePomModel.setPackaging("pom");			
			examplePomModel.setDependencies(dependencies);
			
			// Create the directory structure based on the groupId
			String parentStr = groupId.replace('.', File.separatorChar);
			Path parent = outputDir.resolve(parentStr);
			jarSet.forEach(jar -> {
				LOG.info(logStartProcess(MessageFormat.format(ACTION_EXP_JAR, jar.getFileName().toString())));
				// Get a library version from anywhere, if not, use the
				// masterVersion
				String version = getLibraryVersion(jar);
				version = version == null ? masterVersion : version;

				// Create the corresponding directory
				Path dependencyDir = parent.resolve(jar.getFileName().toString()
						.replaceAll(JAR_FILE_NAME_PATTERN, File.separator)
						+ version + File.separator);
				try {
					Files.createDirectories(dependencyDir);

					// Create the final file name
					String dependencyName = jar.getFileName().toString().replaceAll(JAR_FILE_NAME_PATTERN, "");
					// Copy the jar to the destination
					// Check if this dependency has already been deployed
					Path destination = dependencyDir.resolve(dependencyName + "-" + version + ".jar");
					if(Files.exists(destination)){
						LOG.warn(logJarAlreadyDeployedWarn(jar.toString(), destination.toString()));
					}else{
						Files.copy(jar, dependencyDir.resolve(dependencyName + "-" + version + ".jar"), StandardCopyOption.REPLACE_EXISTING);

						// Create also the .pom version of the same file
						Path pom = dependencyDir.resolve(dependencyName + "-" + version + ".pom");
						Model pomDesc = new Model();
						pomDesc.setGroupId(groupId);
						pomDesc.setArtifactId(dependencyName);
						pomDesc.setVersion(version);
						pomDesc.setName(pomDesc.getArtifactId());
						pomDesc.setPackaging("jar");
						pomDesc.setDescription(GENERATED_POM_DESCRIPTION);
						pomDesc.setModelVersion(MODEL_VERSION);
						
						xmlGenerator.generateDependencyPom(pomDesc, pom);

						//create a dependency reference and add it to the example_model
						Dependency dependency = new Dependency();
						dependency.setGroupId(groupId);
						dependency.setArtifactId(dependencyName);
						dependency.setVersion(version);
						examplePomModel.getDependencies().getDependency().add(dependency);
						deployedJars.incrementAndGet();
					}
										
										
				} catch (Exception e) {
					LOG.error(MessageFormat.format("Error exporting jar {0} to file system", jar.getFileName()
							.toString()), e);
				}
				LOG.info(logEndProcess(MessageFormat.format(ACTION_EXP_JAR, jar.getFileName().toString())));
			});
			
			xmlGenerator.generateDependencyPom(examplePomModel, examplePom);

		} catch (IOException e) {
			LOG.error("Error exporting jars to file system", e);
		}
		LOG.info(logEndProcess(ACTION));
		return deployedJars.get();
	}

	/**
	 * Gets the library version. Uses the BiFunction Declared to match the
	 * corresponding regex to the corresponding part of the path
	 * 
	 * @param jar the jar
	 * @return the library version
	 */
	private String getLibraryVersion(Path jar) {
		String result = versionLookup.apply(jar.getFileName().toString(), jarFileVersionPattern);
		if (result == null) {
			result = versionLookup.apply(jar.getParent().toString(), parentDirVersionPattern);
		}
		return result;
	}

}

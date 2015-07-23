package com.innova4j.maven.jar.deployer;

import static com.innova4j.maven.jar.deployer.utils.Constants.INVALID_ARG_MESSAGE;
import static com.innova4j.maven.jar.deployer.utils.Constants.MISSING_ARG_MESSAGE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.innova4j.maven.jar.deployer.deployers.FileSystemJarDeployer;
import com.innova4j.maven.jar.deployer.deployers.JarDeployer;
import com.innova4j.maven.jar.deployer.exceptions.MJDSyntaxException;
import com.innova4j.maven.jar.deployer.readers.FileSystemJarReader;
import com.innova4j.maven.jar.deployer.readers.JarReader;
import com.innova4j.maven.jar.deployer.utils.Chronometer;
import com.innova4j.maven.jar.deployer.utils.LogUtils;

/**
 * The Class MavenJarDeployer. Main Class of the program to deploy random jars
 * inside a given directory to maven format.
 * 
 * @author Luis Eduardo Ferro Diez - Innova4j
 * @version 1.0
 * @since 29/09/2014
 * 
 */
public class MavenJarDeployer {

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(MavenJarDeployer.class.getSimpleName());

	/** The arg help. */
	public static final String ARG_HELP = "h";

	/** The Constant ARG_LOG. */
	public static final String ARG_LOG = "l";

	/** The Constant ARG_GROUPID. */
	public static final String ARG_GROUPID = "groupId";

	/** The Constant ARG_MASTER_VERSION. */
	public static final String ARG_MASTER_VERSION = "masterVersion";

	/** The Constant ARG_JAR_DIR. */
	public static final String ARG_JAR_DIR = "d";

	/** The Constant ARG_OUTPUT_DIR. */
	public static final String ARG_OUTPUT_DIR = "o";

	/** The Constant VALID_ARGS_REGEX. */
	public static final String VALID_ARGS_REGEX = "-(o|h|l|d|groupId|masterVersion)";

	/** The Constant MANDATORY_ARGS. */
	public static final List<String> MANDATORY_ARGS;

	static {
		MANDATORY_ARGS = new ArrayList<>();
		MANDATORY_ARGS.add(ARG_GROUPID);
		MANDATORY_ARGS.add(ARG_MASTER_VERSION);
		MANDATORY_ARGS.add(ARG_JAR_DIR);
		MANDATORY_ARGS.add(ARG_OUTPUT_DIR);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {

		Set<Path> jars = new HashSet<>();
		int deployedJars = 0;
		Properties log4jConfig = null;
		LOG.info(LogUtils.logStartProcess("Process"));
		Chronometer chrono = new Chronometer();		
		chrono.start();
		try {
			Map<String, String> argMap = buildArgMap(args);
			if (argMap.containsKey(ARG_HELP)) {
				printHelp();
				return;
			}			
			validateArgs(argMap);
			log4jConfig = new Properties();
			if (argMap.containsKey(ARG_LOG)) {
				log4jConfig.load(MavenJarDeployer.class.getResourceAsStream("/log4j-file.properties"));
				log4jConfig.replace("log4j.appender.file.File", argMap.get(ARG_LOG));
			} else {
				log4jConfig.load(MavenJarDeployer.class.getResourceAsStream("/log4j-stdout.properties"));
			}

			PropertyConfigurator.configure(log4jConfig);

			// Search for jars then deploy them
			JarReader jarReader = new FileSystemJarReader();
			JarDeployer jarDeployer = new FileSystemJarDeployer();			
			for(String path: argMap.get(ARG_JAR_DIR).split("\\s?,\\s?")){
				jars.addAll(jarReader.getJarsFromRootPath(Paths.get(path)));
			}
			 
			deployedJars = jarDeployer.deployJars(argMap.get(ARG_GROUPID), argMap.get(ARG_MASTER_VERSION),
					Paths.get(argMap.get(ARG_OUTPUT_DIR)),jars);
						
		} catch (MJDSyntaxException e) {
			if(log4jConfig == null){
				log4jConfig = new Properties();
				log4jConfig.load(MavenJarDeployer.class.getResourceAsStream("/log4j-stdout.properties"));
			}
			LOG.error(e);
			System.out.println(e.getMessage() + "\n");
			printHelp();
		}

		chrono.stop();
		LOG.info(LogUtils.logEndProcess("Process"));			
		LOG.info("Summary:");
		LOG.info("Jars Found: "+ jars.size());
		LOG.info("Jars Deployed: "+ deployedJars);
		LOG.info("Time taken: "+ chrono.getTimeFormatted());
	}

	/**
	 * Builds the arg map.
	 *
	 * @param args the args
	 * @return the map
	 */
	private static Map<String, String> buildArgMap(String[] args) throws MJDSyntaxException {
		Map<String, String> argMap = new HashMap<>();
		if (args != null) {
			Pattern argPattern = Pattern.compile(VALID_ARGS_REGEX);
			String key = null;
			for (String arg : args) {
				Matcher matcher = argPattern.matcher(arg);
				if (matcher.matches()) {
					key = matcher.group(1);
					argMap.put(key, null);
				} else if (arg.startsWith("-")) {
					throw new MJDSyntaxException(MessageFormat.format(INVALID_ARG_MESSAGE, arg));
				} else {
					String prev = argMap.get(key);
					argMap.replace(key, prev!=null?prev+arg:arg);
				}
			}
		}
		return argMap;
	}

	/**
	 * Validate args.
	 *
	 * @param argMap the arg map
	 * @throws MJDSyntaxException the MJD syntax exception
	 */
	private static void validateArgs(Map<String, String> argMap) throws MJDSyntaxException {

		List<String> missingArgs = new ArrayList<String>();		
		// Validate required args
		MANDATORY_ARGS.forEach(key -> {			
			if (!argMap.containsKey(key)) {				
				missingArgs.add(key);
			}
		});

		if (!missingArgs.isEmpty()) {			
			throw new MJDSyntaxException(MessageFormat.format(MISSING_ARG_MESSAGE, missingArgs));
		}
	}

	/**
	 * Prints the help.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void printHelp() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				MavenJarDeployer.class.getResourceAsStream("/program_help.txt")));
		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}
	
	

}

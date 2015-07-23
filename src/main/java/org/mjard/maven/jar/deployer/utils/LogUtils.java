package org.mjard.maven.jar.deployer.utils;

import java.text.MessageFormat;

/**
 * The Class LogUtils.
 *
 * @author Luis Eduardo Ferro Diez
 */
public class LogUtils {
	
	/** The Constant BEGIN_PROCESS_MASK. */
	private static final String BEGIN_PROCESS_MASK = "Preparing to {0}.";
	
	/** The Constant EXCEPTION_IN_PROCESS_MASK. */
	private static final String EXCEPTION_IN_PROCESS_MASK = "An Exception ocurred during {0}, exception was: {1}";

	/** The Constant JAR_FOUND_MASK. */
	private static final String JAR_FOUND_MASK = "{0} found!";
	
	/** The Constant END_PROCESS_MASK. */
	private static final String END_PROCESS_MASK = "{0} finished.";
	
	/** The Constant JAR_ALREADY_DEPLOYED_MASK. */
	private static final String JAR_ALREADY_DEPLOYED_MASK = "The jar {0} has already been deployed as {1}, please verify they are effectively different dependencies with the same name";
	
	/**
	 * Log start progress.
	 *
	 * @param arg the arg
	 * @return the string
	 */
	public static final String logStartProcess(String arg){
		return MessageFormat.format(BEGIN_PROCESS_MASK, arg);
	}
	
	/**
	 * Log end process.
	 *
	 * @param arg the arg
	 * @return the string
	 */
	public static final String logEndProcess(String arg){
		return MessageFormat.format(END_PROCESS_MASK, arg);
	}
	
	/**
	 * Log exception in progress.
	 *
	 * @param args the args
	 * @return the string
	 */
	public static final String logExceptionInProgress(String... args){
		return MessageFormat.format(EXCEPTION_IN_PROCESS_MASK, args);
	}
	
	/**
	 * Log jar found.
	 *
	 * @param arg the arg
	 * @return the string
	 */
	public static final String logJarFound(String arg){
		return MessageFormat.format(JAR_FOUND_MASK, arg);
	}
	
	/**
	 * Log jar already deployed warn.
	 *
	 * @param jar1 the jar1
	 * @param jar2 the jar2
	 * @return the string
	 */
	public static final String logJarAlreadyDeployedWarn(String jar1, String jar2){
		return MessageFormat.format(JAR_ALREADY_DEPLOYED_MASK, jar1, jar2);
	}
}

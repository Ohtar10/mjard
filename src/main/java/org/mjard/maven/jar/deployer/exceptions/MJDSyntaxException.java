package org.mjard.maven.jar.deployer.exceptions;

/**
 * The Class MJDException.
 *
 * @author Luis Eduardo Ferro Diez
 */
public class MJDSyntaxException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4511856302662621894L;

	/**
	 * Instantiates a new MJD exception.
	 */
	public MJDSyntaxException() {
		super();		
	}

	/**
	 * Instantiates a new MJD exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public MJDSyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);		
	}

	/**
	 * Instantiates a new MJD exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public MJDSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new MJD exception.
	 *
	 * @param message the message
	 */
	public MJDSyntaxException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new MJD exception.
	 *
	 * @param cause the cause
	 */
	public MJDSyntaxException(Throwable cause) {
		super(cause);
	}
	
	

}

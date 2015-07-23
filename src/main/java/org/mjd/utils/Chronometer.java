package org.mjd.utils;

import java.text.MessageFormat;

/**
 * The Class Chronometer.
 *
 * @author Luis Eduardo Ferro Diez
 */
public class Chronometer {

	/** The begin. */
	private Long begin;
	
	/** The end. */
	private Long end;
	
	/**
	 * Start.
	 */
	public void start(){
		begin = System.currentTimeMillis();
	}
	
	/**
	 * Stop.
	 */
	public void stop(){
		end = System.currentTimeMillis();
	}
	
	/**
	 * Gets the time formatted.
	 *
	 * @return the time formatted
	 */
	public String getTimeFormatted(){
		long time = end - begin;
        long hours = (time / (1000*60*60)%24);
        long minutes = (time / (1000*60)%60);
        long seconds = (time / (1000)%60);
		return MessageFormat.format("{0}:{1}:{2}", hours, minutes, seconds);
	}
}

/*
 * Oct 3, 2005
 */
package com.thinkparity.browser.platform.util.log4j;

import org.apache.log4j.Logger;

/**
 * LoggerFactory
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class LoggerFactory {

	/**
	 * Obtain a handle to a logger for the browser.
	 * 
	 * @param clasz
	 *            The class for which the logger should be created.
	 * @return A handle to a logger.
	 */
	public static Logger getLogger(final Class clasz) {
		final Logger logger = Logger.getLogger(clasz);
		return logger;
	}

	/**
	 * Create a new LoggerFactory [Singleton, Factory]
	 */
	private LoggerFactory() { super(); }
}

/*
 * Feb 2, 2005
 */
package com.thinkparity.ophelia.model.util.logging;

import org.apache.log4j.Logger;

/**
 * A singleton factory for obtaining log4j logger instances.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.4
 * @deprecated @link{use com.thinkparity.model.LoggerFactory} instead.
 */
public class ModelLoggerFactory {

	/**
	 * Obtain a logger.
	 * 
	 * @param clasz
	 *            The caller's class.
	 * @return The logger.
	 */
	public static Logger getLogger(final Class clasz) {
		final Logger modelLogger = Logger.getLogger(clasz);
		return modelLogger;
	}

	/**
	 * Create a new ModelLoggerFactory [Singleton, Factory]
	 */
	private ModelLoggerFactory() { super(); }
}

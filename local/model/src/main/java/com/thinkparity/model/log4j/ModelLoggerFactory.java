/*
 * Feb 2, 2005
 */
package com.thinkparity.model.log4j;

import org.apache.log4j.Logger;

/**
 * A singleton factory for obtaining log4j logger instances.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.3
 * @see ModelLog4JConfigurator
 */
public class ModelLoggerFactory {

	static { ModelLog4JConfigurator.configure(); }

	/**
	 * Obtain a logger.
	 * 
	 * @param clasz
	 *            The caller's class.
	 * @return The logger.
	 */
	public static Logger getLogger(final Class clasz) {
		final Logger modelLogger = getModelLogger(clasz);
		return modelLogger;
	}

	/**
	 * Obtain a handle to a model logger.
	 * 
	 * @param clasz
	 *            The class for which to obtain a logger.
	 * @return A log4j logger.
	 */
	private static Logger getModelLogger(final Class clasz) {
		final Logger modelLogger = Logger.getLogger(clasz);
		return modelLogger;
	}

	/**
	 * Create a new ModelLoggerFactory [Singleton, Factory]
	 */
	private ModelLoggerFactory() { super(); }
}

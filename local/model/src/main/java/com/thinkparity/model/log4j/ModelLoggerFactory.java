/*
 * Feb 2, 2005
 */
package com.thinkparity.model.log4j;

import org.apache.log4j.Logger;

/**
 * ModelLoggerFactory
 * A singleton factory for creating rootLogger instances for this client.
 * @author raykroeker@gmail.com
 * @version 1.3
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
		return getModelLogger();
	}

	/**
	 * Obtain a handle to a model logger.
	 * 
	 * @return An instance of a log4j logger.
	 */
	private static Logger getModelLogger() {
		final Logger modelLogger = Logger.getLogger("thinkparity.com - model");
		return modelLogger;
	}

	/**
	 * Create a new ModelLoggerFactory [Singleton, Factory]
	 */
	private ModelLoggerFactory() { super(); }
}

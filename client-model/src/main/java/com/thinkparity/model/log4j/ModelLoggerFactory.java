/*
 * Feb 2, 2005
 */
package com.thinkparity.model.log4j;

import org.apache.log4j.Logger;

/**
 * A singleton factory for obtaining log4j logger instances.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.4
 * @see org.apache.log4j.Logger#getLogger(java.lang.Class)
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

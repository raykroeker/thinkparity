/*
 * Feb 2, 2005
 */
package com.thinkparity.model.parity.util.log4j;

import org.apache.log4j.Logger;

/**
 * BrowserLoggerFactory
 * A singleton factory for creating rootLogger instances for this client.
 * @author raykroeker@gmail.com
 * @version 1.3
 */
public class BrowserLoggerFactory {

	/**
	 * Obtain a logger.
	 * 
	 * @param clasz
	 *            The caller's class.
	 * @return The logger.
	 */
	public static Logger getLogger(final Class clasz) {
		final Logger logger = Logger.getLogger(clasz);
		return logger;
	}
}

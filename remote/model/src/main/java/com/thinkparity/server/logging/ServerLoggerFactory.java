/*
 * Nov 28, 2005
 */
package com.thinkparity.server.logging;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ServerLoggerFactory {

	/**
	 * Singleton instance.
	 */
	private static final ServerLoggerFactory singleton;

	/**
	 * Synchronziation lock for the singleton.
	 */
	private static final Object singletonLock;

	static {
		singleton = new ServerLoggerFactory();
		singletonLock = new Object();
	}

	/**
	 * Obtain a handle to a logger.
	 * 
	 * @param clasz
	 *            The context to use for logger creation.
	 * @return A handle to a logger.
	 */
	public static Logger getLogger(final Class clasz) {
		synchronized(singletonLock) { return singleton.getLoggerImpl(clasz); }
	}

	/**
	 * Create a ServerLoggerFactory [Singleton,Factory]
	 */
	private ServerLoggerFactory() { super(); }

	/**
	 * Obtain a handle to a logger.
	 * 
	 * @param clasz
	 *            The context to use for logger creation.
	 * @return A handle to a logger.
	 */	
	private Logger getLoggerImpl(final Class clasz) {
		Assert.assertTrue(
				"getLoggerImpl(Class)", ServerLog4jConfigurator.isConfigured());
		final Logger logger = Logger.getLogger(clasz);
		return logger;
	}
}

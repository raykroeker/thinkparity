/*
 * Nov 28, 2005
 */
package com.thinkparity.server.logging;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ServerLog4jConfigurator {

	/**
	 * Singleton instance.
	 */
	private static final ServerLog4jConfigurator singleton;

	/**
	 * Singleton instance synchronization lock.
	 */
	private static final Object singletonLock;

	static {
		singleton = new ServerLog4jConfigurator();
		singletonLock = new Object();
	}

	/**
	 * Configure log4j for the server.
	 * 
	 * @param log4jDirectory
	 *            The log4j output directory.
	 */
	public static void configure(final File log4jDirectory) {
		synchronized(singletonLock) { singleton.configureImpl(log4jDirectory); }
	}

	/**
	 * Obtain the configuration status.
	 * 
	 * @return True if log4j has been configured for the server; false
	 *         otherwise.
	 */
	static Boolean isConfigured() {
		synchronized(singletonLock) { return singleton.isConfiguredImpl(); }
	}

	/**
	 * Flag indicating whether or not the configuration has been set.
	 * 
	 * @see ServerLog4jConfigurator#configure()
	 */
	private Boolean isConfigured = Boolean.FALSE;

	/**
	 * Create a ServerLog4jConfigurator [Singleton]
	 */
	private ServerLog4jConfigurator() { super(); }

	/**
	 * Configure log4j for the parity server.
	 *
	 */
	public void configureImpl(final File log4jDirectory) {
		if(Boolean.FALSE == isConfigured) {
			final Properties log4jProperties = new Properties();

			configureGlobal(log4jProperties);

			configureServerHTMLAppender(log4jProperties, log4jDirectory);

			PropertyConfigurator.configure(log4jProperties);
			isConfigured = Boolean.TRUE;
		}
	}

	/**
	 * Configure the global log4j properties.
	 * 
	 * @param globalProperties
	 *            The global log4j properties.
	 */
	private void configureGlobal(final Properties globalProperties) {
		globalProperties.setProperty("log4j.logger.com.thinkparity.server", "DEBUG, serverHTML");
	}

	/**
	 * Configure the server html log file.
	 * 
	 * @param serverHTMLProperties
	 *            The server html configuration.
	 * @param log4jDirectory
	 *            The log4j output directory.
	 */
	private void configureServerHTMLAppender(
			final Properties serverHTMLProperties, final File log4jDirectory) {
		serverHTMLProperties.setProperty("log4j.appender.serverHTML", "org.apache.log4j.RollingFileAppender");
		serverHTMLProperties.setProperty("log4j.appender.serverHTML.layout", "org.apache.log4j.HTMLLayout");
		serverHTMLProperties.setProperty("log4j.appender.serverHTML.layout.locationInfo", "true");
		serverHTMLProperties.setProperty("log4j.appender.serverHTML.layout.title", "Parity Server");
		final File serverHTMLFile = new File(log4jDirectory, "parity.server.log4j.html");
		serverHTMLProperties.setProperty("log4j.appender.serverHTML.File", serverHTMLFile.getAbsolutePath());
	}

	/**
	 * Obtain the configuration status.
	 * 
	 * @return True if log4j has been configured for the server; false
	 *         otherwise.
	 */	
	private Boolean isConfiguredImpl() { return isConfigured; }
}

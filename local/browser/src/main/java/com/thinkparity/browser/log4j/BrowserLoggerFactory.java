/*
 * Oct 3, 2005
 */
package com.thinkparity.browser.log4j;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * BrowserLoggerFactory
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class BrowserLoggerFactory {

	/**
	 * Synchronization lock for the framework configuration.
	 */
	private static final Object configureLock = new Object();

	/**
	 * Flag indicating whether initial configuration of the framework has been
	 * done.
	 */
	private static Boolean isConfigured = Boolean.FALSE;

	/**
	 * Obtain a handle to a logger for the browser.
	 * 
	 * @param clasz
	 *            The class for which the logger should be created.
	 * @return A handle to a logger.
	 */
	public static Logger getLogger(final Class clasz) {
		BrowserLoggerFactory.configure();
		return getBrowserLogger(clasz);
	}

	/**
	 * Configure the logging framework.  This will only happen the first time
	 * a logger is requested.
	 *
	 */
	private static void configure() {
		synchronized(configureLock) {
			if(Boolean.FALSE == isConfigured) {
				// TODO:  Figure out how to load the configuration file via
				// an eclipse mechanism.  This will avoid this ugliness in
				// the code.
				final Properties log4jProperties = new Properties();

				log4jProperties.setProperty("log4j.threshold", "debug");
				log4jProperties.setProperty("log4j.rootLogger", "debug, broswerHTML, console");

				configureGlobal(log4jProperties);

				configureBrowserHTMLAppender(log4jProperties);
				configureConsoleAppender(log4jProperties);

				PropertyConfigurator.configure(log4jProperties);

				isConfigured = Boolean.TRUE;
			}
		}
	}

	/**
	 * Configure the browser html appender.
	 * 
	 * @param browserHTML
	 *            The configuration to write the appender properties to.
	 */
	private static void configureBrowserHTMLAppender(final Properties browserHTML) {
		browserHTML.setProperty("log4j.appender.broswerHTML", "org.apache.log4j.RollingFileAppender");
		browserHTML.setProperty("log4j.appender.broswerHTML.layout", "org.apache.log4j.HTMLLayout");
		final File browserHTMLOutputFile =
			new File(System.getProperty("user.dir"), "parity.log4j.html");
		browserHTML.setProperty("log4j.appender.broswerHTML.File", browserHTMLOutputFile.getAbsolutePath());
	}

	/**
	 * Configure the console appender.
	 * 
	 * @param console
	 *            The configuration to write the appender properties to.
	 */
	private static void configureConsoleAppender(final Properties console) {
		console.setProperty("log4j.appender.console", "org.apache.log4j.ConsoleAppender");
		console.setProperty("log4j.appender.console.layout", "org.apache.log4j.PatternLayout");
		console.setProperty("log4j.appender.console.layout.ConversionPattern", "%-5p [%t] [%C{1}]: %m%n");
	}

	/**
	 * Configure global logging settings.
	 * 
	 * @param global
	 *            The global log4j configuration.
	 */
	private static void configureGlobal(final Properties global) {
		global.setProperty("log4j.logger.com.thinkparity.model.xmpp.XMPPSessionImpl", "DEBUG");
		global.setProperty("log4j.logger.com.thinkparity.model", "INFO");
		global.setProperty("log4j.logger.com.thinkparity.model.parity.model.io", "WARN");
	}

	/**
	 * Obtain a logger for a given class.
	 * 
	 * @param clasz
	 *            The class for which to obtain the logger.
	 * @return A log4j logger.
	 */
	private static Logger getBrowserLogger(final Class clasz) {
		final Logger browserLogger = Logger.getLogger(clasz);
		return browserLogger;
	}

	/**
	 * Create a new BrowserLoggerFactory [Singleton, Factory]
	 */
	private BrowserLoggerFactory() { super(); }
}

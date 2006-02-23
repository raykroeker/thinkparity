/*
 * Oct 3, 2005
 */
package com.thinkparity.browser.platform.util.log4j;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.log4j.or.java.awt.PointRenderer;
import com.thinkparity.browser.platform.util.log4j.or.java.awt.event.MouseEventRenderer;

import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * LoggerFactory
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class LoggerFactory {

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
	 * Prefix for all renderer properties.
	 */
	private static final String RENDERER_PROPERTY_PREFIX =
		"log4j.renderer.";

	/**
	 * Obtain a handle to a logger for the browser.
	 * 
	 * @param clasz
	 *            The class for which the logger should be created.
	 * @return A handle to a logger.
	 */
	public static Logger getLogger(final Class clasz) {
		LoggerFactory.configure();
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
				final Properties log4jProperties = new Properties();

				log4jProperties.setProperty("log4j.threshold", "debug");
				log4jProperties.setProperty("log4j.rootLogger", "debug, broswerHTML, console");

				configureGlobal(log4jProperties);
				configureRenderers(log4jProperties);

				final ModelFactory modelFactory = ModelFactory.getInstance();
				configureBrowserHTMLAppender(log4jProperties, modelFactory.getWorkspace(LoggerFactory.class));
				configureConsoleAppender(log4jProperties);

				PropertyConfigurator.configure(log4jProperties);

				isConfigured = Boolean.TRUE;
			}
		}
	}

	/**
	 * Configure the browser html appender.
	 * 
	 * @param log4jProperties
	 *            The configuration to write the appender properties to.
	 */
	private static void configureBrowserHTMLAppender(final Properties log4jProperties, final Workspace workspace) {
		log4jProperties.setProperty("log4j.appender.broswerHTML", "org.apache.log4j.RollingFileAppender");
		log4jProperties.setProperty("log4j.appender.broswerHTML.layout", "org.apache.log4j.HTMLLayout");
		final File browserHTMLOutputFile =
			new File(workspace.getLoggerURL().getFile(), "parity.log4j.html");
		log4jProperties.setProperty("log4j.appender.broswerHTML.File", browserHTMLOutputFile.getAbsolutePath());
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
		global.setProperty("log4j.logger.com.thinkparity.browser.application.browser.Browser", "ERROR");
		global.setProperty("log4j.logger.com.thinkparity.model.xmpp.XMPPSessionImpl", "DEBUG");
		global.setProperty("log4j.logger.com.thinkparity.model", "INFO");
		global.setProperty("log4j.logger.com.thinkparity.model.parity.model.io", "WARN");
	}

	/**
	 * Configure a renderer for log4j.
	 * 
	 * @param log4jProperties
	 *            The log4j configuration.
	 * @param renderedClass
	 *            The class to render.
	 * @param renderingClass
	 *            The class that renders.
	 */
	private static void configureRenderer(final Properties log4jProperties,
			final Class renderedClass, final Class renderingClass) {
		log4jProperties.setProperty(
				RENDERER_PROPERTY_PREFIX + renderedClass.getName(),
				renderingClass.getName());
	}

	/**
	 * Configure all of the browser log4j renderers.
	 * 
	 * @param log4jProperties
	 *            The log4j configuration.
	 */
	private static void configureRenderers(final Properties log4jProperties) {
		configureRenderer(log4jProperties, MouseEvent.class, MouseEventRenderer.class);
		configureRenderer(log4jProperties, Point.class, PointRenderer.class);
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
	 * Create a new LoggerFactory [Singleton, Factory]
	 */
	private LoggerFactory() { super(); }
}

/*
 * Oct 3, 2005
 */
package com.thinkparity.browser.platform.util.log4j;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkparity.browser.model.ModelFactory;

import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * LoggerFactory
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class LoggerFactory {

	/**
	 * Set the html log file.
	 *
	 */
	static {
		final Properties log4jProperties = new Properties();

		final ModelFactory modelFactory = ModelFactory.getInstance();
		final Workspace workspace = modelFactory.getWorkspace(LoggerFactory.class);
		final File htmlLogFile =
			new File(workspace.getLoggerURL().getFile(), "thinkParity.log4j.html");
		log4jProperties.setProperty("log4j.appender.HTML.File", htmlLogFile.getAbsolutePath());

		PropertyConfigurator.configure(log4jProperties);
	}

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

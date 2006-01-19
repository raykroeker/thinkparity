/*
 * 18-Oct-2005
 */
package com.thinkparity.model;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ModelTestLoggerConfigurator {

	static void configure(final String jUnitSessionId) {
		final Properties log4jProperties = new Properties();

		log4jProperties.setProperty("log4j.threshold", "debug");
		log4jProperties.setProperty("log4j.rootLogger", "debug, html");

		final WorkspaceModel workspaceModel = WorkspaceModel.getModel();
		final Workspace workspace = workspaceModel.getWorkspace();
		configureHTMLAppender(jUnitSessionId, log4jProperties, workspace);
		configureConsoleAppender(log4jProperties);

		PropertyConfigurator.configure(log4jProperties);
	}

	/**
	 * Configure the html appender.
	 * 
	 * @param jUnitSessionId
	 *            The junit session id.
	 * @param log4jConfig
	 *            The log4j configuration to set.
	 */
	private static void configureHTMLAppender(final String jUnitSessionId,
			final Properties log4jConfig, final Workspace workspace) {
		log4jConfig.setProperty("log4j.appender.html", "org.apache.log4j.RollingFileAppender");
		log4jConfig.setProperty("log4j.appender.html.MaxFileSize", "3MB");
		log4jConfig.setProperty("log4j.appender.html.layout", "org.apache.log4j.HTMLLayout");
		log4jConfig.setProperty("log4j.appender.html.HTMLLayout.Title", jUnitSessionId);
		final File htmlFile =
			new File(workspace.getLoggerURL().getFile(), "parity.log4j.html");
		log4jConfig.setProperty("log4j.appender.html.File", htmlFile.getAbsolutePath());
		// print the path to the log file when shut down
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("[PARITY] Log file:  " + htmlFile.getAbsolutePath());
			}
		});
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
		console.setProperty("log4j.appender.console.layout.ConversionPattern", "%-5p [%t]: %m%n");
	}

	/**
	 * Create a ModelTestLoggerConfigurator [Singleton]
	 */
	private ModelTestLoggerConfigurator() { super(); }
}

/*
 * 18-Oct-2005
 */
package com.thinkparity.codebase;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class CodebaseTestLoggerConfigurator {

	static void configure(final String jUnitSessionId, final File outputDirectory) {
		final Properties log4jProperties = new Properties();

		log4jProperties.setProperty("log4j.threshold", "debug");
		log4jProperties.setProperty("log4j.rootLogger", "debug, html");

		configureHTMLAppender(jUnitSessionId, log4jProperties, outputDirectory);
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
	 * @param outputDirectory
	 *            The output directory to place the file within.
	 */
	private static void configureHTMLAppender(final String jUnitSessionId,
			final Properties log4jConfig, final File outputDirectory) {
		log4jConfig.setProperty("log4j.appender.html", "org.apache.log4j.RollingFileAppender");
		log4jConfig.setProperty("log4j.appender.html.MaxFileSize", "3MB");
		log4jConfig.setProperty("log4j.appender.html.layout", "org.apache.log4j.HTMLLayout");
		log4jConfig.setProperty("log4j.appender.html.HTMLLayout.Title", jUnitSessionId);
		final File htmlFile = new File(outputDirectory, "codebase.log4j.html");
		log4jConfig.setProperty("log4j.appender.html.File", htmlFile.getAbsolutePath());
		// print the path to the log file when shut down
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Log file:  " + htmlFile.getAbsolutePath());
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
	 * Create a CodebaseTestLoggerConfigurator [Singleton]
	 */
	private CodebaseTestLoggerConfigurator() { super(); }
}

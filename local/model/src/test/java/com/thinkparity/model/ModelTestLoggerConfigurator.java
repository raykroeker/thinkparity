/*
 * 18-Oct-2005
 */
package com.thinkparity.model;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.thinkparity.codebase.assertion.Assert;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ModelTestLoggerConfigurator {

	static void configure(final File outputDirectory) {
		final Properties log4jProperties = new Properties();

		log4jProperties.setProperty("log4j.threshold", "debug");
		log4jProperties.setProperty("log4j.rootLogger", "debug, broswerHTML, console");

		configureBrowserHTMLAppender(log4jProperties, outputDirectory);
		configureConsoleAppender(log4jProperties);

		PropertyConfigurator.configure(log4jProperties);
	}

	/**
	 * Configure the browser html appender.
	 * 
	 * @param browserHTML
	 *            The configuration to write the appender properties to.
	 */
	private static void configureBrowserHTMLAppender(
			final Properties browserHTML, final File outputDirectory) {
		browserHTML.setProperty("log4j.appender.broswerHTML", "org.apache.log4j.RollingFileAppender");
		browserHTML.setProperty("log4j.appender.broswerHTML.layout", "org.apache.log4j.HTMLLayout");
		browserHTML.setProperty("log4j.appender.broswerHTML.MaxFileSize", "300KB");
		browserHTML.setProperty("log4j.appender.broswerHTML.MaxBackupIndex", "5");
		final File browserHTMLOutputFile =
			new File(outputDirectory, "parity.log4j.html");
		if(browserHTMLOutputFile.exists()) { browserHTMLOutputFile.delete(); }
		try {
			Assert.assertTrue(
					"Could not create jUnit log4j file.",
					browserHTMLOutputFile.createNewFile());
		}
		catch(IOException iox) { throw new RuntimeException(iox); }
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
		console.setProperty("log4j.appender.console.layout.ConversionPattern", "%-5p [%t]: %m%n");
	}

	/**
	 * Create a ModelTestLoggerConfigurator [Singleton]
	 */
	private ModelTestLoggerConfigurator() { super(); }
}

/*
 * Feb 1, 2006
 */
package com.raykroeker.junitx;

import java.io.File;
import java.util.Properties;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * The JUnit Log4J configurator initializes the logging framework prior to
 * running a test run.
 * 
 * As a result of calling configure() *all* levels of logging will be turned on
 * for a single html file appender.
 * 
 * @author raykroeker@gmail.com
 */
public class Log4JConfigurator {

	/**
	 * Configure Log4J for the JUnitX library.
	 *
	 */
	static void configure(final TestSession testSession) {
		final Properties properties = new Properties();

		properties.setProperty("log4j.threshold", "debug");

		if(Boolean.getBoolean("junitx.console"))
			properties.setProperty("log4j.rootLogger", "debug, console, html");
		else
			properties.setProperty("log4j.rootLogger", "debug, html");

		configureConsoleAppender(properties);
		configureHTMLAppender(properties, testSession);

		PropertyConfigurator.configure(properties);
	}

	/**
	 * Create a Log4JConfigurator.
	 * 
	 */
	private Log4JConfigurator() { super(); }

	/**
	 * Configure the html appender.
	 * 
	 * @param properties
	 *            The log4j configuration to set.
	 * @param jUnitSessionId
	 *            The junit session id.
	 */
	private static void configureHTMLAppender(final Properties properties,
			final TestSession testSession) {
		properties.setProperty("log4j.appender.html", "org.apache.log4j.RollingFileAppender");
		properties.setProperty("log4j.appender.html.MaxFileSize", "750KB");
		properties.setProperty("log4j.appender.html.layout", "org.apache.log4j.HTMLLayout");
		properties.setProperty("log4j.appender.html.HTMLLayout.Title", JUnitX.getName());
		final File htmlFile = new File(testSession.getSessionDirectory(), JUnitX.getShortName() + ".html");
		properties.setProperty("log4j.appender.html.File", htmlFile.getAbsolutePath());
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				Logger.getLogger(Log4JConfigurator.class).info(
                        MessageFormat.format("{0} {1}",
                                JUnitX.getShortName(),
                                htmlFile.getAbsolutePath()));
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
}

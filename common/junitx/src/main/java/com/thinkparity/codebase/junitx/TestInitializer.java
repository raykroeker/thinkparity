/*
 * Created On: Sep 13, 2006 9:28:47 AM
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class TestInitializer {

    /** A singleton instance. */
    private static final TestInitializer SINGLETON;

    static {
        SINGLETON = new TestInitializer();
    }

    /**
     * Initialize the test framework.
     * 
     * @param testSession
     *            The test session.
     */
    static void initialize(final TestSession testSession) {
        SINGLETON.doInitialize(testSession);
    }

    /** Create TestInitializer. */
    private TestInitializer() {
        super();
    }

    /**
     * Initialize the test framework.
     * 
     * @param testSession
     *            The test session.
     */
    private void doInitialize(final TestSession testSession) {
        doInitializeLogging(testSession);
    }

    /**
     * Set the file appender file.
     * 
     * @param testSession
     *            The test session.
     */
    private void doInitializeLogging(final TestSession testSession) {
        final Properties properties = new Properties();
        final String level = System.getProperty("junitx.log4j.level");
        final Boolean console = Boolean.getBoolean("junitx.log4j.console");
        if (null != level) {
            if (console) {
                properties.setProperty("log4j.logger.com.thinkparity", level + ", FILE, CONSOLE");
            } else {
                properties.setProperty("log4j.logger.com.thinkparity", level + ", FILE");
            }
        } else {
            if (console) {
                properties.setProperty("log4j.logger.com.thinkparity", "INFO, FILE, CONSOLE");
            } else {
                properties.setProperty("log4j.logger.com.thinkparity", "INFO, FILE");
            }
        }
        properties.setProperty("log4j.logger.com.thinkparity.ophelia.model.io", "WARN");

        properties.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
        properties.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d %p %m%n");

        properties.setProperty("log4j.appender.FILE", "org.apache.log4j.RollingFileAppender");
        properties.setProperty("log4j.appender.FILE.layout", "org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.FILE.layout.ConversionPattern", "%d %p %m%n");
        properties.setProperty("log4j.appender.FILE.File",
                new File(testSession.getSessionDirectory(),
                        "junitx.log").getAbsolutePath());
        PropertyConfigurator.configure(properties);
    }
}

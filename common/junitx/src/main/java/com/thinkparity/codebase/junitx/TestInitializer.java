/*
 * Created On: Sep 13, 2006 9:28:47 AM
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Properties;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.apache.log4j.LogManager;
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
     * Initialize logging. Check the operating mode. If in development or
     * testing mode; enable the sql and xmpp debuggers.
     * 
     */
    private void bootstrapLog4J(final File defaultLoggingRoot) {
        final Mode mode = Mode.DEVELOPMENT;
        final Properties logging = new Properties();
        final File loggingRoot = bootstrapLog4JRoot(mode, defaultLoggingRoot);
        System.setProperty("thinkparity.logging.root", loggingRoot.getAbsolutePath());
        // test file appender
        logging.setProperty("log4j.appender.TEST_FILE", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.TEST_FILE.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.TEST_FILE.layout.ConversionPattern", "%d %t %p %m%n");
        logging.setProperty("log4j.appender.TEST_FILE.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Test.log"));
        // test console appender
        logging.setProperty("log4j.appender.TEST_CONSOLE", "org.apache.log4j.ConsoleAppender");
        logging.setProperty("log4j.appender.TEST_CONSOLE.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.TEST_CONSOLE.layout.ConversionPattern", "%d{ISO8601} %t %p %m%n");
        // loggers
        logging.setProperty("log4j.logger.TEST_LOGGER", "INFO, TEST_CONSOLE, TEST_FILE");
        logging.setProperty("log4j.additivity.TEST_LOGGER", "false");
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(logging);
        new Log4JWrapper("TEST_LOGGER").logInfo("{0} - {1}", "thinkParity", "1.0");
    }

    /**
     * Bootstrap the log4j root directory. If a thinkparity.logging.root exists
     * use it as the logging root directory; otherwise use workspace/logs.
     * 
     * 
     * @param mode
     *            The thinkParity operating <code>Mode</code>.
     * @return The logging root directory <code>File</code>.
     */
    private File bootstrapLog4JRoot(final Mode mode, final File defaultLoggingRoot) {
        final String loggingRootProperty = System.getProperty("thinkparity.logging.root");
        if (null == loggingRootProperty) {
            return defaultLoggingRoot;
        } else {
            final File loggingRoot = new File(loggingRootProperty);
            Assert.assertTrue(loggingRoot.exists() && loggingRoot.isDirectory()
                    && loggingRoot.canRead() && loggingRoot.canWrite(),
                    "Specified logging root {0} is not valid.", loggingRoot);
            return loggingRoot;
        }
    }

    /**
     * Initialize the test framework.
     * 
     * @param testSession
     *            The test session.
     */
    private void doInitialize(final TestSession testSession) {
        bootstrapLog4J(testSession.getOutputDirectory());
        redirectStreams(testSession);
    }

    /**
     * Redirect System.out and System.err.
     * 
     * @param testSession
     *            The <code>TestSession</code>.
     */
    private void redirectStreams(final TestSession testSession) {
        try {
            System.setOut(new PrintStream(new FileOutputStream(new File(
                    testSession.getOutputDirectory(), "System.out"))));
        } catch (final Exception x) {
            throw new RuntimeException(x);
        }
        try {
            System.setErr(new PrintStream(new FileOutputStream(new File(
                    testSession.getOutputDirectory(), "System.err"))));
        } catch (final Exception x) {
            throw new RuntimeException(x);
        }
    }
}

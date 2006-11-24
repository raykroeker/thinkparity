/*
 * Created On: Sep 13, 2006 9:28:47 AM
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.config.ConfigFactory;
import com.thinkparity.codebase.log4j.Log4JWrapper;


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
        bootstrapLog4J(testSession.getOutputDirectory());
        doInitializeStreams(testSession);
    }

    private void doInitializeStreams(final TestSession testSession) {
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

    /**
     * Initialize logging. Check the operating mode. If in development or
     * testing mode; enable the sql and xmpp debuggers.
     * 
     */
    private void bootstrapLog4J(final File defaultLoggingRoot) {
        final Mode mode = Mode.TESTING;
        /* HACK if the logging root is set; we know we are being run within the
         * thinkParity server and need not reset the configuration. */
        final String loggingRootProperty = System.getProperty("thinkparity.logging.root");
        final boolean isDesktop = null == loggingRootProperty;

        final Properties logging = bootstrapLog4JConfig(mode);
        final File loggingRoot = bootstrapLog4JRoot(mode, defaultLoggingRoot);
        // console appender
        logging.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
        logging.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d %t %p %m%n");
        // default appender
        logging.setProperty("log4j.appender.DEFAULT", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.DEFAULT.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DEFAULT.layout.ConversionPattern", "%d %t %p %m%n");
        logging.setProperty("log4j.appender.DEFAULT.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity.log"));
        // sql appender
        logging.setProperty("log4j.appender.SQL_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.SQL_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.SQL_DEBUGGER.layout.ConversionPattern", "%d %t %m%n");
        logging.setProperty("log4j.appender.SQL_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity SQL.log"));
        // test appender
        logging.setProperty("log4j.appender.TEST_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.TEST_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.TEST_DEBUGGER.layout.ConversionPattern", "%d %t %m%n");
        logging.setProperty("log4j.appender.TEST_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Test.log"));
        // xmpp appender
        logging.setProperty("log4j.appender.XMPP_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.layout.ConversionPattern", "%d %t %m%n");
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity XMPP.log"));
        // loggers
        switch (mode) {
        case DEMO:
        case PRODUCTION:
            logging.setProperty("log4j.rootLogger", "WARN, DEFAULT");
            logging.setProperty("log4j.logger.com.thinkparity.ophelia", "WARN, DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");
            logging.setProperty("log4j.logger.SQL_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");
            logging.setProperty("log4j.logger.XMPP_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
            break;
        case DEVELOPMENT:
            logging.setProperty("log4j.rootLogger", "INFO, CONSOLE, DEFAULT");
            logging.setProperty("log4j.logger.com.thinkparity.ophelia", "INFO, CONSOLE, DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");
            logging.setProperty("log4j.logger.SQL_DEBUGGER", "DEBUG, SQL_DEBUGGER");
            logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");
            logging.setProperty("log4j.logger.XMPP_DEBUGGER", "DEBUG, XMPP_DEBUGGER");
            logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
            break;
        case TESTING:
            logging.setProperty("log4j.rootLogger", "INFO, DEFAULT");
            logging.setProperty("log4j.logger.com.thinkparity.ophelia", "INFO, DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");
            logging.setProperty("log4j.logger.SQL_DEBUGGER", "DEBUG, SQL_DEBUGGER");
            logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");
            logging.setProperty("log4j.logger.XMPP_DEBUGGER", "DEBUG, XMPP_DEBUGGER");
            logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
            logging.setProperty("log4j.logger.TEST_DEBUGGER", "DEBUG, CONSOLE, TEST_DEBUGGER");
            logging.setProperty("log4j.additivity.TEST_DEBUGGER", "false");
            break;
        default:
            throw Assert.createUnreachable("Unknown operating mode.");
        }
        // renderers
        logging.setProperty("log4j.renderer.java.util.Calendar",
            "com.thinkparity.codebase.log4j.or.CalendarRenderer");
        logging.setProperty("log4j.renderer.com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent",
            "com.thinkparity.codebase.model.util.logging.or.XMPPEventRenderer");
        logging.setProperty("log4j.renderer.com.thinkparity.codebase.model.document.Document",
            "com.thinkparity.codebase.model.util.logging.or.DocumentRenderer");
        logging.setProperty("log4j.renderer.com.thinkparity.codebase.model.document.DocumentVersion",
            "com.thinkparity.codebase.model.util.logging.or.DocumentVersionRenderer");
        logging.setProperty("log4j.renderer.com.thinkparity.codebase.model.user.User",
            "com.thinkparity.codebase.model.util.logging.or.UserRenderer");
        logging.setProperty("log4j.renderer.org.jivesoftware.smack.packet.Packet",
            "com.thinkparity.ophelia.model.util.logging.or.PacketRenderer");
        if (isDesktop)
            LogManager.resetConfiguration();
        PropertyConfigurator.configure(logging);
        new Log4JWrapper("DEFAULT").logInfo("{0} - {1}", "thinkParity", "1.0");
        new Log4JWrapper("SQL_DEBUGGER").logInfo("{0} - {1}", "thinkParity", "1.0");
        new Log4JWrapper("XMPP_DEBUGGER").logInfo("{0} - {1}", "thinkParity", "1.0");
        new Log4JWrapper("TEST_DEBUGGER").logInfo("{0} - {1}", "thinkParity", "1.0");
    }

    /**
     * Create a logging configuration for the operating mode.
     * 
     * @param mode
     *            A thinkParity <code>Mode</code>.
     * @return A log4j configuration <code>Properties</code>.
     */
    private Properties bootstrapLog4JConfig(final Mode mode) {
        switch (mode) {
        case DEMO:
        case PRODUCTION:
        case TESTING:
            return new Properties();
        case DEVELOPMENT:
            return ConfigFactory.newInstance("log4j.properties");
        default:
            throw Assert.createUnreachable("Unknown operating mode.");
        }
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
}

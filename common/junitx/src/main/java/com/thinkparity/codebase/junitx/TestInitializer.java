/*
 * Created On: Sep 13, 2006 9:28:47 AM
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Properties;

import com.thinkparity.codebase.Constants;
import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.config.ConfigFactory;
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
     * Bootstrap the derby log file.
     * 
     * @param sessionOutput
     *            The session output directory <code>File</code>.
     */
    private void bootstrapDerby(final File sessionOutputDirectory) {
        System.setProperty("derby.stream.error.file",
                new File(sessionOutputDirectory,
                        "thinkParity Derby.log").getAbsolutePath());
        System.setProperty("derby.infolog.append", "false");
    }

    /**
     * Initialize logging. Check the operating mode. If in development or
     * testing mode; enable the sql and xmpp debuggers.
     * 
     */
    private void bootstrapLog4J(final File defaultLoggingRoot) {
        final Mode mode = Mode.valueOf(System.getProperty("thinkparity.mode"));
        final Properties logging = bootstrapLog4JConfig(mode);
        final File loggingRoot = bootstrapLog4JRoot(mode, defaultLoggingRoot);
        System.setProperty("thinkparity.logging.root", loggingRoot.getAbsolutePath());
        // test console appender
        logging.setProperty("log4j.appender.TEST_CONSOLE", "org.apache.log4j.ConsoleAppender");
        logging.setProperty("log4j.appender.TEST_CONSOLE.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.TEST_CONSOLE.layout.ConversionPattern", "%d{ISO8601} %t %p %m%n");
        // ophelia console appender
        logging.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
        logging.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        // test file appender
        logging.setProperty("log4j.appender.TEST_FILE", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.TEST_FILE.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.TEST_FILE.layout.ConversionPattern", "%d %t %p %m%n");
        logging.setProperty("log4j.appender.TEST_FILE.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Test.log"));
        // ophelia file default appender
        logging.setProperty("log4j.appender.DEFAULT", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.DEFAULT.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.DEFAULT.MaxBackupIndex", Constants.Log4J.MAX_BACKUP_INDEX);
        logging.setProperty("log4j.appender.DEFAULT.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DEFAULT.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.DEFAULT.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity.log"));
        // ophelia metrics appender
        logging.setProperty("log4j.appender.METRIX_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.METRIX_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.METRIX_DEBUGGER.MaxBackupIndex", Constants.Log4J.MAX_BACKUP_INDEX);
        logging.setProperty("log4j.appender.METRIX_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.METRIX_DEBUGGER.layout.ConversionPattern", Constants.Log4J.METRICS_LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.METRIX_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Metrics.log"));
        // ophelia sql appender
        logging.setProperty("log4j.appender.SQL_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.SQL_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.SQL_DEBUGGER.MaxBackupIndex", Constants.Log4J.MAX_BACKUP_INDEX);
        logging.setProperty("log4j.appender.SQL_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.SQL_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.SQL_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity SQL.log"));
        // ophelia xa appender
        logging.setProperty("log4j.appender.XA_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.XA_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.XA_DEBUGGER.MaxBackupIndex", Constants.Log4J.MAX_BACKUP_INDEX);
        logging.setProperty("log4j.appender.XA_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.XA_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.XA_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity XA.log"));
        // ophelia xmpp appender
        logging.setProperty("log4j.appender.XMPP_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.MaxBackupIndex", Constants.Log4J.MAX_BACKUP_INDEX);
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity XMPP.log"));
        // test logger
        logging.setProperty("log4j.logger.TEST_LOGGER", "ALL, TEST_CONSOLE, TEST_FILE");
        logging.setProperty("log4j.additivity.TEST_LOGGER", "false");
        // ophelia loggers
        switch (mode) {
        case DEMO:
        case PRODUCTION:
            logging.setProperty("log4j.rootLogger", "WARN, DEFAULT");

            logging.setProperty("log4j.logger.com.thinkparity.ophelia", "WARN, DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");

            logging.setProperty("log4j.logger.METRIX_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.METRIX_DEBUGGER", "false");

            logging.setProperty("log4j.logger.SQL_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");

            logging.setProperty("log4j.logger.XA_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.XA_DEBUGGER", "false");

            logging.setProperty("log4j.logger.XMPP_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
            break;
        case DEVELOPMENT:
            logging.setProperty("log4j.rootLogger", "INFO, CONSOLE, DEFAULT");

            logging.setProperty("log4j.logger.com.thinkparity", "ALL, CONSOLE, DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity", "false");

            logging.setProperty("log4j.logger.METRIX_DEBUGGER", "TRACE, METRIX_DEBUGGER");
            logging.setProperty("log4j.additivity.METRIX_DEBUGGER", "false");

            logging.setProperty("log4j.logger.SQL_DEBUGGER", "DEBUG, SQL_DEBUGGER");
            logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");

            logging.setProperty("log4j.logger.XA_DEBUGGER", "TRACE, XA_DEBUGGER");
            logging.setProperty("log4j.additivity.XA_DEBUGGER", "false");

            logging.setProperty("log4j.logger.XMPP_DEBUGGER", "DEBUG, XMPP_DEBUGGER");
            logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
            break;
        case TESTING:
            logging.setProperty("log4j.rootLogger", "INFO, DEFAULT");

            logging.setProperty("log4j.logger.com.thinkparity.ophelia", "INFO, DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");

            logging.setProperty("log4j.logger.METRIX_DEBUGGER", "DEBUG, METRIX_DEBUGGER");
            logging.setProperty("log4j.additivity.METRIX_DEBUGGER", "false");

            logging.setProperty("log4j.logger.SQL_DEBUGGER", "DEBUG, SQL_DEBUGGER");
            logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");

            logging.setProperty("log4j.logger.XA_DEBUGGER", "DEBUG, XA_DEBUGGER");
            logging.setProperty("log4j.additivity.XA_DEBUGGER", "false");

            logging.setProperty("log4j.logger.XMPP_DEBUGGER", "DEBUG, XMPP_DEBUGGER");
            logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
            break;
        default:
            throw Assert.createUnreachable("Unknown operating mode.");
        }
        // renderers
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.jabber.JabberId",
                "com.thinkparity.codebase.log4j.or.JabberIdRenderer");
        logging.setProperty(
                "log4j.renderer.java.util.Calendar",
                "com.thinkparity.codebase.log4j.or.CalendarRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent",
                "com.thinkparity.codebase.model.util.logging.or.XMPPEventRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.container.Container",
                "com.thinkparity.codebase.model.util.logging.or.ContainerRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.document.Document",
                "com.thinkparity.codebase.model.util.logging.or.DocumentRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.document.DocumentVersion",
                "com.thinkparity.codebase.model.util.logging.or.DocumentVersionRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.user.User",
                "com.thinkparity.codebase.model.util.logging.or.UserRenderer");

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
        bootstrapDerby(testSession.getOutputDirectory());
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
            throw new TestException(x);
        }
        try {
            System.setErr(new PrintStream(new FileOutputStream(new File(
                    testSession.getOutputDirectory(), "System.err"))));
        } catch (final Exception x) {
            throw new TestException(x);
        }
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


}

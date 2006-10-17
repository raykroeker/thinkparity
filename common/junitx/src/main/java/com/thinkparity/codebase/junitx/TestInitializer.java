/*
 * Created On: Sep 13, 2006 9:28:47 AM
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.logging.LogManager;

import org.apache.log4j.PropertyConfigurator;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;


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
        doInititializeLogging(testSession);
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

    private void doInititializeLogging(final TestSession testSession) {
        final Config log4j = ConfigFactory.newInstance("log4j.properties");
        final String existingLogger = log4j.getProperty("log4j.logger.com.thinkparity");
        log4j.setProperty("log4j.logger.com.thinkparity", existingLogger + ",CONSOLE,FILE");
        log4j.setProperty("log4j.appender.FILE.File",
                new File(testSession.getSessionDirectory(), JUnitX.getShortName() + ".log").getAbsolutePath());
        LogManager.getLogManager().reset();
        PropertyConfigurator.configure(log4j);        
    }
}

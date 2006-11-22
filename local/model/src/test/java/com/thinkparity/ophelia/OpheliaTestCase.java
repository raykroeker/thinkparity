/*
 * CreatedOn: Feb 13, 2006
 * $Id$
 */
package com.thinkparity.ophelia;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.junitx.TestCase;
import com.thinkparity.codebase.junitx.TestSession;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.session.Environment;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class OpheliaTestCase extends TestCase {

    /** The thinkParity <code>Environment</code>. */
    static final Environment ENVIRONMENT;

    /** The thinkParity <code>TestSession</code>. */
	static final TestSession SESSION;

	static {
        ENVIRONMENT =
            Environment.valueOf(
                    System.getProperty(
                            "thinkparity.environment", "TESTING_LOCALHOST"));
        new Log4JWrapper().logInfo("Environment:  {0}", ENVIRONMENT);
        SESSION = TestCase.getTestSession();
        new Log4JWrapper().logInfo("Session:  {0}", SESSION);
		// init install
		initParityInstall();
        // reference the class to run the static initializer
        OpheliaTestUser.class.getName();
	}

    /**
     * Assert that two byte arrays are equal.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected byte array.
     * @param actual
     *            The actual byte array.
     */
    protected static void assertEquals(final String assertion,
            final byte[] expected, final byte[] actual) {
        assertEquals(new StringBuffer(assertion).append(" [BYTE ARRAY LENGTH DOES NOT MATCH EXPECTATION]").toString(), expected.length, actual.length);
        for(int i = 0; i < expected.length; i++) {
            assertEquals(new StringBuffer(assertion).append(" [BYTE AT POSITION ").append(i).append(" DOES NOT MATCH EXPECTATION]").toString(), expected[i], actual[i]);
        }
    }

    /**
     * Assert a list of flags is equal.
     * 
     * @param expected
     *            An exepected <code>ArtifactFlag</code> <code>List</code>.
     * @param actual
     *            An actual <code>ArtifactFlag</code> <code>List</code>.
     */
    protected static void assertEquals(final String assertion,
            final List<ArtifactFlag> expected, final List<ArtifactFlag> actual) {
        for (final ArtifactFlag expectedFlag : expected) {
            assertTrue(assertion + "  Expected flag not present.", actual.contains(expectedFlag));
        }
        for (final ArtifactFlag actualFlag : actual) {
            assertTrue(assertion + " Actual flag not present.", expected.contains(actualFlag));
        }
    }

    /**
     * Obtain the current date\time.
     * 
     * @return A calendar.
     */
    protected static Calendar currentDateTime() { return DateUtil.getInstance(); }

    /**
     * Initialize the parity install directory for a test run.
     * 
     * @param parent
     *            The parent within which to create the install dir.
     */
    private static void initParityInstall() {
        final File parent = SESSION.getOutputDirectory();
        final File install = new File(parent, "thinkparity.install");
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL]", install.mkdir());
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL CORE]", new File(install, "core").mkdir());
        final File lib = new File(install, "lib");
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL LIB]", lib.mkdir());
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL LIB NATIVE]", new File(lib, "win32").mkdir());

        System.setProperty("thinkparity.install", install.getAbsolutePath());
    }

    /**
	 * Create a ModelTestCase.
	 * 
	 * @param name
	 *            The test name
	 */
	protected OpheliaTestCase(final String name) {
		super(name);
	}

    public File getOutputDirectory() {
        return SESSION.getOutputDirectory();
    }

	/**
	 * @see com.thinkparity.codebase.junitx.TestCase#getInputFiles()
	 */
	protected File[] getInputFiles() throws IOException {
		final File[] inputFiles = new File[5];
		System.arraycopy(super.getInputFiles(), 0, inputFiles, 0, 5);
		return inputFiles;
	
	}
}

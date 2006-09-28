/*
 * CreatedOn: Feb 13, 2006
 * $Id$
 */
package com.thinkparity.ophelia;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.junitx.TestCase;
import com.thinkparity.codebase.junitx.TestSession;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class OpheliaTestCase extends TestCase {

    /** The JUnit eXtension test session. */
	static final TestSession testSession;

    static final String TEST_SERVERHOST = "thinkparity.dyndns.org";

    static final Integer TEST_SERVERPORT = 5224;

	static {
        testSession = TestCase.getTestSession();
        // init archive
		initParityArchive(testSession.getOutputDirectory());
		// init install
		initParityInstall(testSession.getOutputDirectory());
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
     * Obtain the current date\time.
     * 
     * @return A calendar.
     */
    protected static Calendar currentDateTime() { return DateUtil.getInstance(); }

    /**
     * Initialize the parity archive directory for a test run.
     * 
     * @param parent
     *            The parent within which to create the archive dir.
     */
    private static void initParityArchive(final File parent) {
        final File archive = new File(parent, "parity.archive");
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT ARCHIVE]", archive.mkdir());

        System.setProperty("parity.archive.directory", archive.getAbsolutePath());
    }

    /**
     * Initialize the parity install directory for a test run.
     * 
     * @param parent
     *            The parent within which to create the install dir.
     */
    private static void initParityInstall(final File parent) {
        final File install = new File(parent, "parity.install");
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL]", install.mkdir());
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL CORE]", new File(install, "core").mkdir());
        final File lib = new File(install, "lib");
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL LIB]", lib.mkdir());
        Assert.assertTrue("[LMODEL] [TEST INIT] [INIT INSTALL LIB NATIVE]", new File(lib, "win32").mkdir());

        System.setProperty("parity.install", install.getAbsolutePath());
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

	/**
	 * @see com.thinkparity.codebase.junitx.TestCase#getInputFiles()
	 */
	protected File[] getInputFiles() throws IOException {
		final File[] inputFiles = new File[5];
		System.arraycopy(super.getInputFiles(), 0, inputFiles, 0, 5);
		return inputFiles;
	
	}
}

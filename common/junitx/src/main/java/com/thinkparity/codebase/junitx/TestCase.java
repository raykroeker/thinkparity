/*
 * Created On: Feb 1, 2006
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.MessageFormat;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.log4j.Log4JWrapper;


/**
 * The TestCase is an intermediate abstraction between the JUnit library's
 * main test case and the actual test cases. It provides additional
 * functionality such as factilities for manipulating files\folder for the
 * purposes of testing.
 * 
 * @author raykroeker@gmail.com
 */
public abstract class TestCase extends junit.framework.TestCase {

    /**
     * Assert the contents of two streams are equal.
     * 
     * @param assertion
     *            An assertion message.
     * @param actual
     *            The actual <code>InputStream</code>.
     * @param expected
     *            The expected <code>InputStream</code>.
     */
    protected static void assertEquals(final String assertion,
            final InputStream expected, final InputStream actual)
            throws IOException {
        final byte[] expectedBuffer = new byte[384];
        final byte[] actualBuffer = new byte[expectedBuffer.length];

        int offset = 0;
        int expectedRead = expected.read(expectedBuffer);
        int actualRead = actual.read(actualBuffer);
        while (-1 != expectedRead) {
            assertEquals("The number of bytes read at offset " + offset + " does not match expectation.",
                    expectedRead, actualRead);
            for (int i = 0; i < expectedRead; i++) {
                assertEquals("The byte at location " + offset + ":"  + i + " does not match expectation.",
                        expectedBuffer[i], actualBuffer[i]);
            }
            offset += expectedRead;
            expectedRead = expected.read(expectedBuffer);
            actualRead = actual.read(actualBuffer);
        }
    }

	/**
	 * Obtain the test session.
	 * 
	 * @return The test session.
	 */
	protected static TestSession getTestSession() {
		return TestCaseHelper.getTestSession();
	}

    /**
     * Return the current date and time as a GMT ISO string.
     * 
     * @return The date time <code>String</code>.
     * @see DateUtil#toGMTISO(java.util.Calendar)
     * @see DateUtil#getInstance()
     */
    protected static String toGMTISO() {
        return DateUtil.toGMTISO(DateUtil.getInstance());
    }

	/** An apache logger wrapper. */
	protected final Log4JWrapper logger;

	/** A <code>TestCaseHelper</code>. */
	private final TestCaseHelper testCaseHelper;

	/**
     * Create TestCase.
     * 
     * @param name
     *            The test case name.
     */
	protected TestCase(final String name) {
		super(name);
		this.testCaseHelper = new TestCaseHelper(this);
		this.logger = new Log4JWrapper("TEST_LOGGER");
	}

	/**
	 * Add a data item to the test session.
	 * 
	 * @param dataKey
	 *            The data item key.
	 * @param dataValue
	 *            The data item value.
	 */
	protected void addSessionData(final String key, final Object value) {
		testCaseHelper.addSessionDataItem(key, value);
	}

	/**
	 * Assert the content of the two files is equal.
	 * 
	 * @param expected
	 *            The file with the expected content.
	 * @param actual
	 *            The actual content.
	 */
	protected void assertContentEquals(final File expected, final File actual)
			throws FileNotFoundException, IOException {
		final byte[] expectedBytes = readBytes(expected);
		final byte[] actualBytes = readBytes(actual);
		TestCase.assertEquals(expectedBytes.length, actualBytes.length);
		for(int i = 0; i < expectedBytes.length; i++) {
			TestCase.assertEquals(expectedBytes[i], actualBytes[i]);
		}
	}

	/**
	 * Clear the session data.
	 *
	 */
	protected void clearSessionData() {
		testCaseHelper.clearSessionData();
	}

    /**
	 * Create a test directory.
	 * 
	 * @param directoryName
	 *            The name of the directory.
	 * @return The test directory.
	 */
	protected File createDirectory(final String directoryName) {
		return testCaseHelper.createDirectory(directoryName);
	}

	/**
     * Create a failure message.
     * 
     * @param message
     *            A message.
     * @param t
     *            A throwable.
     * @return A failure message.
     */
    protected String createFailMessage(final Object message, final Throwable t) {
        return new StringBuffer()
                .append(message).append(Separator.SystemNewLine)
                .append(TestCaseHelper.createFailMessage(t)).toString();
    }

	/**
	 * Create a failure message for the throwable.
	 * 
	 * @param t
	 *            The throwable.
	 * @return The failure message.
	 */
	protected String createFailMessage(final Throwable t) {
		return createFailMessage(getName(), t);
	}

	/**
     * Obtain a list of input file names.
     * 
     * @return A <code>String[]</code>.
     */
    protected String[] getInputFileNames() {
        return TestCaseHelper.getInputFileNames();
    }

    protected String[] getInputFileMD5Checksums() {
        return TestCaseHelper.getInputFileMD5Checksums();
    }

    /**
	 * Obtain a list of input test files.
	 * 
	 * @return A list of input test files.
	 */
	protected File[] getInputFiles() throws IOException {
		return TestCaseHelper.getInputFiles();
	}

	/**
	 * Obtain the directory within which the input files reside.
	 * 
	 * @return The directory within which the input files reside.
	 */
	protected File getInputFilesDirectory() {
		return TestCaseHelper.getInputFilesDirectory();
	}

	/**
	 * Obtain a count of the number of input files.
	 * 
	 * @return The number of input files.
	 * @throws IOException
	 */
	protected Integer getInputFilesLength() throws IOException {
		return getInputFiles().length;
	}

	/**
	 * Obtain a list of modified input files.
	 * 
	 * @return A list of modified input files.
	 * @throws IOException
	 */
	protected File[] getModFiles() throws IOException {
		return TestCaseHelper.getModFiles();
	}

	/**
	 * Obtain random text of a given length.
	 * 
	 * @param textLength
	 *            The length of text to generate.
	 * @return The random text.
	 */
	protected String getTestText(final Integer textLength) {
		return testCaseHelper.getTestText(textLength);
	}

    /**
     * Log a debug statement.
     * 
     * @param pattern
     *            A statement pattern.
     * @param arguments
     *            The pattern arguments.
     */
    protected void logDebug(final String debugPattern, final Object... debugArguments) {
        logger.logDebug(debugPattern, debugArguments);
    }

    /**
     * Log a trace statement.
     * 
     * @param pattern
     *            A statement pattern.
     * @param arguments
     *            The pattern arguments.
     */
    protected void logTrace(final String tracePattern, final Object... traceArguments) {
        logger.logTrace(tracePattern, traceArguments);
    }

    /**
     * Log a warning statement.
     * 
     * @param pattern
     *            A statement pattern.
     * @param arguments
     *            The pattern arguments.
     */
	protected void logWarning(final String warningPattern,
            final Object... warningArguments) {
	    logger.logWarning(warningPattern, warningArguments);
    }

	/**
	 * Remove a data item from the test session.
	 * 
	 * @param key
	 *            The data item key.
	 */
	protected void removeSessionData(final String key) {
		testCaseHelper.removeSessionDataItem(key);
	}

	/**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        logger.logTraceId();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logger.logTraceId();
        super.tearDown();
    }

    /**
	 * Read the byte content from a file.
	 * 
	 * @param file
	 *            The file to read.
	 * @return The byte content.
	 */
	private byte[] readBytes(final File file) throws FileNotFoundException,
			IOException {
		FileInputStream fis = null;
		final ByteBuffer byteBuffer;
		try {
			fis = new FileInputStream(file);
			byteBuffer = ByteBuffer.allocate(fis.available());
			final byte[] fileContentBuffer = new byte[512];
			int numBytesRead = fis.read(fileContentBuffer);
			while(-1 != numBytesRead) {
				byteBuffer.put(fileContentBuffer, 0, numBytesRead);
				// re-read from the stream
				numBytesRead = fis.read(fileContentBuffer);
			}
		}
		finally { fis.close(); }
		return byteBuffer.array();
	}

    protected static void assertTrue(final boolean expression,
            final String assertionPattern, final Object... assertionArguments) {
        assertTrue(new MessageFormat(assertionPattern)
                .format(assertionArguments), expression);
    }
}

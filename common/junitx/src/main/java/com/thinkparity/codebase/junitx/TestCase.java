/*
 * Created On: Feb 1, 2006
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Calendar;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.DateUtil.DateImage;
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

    /** An apache <code>Log4JWrapper</code> that outputs to the test log.*/
    protected static final Log4JWrapper TEST_LOGGER;

    static {
        TEST_LOGGER = new Log4JWrapper("TEST_LOGGER");
    }

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
            final Calendar expected, final Calendar actual) {
        final String e = DateUtil.format(expected, DateImage.ISO);
        final String a = DateUtil.format(actual, DateImage.ISO);
        assertEquals(assertion, e, a);
    }

    /**
     * Assert the contents of the two files are equal.
     * 
     * @param assertion
     *            An assertion message.
     * @param expected
     *            The expected <code>File</code>.
     * @param actual
     *            The actual <code>File</code>.
     * @throws IOException
     */
    protected static void assertEquals(final String assertion,
            final File expected, final File actual) throws IOException {
        final InputStream expectedStream = new FileInputStream(expected);
        try {
            final InputStream actualStream = new FileInputStream(actual);
            try {
                assertEquals(assertion, expectedStream, actualStream);
            } finally {
                actualStream.close();
            }
        } finally {
            expectedStream.close();
        }
    }

    /**
     * Assert the contents of the a file and an input stream are equal.
     * 
     * @param assertion
     *            An assertion message.
     * @param expected
     *            The expected <code>File</code>.
     * @param actual
     *            The actual <code>InputStream</code>.
     * @throws IOException
     */
    protected static void assertEquals(final String assertion,
            final File expected, final InputStream actual) throws IOException {
        final InputStream expectedStream = new FileInputStream(expected);
        try {
            assertEquals(assertion, expectedStream, actual);
        } finally {
            expectedStream.close();
        }
    }

    /**
     * Assert the contents of the a file and an input stream are equal.
     * 
     * @param assertion
     *            An assertion message.
     * @param expected
     *            The expected <code>InputStream</code>.
     * @param actual
     *            The actual <code>File</code>.
     * @throws IOException
     */
    protected static void assertEquals(final String assertion,
            final InputStream expected, final File actual) throws IOException {
        final InputStream actualStream = new FileInputStream(actual);
        try {
            assertEquals(assertion, expected, actualStream);
        } finally {
            actualStream.close();
        }
    }

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
        final byte[] expectedBuffer = new byte[TestCaseHelper.getBufferSize()];
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

    protected static void assertTrue(final boolean expression,
            final String assertionPattern, final Object... assertionArguments) {
        assertTrue(new MessageFormat(assertionPattern)
                .format(assertionArguments), expression);
    }

    /**
     * Calculate a checksum for a file's contents. Create a channel to read the
     * file.
     * 
     * @param file
     *            A <code>File</code>.
     * @return An MD5 checksum <code>String</code>.
     */
    protected static final String checksum(final File file) throws IOException {
        return TestCaseHelper.checksum(file);
    }

    /**
	 * Create a failure message for the throwable.
	 * 
	 * @param t
	 *            The throwable.
	 * @return The failure message.
	 */
	protected static String createFailMessage(final Throwable cause) {
		return createFailMessage(cause, "Test failure.");
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
    protected static String createFailMessage(final Throwable cause,
            final String message, final Object... arguments) {
        return TestCaseHelper.createFailMessage(cause, message, arguments);
    }

    /**
     * Fail a test.
     * 
     * @param message
     *            A fail message <code>String</code>.
     * @param arguments
     *            The fail message arguments <code>Object[]</code>.
     */
    protected static final void fail(final String message,
            final Object... arguments) {
        fail(MessageFormat.format(message, arguments));
    }

    /**
     * Fail a test.
     * 
     * @param message
     *            A fail message <code>String</code>.
     * @param arguments
     *            The fail message arguments <code>Object[]</code>.
     */
    protected static final void fail(final Throwable cause,
            final String message, final Object... arguments) {
        fail(createFailMessage(cause, message, arguments));
    }

    /**
     * Copy the content of a file to another file. Create a channel to read the
     * file.
     * 
     * @param readFile
     *            A <code>File</code>.
     * @param writeFile
     *            A <code>File</code>.
     * @throws IOException
     */
    protected static final void fileToFile(final File readFile,
            final File writeFile) throws IOException {
        TestCaseHelper.fileToFile(readFile, writeFile);
    }

    /**
     * Obtain the default buffer size for a test case.
     * 
     * @return A buffer size <code>Integer</code>.
     */
    protected static final ByteBuffer getBuffer() {
        return TestCaseHelper.getBuffer();
    }

	protected static final byte[] getBufferArray() {
        return TestCaseHelper.getBufferArray();
    }

	protected static final Object getBufferLock() {
        return TestCaseHelper.getBufferLock();
    }

	protected static final File getSequenceFile(final Integer sequence,
            final Integer index) {
        return TestCaseHelper.getSequenceFile(sequence, index);
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
     * Copy the content of a stream to a file. Use a channel to write to the
     * file.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @param file
     *            A <code>File</code>.
     * @throws IOException
     */
    protected static final void streamToFile(final InputStream stream,
            final File file) throws IOException {
        TestCaseHelper.streamToFile(stream, file);
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

    /**
     * Write a string to a file.
     * 
     * @param file
     *            A <code>File</code>.
     * @param string
     *            A <code>String</code>.
     * @throws IOException
     */
    protected static void write(final File file, final String string)
            throws IOException {
        TestCaseUtil.write(file, string);
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
		this.logger = TEST_LOGGER;
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
	 * Clear the session data.
	 *
	 */
	protected void clearSessionData() {
		testCaseHelper.clearSessionData();
	}

    /**
     * Copy the input files to a target directory.
     * 
     * @param target
     *            A target directory <code>File</code>.
     */
    protected void copyInputFiles(final File target) throws IOException {
        assertNotNull("Target cannot be null.", target);
        assertTrue(target.isDirectory(), "Target must be a directory.");
        assertTrue(target.canRead(), "Target must be readable.");
        assertTrue(target.canWrite(), "Target must be writable.");
        final File[] inputFiles = getInputFiles();
        for (final File inputFile : inputFiles) {
            fileToFile(inputFile, new File(target, inputFile.getName()));
        }
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

    protected String[] getInputFileMD5Checksums() {
        return TestCaseHelper.getInputFileMD5Checksums();
    }

    /**
     * Obtain a list of input file names.
     * 
     * @return A <code>String[]</code>.
     */
    protected String[] getInputFileNames() {
        return TestCaseHelper.getInputFileNames();
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
     * Obtain the test case directory.
     * 
     * @return A test case specific directory <code>File</code>.
     */
    protected File getTestCaseDirectory() {
        return testCaseHelper.getTestCaseDirectory();
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
}

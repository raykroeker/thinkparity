/*
 * Feb 1, 2006
 */
package com.raykroeker.junitx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

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
	 * Obtain the test session.
	 * 
	 * @return The test session.
	 */
	protected static TestSession getTestSession() {
		return TestCaseHelper.getTestSession();
	}

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger testLogger;

	/**
	 * The test case helper class.
	 * 
	 */
	private final TestCaseHelper testCaseHelper;

	/**
	 * Create a TestCase.
	 * 
	 * @param name
	 *            The test name.
	 */
	protected TestCase(final String name) {
		super(name);
		this.testCaseHelper = new TestCaseHelper(this);
		this.testLogger = Logger.getLogger(getClass());
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
	 * Create a failure message for the throwable.
	 * 
	 * @param t
	 *            The throwable.
	 * @return The failure message.
	 */
	protected String createFailMessage(final Throwable t) {
		return TestCaseHelper.createFailMessage(t);
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
	 * Remove a data item from the test session.
	 * 
	 * @param key
	 *            The data item key.
	 */
	protected void removeSessionData(final String key) {
		testCaseHelper.removeSessionDataItem(key);
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

	protected static void assertEquals(final Iterable expected, final Iterable actual) {}
	protected static void assertEquals(final String message, final Iterable expected, final Iterable actual) {}
}

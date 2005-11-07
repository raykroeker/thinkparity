/*
 * Nov 5, 2003
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * <b>Title:</b>  CodebaseTestCase
 * <br><b>Description:</b>  
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class CodebaseTestCase extends TestCase {

	/**
	 * Test logger.
	 */
	protected final Logger logger = Logger.getRootLogger();

	private final CodebaseTestHelper helper;

	/**
	 * Create a new CodebaseTestCase
	 * @param arg0
	 */
	protected CodebaseTestCase(String name) {
		super(name);
		this.helper = new CodebaseTestHelper();
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
		helper.assertContentEquals(expected, actual);
	}

	/**
	 * Create a test directory.
	 * 
	 * @param name
	 *            The name of the test directory.
	 */
	protected File createTestDirectory(final String name) {
		return helper.createTestDirectory(name);
	}

	/**
	 * Write the stack of the throwable to a string for a failure message.
	 * 
	 * @param throwable
	 *            The error that occured.
	 * @return The stack trace as a string.
	 * @see Throwable#printStackTrace(java.io.PrintWriter)
	 */
	protected String getFailMessage(final Throwable throwable) {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		throwable.printStackTrace(printWriter);
		return stringWriter.toString();
	}

	protected Collection<CodebaseTestFile> getJUnitTestFiles() {
		return helper.getJUnitTestFiles();
	}

	protected Integer getJUnitTestFilesSize() {
		return helper.getJUnitTestFilesSize();
	}
}

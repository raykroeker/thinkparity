/*
 * Jul 1, 2005
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

/**
 * CodebaseTestHelper
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CodebaseTestHelper {

	/**
	 * JUnit output directory.
	 */
	private static final File jUnitSessionDirectory;

	/**
	 * Unique session id for each junit session.
	 */
	private static final String jUnitSessionId;

	/**
	 * Start time of the junit session.
	 */
	private static final Long jUnitSessionStart;

	/**
	 * Files to use when testing.
	 */
	private static final Vector<JUnitTestFile> jUnitTestFiles;

	static {
		// record the session start time.
		jUnitSessionStart = System.currentTimeMillis();
		// record the session id.
		jUnitSessionId = "jUnit." + jUnitSessionStart;
		// load test files
		jUnitTestFiles = new Vector<JUnitTestFile>(4);
		jUnitTestFiles.add(new JUnitTestFile("JUnitTestFramework.doc"));
		jUnitTestFiles.add(new JUnitTestFile("JUnitTestFramework.odt"));
		jUnitTestFiles.add(new JUnitTestFile("JUnitTestFramework.png"));
		jUnitTestFiles.add(new JUnitTestFile("JUnitTestFramework.txt"));
		jUnitTestFiles.add(new JUnitTestFile("JUnitTestFramework1MB.txt"));
		jUnitTestFiles.add(new JUnitTestFile("JUnitTestFramework2MB.txt"));
		jUnitTestFiles.add(new JUnitTestFile("JUnitTestFramework4MB.txt"));
		// set session directory
		jUnitSessionDirectory = new File(new StringBuffer(System.getProperty("user.dir"))
			.append(File.separatorChar).append("test-sessions")
			.append(File.separatorChar).append(jUnitSessionId)
			.toString());
		if(jUnitSessionDirectory.exists())
			FileUtil.deleteTree(jUnitSessionDirectory);
		Assert.assertTrue("CodebaseTestHelper<init>", jUnitSessionDirectory.mkdirs());
		// initialize the logger
		CodebaseTestLoggerConfigurator.configure(jUnitSessionId, jUnitSessionDirectory);
	}

	/**
	 * Create a CodebaseTestHelper
	 */
	CodebaseTestHelper() { super(); }

	/**
	 * Assert the content of the two files is equal.
	 * 
	 * @param expected
	 *            The file with the expected content.
	 * @param actual
	 *            The actual content.
	 */
	void assertContentEquals(final File expected, final File actual)
			throws FileNotFoundException, IOException {
		final byte[] expectedBytes = FileUtil.readBytes(expected);
		final byte[] actualBytes = FileUtil.readBytes(actual);
		CodebaseTestCase.assertEquals(expectedBytes.length, actualBytes.length);
		for(int i = 0; i < expectedBytes.length; i++) {
			CodebaseTestCase.assertEquals(expectedBytes[i], actualBytes[i]);
		}
	}

	/**
	 * Create a test directory.
	 * 
	 * @param name
	 *            The name of the test directory.
	 * @return The test directory.
	 */
	File createTestDirectory(final String name) {
		testDirectoryCount++;
		final File testDirectory =
			new File(CodebaseTestHelper.jUnitSessionDirectory, testDirectoryCount.toString());
		Assert.assertTrue("createTestDirectory(String)", testDirectory.mkdir());
		return testDirectory;
	}
	
	/**
	 * Counter for the directories created.
	 */
	private static Integer testDirectoryCount = 0;

	/**
	 * Obtain a list of the test files available to the jUnit test framework.
	 * 
	 * @return A list of the test files available to the jUnit test framework.
	 */
	Collection<JUnitTestFile> getJUnitTestFiles() { 
		return Collections.unmodifiableCollection(CodebaseTestHelper.jUnitTestFiles);
	}

	/**
	 * Obtain the number of jUnit test files.
	 * 
	 * @return The number of jUnit test files.
	 */
	Integer getJUnitTestFilesSize(){
		return CodebaseTestHelper.jUnitTestFiles.size();
	}
}

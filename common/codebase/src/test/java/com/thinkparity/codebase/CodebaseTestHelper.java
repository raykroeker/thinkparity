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
	private static final Vector<CodebaseTestFile> jUnitTestFiles;

	static {
		// record the session start time.
		jUnitSessionStart = System.currentTimeMillis();
		// record the session id.
		jUnitSessionId = "jUnit." + jUnitSessionStart;
		// set the resources directory
		final File resourcesDirectory = new File(new StringBuffer(System.getProperty("user.dir"))
				.append(File.separatorChar).append("target")
				.append(File.separatorChar).append("test-classes")
				.append(File.separatorChar).append("com")
				.append(File.separatorChar).append("thinkparity")
				.append(File.separatorChar).append("codebase")
				.toString());
		// set the test files
		final File jUnitResourcesFiles =
			new File(resourcesDirectory, "junit.files");
		jUnitTestFiles = new Vector<CodebaseTestFile>(4);
		jUnitTestFiles.add(
				new CodebaseTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework.doc")));
		jUnitTestFiles.add(
				new CodebaseTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework.odt")));
		jUnitTestFiles.add(
				new CodebaseTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework.png")));
		jUnitTestFiles.add(
				new CodebaseTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework.txt")));
		jUnitTestFiles.add(
				new CodebaseTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework 1MB.txt")));
		jUnitTestFiles.add(
				new CodebaseTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework 2MB.txt")));
		jUnitTestFiles.add(
				new CodebaseTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework 4MB.txt")));
		// set the output directory
		jUnitSessionDirectory = new File(new StringBuffer(System.getProperty("user.dir"))
			.append(File.separatorChar).append("target")
			.append(File.separatorChar).append(jUnitSessionId)
			.toString());
		if(jUnitSessionDirectory.exists())
			FileUtil.deleteTree(jUnitSessionDirectory);
		Assert.assertTrue("CodebaseTestHelper<init>", jUnitSessionDirectory.mkdir());
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
	Collection<CodebaseTestFile> getJUnitTestFiles() { 
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

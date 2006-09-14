/*
 * Feb 1, 2006
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.thinkparity.codebase.StringUtil;


/**
 * @author raykroeker@gmail.com
 */
public class TestCaseHelper {

	/**
	 * List of input files to use for testing.
	 * 
	 */
	private static List<File> inputFiles;

	/**
	 * List of modified input files to use for testing.
	 * 
	 */
	private static List<File> modFiles;

	/**
	 * The directory that contains the input files.
	 * 
	 * @see #getInputFilesDirectory()
	 */
	private static File inputFilesDirectory;

	/**
	 * JUnit root directory. Either the "${basedir}/target" or
	 * "${user.dir}/target" directories must exist in order to use the JUnit
	 * extensions. The root directory will be "test-sessions" beneath the first
	 * of these two directories that exists.
	 * 
	 * @see #getRootDirectory()
	 */
	private static File rootDirectory;

	/**
	 * Test randomizer.
	 */
	private static Random testRandomizer;

	/**
	 * Session id of the current jUnit runtime session.
	 * 
	 */
	private static final TestSession testSession;

	/**
	 * The alphabet used to obtain test text.
	 * 
	 */
	private static char[] testTextAlphabet;

	static {
		testSession = new TestSession(getRootDirectory());
        TestInitializer.initialize(testSession);
		try {
            getInputFiles();
		} catch (final IOException iox) {
            TestCase.fail(createFailMessage(iox));
		}
		Logger.getLogger(JUnitX.class).log(Level.INFO, JUnitX.MESSAGE_INIT);
	}

	/**
	 * Create a failure message for the throwable.
	 * 
	 * @param t
	 *            The throwable.
	 * @return The failure message.
	 */
	static String createFailMessage(final Throwable t) {
		return StringUtil.printStackTrace(t);
	}

	/**
	 * Obtain a list of all of the input files.
	 * @return The input files.
	 */
	static File[] getInputFiles() throws IOException {
		if(null == inputFiles) {
			inputFiles = new LinkedList<File>();
			inputFiles.add(copyInputFile("JUnitTestFramework.doc"));
			inputFiles.add(copyInputFile("JUnitTestFramework.odt"));
			inputFiles.add(copyInputFile("JUnitTestFramework.png"));
			inputFiles.add(copyInputFile("JUnitTestFramework.txt"));
			inputFiles.add(copyInputFile("JUnitTestFramework.unknown"));
			inputFiles.add(copyInputFile("JUnitTestFramework1MB.txt"));
			inputFiles.add(copyInputFile("JUnitTestFramework2MB.txt"));
			inputFiles.add(copyInputFile("JUnitTestFramework4MB.txt"));
		}
		return inputFiles.toArray(new File[] {});
	}

	static File[] getModFiles() throws IOException {
		if(null == modFiles) {
			modFiles = new LinkedList<File>();
			modFiles.add(copyInputFile("JUnitTestFrameworkMod.doc"));
			modFiles.add(copyInputFile("JUnitTestFrameworkMod.odt"));
			modFiles.add(copyInputFile("JUnitTestFrameworkMod.png"));
			modFiles.add(copyInputFile("JUnitTestFrameworkMod.txt"));
			modFiles.add(copyInputFile("JUnitTestFrameworkMod.unknown"));
			modFiles.add(copyInputFile("JUnitTestFramework1MBMod.txt"));
			modFiles.add(copyInputFile("JUnitTestFramework2MBMod.txt"));
			modFiles.add(copyInputFile("JUnitTestFramework4MBMod.txt"));
		}
		return modFiles.toArray(new File[] {});
	}

	/**
	 * Obtain the directory within which the input files will reside.
	 * 
	 * @return The input directory.
	 */
	static File getInputFilesDirectory() {
		if(null == inputFilesDirectory) {
			inputFilesDirectory = new File(testSession.getSessionDirectory(), JUnitX.getShortName() + "Input");
			Assert.assertTrue(inputFilesDirectory.mkdir());
		}
		return inputFilesDirectory;
	}

	/**
	 * Obtain the test session.
	 * 
	 * @return The test session.
	 */
	static TestSession getTestSession() { return testSession; }

	/**
	 * Copy the input file of the given name to the input files directory. This
	 * api will use the classloader to read the resource then copy the stream
	 * directly to a file of the same name in the input directory.
	 * 
	 * @param inputName
	 *            The input file name.
	 * @param outputName
	 *            The output file name.
	 * @throws IOException
	 */
	private static File copyInputFile(final String inputName) throws IOException {
		return copyInputFile(inputName, inputName);
	}

	/**
	 * Copy the input file of the given name to the input files directory. This
	 * api will use the classloader to read the resource then copy the stream
	 * directly to a file of the same name in the input directory.
	 * 
	 * @param inputName
	 *            The input file name.
	 * @param outputName
	 *            The output file name.
	 * @throws IOException
	 */
	private static File copyInputFile(final String inputName,
			final String outputName) throws IOException {
		final File outputFile = new File(getInputFilesDirectory(), outputName);
		Assert.assertTrue(outputFile.createNewFile());
		final InputStream is = TestCaseHelper.class.getResourceAsStream(inputName);
		final OutputStream os = new FileOutputStream(outputFile);
		try {
			int len;
			final byte[] b = new byte[512];
			while((len = is.read(b)) > 0) {
				os.write(b, 0, len);
			}
			os.flush();
		}
		finally {
			try { os.close(); }
			finally { is.close(); }
		}
		return outputFile;
	}

	/**
	 * Obtain a handle to the JUnit root directory.
	 * 
	 * @return The JUnit root directory. If the root directory cannot be
	 *         resolved the test run will fail.
	 */
	private static File getRootDirectory() {
		if(null == rootDirectory) {
			File targetDirectory = getRootDirectoryTarget("basedir");
			if(isViableRootDirectory(targetDirectory)) {
				rootDirectory = new File(targetDirectory, "test-sessions");
			}
			else {
				targetDirectory = getRootDirectoryTarget("user.dir");
				if(isViableRootDirectory(targetDirectory)) {
					rootDirectory = new File(targetDirectory, "test-sessions");
				}
				else { TestCase.fail(""); }
			}
			if(!rootDirectory.exists())
				Assert.assertTrue(rootDirectory.mkdir());
		}
		return rootDirectory;
	}

	/**
	 * Obtain the target directory beneath the directory that exists at the
	 * system property of the given key.
	 * 
	 * @param propertyKey
	 *            The system property key.
	 * @return The target directory.
	 */
	private static File getRootDirectoryTarget(final String propertyKey) {
		final String propertyValue = System.getProperty(propertyKey);
		if(null == propertyValue) { return null; }
		else { return new File(propertyValue, "target"); }
	}

	/**
	 * Obtain the text alphabet.
	 * 
	 * @return The text alphabet.
	 */
	private static char[] getTestTextAlphabet() {
		if(null == testTextAlphabet) {
			testTextAlphabet = new char[] {
					'0','1','2','3','4','5','6','7','8','9',
					'A','B','C','D','E','F','G','H','I','J',
					'K','L','M','N','O','P','Q','R','S','T',
					'U','V','W','X','Y','Z',
					'0','1','2','3','4','5','6','7','8','9',
					'a','b','c','d','e','f','g','h','i','j',
					'k','l','m','n','o','p','q','r','s','t',
					'u','v','w','x','y','z',' '};
		}
		return testTextAlphabet;
	}

	/**
	 * Determine whether or not the directory is a viable JUnit root directory.
	 * 
	 * @param directory
	 *            The directory to check.
	 * @return True if the directory exists; can be read from and written to.
	 */
	private static Boolean isViableRootDirectory(final File directory) {
		return null != directory
			&& directory.exists()
			&& directory.isDirectory()
			&& directory.canRead()
			&& directory.canWrite();
	}

	/**
	 * The test case.
	 * 
	 */
	private TestCase testCase;

	/**
	 * The test case directory. Is created as required.
	 * 
	 * @see #getTestCaseDirectory()
	 */
	private File testCaseDirectory;

	/**
	 * Create a TestCaseHelper.
	 */
	TestCaseHelper(final TestCase testCase) {
		super();
		this.testCase = testCase;
	}

	/**
	 * Add a data item to the test session.
	 * 
	 * @param dataKey
	 *            The data item key.
	 * @param dataValue
	 *            The data item value.
	 */
	void addSessionDataItem(final String key, final Object value) {
		final Object originalValue = testSession.getData(key);
		Assert.assertNull(originalValue);
		testSession.setData(key, value);
	}

	/**
	 * Clear the session data.
	 *
	 */
	void clearSessionData() { testSession.clearData(); }

	/**
	 * Create a directory.
	 * 
	 * @param directoryName
	 *            The directory name.
	 * @return The directory.
	 */
	File createDirectory(final String directoryName) {
		final File directory = new File(getTestCaseDirectory(), directoryName);
		Assert.assertTrue(directory.mkdir());
		return directory;
	}

	/**
	 * Obtain test text of a given length.
	 * 
	 * @param textLength
	 *            The test text length.
	 * @return The test text.
	 */
	String getTestText(final Integer textLength) {
		Assert.assertTrue(textLength > 0);
		final StringBuffer textBuffer = new StringBuffer(textLength);
		for(int i = 0; i < textLength; i++) {
			textBuffer.append(
					getTestTextAlphabet()[getTestRandomizer().nextInt(
							testTextAlphabet.length)]);
		}
		return textBuffer.toString();
	}

	/**
	 * Remove a data item from the test session
	 * @param key The data item key.
	 */
	void removeSessionDataItem(final String key) {
		final Object originalValue = testSession.getData(key);
		Assert.assertNotNull(originalValue);
		testSession.setData(key, null);
	}

	/**
	 * Obtain the directory for the test case.
	 * 
	 * @return The directory for the test case.
	 */
	private File getTestCaseDirectory() {
		if(null == testCaseDirectory) {
			testCaseDirectory =
				new File(testSession.getSessionDirectory(), testCase.getName());
			Assert.assertTrue(testCaseDirectory.mkdir());
		}
		return testCaseDirectory;
	}

	/**
	 * Obtain a randomizer for the test session.
	 * 
	 * @return A randomizer.
	 */
	private Random getTestRandomizer() {
		if(null == testRandomizer) { testRandomizer = new Random(); }
		return testRandomizer;
	}
}

/*
 * Feb 1, 2006
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;


/**
 * @author raykroeker@gmail.com
 */
public class TestCaseHelper {

	/** A <code>List</code> of input file md5 checksum <code>String</code>s. */
    private static List<String> inputFileMD5Checksums;

    /** A <code>List</code> of input file name <code>String</code>s. */
    private static List<String> inputFileNames;

    /** A <code>List</code> of input <code>File</code>s. */
	private static List<File> inputFiles;
    
	/**
	 * The directory that contains the input files.
	 * 
	 * @see #getInputFilesDirectory()
	 */
	private static File inputFilesDirectory;

	private static FileSystem inputFilesFileSystem;

	/**
	 * List of modified input files to use for testing.
	 * 
	 */
	private static List<File> modFiles;

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
		try {
		    testSession = new TestSession(getRootDirectory());
		    TestInitializer.initialize(testSession);
            getInputFiles();
		} catch (final IOException iox) {
            throw new TestException(iox);
		}
		new Log4JWrapper("TEST_LOGGER").logInfo(JUnitX.MESSAGE_INIT);
	}

    /**
     * Create a formatted failure message.
     * 
     * @param cause
     *            The <code>Throwable</code> cause of the failure.
     * @param The
     *            message <code>String</code>. The message
     *            <code>Object[]</code> arguments.
     * @return The failure message.
     */
	static String createFailMessage(final Throwable cause, final String message,
            final Object... arguments) {
        final StringBuilder failMessageBuilder = new StringBuilder(
                MessageFormat.format(message, arguments))
            .append(Separator.SystemNewLine);
		failMessageBuilder.append(StringUtil.printStackTrace(cause));
        return failMessageBuilder.toString();
	}

    static String[] getInputFileMD5Checksums() {
        if (null == inputFileMD5Checksums) {
            inputFileMD5Checksums = new ArrayList<String>();
            inputFileMD5Checksums.add("bff1335bddbbec6825f28a452cf89eb7");
            inputFileMD5Checksums.add("da943e042bba49db773955de4596ade8");
            inputFileMD5Checksums.add("c672813799783c87e1d0c71ea01aa203");
            inputFileMD5Checksums.add("f8600055e0e298e758d7a3f47a0794be");
            inputFileMD5Checksums.add("f025bb38caeae6d25d41af5da12a1088");
            inputFileMD5Checksums.add("65cbd1464eb95d74cdcc9ec73fb92a7e");
            inputFileMD5Checksums.add("65cbd1464eb95d74cdcc9ec73fb92a7e");
            inputFileMD5Checksums.add("23c48cbb68603cd9a078e548cce61ab7");
            inputFileMD5Checksums.add("a32663e1998e908a73e4c9e88287b9ba");
            inputFileMD5Checksums.add("19edc66c9f6603dad03fd3dde85f02cb");
            inputFileMD5Checksums.add("30e61cba71f4f7499f74f0dbc6ede060");
        }
        return inputFileMD5Checksums.toArray(new String[] {});
    }
	/**
     * Obtain a list of the input file names.
     * 
     * @return A <code>String[]</code>.
     */
    static String[] getInputFileNames() {
        if (null == inputFileNames) {
            inputFileNames = new ArrayList<String>();
            inputFileNames.add("JUnitTestFramework");
            inputFileNames.add("JUnitTestFramework.doc");
            inputFileNames.add("JUnitTestFramework.odt");
            inputFileNames.add("JUnitTestFramework.pdf");
            inputFileNames.add("JUnitTestFramework.png");
            inputFileNames.add("JUnitTestFramework.txt");
            inputFileNames.add("JUnitTestFramework.unknown");
            inputFileNames.add("JUnitTestFramework1MB.txt");
            inputFileNames.add("JUnitTestFramework2MB.txt");
            inputFileNames.add("JUnitTestFramework4MB.txt");
            inputFileNames.add("JUnitTestFramework8MB.txt");
        }
        return inputFileNames.toArray(new String[] {});
    }

    /**
     * Obtain a list of all of the input files.
     * 
     * @return A <code>File[]</code>.
     */
	static File[] getInputFiles() throws IOException {
		if (null == inputFiles) {
			inputFiles = new LinkedList<File>();
            for (final String inputFileName : getInputFileNames()) {
                inputFiles.add(copyFile(inputFileName));
            }
		}
		return inputFiles.toArray(new File[] {});
	}

	/**
	 * Obtain the directory within which the input files will reside.
	 * 
	 * @return The input directory.
	 */
	static File getInputFilesDirectory() {
		if(null == inputFilesDirectory) {
			inputFilesDirectory = new File(testSession.getSessionDirectory(), JUnitX.getShortName() + "Input");
			Assert.assertTrue(inputFilesDirectory.mkdir(),
                    "Could not create directory {0}.", inputFilesDirectory);
		}
		return inputFilesDirectory;
	}

	static File[] getModFiles() throws IOException {
		if(null == modFiles) {
			modFiles = new LinkedList<File>();
            modFiles.add(copyFile("JUnitTestFrameworkMod"));
			modFiles.add(copyFile("JUnitTestFrameworkMod.doc"));
			modFiles.add(copyFile("JUnitTestFrameworkMod.odt"));
            modFiles.add(copyFile("JUnitTestFrameworkMod.pdf"));
			modFiles.add(copyFile("JUnitTestFrameworkMod.png"));
			modFiles.add(copyFile("JUnitTestFrameworkMod.txt"));
			modFiles.add(copyFile("JUnitTestFrameworkMod.unknown"));
			modFiles.add(copyFile("JUnitTestFramework1MBMod.txt"));
			modFiles.add(copyFile("JUnitTestFramework2MBMod.txt"));
			modFiles.add(copyFile("JUnitTestFramework4MBMod.txt"));
		}
		return modFiles.toArray(new File[] {});
	}

	static final File getSequenceFile(final Integer sequence,
            final Integer index) {
        final FileSystem target = getInputFilesFileSystem();
        final String pathPrefix = new StringBuilder("sequence-")
            .append(sequence).append("/gen-").toString();
        final String targetPath = new StringBuilder(pathPrefix)
            .append(index).toString();
        File sequenceFile = target.findFile(targetPath); 
        if (null == sequenceFile) {
            try {
                final String sourcePath = new StringBuilder(pathPrefix)
                    .append("0").toString();
                sequenceFile = copyFile(sourcePath, targetPath);
            } catch (final IOException iox)  {
                throw new TestException("Could not copy sequence file.", iox);
            }
        }
        return sequenceFile;
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
     * @param path
     *            The path of the resource beneath "junitx-files/".
     * @throws IOException
     */
	private static File copyFile(final String path) throws IOException {
        return copyFile(path, path);
    }

    private static File copyFile(final String sourcePath,
            final String targetPath) throws IOException {
        final FileSystem target = getInputFilesFileSystem();
        if (null == target.findFile(targetPath)) {
            final File targetFile = target.createFile(targetPath);
            final OutputStream output = new FileOutputStream(targetFile);
            try {
                final String inputPath = "junitx-files/" + sourcePath;
                final InputStream input = getInputStream(inputPath);
                try {
                    // BUFFER - 1KB - TestCaseHelper#copyInputFile(String, String)
                    StreamUtil.copy(input, output, ByteBuffer.allocate(1024));
                } finally {
                    input.close();
                }
            } finally {
                output.close();
            }
        }
		return target.findFile(targetPath);
	}

	private static FileSystem getInputFilesFileSystem() {
        if (null == inputFilesFileSystem) {
            inputFilesFileSystem = new FileSystem(getInputFilesDirectory());
        }
        return inputFilesFileSystem;
    }

    /**
     * Obtain a resource as an input stream from the class.
     * 
     * @param name
     *            A resource name <code>String</code>.
     * @return An <code>InputStream</code>.
     */
    private static InputStream getInputStream(final String resourcePath) {
        return ResourceUtil.getInputStream(resourcePath);
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
				Assert.assertTrue(rootDirectory.mkdir(),
                        "Could not create directory {0}.", rootDirectory);
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
		Assert.assertIsNull(originalValue,
                "Cannot overwrite original value {0} for {1}.", originalValue, key);
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
		Assert.assertTrue(directory.mkdir(),
                "Cannot create directory {0}.", directory);
		return directory;
	}

	/**
	 * Obtain the directory for the test case.
	 * 
	 * @return The directory for the test case.
	 */
	File getTestCaseDirectory() {
		if(null == testCaseDirectory) {
			testCaseDirectory =
				new File(testSession.getSessionDirectory(), testCase.getName());
			Assert.assertTrue(testCaseDirectory.mkdir(),
                    "Cannot create directory {0}.", testCaseDirectory);
		}
		return testCaseDirectory;
	}

	/**
	 * Obtain test text of a given length.
	 * 
	 * @param textLength
	 *            The test text length.
	 * @return The test text.
	 */
	String getTestText(final Integer textLength) {
		Assert.assertTrue(textLength > 0, "Text length is {0}.", textLength);
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
		Assert.assertNotNull(originalValue, "Session value for {0} is null.", key);
		testSession.setData(key, null);
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

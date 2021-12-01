/*
 * Feb 1, 2006
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import junit.framework.TestCase;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.codec.MD5Util;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.nio.ChannelUtil;

/**
 * @author raymond@raykroeker.com
 */
public class TestCaseHelper {

    /** A test case <code>ByteBuffer</code>. */
	private static ByteBuffer buffer;

    /** A test case buffer <code>byte[]</code>. */
    private static byte[] bufferArray;

    /** A test case buffer lock <code>Object</code>. */
    private static Object bufferLock;
    
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
     * Copy the content of one channel to another. Use the workspace buffer as
     * an intermediary.
     * 
     * @param readChannel
     *            A <code>ReadableByteChannel</code>.
     * @param writeChannel
     *            A <code>WritableByteChannel</code>.
     * @throws IOException
     */
    static final void channelToChannel(
            final ReadableByteChannel readChannel,
            final WritableByteChannel writeChannel) throws IOException {
        synchronized (getBufferLock()) {
            ChannelUtil.copy(readChannel, writeChannel, getBuffer());
        }
    }

	/**
     * Copy the content of a channel to a file. Create a channel to write to the
     * file.
     * 
     * @param channel
     *            A <code>ReadableByteChannel</code>.
     * @param file
     *            A <code>File</code>.
     * @throws IOException
     */
    static final void channelToFile(final ReadableByteChannel channel,
            final File file) throws IOException {
        final WritableByteChannel writeChannel = ChannelUtil.openWriteChannel(file);
        try {
            channelToChannel(channel, writeChannel);
        } finally {
            writeChannel.close();
        }
    }

	/**
     * Copy the content of a channel to a stream. Use the workspace buffer as an
     * intermediary.
     * 
     * @param channel
     *            A <code>ReadableByteChannel</code>.
     * @param stream
     *            An <code>OutputStream</code>.
     * @throws IOException
     */
    static final void channelToStream(final ReadableByteChannel channel,
            final OutputStream stream) throws IOException {
        synchronized (getBufferLock()) {
            StreamUtil.copy(channel, stream, getBuffer());
        }
    }

	/**
     * Calculate a checksum for a file's contents. Create a channel to read the
     * file.
     * 
     * @param file
     *            A <code>File</code>.
     * @return An MD5 checksum <code>String</code>.
     */
    static final String checksum(final File file) throws IOException {
        final ReadableByteChannel channel = ChannelUtil.openReadChannel(file);
        try {
            return checksum(channel);
        } finally {
            channel.close();
        }
    }

    /**
     * Calculate a checksum for a readable byte channel. Use the workspace
     * buffer as an intermediary.
     * 
     * @param channel
     *            A <code>ReadableByteChannel</code>.
     * @return An MD5 checksum <code>String</code>.
     */
    static final String checksum(final ReadableByteChannel channel)
            throws IOException {
        synchronized (getBufferLock()) {
            return MD5Util.md5Base64(channel, getBufferArray());
        }
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
    static final void fileToFile(final File readFile,
            final File writeFile) throws IOException {
        final ReadableByteChannel readChannel = ChannelUtil.openReadChannel(readFile);
        try {
            channelToFile(readChannel, writeFile);
        } finally {
            readChannel.close();
        }
    }

    /**
     * Copy the content of a file to a stream. Create a channel to read the
     * file.
     * 
     * @param file
     *            A <code>File</code>.
     * @param stream
     *            An <code>OutputStream</code>.
     * @throws IOException
     */
    static final void fileToStream(final File file,
            final OutputStream stream) throws IOException {
        final ReadableByteChannel channel = ChannelUtil.openReadChannel(file);
        try {
            channelToStream(channel, stream);
        } finally {
            channel.close();
        }
    }

	static ByteBuffer getBuffer() {
        if (null == buffer) {
            buffer = ByteBuffer.allocateDirect(getBufferSize());
        }
        return buffer;
    }

	static final byte[] getBufferArray() {
        if (null == bufferArray) {
            bufferArray = new byte[getBufferSize()];
        }
        return bufferArray;
    }

	static final Object getBufferLock() {
        if (null == bufferLock) {
            bufferLock = new Object();
        }
        return bufferLock;
    }

    /**
     * Obtain the buffer size for a test case.
     * 
     * @return A buffer size <code>Integer</code>.
     */
    static final Integer getBufferSize() {
        // BUFFER - 2MB - TestCase#getDefaultBufferSize()
        return 1024 * 1024 * 2;
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
     * Copy the content of a stream to a channel. Use the workspace byte buffer
     * as an intermediary.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @param channel
     *            A <code>WritableByteChannel</code>.
     * @throws IOException
     */
    static final void streamToChannel(final InputStream stream,
            final WritableByteChannel channel) throws IOException {
        synchronized (getBufferLock()) {
            StreamUtil.copy(stream, channel, getBuffer());
        }
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
    static final void streamToFile(final InputStream stream,
            final File file) throws IOException {
        final WritableByteChannel channel = ChannelUtil.openWriteChannel(file);
        try {
            streamToChannel(stream, channel);
        } finally {
            channel.close();
        }
    }

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

    private static File copyFile(final String readPath, final String writePath)
            throws IOException {
        final FileSystem target = getInputFilesFileSystem();
        if (null == target.findFile(writePath)) {
            final File writeFile = target.createFile(writePath);
            final String inputPath = "junitx-files/" + readPath;
            final InputStream stream = getInputStream(inputPath);
            try {
                streamToFile(stream, writeFile);
            } finally {
                stream.close();
            }
        }
		return target.findFile(writePath);
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

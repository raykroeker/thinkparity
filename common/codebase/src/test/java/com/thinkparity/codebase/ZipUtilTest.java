/*
 * Jan 31, 2006
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ZipUtilTest extends CodebaseTestCase {

	private Fixture datum;

	/**
	 * Create ZipUtilTest.
	 * 
	 */
	public ZipUtilTest() {
        super("ZipUtilTest");
	}

    /**
     * Test zip creation/extraction.
     *
     */
	public void testZipUtil() {
        logger.logTrace("Test zip creation.");
        final FileSystem inputFileSystem = new FileSystem(datum.inputDirectory);
        try {
    	    ZipUtil.createZipFile(datum.outputZipFile, datum.inputDirectory, getDefaultBuffer());
        } catch (final FileNotFoundException fnfx) {
            fail(createFailMessage(fnfx));
        } catch (final IOException iox) {
            fail(createFailMessage(iox));
        }
        logger.logTrace("Test zip extraction.");
        final FileSystem outputFileSystem = new FileSystem(datum.outputDirectory);
        try {
            ZipUtil.extractZipFile(datum.outputZipFile, datum.outputDirectory, getDefaultBuffer());
        } catch (final FileNotFoundException fnfx) {
            fail(createFailMessage(fnfx));
        } catch (final IOException iox) {
            fail(createFailMessage(iox));
        }
        final File[] inputFiles = inputFileSystem.list("/", Boolean.TRUE);
        final File[] outputFiles = outputFileSystem.list("/", Boolean.TRUE);
        assertEquals("Input files do not match output files.", inputFiles.length, outputFiles.length);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
        final FileSystem inputDirectoryFileSystem = new FileSystem(createDirectory("zipInput"));
        inputDirectoryFileSystem.createDirectory("/level 1/level 2");
        File file;
        for (final File inputFile : getInputFiles()) {
            file = new File(inputDirectoryFileSystem.getRoot(), inputFile.getName());
            FileUtil.copy(inputFile, file, getDefaultBuffer());

            file = new File(inputDirectoryFileSystem.find("/level 1"), inputFile.getName());
            FileUtil.copy(inputFile, file, getDefaultBuffer());

            file = new File(inputDirectoryFileSystem.find("/level 1/level 2"), inputFile.getName());
            FileUtil.copy(inputFile, file, getDefaultBuffer());
        }

		final File outputZipFile = new File(getTestCaseDirectory(), "zipOutput.zip");
        final File outputDirectory = createDirectory("zipExtract");
        datum = new Fixture(inputDirectoryFileSystem.getRoot(), outputDirectory, outputZipFile);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		datum = null;
	}

	private class Fixture {
		private final File inputDirectory;
        private final File outputDirectory;
        private final File outputZipFile;
		private Fixture(final File inputDirectory, final File outputDirectory,
                final File outputZipFile) {
			super();
			this.inputDirectory = inputDirectory;
            this.outputDirectory = outputDirectory;
			this.outputZipFile = outputZipFile;
		}
	}
}

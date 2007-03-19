/*
 * Oct 17, 2005
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class FileUtilTest extends CodebaseTestCase {

	private Vector<CopyData> copyData;

	/**
	 * Create a FileUtilTest.
	 */
	public FileUtilTest() { super("FileUtilTest"); }

	public void testCopy() {
		try {
			for(final CopyData data : copyData) {
				FileUtil.copy(data.file, data.target, getDefaultBuffer());
				FileUtilTest.assertTrue(data.target.exists());
                final InputStream expected = new FileInputStream(data.file);
                try {
                    final InputStream actual = new FileInputStream(data.target);
                    try {
                        assertEquals("Expected file does not match actual.", expected, actual);
                    } finally {
                        actual.close();
                    }
                } finally {
                    expected.close();
                }
				assertTrue("Could not delete target file.", data.target.delete());
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		setUpCopy();
	}

	/**
	 * Setup for the testCopy.
	 * 
	 * @throws Exception
	 */
	protected void setUpCopy() throws Exception {
		copyData = new Vector<CopyData>(getInputFilesLength());
		final File targetDirectory = createDirectory("targetDirectory");

		for(File inputFile : getInputFiles()) {
			copyData.add(
					new CopyData(inputFile, new File(targetDirectory, inputFile.getName())));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownCopy();
	}

	protected void tearDownCopy() throws Exception {}

	private class CopyData {
		private final File file;
		private final File target;
		private CopyData(final File file, final File target) {
			this.file = file;
			this.target = target;
		}
	}
}


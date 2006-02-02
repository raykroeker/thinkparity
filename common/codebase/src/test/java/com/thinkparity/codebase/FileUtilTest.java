/*
 * Oct 17, 2005
 */
package com.thinkparity.codebase;

import java.io.File;
import java.util.Vector;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class FileUtilTest extends CodebaseTestCase {

	private class CopyData {
		private final File file;
		private final File target;
		private CopyData(final File file, final File target) {
			this.file = file;
			this.target = target;
		}
	}

	private class ReadData {
		private final File file;
		private final Long size;
		private ReadData(final File file, final Long size) {
			this.file = file;
			this.size = size;
		}
	}

	private Vector<CopyData> copyData;
	private Vector<ReadData> readData;

	/**
	 * Create a FileUtilTest.
	 */
	public FileUtilTest() { super("FileUtilTest"); }

	public void testCopy() {
		try {
			for(CopyData data : copyData) {
				FileUtil.copy(data.file, data.target);
				FileUtilTest.assertTrue(data.target.exists());
				assertContentEquals(data.file, data.target);
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	public void testRead() {
		try {
			byte[] fileBytes;
			String fileString;
			for(ReadData data : readData) {
				fileBytes = FileUtil.readBytes(data.file);

				assertNotNull(fileBytes);
				assertEquals(data.size.intValue(), fileBytes.length);

				fileString = FileUtil.readString(data.file);
				assertNotNull(fileString);
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
		setUpRead();
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

	protected void setUpRead() throws Exception {
		readData = new Vector<ReadData>(getInputFilesLength());
		Long size;

		for(File inputFile : getInputFiles()) {
			size = inputFile.length();

			readData.add(new ReadData(inputFile, size));
			readData.add(new ReadData(inputFile, size));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownCopy();
		tearDownRead();
	}

	protected void tearDownCopy() throws Exception {}

	protected void tearDownRead() throws Exception {
		readData.clear();
		readData = null;
	}
}


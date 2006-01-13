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
	public FileUtilTest() { super("File util test"); }

	public void testCopy() {
		try {
			for(CopyData data : copyData) {
				FileUtil.copy(data.file, data.target);
				FileUtilTest.assertTrue(data.target.exists());
				assertContentEquals(data.file, data.target);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
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
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setUpCopy();
		setUpRead();
	}

	protected void setUpCopy() throws Exception {
		copyData = new Vector<CopyData>(getJUnitTestFilesSize());
		final File targetDirectory = createTestDirectory("setUpCopy");

		for(JUnitTestFile testFile : getJUnitTestFiles()) {
			copyData.add(
					new CopyData(testFile.getFile(),
							new File(targetDirectory, testFile.getName())));
		}
	}

	protected void setUpRead() throws Exception {
		readData = new Vector<ReadData>(getJUnitTestFilesSize());
		Long size;

		for(JUnitTestFile testFile : getJUnitTestFiles()) {
			size = testFile.getSize();

			readData.add(new ReadData(testFile.getFile(), size));

			readData.add(new ReadData(testFile.getFile(), size));

			readData.add(new ReadData(testFile.getFile(), size));

			readData.add(new ReadData(testFile.getFile(), size));
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


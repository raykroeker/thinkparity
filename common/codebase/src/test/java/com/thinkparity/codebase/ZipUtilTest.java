/*
 * Jan 31, 2006
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ZipUtilTest extends CodebaseTestCase {

	private class Fixture {
		private final File inputDirectory;
		private final File zipFile;
		private Fixture(final File inputDirectory, final File zipFile) {
			super();
			this.inputDirectory = inputDirectory;
			this.zipFile = zipFile;
		}
	}

	private Vector<Fixture> data;

	/**
	 * Create a ZipUtilTest.
	 * 
	 */
	public ZipUtilTest() { super("Zip Util Test"); }

	public void testCreateZipFile() {
		try {
			for(Fixture datum : data) {
				ZipUtil.createZipFile(datum.zipFile, datum.inputDirectory);
			}
		}
		catch(final Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(1);
		File inputDirectory, outputDirectory, zipFile;

		// copy the resources to a test directory
		inputDirectory = createTestDirectory(getName() + ".Input");
		File inputFile;
		InputStream inputStream;
		OutputStream outputStream;
		for(JUnitTestFile jUnitTestFile : getJUnitTestFiles()) {
			inputFile = new File(inputDirectory, jUnitTestFile.getName());
			Assert.assertTrue("", inputFile.createNewFile());
			inputStream = JUnitTestFile.class.getResourceAsStream(jUnitTestFile.getName());
			outputStream = new FileOutputStream(inputFile);
			try {
				StreamUtil.copy(inputStream, outputStream, 512);
				outputStream.flush();
			}
			finally {
				try { outputStream.close(); }
				finally { inputStream.close(); }
			}
		}
		outputDirectory = createTestDirectory(getName() + ".Output");
		zipFile = new File(outputDirectory, getName() + ".zip");
		data.add(new Fixture(inputDirectory, zipFile));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}

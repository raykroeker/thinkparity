/*
 * Jan 31, 2006
 */
package com.thinkparity.codebase;

import java.io.File;
import java.util.Vector;

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
		File inputDirectory, zipFile;

		inputDirectory = new File(
				System.getProperty("user.dir"),
				"target" + File.separator + "test-classes" + File.separator +
				"com" + File.separator + "thinkparity" + File.separator + "codebase");
		zipFile = new File(getJUnitSessionDirectory(), System.currentTimeMillis() + ".zip");
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

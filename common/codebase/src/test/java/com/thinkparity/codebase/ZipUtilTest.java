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
	public ZipUtilTest() { super("ZipUtilTest"); }

	public void testCreateZipFile() {
		try {
			for(Fixture datum : data) {
				ZipUtil.createZipFile(datum.zipFile, datum.inputDirectory);
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(1);
		final File zipFile = new File(createDirectory("Output"), getName() + ".zip");

		data.add(new Fixture(getInputFilesDirectory(), zipFile));
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

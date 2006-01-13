/*
 * 18-Oct-2005
 */
package com.thinkparity.model;

import java.io.File;

/**
 * The JUnitTestFile is a convenience class for loading various resource files
 * for testing via the classloader. The test files *must* be in the same package
 * as this class and must Not contain a space in the file name. This is due to
 * how the class loader uses URLs to load files.
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class JUnitTestFile {

	/**
	 * The java file.
	 * 
	 */
	private final File file;

	/**
	 * Create a JUnitTestFile.
	 * 
	 * @param jUnitFilename
	 *            The test file name.
	 */
	JUnitTestFile(final String jUnitFilename) {
		super();
		this.file =
			new File(JUnitTestFile.class.getResource(jUnitFilename).getFile());
	}
	
	/**
	 * Obtain the test file absolute path.
	 * 
	 * @return The test file's absolute path.
	 */
	public String getAbsolutePath() { return file.getAbsolutePath(); }

	/**
	 * Obtain the test file.
	 * 
	 * @return The test file.
	 */
	public File getFile() { return file; }

	/**
	 * Obtain the test file name.
	 * 
	 * @return The test file name.
	 */
	public String getName() { return file.getName(); }

	/**
	 * Obtain the test file size.
	 * 
	 * @return The test file's size in bytes; or 0L if the file does not exist.
	 */
	public Long getSize() { return file.length(); }
}

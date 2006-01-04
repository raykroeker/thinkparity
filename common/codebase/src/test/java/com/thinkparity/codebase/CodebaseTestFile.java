/*
 * 18-Oct-2005
 */
package com.thinkparity.codebase;

import java.io.File;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class CodebaseTestFile {

	private final File file;

	/**
	 * Create a CodebaseTestFile.
	 */
	CodebaseTestFile(final File file) {
		super();
		this.file = file; 
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

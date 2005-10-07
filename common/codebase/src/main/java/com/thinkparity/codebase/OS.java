/*
 * Aug 6, 2005
 */
package com.thinkparity.codebase;

/**
 * OS
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum OS {

	LINUX, WINDOWS_2000, WINDOWS_XP;

	/**
	 * The version of the os.
	 */
	private String version;

	/**
	 * Set the version of the os.
	 * 
	 * @param version
	 *            The version of the os.
	 */
	void setVersion(final String version) { this.version = version; }

	/**
	 * Obtain the version of the os.
	 * 
	 * @return The version of the os.
	 */
	public String getVersion() { return version; }

}

/*
 * Aug 6, 2005
 */
package com.thinkparity.codebase;

/**
 * OSUtil
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class OSUtil {

	/**
	 * Cached os value.
	 */
	private static OS os;

	/**
	 * Obtain the underlying operating system based upon the os.name and
	 * os.version system properties.
	 * 
	 * @return <code>com.raykroeker.util.OS</code>
	 */
	public static synchronized OS getOS() {
		if(null == os) {
			os = OS.valueOf(System.getProperty("os.name"), System
					.getProperty("os.version"));
		}
		return os;
	}

	/**
	 * Create a OSUtil
	 */
	private OSUtil() { super(); }
}

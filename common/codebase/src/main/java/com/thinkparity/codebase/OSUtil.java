/*
 * Aug 6, 2005
 */
package com.thinkparity.codebase;

/**
 * OSUtil
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class OSUtil {

	/**
	 * Synchronization lock for os.
	 */
	private static final Object LOCK = new Object();

	/**
	 * Cached os value.
	 */
	private static OS os;

	/**
	 * Obtain the underlying operating system based upon the os.name and
	 * os.version system properties.
	 * 
	 * @return An enumerated operating system value.
	 */
	public static synchronized OS getOS() {
		synchronized(LOCK) {
			if(null == os) {
				final String osName = System.getProperty("os.name");
				if("Windows XP".equals(osName)) { os = OS.WINDOWS_XP; }
				else if("Windows 2000".equals(osName)) { os = OS.WINDOWS_2000; }
				else if("Linux".equals(osName)) { os = OS.LINUX; }
                else if("Mac OS X".equals(osName)) { os = OS.OSX; }

				// set the version
				if(null != os) { os.setVersion(System.getProperty("os.version")); }
			}
			return os;
		}
	}

	/**
	 * Create an OSUtil [Singleton]
	 */
	private OSUtil() { super(); }
}

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

	/** The value of os. */
	private static final OS os;

    static {
        final String osName = System.getProperty("os.name");
        if ("Windows XP".equals(osName)) {
            os = OS.WINDOWS_XP;
        } else if ("Windows 2000".equals(osName)) {
            os = OS.WINDOWS_2000;
        } else if ("Linux".equals(osName)) {
            os = OS.LINUX;
        } else if ("Mac OS X".equals(osName)) {
            os = OS.OSX;
        } else {
            os = null;
        }

        // set the version
        if (null != os)
            os.setVersion(System.getProperty("os.version"));
    }

	/**
     * Obtain the underlying operating system based upon the os.name and
     * os.version system properties.
     * 
     * @return An enumerated operating system value.
     */
	public static OS getOS() {
        return os;
	}

    /**
     * Determine if the platform is windows.
     * 
     * @return True if it is Windows 2000 or Windows XP.
     */
    public static Boolean isWindows() {
        switch (getOS()) {
        case WINDOWS_2000:
        case WINDOWS_XP:
            return Boolean.TRUE;
        default:
            return Boolean.FALSE;
        }
    }

	/**
	 * Create an OSUtil [Singleton]
	 */
	private OSUtil() { super(); }
}

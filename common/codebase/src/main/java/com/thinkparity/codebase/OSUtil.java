/*
 * Aug 6, 2005
 */
package com.thinkparity.codebase;

import com.thinkparity.codebase.assertion.Assert;

/**
 * OSUtil
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class OSUtil {

	/** The value of os. */
	private static final OS SYSTEM_OS;

    static {
        SYSTEM_OS = getOs(System.getProperty("os.name"));
    }

	/**
     * Obtain the operating system.
     * 
     * @return The <code>OS</code>.
     */
    public static OS getOs() {
        return getOS();
    }
	
    /**
     * Obtain the enumerated operating system based upon the os name. The os
     * determination is based upon the possible values for the java system
     * property "os.name".
     * 
     * @param osName
     *            The operating system name <code>String</code>.
     * @return An <code>OS</code>.
     */
    public static OS getOs(final String osName) {
        final OS os;
        if ("Windows XP".equals(osName)) {
            os = OS.WINDOWS_XP;
        } else if ("Windows Vista".equals(osName)) {
            os = OS.WINDOWS_VISTA;
        } else if ("Linux".equals(osName)) {
            os = OS.LINUX;
        } else if ("Mac OS X".equals(osName)) {
            os = OS.MAC_OSX;
        } else {
            throw Assert.createUnreachable("Unknown operating system.");
        }
        return os;
    }

    /**
     * Obtain the operating system.
     * 
     * @return The <code>OS</code>.
     */
	public static OS getOS() {
        return SYSTEM_OS;
	}

    /**
     * Determine if the platform is windows.
     * 
     * @return True if it is Windows 2000 or Windows XP.
     */
    public static Boolean isWindows() {
        return Platform.WIN32 == getOS().getPlatform();
    }

	/**
	 * Create an OSUtil [Singleton]
     * 
	 */
	private OSUtil() {
        super();
	}
}

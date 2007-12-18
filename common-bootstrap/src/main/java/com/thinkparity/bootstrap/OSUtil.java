/*
 * Aug 6, 2005
 */
package com.thinkparity.bootstrap;

import com.thinkparity.bootstrap.assertion.Assert;

/**
 * <b>Title:</b>thinkParity Bootstrap OS Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class OSUtil {

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
    public static OS getOS() {
        return SYSTEM_OS;
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
    private static OS getOs(final String osName) {
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
     * Create OSUtil.
     * 
     */
    private OSUtil() {
        super();
    }
}

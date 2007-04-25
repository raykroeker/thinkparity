/*
 * Created On:  Apr 18, 2007 8:20:51 PM
 */
package com.thinkparity.ophelia.browser.util.firewall.win32;

import java.io.File;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.Constants;
import com.thinkparity.ophelia.browser.util.firewall.FirewallAccessException;
import com.thinkparity.ophelia.browser.util.firewall.FirewallUtil;

/**
 * <b>Title:</b>thinkParity OpheliaUI Win32 Firewall Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Win32FirewallUtil implements FirewallUtil {

    /** The firewall util native library name <code>String</code>. */
    private static final String FIREWALL_UTIL_LIBNAME;

    /** A <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    static {
        LOGGER = new Log4JWrapper();
        FIREWALL_UTIL_LIBNAME = "Win32FirewallUtil";

        try {
            System.loadLibrary(FIREWALL_UTIL_LIBNAME);
        } catch (final Throwable t) {
            LOGGER.logError(t, "Could not load native library from path {0}.",
                    System.getProperty("java.library.path"));
            throw new BrowserException("Could not load native library " +
                    FIREWALL_UTIL_LIBNAME + ".", t);
        }
    }

    /**
     * Create Win32FirewallUtil.
     *
     */
    public Win32FirewallUtil() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.util.firewall.FirewallUtil#addExecutable(java.io.File)
     * 
     */
    public void addExecutable(final File executable)
            throws FirewallAccessException {
        if (isExceptionAllowedNative()) {
            if (isExecutableEnabledNative(executable.getAbsolutePath())) {
                LOGGER.logInfo("{0} has already been added to the firewall.",
                        executable.getAbsolutePath());
                if (removeExecutableNative(executable.getAbsolutePath())) {
                    LOGGER.logInfo("{0} has been removed from the firewall.",
                            executable.getAbsolutePath());
                } else {
                    LOGGER.logFatal("{0} could not be removed from the firewall.",
                            executable.getAbsolutePath());
                    throw new FirewallAccessException();
                }
            }
            if (addExecutableNative(Constants.Sundry.FIREWALL_RULE_NAME,
                    executable.getAbsolutePath())) {
                LOGGER.logInfo("{0},{1} has been added to the firewall.",
                        Constants.Sundry.FIREWALL_RULE_NAME, executable.getPath());
            } else {
                LOGGER.logFatal("{0},{1} could not been added to the firewall.  {2}",
                        Constants.Sundry.FIREWALL_RULE_NAME, executable.getPath());
                throw new FirewallAccessException();
            }
        } else {
            LOGGER.logFatal("\"Don't allow exceptions\" is selected.");
            throw new FirewallAccessException();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.util.firewall.FirewallUtil#isEnabled()
     * 
     */
    public Boolean isEnabled() {
        return isEnabledNative();
    }

    /**
     * @see com.thinkparity.ophelia.browser.util.firewall.FirewallUtil#removeExecutable(java.io.File)
     * 
     */
    public void removeExecutable(final File executable) {
        removeExecutableNative(executable.getAbsolutePath());
    }

    /**
     * Add the executable to the firewall.
     * 
     * @param executablePath
     *            An executable path <code>String</code>.
     */
    private native boolean addExecutableNative(final String name,
            final String path);

    /**
     * Native implementation to determine if the firewall is enabled.
     * 
     * @return True if the firewall is enabled.
     */
    private native boolean isEnabledNative();

    /**
     * Native implementation to determine if firewall exceptions area allowed.
     * 
     * @return True if the firewall is enabled.
     */
    private native boolean isExceptionAllowedNative();

    /**
     * Native implementation to determine if the firewall is enabled.
     * 
     * @return True if the firewall is enabled.
     */
    private native boolean isExecutableEnabledNative(final String path);

    /**
     * Remove the executable from the firewall.
     * 
     * @param executablePath
     *            An executable path <code>String</code>.
     */
    private native boolean removeExecutableNative(final String path);
}

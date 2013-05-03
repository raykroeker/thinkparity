/*
 * Created On:  Apr 18, 2007 8:24:37 PM
 */
package com.thinkparity.ophelia.browser.util.firewall;

import java.io.File;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.util.firewall.win32.Win32FirewallUtil;

/**
 * <b>Title:</b>thinkParity OpheliaUI Firewall Util Provider<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class FirewallUtilProvider {

    /** A singleton instance of <code>FirewallUtil</code>. */
    private static FirewallUtil INSTANCE;

    /**
     * Obtain a firewall util.
     * 
     * @return An instance of <code>FirewallUtil</code>.
     */
    public static FirewallUtil getInstance() {
        if (null == INSTANCE) {
            switch (OSUtil.getOS()) {
            case WINDOWS_XP:
            case WINDOWS_VISTA:
                INSTANCE = new Win32FirewallUtil();
                break;
            case LINUX:
            case MAC_OSX:
                INSTANCE = new FirewallUtil() {
                    public void addExecutable(final File executable)
                            throws FirewallAccessException {}
                    public Boolean isEnabled() {
                        return Boolean.FALSE;
                    }
                    public void removeExecutable(final File executable) {}
                };
                break;
            default:
                throw Assert.createUnreachable("Unknown os.");
            }
        }
        return INSTANCE;
    }
}

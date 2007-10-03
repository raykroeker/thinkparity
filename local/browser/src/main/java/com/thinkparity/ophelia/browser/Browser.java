/*
 * Created On: Dec 30, 2005
 * $Id$
 */
package com.thinkparity.ophelia.browser;

import com.thinkparity.codebase.Mode;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.workspace.CannotLockException;

import com.thinkparity.ophelia.browser.Constants.EnvironmentVariable;
import com.thinkparity.ophelia.browser.environment.EnvironmentManager;
import com.thinkparity.ophelia.browser.mode.ModeManager;
import com.thinkparity.ophelia.browser.mode.demo.DemoManager;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.profile.Profile;
import com.thinkparity.ophelia.browser.profile.ProfileManager;
import com.thinkparity.ophelia.browser.util.Swing;

/**
 * <b>Title:</b>thinkParity Ophelia UI Browser<br>
 * <b>Description:</b>The ui main class.<br>
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public final class Browser {

    /**
     * Browser entry point.
     * 
     * @param args
     *            A <code>String[]</code>.
     */
    public static void main(String[] args) {
        Swing.init();
        final Mode mode = new ModeManager().select();
        if (Mode.DEMO == mode) {
            new DemoManager().execute();
        } else {
            final Boolean internal = isInternal();
            final Environment environment = new EnvironmentManager(internal).select();
            final Profile profile = new ProfileManager(!internal, environment).select();
            if (null != mode && null != environment && null != profile) {
                try {
                    BrowserPlatform.create(mode, environment, profile).start();
                } catch (final CannotLockException clx) {
                    clx.printStackTrace(System.err);
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Determine whether or not this is an internal build. We are running an
     * internal build if the correct flag is set; as well as a checksum of the
     * build id.
     * 
     * @return True if this is an internal build.
     */
    private static Boolean isInternal() {
        final String value = System.getenv(EnvironmentVariable.ThinkParity.KEY);
        if (null == value) {
            return Boolean.FALSE;
        } else {
            return EnvironmentVariable.ThinkParity.VALUE_INTERNAL.equals(value);
        }
    }

    /**
     * Create Browser.
     * 
     */
    private Browser() {
        super();
    }
}

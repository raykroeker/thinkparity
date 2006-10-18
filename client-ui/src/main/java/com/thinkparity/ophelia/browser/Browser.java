/*
 * Created On: Dec 30, 2005
 * $Id$
 */
package com.thinkparity.ophelia.browser;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.ophelia.browser.environment.EnvironmentManager;
import com.thinkparity.ophelia.browser.mode.ModeManager;
import com.thinkparity.ophelia.browser.mode.demo.DemoManager;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.profile.Profile;
import com.thinkparity.ophelia.browser.profile.ProfileManager;
import com.thinkparity.ophelia.browser.util.Swing;

/**
 * The browser entry point.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Browser {

    /**
     * Run Browser
     * 
     * @param args
     *            Command line arguments.
     */
    public static void main(String[] args) {
        Swing.init();
        final Mode mode = new ModeManager().select();
        if (Mode.DEMO == mode) {
            new DemoManager().execute();
        } else {
            final Environment environment = new EnvironmentManager().select();
            final Profile profile = new ProfileManager(mode).select();
            if (null != mode && null != environment && null != profile) {
                BrowserPlatform.create(mode, environment, profile).start();
            }
        }
    }

    /** Create Browser. */
    private Browser() {
        super();
    }
}

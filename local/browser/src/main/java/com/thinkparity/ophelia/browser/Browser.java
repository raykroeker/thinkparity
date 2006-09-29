/*
 * Created On: Dec 30, 2005
 * $Id$
 */
package com.thinkparity.ophelia.browser;



import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.profile.EnvironmentManager;
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
        final Environment environment = new EnvironmentManager().select();
        final Profile profile = new ProfileManager().select();
        if (null != environment && null != profile) {
            BrowserPlatform.create(environment, profile).start();
        }
    }

	/** Create Browser. */
	private Browser() { super(); }
}

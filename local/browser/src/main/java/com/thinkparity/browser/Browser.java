/*
 * Created On: Dec 30, 2005
 * $Id$
 */
package com.thinkparity.browser;

import com.thinkparity.codebase.swing.Swing;

import com.thinkparity.browser.platform.BrowserPlatform;
import com.thinkparity.browser.profile.Profile;
import com.thinkparity.browser.profile.ProfileManager;

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
        final Profile profile = new ProfileManager().select();
        if(null != profile) { BrowserPlatform.create(profile).start(); }
    }

	/** Create Browser. */
	private Browser() { super(); }
}

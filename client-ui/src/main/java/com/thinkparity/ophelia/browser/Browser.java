/*
 * Created On: Dec 30, 2005
 * $Id$
 */
package com.thinkparity.ophelia.browser;

import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.config.ConfigFactory;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.ophelia.browser.environment.EnvironmentManager;
import com.thinkparity.ophelia.browser.mode.ModeManager;
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

    static {
        /*
         * NOTE Temporary log4j configuration such that the managers can log to
         * a console.
         */
        final Properties log4j = ConfigFactory.newInstance("log4j.properties");
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(log4j);
    }

	/**
	 * Run Browser
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
        Swing.init();
        final Mode mode = new ModeManager().select();
        final Environment environment = new EnvironmentManager().select();
        final Profile profile = new ProfileManager(mode).select();
        if (null != mode && null != environment && null != profile) {
            BrowserPlatform.create(mode, environment, profile).start();
        }
    }

	/** Create Browser. */
	private Browser() { super(); }
}

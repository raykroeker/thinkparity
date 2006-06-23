/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.platform;

import org.apache.log4j.Logger;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.profile.Profile;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPlatformInitializer {

    private static void initParityServerHost() {
        final String serverHost = System.getProperty("parity.serverhost");
        if(null == serverHost) { System.setProperty("parity.serverhost", "thinkparity.dyndns.org"); }
    }

    private static void initParityServerPort() {
        final Integer serverPort= Integer.getInteger("parity.serverport", null);
        if(null == serverPort) { System.setProperty("parity.serverport", "5222"); }
    }

	/** A profile. */
    private final Profile profile;

	/**
     * Create BrowserPlatformInitializer.
     * 
     * @param profile
     *            A browser profile to initialize.
     */
    BrowserPlatformInitializer(final Profile profile) {
        this.profile = profile;
    }

    /**
	 * Initialize the browser2 platform.
	 *
	 */
	void initialize() {
		System.setProperty("parity.insecure", "true");
        initializePropertyParityArchive();
        initializePropertyParityWorkspace();
        initParityServerHost();
        initParityServerPort();

		final Logger logger = Logger.getLogger(BrowserPlatformInitializer.class);
        logger.info("[BROWSER2] [PLATFORM] [INIT]");

        // init model
        ModelFactory.getInstance().initialize();
	}

    private void initializePropertyParityArchive() {
        System.setProperty("parity.archive.directory", profile.getParityArchive());
    }

    /** Initialize the system property for the parity workspace. */
    private void initializePropertyParityWorkspace() {
        System.setProperty("parity.workspace", profile.getParityWorkspace());
    }
}

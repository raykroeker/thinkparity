/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.platform;

import java.io.File;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.model.workspace.Preferences;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPlatformInitializer {

	/**
	 * Initialize the browser2 platform.
	 *
	 */
	static void initialize() {
		System.setProperty("sun.awt.noerasebackground", "true");
		System.setProperty("parity.insecure", "true");
        initializePropertyParityWorkspace();
        initParityServerHost();
        initParityServerPort();

		final Logger logger =
            LoggerFactory.getLogger(BrowserPlatformInitializer.class);
        logger.info("[BROWSER2] [PLATFORM] [INIT]");

        // init model
        ModelFactory mFactory = null;
        try {
            mFactory = ModelFactory.getInstance();
    		mFactory.initialize();

            final Preferences preferences =
                mFactory.getPreferences(BrowserPlatformInitializer.class);
            final File root;
            switch(OSUtil.getOS()) {
            case WINDOWS_XP:
            case WINDOWS_2000:
                final File user = new File(System.getenv("USERPROFILE"));
                root = new File(user, "My Documents");
                break;
            case LINUX:
                root = new File(System.getenv("HOME"));
                break;
            default:
                throw Assert.createUnreachable("[BROWSER2] [INITIALISE]");
            }

            final File thinkParity = new File(root, IParityModelConstants.ROOT_DIRECTORY);
            if(!thinkParity.exists()) { Assert.assertTrue("", thinkParity.mkdir()); }

            final File archive = new File(thinkParity, IParityModelConstants.ARCHIVE_DIRECTORY);
            if(!thinkParity.exists()) { Assert.assertTrue("", archive.mkdir()); }

            final File export = new File(thinkParity, IParityModelConstants.EXPORT_DIRECTORY);
            if(!thinkParity.exists()) { Assert.assertTrue("", export.mkdir()); }

            preferences.setArchiveOutputDirectory(archive);
        }
        catch(final Throwable t) {
            logger.fatal("[LBROWSER] [PLATFORM] [INIT] [UNEXPECTED ERROR]", t);
            throw new RuntimeException(t);
        }

		// init look and feel
		try { UIManager.setLookAndFeel(new WindowsLookAndFeel()); }
		catch(final Throwable t) {
			logger.fatal("[LBROWSER] [PLATFORM] [INIT]", t);
            throw new RuntimeException(t);

		}
	}

	/** Initialize the system property for the parity workspace. */
    private static void initializePropertyParityWorkspace() {
        if(null == System.getProperty("parity.workspace")) {
            final String parityWorkspacePath;
            switch(OSUtil.getOS()) {
            case WINDOWS_2000:
            case WINDOWS_XP:
                parityWorkspacePath = new StringBuffer()
                    .append(System.getenv("APPDATA"))
                    .toString();
                break;
            case LINUX:
                parityWorkspacePath = new StringBuffer()
                    .append(System.getenv("HOME"))
                    .toString();
                break;
            default:
                throw Assert.createUnreachable("Unsupported os:  " + OSUtil.getOS());
            }
            System.setProperty("parity.workspace", parityWorkspacePath);
        }
    }

    private static void initParityServerHost() {
        final String serverHost = System.getProperty("parity.serverhost");
        if(null == serverHost) { System.setProperty("parity.serverhost", "thinkparity.dyndns.org"); }
    }

    private static void initParityServerPort() {
        final Integer serverPort= Integer.getInteger("parity.serverport", null);
        if(null == serverPort) { System.setProperty("parity.serverport", "5222"); }
    }
}

/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.platform;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
public class Browser2PlatformInitializer {

	/**
	 * Initialize the browser2 platform.
	 *
	 */
	static void initialize() {
		System.setProperty("sun.awt.noerasebackground", "true");
		System.setProperty("parity.insecure", "true");

		LoggerFactory.getLogger(Browser2PlatformInitializer.class);

        final ModelFactory modelFactory = ModelFactory.getInstance();
        modelFactory.initialize();

        final Preferences preferences =
            modelFactory.getPreferences(Browser2PlatformInitializer.class);
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

		// Set the application lnf
		try { UIManager.setLookAndFeel(new WindowsLookAndFeel()); }
		catch(final UnsupportedLookAndFeelException ulafx) {
			throw new RuntimeException(ulafx);
		}
	}
}

/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.platform;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

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
		ModelFactory.getInstance().initialize();

		// Set the application lnf
		try { UIManager.setLookAndFeel(new WindowsLookAndFeel()); }
		catch(final UnsupportedLookAndFeelException ulafx) {
			throw new RuntimeException(ulafx);
		}
	}
}

/*
 * Feb 11, 2006
 */
package com.thinkparity.browser.util;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.window.Window;

import com.thinkparity.model.log4j.ModelLoggerFactory;

import com.l2fprod.gui.nativeskin.NativeConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class NativeSkinUtil {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final NativeSkinUtil singleton;

	static { singleton = new NativeSkinUtil(); }

	public static void applyNativeSkin(final Window window) {}

	/**
	 * Apply the native skin to the browser window.
	 * 
	 * @param browserWindow
	 *            The browser window.
	 */
	public static void applyNativeSkin(final BrowserWindow browserWindow) {
		singleton.doApplyNativeSkin(browserWindow);
	}

	/**
	 * An apache logger.
	 * 
	 */
	private final Logger logger;

	/**
	 * Create a NativeSkinUtil [Singleton]
	 * 
	 */
	private NativeSkinUtil() {
		super();
		this.logger = ModelLoggerFactory.getLogger(getClass());
		logger.info("[VERSION] " + NativeConstants.VERSION);
	}

	/**
	 * Apply the native skin to the browser window.
	 * 
	 * @param browserWindow
	 *            The browser window.
	 */
	private void doApplyNativeSkin(final BrowserWindow browserWindow) {}
}

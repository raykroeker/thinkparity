/*
 * Mar 17, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Point;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class BrowserAvatar extends Avatar {

	/**
	 * Lazy browser instance.
	 * 
	 */
	private Browser browser;

	/**
	 * Create a BrowserAvatar.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 */
	protected BrowserAvatar(final String l18nContext) {
		super(l18nContext, ScrollPolicy.NONE);
	}

	/**
	 * Obtain the browser application.
	 * 
	 * @return The browser application.
	 */
	protected Browser getBrowser() {
		if(null == browser) { browser = Browser.getInstance(); }
		return browser;
	}

	/**
	 * Move the browser window.
	 * 
	 * @param l
	 *            A location to move the window to relative to where it was.
	 */
	protected void moveBrowserWindow(final Point l) {
        getBrowser().moveBrowserWindow(l);
	}
}

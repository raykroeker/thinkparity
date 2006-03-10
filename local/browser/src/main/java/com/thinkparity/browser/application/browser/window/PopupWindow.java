/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.application.browser.window;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.window.Window;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PopupWindow extends Window {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a PopupWindow.
	 * @param l18Context
	 */
	public PopupWindow(final BrowserWindow browserWindow) {
		super(browserWindow, Boolean.FALSE, "");
	}

	/**
	 * @see com.thinkparity.browser.platform.application.window.Window#getId()
	 * 
	 */
	public WindowId getId() { return WindowId.POPUP; }
}

/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.application.browser.window;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.window.Window;

/**
 * A window to use for pop-up dialogues.
 * 
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
     * 
     * @param owner
     *            The parity owner.
     */
	public PopupWindow(final BrowserWindow owner) {
		super(owner, Boolean.FALSE, "PopupWindow");
        applyEscapeListener();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.window.Window#getId()
	 * 
	 */
	public WindowId getId() { return WindowId.POPUP; }
}

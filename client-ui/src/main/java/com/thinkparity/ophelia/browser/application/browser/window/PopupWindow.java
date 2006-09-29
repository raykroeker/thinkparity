/*
 * Mar 9, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.window;


import com.thinkparity.ophelia.browser.application.browser.BrowserWindow;
import com.thinkparity.ophelia.browser.platform.application.window.Window;

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
		super(owner, Boolean.TRUE, "PopupWindow");
        applyEscapeListener();
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.window.Window#getId()
	 * 
	 */
	public WindowId getId() { return WindowId.POPUP; }
}

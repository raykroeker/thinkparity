/*
 * Apr 7, 2006
 */
package com.thinkparity.browser.application.browser.window;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.window.Window;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ConfirmWindow extends Window {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Create a ConfirmWindow.
     * 
     */
    public ConfirmWindow(final BrowserWindow browserWindow) {
        super(browserWindow, Boolean.TRUE, "ConfirmWindow");
    }

    /**
     * @see com.thinkparity.browser.platform.application.window.Window#getId()
     * 
     */
    public WindowId getId() { return WindowId.CONFIRM; }
}

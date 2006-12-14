/*
 * Apr 7, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.window;


import com.thinkparity.ophelia.browser.application.browser.BrowserWindow;
import com.thinkparity.ophelia.browser.platform.application.window.Window;

/**
 * A window to use for confirmation dialogues.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ConfirmWindow extends Window {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Create a ConfirmWindow.
     * 
     * @param owner
     *            The parity owner.
     */
    public ConfirmWindow(final BrowserWindow owner) {
        super(owner, Boolean.TRUE, Boolean.FALSE, "ConfirmWindow");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.window.Window#getId()
     * 
     */
    public WindowId getId() { return WindowId.CONFIRM; }
}

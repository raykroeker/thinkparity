/**
 * Created On: 21-Jul-06 12:33:38 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.window;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.window.Window;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ErrorWindow extends Window {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Create a ConfirmWindow.
     * 
     * @param owner
     *            The parity owner.
     */
    public ErrorWindow(final BrowserWindow owner) {
        super(owner, Boolean.TRUE, "ErrorWindow");
    }

    /**
     * @see com.thinkparity.browser.platform.application.window.Window#getId()
     * 
     */
    public WindowId getId() { return WindowId.ERROR; }
}

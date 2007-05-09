/**
 * Created On: 8-May-07 12:52:06 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.window;

import com.thinkparity.ophelia.browser.application.browser.BrowserWindow;
import com.thinkparity.ophelia.browser.platform.application.window.Window;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class StatusWindow extends Window {

    /**
     * @see java.io.Serializable
     * 
     */
    private static final long serialVersionUID = 1;

    /**
     * Create a StatusWindow.
     * 
     * @param owner
     *            The parity owner.
     */
    public StatusWindow(final BrowserWindow owner) {
        super(owner, Boolean.TRUE, Boolean.TRUE, "StatusWindow");
        applyEscapeListener();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.window.Window#getId()
     * 
     */
    public WindowId getId() { return WindowId.STATUS; }
}

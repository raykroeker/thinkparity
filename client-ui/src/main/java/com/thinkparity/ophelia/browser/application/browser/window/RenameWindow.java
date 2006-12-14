/*
 * Created On: Fri Jun 02 2006 08:48 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.window;


import com.thinkparity.ophelia.browser.application.browser.BrowserWindow;
import com.thinkparity.ophelia.browser.platform.application.window.Window;

/**
 * A window to use for the rename dialogue.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class RenameWindow extends Window {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Create a ConfirmWindow.
     * 
     * @param owner
     *            The parity owner.
     */
    public RenameWindow(final BrowserWindow owner) {
        super(owner, Boolean.TRUE, Boolean.FALSE, "RenameWindow");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.window.Window#getId()
     * 
     */
    public WindowId getId() { return WindowId.RENAME; }
}

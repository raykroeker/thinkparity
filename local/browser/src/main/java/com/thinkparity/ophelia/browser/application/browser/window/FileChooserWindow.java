/**
 * Created On: Mar 12, 2007 4:13:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.window;

import com.thinkparity.ophelia.browser.application.browser.BrowserWindow;
import com.thinkparity.ophelia.browser.platform.application.window.Window;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class FileChooserWindow extends Window {

    /**
     * Create a FileChooserWindow.
     * 
     * @param owner
     *            The parity owner.
     */
    public FileChooserWindow(final BrowserWindow owner) {
        super(owner, Boolean.TRUE, Boolean.TRUE, "FileChooserWindow");
        applyEscapeListener();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.window.Window#getId()
     * 
     */
    public WindowId getId() { return WindowId.FILE_CHOOSER; }
}

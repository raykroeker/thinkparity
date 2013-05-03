/*
 * Created On: Fri Jun 02 2006 15:59 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.profile;

import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowPanel;
import com.thinkparity.ophelia.browser.platform.application.window.WindowTitle;
import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * <b>Title:</b>thinkParity OpheliaUI Profile Manager Window<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
final class ProfileManagerWindow extends OpheliaJFrame {

    /** The window panel. */
    private final WindowPanel windowPanel;

    /**
     * Create ProfileManagerWindow.
     *
     */
    ProfileManagerWindow() {
        super("ProfileManagerWindow");
        this.windowPanel = new WindowPanel();
        this.windowPanel.getWindowTitle().setBorderType(WindowTitle.BorderType.WINDOW_BORDER2);
    }

    /**
     * Open the avatar in the profile window.
     * 
     * @param avatar
     *            The profile manager avatar.
     */
    void initComponents(final String title, final Avatar avatar) {
        windowPanel.addPanel(avatar);
        add(windowPanel);
        avatar.reload();
        pack();
    }
}

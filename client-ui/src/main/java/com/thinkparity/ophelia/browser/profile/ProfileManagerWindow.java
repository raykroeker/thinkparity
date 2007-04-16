/*
 * Created On: Fri Jun 02 2006 15:59 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.profile;


import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder;
import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * <b>Title:</b>thinkParity OpheliaUI Profile Manager Window<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
final class ProfileManagerWindow extends OpheliaJFrame {

    /**
     * Create ProfileManagerWindow.
     *
     */
    ProfileManagerWindow() {
        super("ProfileManagerWindow");
        setIconImage(com.thinkparity.ophelia.browser.Constants.Images.WINDOW_ICON_IMAGE);
        setResizable(false);
        setUndecorated(true);
    }

    /**
     * Open the avatar in the profile window.
     * 
     * @param avatar
     *            The profile manager avatar.
     */
    void open(final String title, final Avatar avatar) {
        getRootPane().setBorder(new WindowBorder());
        setTitle(title);
        add(avatar);
        pack();
        avatar.reload();
        setLocation(calculateLocation());
        setVisible(true);
    }
}

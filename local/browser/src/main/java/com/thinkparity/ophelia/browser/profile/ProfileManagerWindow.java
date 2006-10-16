/*
 * Created On: Fri Jun 02 2006 15:59 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.profile;


import javax.swing.JFrame;

import com.thinkparity.codebase.swing.AbstractJFrame;

import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder;
import com.thinkparity.ophelia.browser.util.l2fprod.NativeSkin;

/**
 *
 * @author raymond@thinkparity.com
 * @revision $Revision$
 */
class ProfileManagerWindow extends AbstractJFrame {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Create ProfileManagerWindow. */
    ProfileManagerWindow() {
        super("ProfileManagerWindow");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        avatar.setRoundCorners(Boolean.TRUE);
        setLocation(calculateLocation());
        roundCorners();
        setVisible(true);
    }
    
    /**
     * Make the corners round.
     */
    private void roundCorners() {
    	new NativeSkin().roundCorners(this);
    }
}

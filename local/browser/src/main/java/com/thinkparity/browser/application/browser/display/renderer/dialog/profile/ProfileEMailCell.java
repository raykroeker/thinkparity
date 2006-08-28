/*
 * Created On: Aug 25, 2006 8:35:29 AM
 */
package com.thinkparity.browser.application.browser.display.renderer.dialog.profile;

import java.awt.Component;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.component.MenuFactory;

import com.thinkparity.model.profile.ProfileEMail;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProfileEMailCell extends ProfileEMail {

    /** Create ProfileEMailCell. */
    public ProfileEMailCell() {
        super();
    }

    public void triggerPopup(final Component invoker, final int x, final int y) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        if (!isVerified()) {
            jPopupMenu.add(new JMenuItem("Verify"));
        }
        jPopupMenu.add(new JMenuItem("Remove"));
        jPopupMenu.show(invoker, x, y);
    }
}

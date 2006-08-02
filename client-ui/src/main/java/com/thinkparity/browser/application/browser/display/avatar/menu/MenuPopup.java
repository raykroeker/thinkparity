/**
 * Created On: 1-Aug-06 5:02:31 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar.menu;

import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.Browser;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface MenuPopup {
    /**
     * Trigger a popup event.
     * 
     */
    public void trigger(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e);
}

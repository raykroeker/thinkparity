/*
 * Apr 12, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main.popup;

import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.Browser;

/**
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Popup {

    /**
     * Trigger a popup event.
     * 
     */
    public void trigger(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e);
}

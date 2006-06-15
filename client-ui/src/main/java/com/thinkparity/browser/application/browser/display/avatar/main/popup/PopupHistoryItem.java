/*
 * Created On: Apr 17, 2006
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar.main.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellHistoryItem;
import com.thinkparity.browser.platform.Platform.Connection;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PopupHistoryItem implements Popup {

    /** The history item. */
    private final MainCellHistoryItem historyItem;

    /** Popup localization. */
    private final PopupL18n l18n;

    /** Create a PopupHistoryItem. */
    public PopupHistoryItem(final MainCellHistoryItem historyItem) {
        super();
        this.historyItem = historyItem;
        this.l18n = new PopupL18n("HistoryListItem");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.popup.Popup#trigger(com.thinkparity.browser.application.browser.Browser, javax.swing.JPopupMenu, java.awt.event.MouseEvent)
     */
    public void trigger(final Browser application, final JPopupMenu jPopupMenu,
            final MouseEvent e) {
        if(Connection.ONLINE == application.getConnection()) { triggerOnline(application, jPopupMenu, e); }
        else if(Connection.OFFLINE == application.getConnection()) { triggerOffline(application, jPopupMenu, e); }
        else { Assert.assertUnreachable("[LBROWSER] [APPLICATION] [BROWSER] [AVATAR] [HISTORY ITEM POPUP] [TRIGGER] [UNKNOWN CONNECTION STATUS]"); }
    }

    /**
     * Trigger the history item popup when offline.
     *
     * @param application
     *      The browser application.
     * @param jPopupMenu
     *      The JPopupMenu to populate.
     * @param e
     *      The source event.
     */
    private void triggerOffline(final Browser application, final JPopupMenu jPopupMenu,
            final MouseEvent e) {
        jPopupMenu.add(new OpenVersion(application));
    }

    /**
     * Trigger the history item popup when online.
     *
     * @param application
     *      The browser application.
     * @param jPopupMenu
     *      The JPopupMenu to populate.
     * @param e
     *      The source event.
     */
    private void triggerOnline(final Browser application, final JPopupMenu jPopupMenu,
            final MouseEvent e) {
        jPopupMenu.add(new OpenVersion(application));
    }

    /**
     * Obtain a localised string.
     * 
     * @param localKey
     *            The localisation local key.
     * @return A localised string.
     */
    private String getString(final String localKey) {
        return l18n.getString(localKey);
    }

    /** An open version {@link JMenuItem}. */
    private class OpenVersion extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create the OpenVersion menu item.
         * 
         * @param application
         *            The browser application.
         */
        private OpenVersion(final Browser application) {
            super(getString("OpenVersion"), getString("OpenMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runOpenDocumentVersion(historyItem.getDocumentId(), historyItem.getVersionId());
                }
            });
        }
    }
}

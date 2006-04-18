/*
 * Apr 17, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellHistoryItem;

import com.thinkparity.model.xmpp.user.User;

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
        if(historyItem.isSetVersionId()) {
            jPopupMenu.add(new OpenVersion(application));
            if(!historyItem.getDocument().isClosed()) {
                final Set<User> team = historyItem.getDocumentTeam();
                if(0 < team.size()) {
                    final JMenu jMenu = MenuFactory.create(getString("SendVersion"));
                    for(final User teamMember : team)
                        jMenu.add(new Send(application, teamMember));
                    jPopupMenu.add(jMenu);
                }
            }
        }
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

    /**
     * Obtain a localised\formatted string.
     * 
     * @param localKey
     *            The localisation local key.
     * @param The
     *            format data.
     * @return A localised string.
     */
    private String getString(final String localKey, final Object[] arguments) {
        return l18n.getString(localKey, arguments);
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

    /** A send {@link JMenuItem}. */
    private class Send extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        private Send(final Browser application, final User teamMember) {
            super(getString("SendVersion.TeamMember", new Object[] {teamMember.getFirstName(), teamMember.getLastName()}), getString("SendVersion.TeamMemberMnemonic", new Object[] {teamMember.getFirstName(), teamMember.getLastName()}).charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runSendArtifactVersion(
                            historyItem.getDocumentId(),
                            teamMember, historyItem.getVersionId());
                }
            });
        }
    }
}

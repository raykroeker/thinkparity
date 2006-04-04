/*
 * DisplayHistoryItem.java
 *
 * Created on March 28, 2006, 9:29 PM
 */
package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.MenuItemFactory;
import com.thinkparity.browser.model.util.ArtifactUtil;
import com.thinkparity.browser.platform.util.l10n.ListItemLocalization;

import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 *
 * @author raykroeker@gmail.com
 */
public class DisplayHistoryItem {

    /**
     * The history item localization.
     *
     */
    protected final ListItemLocalization l18n;

    private HistoryItems avatar;

    private HistoryItem historyItem;

    /**
     * Creates a new instance of DisplayHistoryItem
     *
     */
    public DisplayHistoryItem() {
        super();
        this.l18n = new ListItemLocalization("HistoryListItem");
    }

    /**
     * Obtain the display text for the history item.
     * 
     * @return The display text.
     */
	public String getDisplay() {
        final String localKey;
        final Object[] arguments;
        if(isVersionAttached()) {
            localKey = "ItemVersionText";
            arguments = new Object[] {
                getDisplayDate(),
                getDisplayVersion(),
                getDisplayText()
            };
        }
        else {
            localKey = "ItemText";
            arguments = new Object[] {
                getDisplayDate(),
                getDisplayText()
            };
        }
        return getString(localKey, arguments);
    }

    public HistoryItem getHistoryItem() { return historyItem; }

    public Boolean isVersionAttached() { return historyItem.isSetVersionId(); }

    /**
     * Populate the history item context menu.
     * 
     * @param e
     *            The mouse event.
     * @param jPopupMenu
     *            The menu to populate.
     */
    public void populatePopupMenu(final MouseEvent e,
            final JPopupMenu jPopupMenu) {
        if(isVersionAttached()) {
            jPopupMenu.add(getOpenVersionMenuItem());
            if(!isClosed()) {
                final Contact[] team = avatar.getTeam(historyItem.getDocumentId());
                if(0 < team.length) {
                    jPopupMenu.add(getSendVersionMenu(team));
                }
            }
        }
    }

    private Boolean isClosed() {
        return ArtifactUtil.isClosed(historyItem.getDocumentId(), ArtifactType.DOCUMENT);
    }

    /**
     * Set the avatar.
     * 
     * @param avatar
     *            The avatar.
     */
	public void setAvatar(final HistoryItems avatar) { this.avatar = avatar; }

    public void setHistoryItem(final HistoryItem historyItem) {
        this.historyItem = historyItem;
    }

    protected Integer getMnemonic(final String localKey) {
        final String mnemonicString = getString(localKey + "Mnemonic");
        return new Integer(mnemonicString.charAt(0));
    }

    protected Integer getMnemonic(final String localKey,
            final Object[] arguments) {
        // here's hoping the team isn't bigger than 9
        final String mnemonicString = getString(localKey + "Mnemonic", arguments);
        return new Integer(mnemonicString.charAt(0));
    }

    /**
     * Obtain localized text.
     * @param localKey The localization key.
     * @return The localized text.
     */
    protected String getString(final String localKey) {
        return l18n.getString(localKey);
    }

    protected String getString(final String localKey, final Object[] arguments) {
        return l18n.getString(localKey, arguments);
    }

    /**
     * Create a menu item.
     * 
     * @param localKey
     *            The menu item text\mnemonic's localization local key.
     * @param l
     *            The menu item's action listener.
     * @return The menu item.
     */
    private JMenuItem createJMenuItem(final String localKey,
            final ActionListener l) {
        final JMenuItem jMenuItem = MenuItemFactory.create(getString(localKey), getMnemonic(localKey));
        jMenuItem.addActionListener(l);
        return jMenuItem;
    }

    /**
     * Create a menu item.
     * 
     * @param localKey
     *            The menu item text\mnemonic's localization local key.
     * @param arguments
     *            The localization format arguments.
     * @param l
     *            The menu item's action listener.
     * @return The menu item.
     */
    private JMenuItem createJMenuItem(final String localKey,
            final Object[] arguments, final ActionListener l) {
        return MenuItemFactory.create(getString(localKey, arguments),
                    getMnemonic(localKey, arguments), l);
    }

    /**
     * Obtain the display text for the date.
     * 
     * @return The display text for the date.
     */
    private Date getDisplayDate() {
        return historyItem.getDate().getTime();
    }

    /**
     * Obtain the display text.
     * 
     * @return The display text.
     */
    private String getDisplayText() {
        return historyItem.getEvent();
    }

    /**
     * Obtain the display text for the version.
     * 
     * @return The display text for the version.
     */
    private Long getDisplayVersion() {
        return historyItem.getVersionId();
    }

    /**
     * Obtain the document id.
     * 
     * @return The document id.
     */
    private Long getDocumentId() { return historyItem.getDocumentId(); }

    /**
     * Create the open version menu item.
     * 
     * @return The open version menu item.
     */
    private JMenuItem getOpenVersionMenuItem() {
        return createJMenuItem("OpenVersion", new ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent e) {
            	avatar.getController().runOpenDocumentVersion(getDocumentId(), getVersionId());
            }
        });
    }

    /**
     * Create the send version menu. This menu is a 2nd-tier menu containing all
     * of the document's team members.
     * 
     * @return The send version menu.
     */
    private JMenu getSendVersionMenu(final Contact[] team) {
        final JMenu jMenu = MenuFactory.create(getString("SendVersion"));
        for(final Contact teamMember : team) {
            jMenu.add(createJMenuItem(
                    "SendVersion.TeamMember",
                    new Object[] {teamMember.getFirstName(), teamMember.getLastName(), teamMember.getOrganization()},
                    new ActionListener() {
                        public void actionPerformed(final ActionEvent e) {
                            avatar.getController().runSendArtifactVersion(getDocumentId(), teamMember, getVersionId());
                        }
                    }));
        }
        return jMenu;
    }

    /**
     * Obtain the version id.
     * 
     * @return The version id.
     */
    private Long getVersionId() { return historyItem.getVersionId(); }
}

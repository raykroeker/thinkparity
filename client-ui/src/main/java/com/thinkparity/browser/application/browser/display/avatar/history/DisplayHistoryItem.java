/*
 * DisplayHistoryItem.java
 *
 * Created on March 28, 2006, 9:29 PM
 */
package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.component.MenuItemFactory;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.l10n.ListItemLocalization;

import com.thinkparity.model.parity.model.document.history.HistoryItem;

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

    private Avatar avatar;

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
     * Obtain the avatar.
     * 
     * @return The avatar.
     */
	public Avatar getAvatar() { return avatar; }

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

    public void populatePopupMenu(final MouseEvent e, final JPopupMenu jPopupMenu) {
        if(isVersionAttached()) {
            jPopupMenu.add(getOpenVersionMenuItem());
            jPopupMenu.add(getSendVersionMenuItem());
        }
    }

    /**
     * Set the avatar.
     * 
     * @param avatar
     *            The avatar.
     */
	public void setAvatar(final Avatar avatar) { this.avatar = avatar; }

    public void setHistoryItem(final HistoryItem historyItem) {
        this.historyItem = historyItem;
    }

    protected Integer getMnemonic(final String localKey) {
        final String mnemonicString = getString(localKey + "Mnemonic");
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

    private JMenuItem createJMenuItem(final String menuItemLocalKey, final ActionListener actionListener) {
        final JMenuItem jMenuItem = MenuItemFactory.create(getString(menuItemLocalKey), getMnemonic(menuItemLocalKey));
        jMenuItem.addActionListener(actionListener);
        return jMenuItem;
    }

    private Date getDisplayDate() {
        return historyItem.getDate().getTime();
    }

    private String getDisplayText() {
        return historyItem.getEvent();
    }

    private Long getDisplayVersion() {
        return historyItem.getVersionId();
    }

    private Long getDocumentId() { return historyItem.getDocumentId(); }

    private JMenuItem getOpenVersionMenuItem() {
        return createJMenuItem("OpenVersion", new ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent e) {
            	avatar.getController().runOpenDocumentVersion(getDocumentId(), getVersionId());
            }
        });
    }

    private JMenuItem getSendVersionMenuItem() {
        return createJMenuItem("SendVersion", new ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent e) {
            	avatar.getController().displaySendVersion(getDocumentId(), getVersionId());
            }
        });
    }

    private Long getVersionId() { return historyItem.getVersionId(); }
}

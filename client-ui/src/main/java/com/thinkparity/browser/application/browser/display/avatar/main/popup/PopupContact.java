/**
 * Created On: 3-Jul-2006 3:09:50 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar.main.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.avatar.contact.CellContact;
import com.thinkparity.browser.application.browser.display.provider.FlatSingleContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PopupContact implements Popup {

    static {
        // HACK
        com.thinkparity.browser.application.browser.component.MenuItemFactory.create("", 0);
    }

    /** The main display content provider. */
    private final FlatSingleContentProvider contentProvider;

    /** The document main cell. */
    private final CellContact contact;

    /** The popup localisation. */
    private final PopupL18n l18n;

    /**
     * Create a PopupContact.
     * 
     * @param contentProvider
     *            The content provider.
     * @param contact
     *            The selected contact.
     */
    public PopupContact(final FlatSingleContentProvider contentProvider,
            final CellContact contact) {
        super();
        this.contentProvider = contentProvider;
        this.contact = contact;
        this.l18n = new PopupL18n("ContactListItem");
    }

    /**
     * Trigger a popup menu for a contact.
     * 
     * @see com.thinkparity.browser.application.browser.display.avatar.main.popup.Popup#trigger()
     *
     */
    public void trigger(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
        // MENU_ITEM Open
        jPopupMenu.add(new Open(application));
        
        // MENU_ITEM Delete
        jPopupMenu.add(new Delete(application));
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

    /** An open {@link JMenuItem}. */
    private class Open extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create the Open menu item.
         *
         * @param application
         *            The browser application.
         */
        private Open(final Browser application) {
            super(getString("Open"), getString("OpenMnemonic").charAt(0));
            this.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runOpenContact(contact.getId());
                }
            });
        }
    }
    
    /** An open {@link JMenuItem}. */
    private class Delete extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create the Delete menu item.
         *
         * @param application
         *            The browser application.
         */
        private Delete(final Browser application) {
            super(getString("Delete"), getString("DeleteMnemonic").charAt(0));
            this.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runDeleteContact(contact.getId());
                }
            });
        }
    }
}

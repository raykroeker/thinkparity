/**
 * Created On: 1-Aug-06 5:01:19 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.Browser;


/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PopupNew implements MenuPopup {

    static {
        // HACK
        com.thinkparity.browser.application.browser.component.MenuItemFactory.create("", 0);
    }
    
    /** The popup localisation. */
    private final MenuPopupL18n l18n;
    
    /**
     * Create a PopupNew.
     * 
     * @param contentProvider
     *            The content provider.
     * @param contact
     *            The selected contact.
     */
    public PopupNew() {
        super();
        this.l18n = new MenuPopupL18n("NewMenuListItem");
    }
    
    /**
     * Trigger a popup menu for the "New" menu.
     */
    public void trigger(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
        // MENU_ITEM New Package
        jPopupMenu.add(new NewContainer(application));
        
        // MENU_ITEM New Contact
        jPopupMenu.add(new NewContact(application));
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
    
    /** A "new container" {@link JMenuItem}. */
    private class NewContainer extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create the Open menu item.
         *
         * @param application
         *            The browser application.
         */
        private NewContainer(final Browser application) {
            super(getString("NewContainer"), getString("NewContainerMnemonic").charAt(0));
            this.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runCreateContainer();
                }
            });
        }
    }
    
    /** A "new contact" {@link JMenuItem}. */
    private class NewContact extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create the Open menu item.
         *
         * @param application
         *            The browser application.
         */
        private NewContact(final Browser application) {
            super(getString("NewContact"), getString("NewContactMnemonic").charAt(0));
            this.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runAddContact();
                }
            });
        }
    }    
}

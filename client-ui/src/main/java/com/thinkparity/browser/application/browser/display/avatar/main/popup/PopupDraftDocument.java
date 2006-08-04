/*
 * Apr 12, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.avatar.container.MainCellDraftDocument;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.platform.Platform.Connection;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PopupDraftDocument implements Popup {

    static {
        // HACK
        com.thinkparity.browser.application.browser.component.MenuItemFactory.create("", 0);
    }

    /** The main display content provider. */
    private final CompositeFlatSingleContentProvider contentProvider;

    /** The document main cell. */
    private final MainCellDraftDocument document;

    /** The popup localisation. */
    private final PopupL18n l18n;

    /**
     * Create a PopupDocument.
     * 
     * @param contentProvider
     *            The main display's content provider.
     * @param document
     *            The selected document.
     */
    public PopupDraftDocument(
            final CompositeFlatSingleContentProvider contentProvider,
            final MainCellDraftDocument document) {
        super();
        this.contentProvider = contentProvider;
        this.document = document;
        this.l18n = new PopupL18n("DraftDocumentListItem");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.popup.Popup#trigger()
     *
     */
    public void trigger(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
        if(Connection.ONLINE == application.getConnection()) { triggerOnline(application, jPopupMenu, e); }
        else if(Connection.OFFLINE == application.getConnection()) { triggerOffline(application, jPopupMenu, e); }
        else { Assert.assertUnreachable("[LBROWSER] [APPLICATION] [BROWSER] [AVATAR] [DRAFT DOCUMENT POPUP] [TRIGGER] [UNKNOWN CONNECTION STATUS]"); }
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
    
    /**
     * Trigger a container popup when the user is online.
     *
     * @param application
     *      The browser application.
     * @param jPopupMenu
     *      The popup menu to populate.
     * @param e
     *      The source mouse event
     */
    private void triggerOnline(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
        jPopupMenu.add(new Open(application));
        jPopupMenu.add(new Rename(application));
        jPopupMenu.addSeparator();
        jPopupMenu.add(new Revert(application));
        jPopupMenu.add(new Remove(application));
    }    
   
    /**
     * Trigger a container popup when the user is offline.
     *
     * @param application
     *      The browser application.
     * @param jPopupMenu
     *      The popup menu to populate.
     * @param e
     *      The source mouse event
     */
    private void triggerOffline(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
        jPopupMenu.add(new Open(application));
        jPopupMenu.add(new Rename(application));
        jPopupMenu.addSeparator();
        jPopupMenu.add(new Revert(application));
        jPopupMenu.add(new Remove(application));
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
                    application.runOpenDocument(document.getContainerId(),document.getId());
                }
            });
        }
    }    
    
    /** A rename {@linke JMenuItem}. */
    private class Rename extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create Rename.
         * @param application
         *      The browser application.
         */
        private Rename(final Browser application) {
            super(getString("Rename"), getString("RenameMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runRenameDocument(document.getContainerId(),document.getId());
                }
            });
        }
    }    
    
    /** A revert {@linke JMenuItem}. */
    private class Revert extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create Rename.
         * @param application
         *      The browser application.
         */
        private Revert(final Browser application) {
            super(getString("Revert"), getString("RevertMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.userError("ErrorNotImplemented");
                }
            });
        }
    }      
    
    /** A remove {@linke JMenuItem}. */
    private class Remove extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create Rename.
         * @param application
         *      The browser application.
         */
        private Remove(final Browser application) {
            super(getString("Remove"), getString("RemoveMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.userError("ErrorNotImplemented");
                }
            });
        }
    }        
}

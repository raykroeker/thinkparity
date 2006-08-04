/**
 * Created On: 4-Aug-06 3:05:33 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar.main.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.avatar.container.CellDraft;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.platform.Platform.Connection;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PopupDraft implements Popup {

    static {
        // HACK
        com.thinkparity.browser.application.browser.component.MenuItemFactory.create("", 0);
    }

    /** The main display content provider. */
    private final CompositeFlatSingleContentProvider contentProvider;

    /** The document main cell. */
    private final CellDraft draft;

    /** The popup localisation. */
    private final PopupL18n l18n;

    /**
     * Create a PopupDraft.
     * 
     * @param contentProvider
     *            The main display's content provider.
     * @param draft
     *            The selected draft.
     */
    public PopupDraft(
            final CompositeFlatSingleContentProvider contentProvider,
            final CellDraft draft) {
        super();
        this.contentProvider = contentProvider;
        this.draft = draft;
        this.l18n = new PopupL18n("DraftListItem");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.popup.Popup#trigger()
     *
     */
    public void trigger(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
        if(Connection.ONLINE == application.getConnection()) { triggerOnline(application, jPopupMenu, e); }
        else if(Connection.OFFLINE == application.getConnection()) { triggerOffline(application, jPopupMenu, e); }
        else { Assert.assertUnreachable("[LBROWSER] [APPLICATION] [BROWSER] [AVATAR] [DRAFT POPUP] [TRIGGER] [UNKNOWN CONNECTION STATUS]"); }
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
        // MENU_ITEM Add document
        jPopupMenu.add(new AddDocument(application));
        
        // MENU_ITEM Publish
        jPopupMenu.add(new Publish(application));
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
        // MENU_ITEM Add document
        jPopupMenu.add(new AddDocument(application));        
    }
    
    /** A "add document" {@link JMenuItem}. */    
    private class AddDocument extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create a AddContainer JMenuItem.
         * 
         * @param application
         *            The browser application.
         */
        private AddDocument(final Browser application) {
            super(getString("AddDocument"), getString("AddDocumentMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runCreateDocument(draft.getContainerId());
                }
            });
        }
    }
    
    /** A publish {@link JMenuItem}. */
    private class Publish extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create a Publish JMenuItem.
         * 
         * @param application
         *            The browser application.
         */
        private Publish(final Browser application) {
            super(getString("Publish"), getString("PublishMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runPublishContainer(draft.getContainerId());
                }
            });
        }
    }
}

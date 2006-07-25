/**
 * Created On: 13-Jul-06 3:30:00 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar.main.popup;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.display.avatar.container.CellContainer;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.platform.Platform.Connection;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.KeyRequest;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PopupContainer implements Popup {

    static {
        // HACK
        com.thinkparity.browser.application.browser.component.MenuItemFactory.create("", 0);
    }

    /** The main display content provider. */
    private final CompositeFlatSingleContentProvider contentProvider;

    /** The document main cell. */
    private final CellContainer container;

    /** The popup localisation. */
    private final PopupL18n l18n;

    /**
     * Create a PopupContainer.
     * 
     * @param contentProvider
     *            The container display's content provider.
     * @param container
     *            The selected container (or null if none).
     */
    public PopupContainer(
            final CompositeFlatSingleContentProvider contentProvider,
            final CellContainer container) {
        super();
        this.contentProvider = contentProvider;
        this.container = container;
        this.l18n = new PopupL18n("ContainerListItem");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.popup.Popup#trigger()
     *
     */
    public void trigger(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
        if(Connection.ONLINE == application.getConnection()) { triggerOnline(application, jPopupMenu, e); }
        else if(Connection.OFFLINE == application.getConnection()) { triggerOffline(application, jPopupMenu, e); }
        else { Assert.assertUnreachable("[LBROWSER] [APPLICATION] [BROWSER] [AVATAR] [CONTAINER POPUP] [TRIGGER] [UNKNOWN CONNECTION STATUS]"); }
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
        if (null==container) {  // No selected container, ie. right clicked on a blank area
            // MENU_ITEM New container
            jPopupMenu.add(new NewContainer(application));
        }
        else {
            // MENU_ITEM Add document
            jPopupMenu.add(new AddDocument(application));
            if(container.isKeyHolder()) {
                if(!container.isWorkingVersionEqual()) {
                    // MENU_ITEM Publish
                    jPopupMenu.add(new Publish(application));
                }
            }
        }
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
        if (null==container) {  // No selected container, ie. right clicked on a blank area
            // MENU_ITEM New container
            jPopupMenu.add(new NewContainer(application));
        }
        else {
            // MENU_ITEM Add document
            jPopupMenu.add(new AddDocument(application));
            if(container.isKeyHolder()) {
                if(!container.isWorkingVersionEqual()) {
                    // MENU_ITEM Publish
                    jPopupMenu.add(new Publish(application));
                }
            }
        }
    }   
    

    

    /**
     * Create the share menu item.
     * 
     * @param application
     *            The browser application.
     * @return The share menu item.
     */
/*    private JMenu createShare(final Browser application) {
        final JMenu shareMenu = MenuFactory.create(getString("ShareMenu"));
        final Contact[] quickShareContacts = getQuickShareContacts();
        for(final Contact contact : quickShareContacts) {
            shareMenu.add(new QuickShare(application, contact));
        }
        shareMenu.add(new Share(application));
        return shareMenu;
    }

    private Contact[] getQuickShareContacts() {
        return (Contact[]) contentProvider.getElements(3, document.getId());
    }*/

    /**
     * Trigger a document popup when the user is online.
     *
     * @param application
     *      The browser application.
     * @param jPopupMenu
     *      The popup menu to populate.
     * @param e
     *      The source mouse event
     */
/*    private void triggerOnline(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
        final Set<User> team = document.getTeam();

        // MENU_ITEM Open
        if(document.isClosed()) {
            // MENU_ITEM Reactivate
            if(document.isClosed()) {
                jPopupMenu.add(new Reactivate(application));
            }
            // MENU_ITEM Delete
            jPopupMenu.addSeparator();
            jPopupMenu.add(new Delete(application));
        }
        else {
            if(document.isUrgent()) {
                final List<KeyRequest> keyRequests = document.getKeyRequests();
                if(keyRequests.size() >= 1) {
                    final Set<JabberId> requestedBySet = new HashSet<JabberId>();
                    for(final KeyRequest keyRequest : keyRequests) {
                        // if a single user has requested more than once; we only
                        // display one menu item.
                        if(!requestedBySet.contains(keyRequest.getRequestedBy())) {
                            // MENU_ITEM Accept Key Request ${RequestedBy}
                            jPopupMenu.add(new AcceptKeyRequest(application, keyRequest.getId(), keyRequest.getRequestedByName()));
                            requestedBySet.add(keyRequest.getRequestedBy());
                        }
                    }
                    // MENU_ITEM Decline All Key Requests
                    jPopupMenu.add(new DeclineAllKeyRequests(application));
                    jPopupMenu.addSeparator();
                }
            }
            // MENU_ITEM Share
            jPopupMenu.add(createShare(application));
            if(document.isKeyHolder()) {
                if(0 < team.size()) {
                    final JMenu jMenu = MenuFactory.create(getString("SendKey"));
                    for(final User teamMember : team)
                        jMenu.add(new SendKey(application, teamMember));
                    // MENU_ITEM Send Key ${TeamMember}
                    jPopupMenu.add(jMenu);
                }
                if(!document.isWorkingVersionEqual()) {
                    // MENU_ITEM Publish
                    jPopupMenu.add(new Publish(application));
                }
                // MENU_ITEM Close
                jPopupMenu.add(new Close(application));
            }
            // MENU_ITEM Request Key
            if(!document.isKeyHolder()) {
                jPopupMenu.add(new RequestKey(application));
            }
            // MENU_ITEM Rename
            if(!document.isDistributed()) {
                jPopupMenu.add(new Rename(application));
            }
            if(document.isKeyHolder()) {
                if(!document.isDistributed()) {
                    // MENU_ITEM Delete
                    jPopupMenu.addSeparator();
                    jPopupMenu.add(new Delete(application));
                }
            }
            else {
                // MENU_ITEM Delete
                jPopupMenu.addSeparator();
                jPopupMenu.add(new Delete(application));
            }
        }

        // DEBUG Document Menu Options
        if(e.isShiftDown()) {
            final Clipboard systemClipboard =
                Toolkit.getDefaultToolkit().getSystemClipboard();
            final ActionListener debugActionListener = new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    final StringSelection stringSelection =
                        new StringSelection(((JComponent) e.getSource()).getClientProperty("COPY_ME").toString());
                    systemClipboard.setContents(stringSelection, null);
                }
            };
            final JMenuItem idJMenuItem = new JMenuItem("Id - " + document.getId());
            idJMenuItem.putClientProperty("COPY_ME", document.getId());
            idJMenuItem.addActionListener(debugActionListener);

            final JMenuItem uidJMenuItem = new JMenuItem("Unique id - " + document.getUniqueId());
            uidJMenuItem.putClientProperty("COPY_ME", document.getUniqueId());
            uidJMenuItem.addActionListener(debugActionListener);

            jPopupMenu.addSeparator();
            jPopupMenu.add(idJMenuItem);
            jPopupMenu.add(uidJMenuItem);
        }
    }
*/
    
    /** A "new container" {@link JMenuItem}. */    
    private class NewContainer extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create a AddContainer JMenuItem.
         * 
         * @param application
         *            The browser application.
         */
        private NewContainer(final Browser application) {
            super(getString("NewContainer"), getString("NewContainerMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runCreateContainer();
                }
            });
        }
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
                    application.runCreateDocument(container.getId());
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
                        application.runPublishContainer(container.getId());
                    }
                });
            }
        }
        
    /** An accept key request {@link JMenuItem}. */
/*    private class AcceptKeyRequest extends JMenuItem {

        *//** @see java.io.Serializable *//*
        private static final long serialVersionUID = 1;

        *//**
         * Create a AcceptKeyRequest JMenuItem.
         *
         * @param application
         *            The browser application.
         * @param keyRequestId
         *            The key request id.
         * @param requestedByName
         *            The requestor's name.
         *//*
        private AcceptKeyRequest(final Browser application,
                final Long keyRequestId, final String requestedByName) {
            super(getString("KeyRequestAccept", new Object[] {requestedByName}),
                    getString("KeyRequestAcceptMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runAcceptKeyRequest(document.getId(), keyRequestId);
                }
            });
        }
    }*/

    /** A close {@link JMenuItem}. */
/*    private class Close extends JMenuItem {

        *//** @see java.io.Serializable *//*
        private static final long serialVersionUID = 1;

        *//**
         * Create a Close JMenuItem.
         *
         * @param application
         *            The browser application.
         *//*
        private Close(final Browser application) {
            super(getString("Close"), getString("CloseMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runCloseDocument(document.getId());
                }
            });
        }
    }*/

    /** A decline all key requests {@link JMenuItem}. */
/*    private class DeclineAllKeyRequests extends JMenuItem {

        *//** @see java.io.Serializable *//*
        private static final long serialVersionUID = 1;

        *//**
         * Create a DeclineAllKeyRequests JMenuItem.
         *
         * @param application
         *            The browser application.
         *//*
        private DeclineAllKeyRequests(final Browser application) {
            super(getString("KeyRequestDecline"), getString("KeyRequestDeclineMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runDeclineAllKeyRequests(document.getId());
                }
            });
        }
    }*/

    /** A delete {@link JMenuItem}. */
/*    private class Delete extends JMenuItem {

        *//** @see java.io.Serializable *//*
        private static final long serialVersionUID = 1;

        *//**
         * Create a Delete JMenuItem.
         *
         * @param application
         *            The browser application.
         *//*
        private Delete(final Browser application) {
            super(getString("Delete"), getString("DeleteMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runDeleteDocument(document.getId());
                }
            });
        }
    }*/



    /** A quick share {@link JMenuItem}. */
/*    private class QuickShare extends JMenuItem {

        *//** @see java.io.Serializable *//*
        private static final long serialVersionUID = 1;

        *//**
         * Create an AddTeamMember JMenuItem.
         *
         * @param application
         *      The browser application.
         *//*
        private QuickShare(final Browser application, final Contact contact) {
            super(getString("QuickShare", new Object[] {contact.getName()}), getString("QuickShareMnemonic", new Object[] {contact.getName()}).charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runAddNewDocumentTeamMember(document.getId(), contact.getId());
                }
            });
        }
    }*/

    /** A reactivate {@linke JMenuItem}. */
/*    private class Reactivate extends JMenuItem {

        *//** @see java.io.Serializable *//*
        private static final long serialVersionUID = 1;

        *//**
         * Create Rename.
         * @param application
         *      The browser application.
         *//*
        private Reactivate(final Browser application) {
            super(getString("Reactivate"), getString("ReactivateMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runReactivateDocument(document.getId());
                }
            });
        }
    }
*/
    /** A rename {@linke JMenuItem}. */
/*    private class Rename extends JMenuItem {

        *//** @see java.io.Serializable *//*
        private static final long serialVersionUID = 1;

        *//**
         * Create Rename.
         * @param application
         *      The browser application.
         *//*
        private Rename(final Browser application) {
            super(getString("Rename"), getString("RenameMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runRenameDocument(document.getId());
                }
            });
        }
    }*/

    /** A request key {@link JMenuItem}. */
/*    private class RequestKey extends JMenuItem {

        *//** @see java.io.Serializable *//*
        private static final long serialVersionUID = 1;

        *//**
         * Create a RequestKey JMenuItem.
         *
         * @param application
         *            The browser application.
         *//*
        private RequestKey(final Browser application) {
            super(getString("RequestKey"), getString("RequestKeyMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runRequestKey(document.getId());
                }
            });
        }
    }*/

    /** A send key {@link JMenuItem}. */
/*    private class SendKey extends JMenuItem {

        *//** @see java.io.Serializable *//*
        private static final long serialVersionUID = 1;

        private SendKey(final Browser application, final User teamMember) {
            super(getString("SendKey.TeamMember", new Object[] {teamMember.getName()}), getString("SendKey.TeamMemberMnemonic", new Object[] {teamMember.getName()}).charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runSendDocumentKey(
                        document.getId(), teamMember.getId());
                }
            });
        }
    }*/

    /** A share {@link JMenuItem}. */
/*    private class Share extends JMenuItem {

        *//** @see java.io.Serializable *//*
        private static final long serialVersionUID = 1;

        *//**
         * Create Share.
         *
         * @param application
         *      The browser application.
         *//*
        private Share(final Browser application) {
            super(getString("Share"), getString("ShareMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runAddNewDocumentTeamMember();
                }
            });
        }
    }*/
}

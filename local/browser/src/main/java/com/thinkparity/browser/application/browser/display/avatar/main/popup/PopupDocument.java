/*
 * Apr 12, 2006
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
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellDocument;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.platform.Platform.Connection;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.KeyRequest;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PopupDocument implements Popup {

    static {
        // HACK
        com.thinkparity.browser.application.browser.component.MenuItemFactory.create("", 0);
    }

    /** The connection status. */
    private final Connection connection;

    /** The main display content provider. */
    private final CompositeFlatSingleContentProvider contentProvider;

    /** The document main cell. */
    private final MainCellDocument document;

    /** The popup localisation. */
    private final PopupL18n l18n;

    /**
     * Create a PopupDocument.
     *
     * @param document
     *            The document main cell.
     */
    public PopupDocument(
            final CompositeFlatSingleContentProvider contentProvider,
            final MainCellDocument document, final Connection connection) {
        super();
        this.connection = connection;
        this.contentProvider = contentProvider;
        this.document = document;
        this.l18n = new PopupL18n("DocumentListItem");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.popup.Popup#trigger()
     *
     */
    public void trigger(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
        if(Connection.ONLINE == connection) { triggerOnline(application, jPopupMenu, e); }
        else if(Connection.OFFLINE == connection) { triggerOffline(application, jPopupMenu, e); }
        else { Assert.assertUnreachable("[LBROWSER] [APPLICATION] [BROWSER] [AVATAR] [DOCUMENT POPUP] [TRIGGER] [UNKNOWN CONNECTION STATUS]"); }
    }

    /**
     * Create the share menu item.
     * 
     * @param application
     *            The browser application.
     * @return The share menu item.
     */
    private JMenu createShare(final Browser application) {
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
     * Trigger a document popup when the user is offline.
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
        if(document.isRenameable()) {
            jPopupMenu.add(new Rename(application));
        }
    }

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
    private void triggerOnline(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
        final Set<User> team = document.getTeam();

        if(document.isUrgent()) {
            final List<KeyRequest> keyRequests = document.getKeyRequests();
            if(keyRequests.size() >= 1) {
                final Set<JabberId> requestedBySet = new HashSet<JabberId>();
                for(final KeyRequest keyRequest : keyRequests) {
                    // if a single user has requested more than once; we only
                    // display one menu item.
                    if(!requestedBySet.contains(keyRequest.getRequestedBy())) {
                        jPopupMenu.add(new AcceptKeyRequest(application, keyRequest.getId(), keyRequest.getRequestedByName()));
                        requestedBySet.add(keyRequest.getRequestedBy());
                    }
                }
                jPopupMenu.add(new DeclineAllKeyRequests(application));
                jPopupMenu.addSeparator();
            }
        }
        jPopupMenu.add(new Open(application));
        jPopupMenu.add(createShare(application));
        if(document.isKeyHolder()) {
            if(!document.isWorkingVersionEqual())
                jPopupMenu.add(new Publish(application));

            System.out.println("[LBROWSER] [APPLICATION] [BROWSER] [DISPLAY] [AVATAR] [POPUP DOCUMENT] [TRIGGER ONLINE] [TEAM SIZE] [" + team.size() + "]");
            if(0 < team.size()) {
                final JMenu jMenu = MenuFactory.create(getString("SendKey"));
                for(final User teamMember : team)
                    jMenu.add(new SendKey(application, teamMember));
                jPopupMenu.add(jMenu);
            }
            jPopupMenu.add(new Close(application));
        }
        else { jPopupMenu.add(new RequestKey(application)); }
        if(document.isRenameable()) {
            jPopupMenu.add(new Rename(application));
        }
        jPopupMenu.addSeparator();
        jPopupMenu.add(new Delete(application));

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

    /** An accept key request {@link JMenuItem}. */
    private class AcceptKeyRequest extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create a AcceptKeyRequest JMenuItem.
         *
         * @param application
         *            The browser application.
         * @param keyRequestId
         *            The key request id.
         * @param requestedByName
         *            The requestor's name.
         */
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
    }

    /** A share {@link JMenuItem}. */
    private class Share extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create Share.
         *
         * @param application
         *      The browser application.
         */
        private Share(final Browser application) {
            super(getString("Share"), getString("ShareMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runAddNewDocumentTeamMember();
                }
            });
        }
    }

    /** A close {@link JMenuItem}. */
    private class Close extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create a Close JMenuItem.
         *
         * @param application
         *            The browser application.
         */
        private Close(final Browser application) {
            super(getString("Close"), getString("CloseMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runCloseDocument(document.getId());
                }
            });
        }
    }

    /** A decline all key requests {@link JMenuItem}. */
    private class DeclineAllKeyRequests extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create a DeclineAllKeyRequests JMenuItem.
         *
         * @param application
         *            The browser application.
         */
        private DeclineAllKeyRequests(final Browser application) {
            super(getString("KeyRequestDecline"), getString("KeyRequestDeclineMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runDeclineAllKeyRequests(document.getId());
                }
            });
        }
    }

    /** A delete {@link JMenuItem}. */
    private class Delete extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create a Delete JMenuItem.
         *
         * @param application
         *            The browser application.
         */
        private Delete(final Browser application) {
            super(getString("Delete"), getString("DeleteMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runDeleteDocument(document.getId());
                }
            });
        }
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
                    application.runOpenDocument(document.getId());
                }
            });
        }
    }

    /** A publish {@link JMenuItem}. */
    private class Publish extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create a Send JMenuItem.
         *
         * @param application
         *            The browser application.
         */
        private Publish(final Browser application) {
            super(getString("Publish"), getString("PublishMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runPublishDocument();
                }
            });
        }
    }

    /** A quick share {@link JMenuItem}. */
    private class QuickShare extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create an AddTeamMember JMenuItem.
         *
         * @param application
         *      The browser application.
         */
        private QuickShare(final Browser application, final Contact contact) {
            super(getString("QuickShare", new Object[] {contact.getFirstName(), contact.getLastName()}), getString("QuickShareMnemonic", new Object[] {contact.getFirstName(), contact.getLastName()}).charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runAddNewDocumentTeamMember(document.getId(), contact.getId());
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
                    application.runRenameDocument(document.getId());
                }
            });
        }
    }

    /** A request key {@link JMenuItem}. */
    private class RequestKey extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create a RequestKey JMenuItem.
         *
         * @param application
         *            The browser application.
         */
        private RequestKey(final Browser application) {
            super(getString("RequestKey"), getString("RequestKeyMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runRequestKey(document.getId());
                }
            });
        }
    }

    /** A send key {@link JMenuItem}. */
    private class SendKey extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        private SendKey(final Browser application, final User teamMember) {
            super(getString("SendKey.TeamMember", new Object[] {teamMember.getFirstName(), teamMember.getLastName()}), getString("SendKey.TeamMemberMnemonic", new Object[] {teamMember.getFirstName(), teamMember.getLastName()}).charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.runSendDocumentKey(
                        document.getId(), teamMember.getId());
                }
            });
        }
    }

}

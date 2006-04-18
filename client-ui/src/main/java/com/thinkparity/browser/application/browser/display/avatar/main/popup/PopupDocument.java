/*
 * Apr 12, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellDocument;

import com.thinkparity.model.parity.model.artifact.KeyRequest;
import com.thinkparity.model.xmpp.JabberId;



/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PopupDocument implements Popup {

    static {
        // HACK
        com.thinkparity.browser.application.browser.component.MenuItemFactory.create("", 0);
    }

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
    public PopupDocument(final MainCellDocument document) {
        super();
        this.document = document;
        this.l18n = new PopupL18n("DocumentListItem");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.popup.Popup#trigger()
     * 
     */
    public void trigger(final Browser application, final JPopupMenu jPopupMenu, final MouseEvent e) {
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
                jPopupMenu.addSeparator();
                jPopupMenu.add(new DeclineAllKeyRequests(application));
            }
        }
        jPopupMenu.add(new Open(application));
        if(document.isClosed()) {
            jPopupMenu.addSeparator();
            jPopupMenu.add(new Delete(application));
        }
        else {
            jPopupMenu.add(new Send(application));
            if(document.isKeyHolder()) {
                jPopupMenu.addSeparator();
                jPopupMenu.add(new Close(application));
            }
            else {
                jPopupMenu.add(new RequestKey(application));
                jPopupMenu.addSeparator();
                jPopupMenu.add(new Delete(application));
            }
        }

        // DEBUG Document Menu Options
//        if(e.isShiftDown()) {
//            final Clipboard systemClipboard =
//                Toolkit.getDefaultToolkit().getSystemClipboard();
//            final ActionListener debugActionListener = new ActionListener() {
//                public void actionPerformed(final ActionEvent e) {
//                    final StringSelection stringSelection =
//                        new StringSelection(((JComponent) e.getSource()).getClientProperty("COPY_ME").toString());
//                    systemClipboard.setContents(stringSelection, null);
//                }
//            };
//            final JMenuItem idJMenuItem = new JMenuItem("Id:" + displayDocument.getId());
//            idJMenuItem.putClientProperty("COPY_ME", displayDocument.getId());
//            idJMenuItem.addActionListener(debugActionListener);
//
//            final JMenuItem uidJMenuItem = new JMenuItem("U Id:" + displayDocument.getUniqueId());
//            uidJMenuItem.putClientProperty("COPY_ME", displayDocument.getUniqueId());
//            uidJMenuItem.addActionListener(debugActionListener);
//
//            jPopupMenu.addSeparator();
//            jPopupMenu.add(idJMenuItem);
//            jPopupMenu.add(uidJMenuItem);
//        }
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

    /** A send {@link JMenuItem}. */
    private class Send extends JMenuItem {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /**
         * Create a Send JMenuItem.
         * 
         * @param application
         *            The browser application.
         */
        private Send(final Browser application) {
            super(getString("Send"), getString("SendMnemonic").charAt(0));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    application.displaySessionSendFormAvatar();
                }
            });
        }
    }
}

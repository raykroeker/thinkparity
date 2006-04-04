/*
 * Mar 22, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.model.util.ArtifactUtil;

import com.thinkparity.model.parity.model.artifact.KeyRequest;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.xmpp.JabberId;

/**
 * The display document is created by the content provider and is a wrapper
 * around a parity document as well as any system messages for that document.
 * 
 * It exposes key fields required by the list in order to easily display.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DisplayDocument extends Document {

	private static final Integer DISPLAY_MAX_LENGTH;

    static { DISPLAY_MAX_LENGTH = 35; }

	/**
	 * Using the browser from the list item; run the accept key request action.
	 * 
	 * @param listItem
	 *            The list item.
	 * @param artifactId
	 *            The artifact id.
	 * @param systemMessageId
	 *            The system message id.
	 */
	private static void runAcceptKeyRequest(final ListItem listItem,
			final Long artifactId, final Long keyRequestId) {
		listItem.getController().runAcceptKeyRequest(artifactId, keyRequestId);
	}

	/**
	 * Run the decline all key requests action.
	 * 
	 * @param listItem
	 *            The list item.
	 * @param documentId
	 *            The document id.
	 */
	private static void runDeclineAllKeyRequests(final ListItem listItem,
			final Long documentId) {
		listItem.getController().runDeclineAllKeyRequests(documentId);
	}

	/**
	 * Using the browser from the list item; run the decline key request action.
	 * 
	 * @param listItem
	 *            The list item.
	 * @param systemMessageId
	 *            The system message id.
	 */
	private static void runDeclineKeyRequest(final ListItem listItem,
			final Long artifactId, final Long keyRequestId) {
		listItem.getController().runDeclineKeyRequest(artifactId, keyRequestId);
	}

	private final List<KeyRequest> keyRequests;

	private Boolean urgent;

	/**
	 * Create a DisplayDocument.
	 * 
	 */
	public DisplayDocument(final Document d) {
        super(d.getCreatedBy(), d.getCreatedOn(), d.getDescription(),
                d.getFlags(), d.getUniqueId(), d.getName(), d.getUpdatedBy(),
                d.getUpdatedOn());
        setId(d.getId());
        setState(d.getState());
		this.keyRequests = new LinkedList<KeyRequest>();
	}

    public String getDisplay() {
	    if(DISPLAY_MAX_LENGTH < getName().length()) {
            return getName().substring(0, DISPLAY_MAX_LENGTH - 1 - 3) + "...";
        }
        else { return getName(); }
	}

    public String getDisplayToolTip() {
        if(DISPLAY_MAX_LENGTH < getName().length()) {
            return getName();
        }
        else { return ""; }
    }

	public String getUrgentInfo(final ListItem listItem) {
		return null;
	}

	public Boolean hasBeenSeen() {
		return ArtifactUtil.hasBeenSeen(getId(), getType());
	}

	public Boolean isClosed() {
        return ArtifactUtil.isClosed(getId(), getType());
	}

	public Boolean isKeyHolder() { return ArtifactUtil.isKeyHolder(getId()); }

	public Boolean isUrgent() { return urgent; }

	public void populateUrgentMenu(final ListItem listItem, final MouseEvent e,
			final JPopupMenu jPopupMenu) {
		if(urgent) {
			if(keyRequests.size() > 1) {
				final Set<JabberId> requestedBySet = new HashSet<JabberId>();
				for(final KeyRequest keyRequest : keyRequests) {
					if(!requestedBySet.contains(keyRequest.getRequestedBy())) {
						jPopupMenu.add(getKeyRequestAcceptMultipleMenuItem(listItem, keyRequest));
						requestedBySet.add(keyRequest.getRequestedBy());
					}
				}
				jPopupMenu.addSeparator();
				jPopupMenu.add(getKeyRequestDeclineAllMenuItem(listItem));
			}
			else {
				jPopupMenu.add(getKeyRequestAcceptMenuItem(listItem, keyRequests.get(0)));
				jPopupMenu.add(getKeyRequestDeclineMenuItem(listItem, keyRequests.get(0)));
			}
		}
	}

	public void setKeyRequests(final List<KeyRequest> keyRequests) {
		urgent = keyRequests.size() > 0;
		this.keyRequests.clear();
		this.keyRequests.addAll(keyRequests);
	}

	private JMenuItem getKeyRequestAcceptMenuItem(final ListItem listItem,
			final KeyRequest keyRequest) {
		final Object[] arguments = new Object[] {
				keyRequest.getRequestedByName()
		};
		return listItem.createJMenuItem("KeyRequestAccept", arguments, new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				runAcceptKeyRequest(listItem, getId(), keyRequest.getId());
			}
		});
	}

	private JMenuItem getKeyRequestAcceptMultipleMenuItem(final ListItem listItem,
			final KeyRequest keyRequest) {
		final Object[] arguments = new Object[] {
				keyRequest.getRequestedByName()
		};
		return listItem.createJMenuItem(
				"KeyRequestAcceptMultiple", arguments, new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				runAcceptKeyRequest(listItem, getId(), keyRequest.getId());
			}
		});
	}

	private JMenuItem getKeyRequestDeclineAllMenuItem(final ListItem listItem) {
		return listItem.createJMenuItem("KeyRequestDeclineAll", new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				runDeclineAllKeyRequests(listItem, getId());
			}
		});
	}

	private JMenuItem getKeyRequestDeclineMenuItem(final ListItem listItem,
			final KeyRequest keyRequest) {
		return listItem.createJMenuItem("KeyRequestDecline", new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				runDeclineKeyRequest(listItem, getId(), keyRequest.getId());
			}
		});
	}
}

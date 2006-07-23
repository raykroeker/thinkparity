/*
 * Feb 28, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.contact.*;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.events.XMPPContactListener;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class XMPPContact {

	private static final List<XMPPContactListener> listeners;

	private static final Object listenersLock;

	static {
		listeners = new LinkedList<XMPPContactListener>();
		listenersLock = new Object();

		ProviderManager.addIQProvider("query", "jabber:iq:parity:invitecontact", new IQInviteContactProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:acceptcontactinvitation", new IQAcceptContactInvitationProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:declinecontactinvitation", new IQDeclineContactInvitationProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:readcontacts", new IQReadContactsProvider());
	}

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * The core xmpp functionality.
	 * 
	 */
	private final XMPPCore xmppCore;

	/**
	 * Create a XMPPContact.
	 * 
	 */
	XMPPContact(final XMPPCore xmppCore) {
		super();
		this.logger = Logger.getLogger(getClass());
		this.xmppCore = xmppCore;
	}

	/**
	 * Accept the contact invitation.
	 * 
	 * @param contact
	 *            The contact.
	 * @throws SmackException
	 */
	void accept(final JabberId contact) throws SmackException {
		logger.info("[XMPP] [CONTACT] [ACCEPT INVITATION]");
		logger.debug(contact);
		final IQ iq = new IQAcceptContactInvitation(contact);
		iq.setType(IQ.Type.SET);
		xmppCore.sendAndConfirmPacket(iq);
	}

	/**
	 * Add required listeners to the xmpp connection.
	 * 
	 * @param xmppConnection
	 *            The xmpp connection.
	 */
	void addPacketListeners(final XMPPConnection xmppConnection) {
		// invite contact
		xmppConnection.addPacketListener(
				new PacketListener() {
					public void processPacket(final Packet packet) {
						notifyInvitation((IQInviteContact) packet);
					}
				},
				new PacketTypeFilter(IQInviteContact.class));
		// accept invitation
		xmppConnection.addPacketListener(
				new PacketListener() {
					public void processPacket(final Packet packet) {
						notifyInvitationAccepted((IQAcceptContactInvitation) packet);
					}
				},
				new PacketTypeFilter(IQAcceptContactInvitation.class));
		// decline invitation
		xmppConnection.addPacketListener(
				new PacketListener() {
					public void processPacket(final Packet packet) {
						notifyInvitationDeclined((IQDeclineContactInvitation) packet);
					}
				},
				new PacketTypeFilter(IQDeclineContactInvitation.class));
	}

	/**
	 * Add a contact listener.
	 * 
	 * @param l
	 *            The contact listener.
	 */
	void addListener(final XMPPContactListener l) {
		logger.info("addListener(XMPPContactListener)");
		logger.debug(l);
		Assert.assertNotNull("Cannot add a null contact listener.", l);
		synchronized(listenersLock) {
			if(listeners.contains(l)) { return; }
			listeners.add(l);
		}
	}

	/**
	 * Decline a contact invitation.
	 * 
	 * @param contact
	 *            The contact.
	 * @throws SmackException
	 */
	void decline(final JabberId contact) throws SmackException {
		logger.info("[XMPP] [CONTACT] [DECLINE INVITATION]");
		logger.debug(contact);
		final IQ iq = new IQDeclineContactInvitation(contact);
		iq.setType(IQ.Type.SET);
		xmppCore.sendAndConfirmPacket(iq);
	}

	/**
     * Invite a contact.
     * 
     * @param email
     *            An e-mail address.
     * @throw SmackException
     */
	void invite(final String email) throws SmackException {
		logger.info("[XMPP] [CONTACT] [INVITE]");
		logger.debug(email);
        final XMPPMethod method = new XMPPMethod("contact:invite");
        method.setParameter("email", email);
        method.execute(xmppCore.getConnection());
	}

	/**
     * Read the contacts.
     * 
     * @return The list of contacts for the user.
     */
	List<Contact> read() throws SmackException {
		logger.info("[XMPP] [CONTACT] [READ CONTACTS]");
		final IQ iq = new IQReadContacts();
		iq.setType(IQ.Type.GET);
		final IQReadContactsResult result =
			(IQReadContactsResult) xmppCore.sendAndConfirmPacket(iq);
		return result.getContacts();
	}

	/**
	 * Notify the listeners that an invitation has been received.
	 * 
	 * @param invitation
	 *            The invitation.
	 */
	private void notifyInvitation(final IQInviteContact invitation) {
		synchronized(listenersLock) {
			for(final XMPPContactListener l : listeners) {
				l.invitationExtended(invitation.getContact());
			}
		}
	}

	/**
	 * Notify the listeners that an invitation has been accepted.
	 * 
	 * @param acceptance
	 *            The invitation acceptance.
	 */
	private void notifyInvitationAccepted(
			final IQAcceptContactInvitation acceptance) {
		synchronized(listenersLock) {
			for(final XMPPContactListener l : listeners) {
				l.invitationAccepted(acceptance.getContact());
			}
		}
	}

	/**
	 * Notify the listeners that an invitation has been declined.
	 * 
	 * @param declination.
	 *            The invitation declination.
	 */
	private void notifyInvitationDeclined(
			final IQDeclineContactInvitation declination) {
		synchronized(listenersLock) {
			for(final XMPPContactListener l : listeners) {
				l.invitationDeclined(declination.getContact());
			}
		}
	}
}

/*
 * Feb 28, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.VCardBuilder;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.Constants.Xml;
import com.thinkparity.model.Constants.Xml.Service;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.AbstractThinkParityIQ;
import com.thinkparity.model.smackx.packet.AbstractThinkParityIQProvider;
import com.thinkparity.model.smackx.packet.contact.IQReadContactsProvider;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.events.XMPPContactListener;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.10
 */
class XMPPContact extends AbstractXMPP {

    /** Remote event listeners. */
	private static final List<XMPPContactListener> LISTENERS;

	static {
		LISTENERS = new LinkedList<XMPPContactListener>();

        ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:contactdeleted", new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleContactDeletedIQ query = new HandleContactDeletedIQ();
                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    if (isStartTag("deletedBy")) {
                        query.deletedBy = readJabberId2();
                    } else if (isStartTag("deletedOn")) {
                        query.deletedOn = readCalendar2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:contact:contactupdated", new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleContactUpdatedIQ query = new HandleContactUpdatedIQ();
                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    if (isStartTag("contactId")) {
                        query.contactId = readJabberId2();
                    } else if (isStartTag("updatedOn")) {
                        query.updatedOn = readCalendar2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:invitationextended", new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleInvitationExtendedIQ query = new HandleInvitationExtendedIQ();
                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    if (isStartTag("extendedTo")) {
                        query.invitedAs = readEMail2();
                    } else if (isStartTag("extendedBy")) {
                        query.invitedBy = readJabberId2();
                    } else if (isStartTag("extendedOn")) {
                        query.invitedOn = readCalendar2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:invitationaccepted", new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleInvitationAcceptedIQ query = new HandleInvitationAcceptedIQ();
                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    if (isStartTag("acceptedBy")) {
                        query.acceptedBy = readJabberId2();
                    } else if (isStartTag("acceptedOn")) {
                        query.acceptedOn = readCalendar2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:contact:invitationdeleted", new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleInvitationDeletedIQ query = new HandleInvitationDeletedIQ();
                Boolean isComplete = Boolean.FALSE;
                while (!isComplete) {
                    if (isStartTag("deletedBy")) {
                        query.deletedBy = readJabberId2();
                    } else if (isStartTag("deletedOn")) {
                        query.deletedOn = readCalendar2();
                    } else if (isStartTag("invitedAs")) {
                        query.invitedAs = readEMail2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:invitationdeclined", new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser(parser);
                final HandleInvitationDeclinedIQ query = new HandleInvitationDeclinedIQ();

                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    next(1);
                    if (isStartTag(Xml.Contact.INVITED_AS)) {
                        next(1);
                        query.invitedAs = readEMail();
                        next(1);
                    } else if (isEndTag(Xml.Contact.INVITED_AS)) {
                        next(1);
                    } else if (isStartTag(Xml.Contact.DECLINED_BY)) {
                        next(1);
                        query.declinedBy = readJabberId();
                        next(1);
                    } else if (isEndTag(Xml.Contact.DECLINED_BY)) {
                        next(1);
                    } else if (isStartTag(Xml.Contact.DECLINED_ON)) {
                        next(1);
                        query.declinedOn = readCalendar();
                        next(1);
                    } else if (isEndTag(Xml.Contact.DECLINED_ON)) {
                        next(1);
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:readcontacts", new IQReadContactsProvider());
	}

	/**
	 * Create a XMPPContact.
	 * 
	 */
	XMPPContact(final XMPPCore xmppCore) {
		super(xmppCore);
	}

    /**
     * Accept the contact invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedBy
     *            The invited by user id <code>JabberId</code>.
     * @param acceptedOn
     *            When the user accepted <code>Calendar</code>.
     */
	void acceptInvitation(final JabberId userId, final JabberId invitedBy,
            final Calendar acceptedOn) {
		logApiId();
        logVariable("userId", userId);
        logVariable("invitedBy", invitedBy);
        logVariable("acceptedOn", acceptedOn);
		final XMPPMethod accept = new XMPPMethod("contact:acceptinvitation");
        accept.setParameter("userId", userId);
        accept.setParameter("invitedBy", invitedBy);
        accept.setParameter("acceptedOn", acceptedOn);
		execute(accept);
	}

	/**
	 * Add a contact listener.
	 * 
	 * @param l
	 *            The contact listener.
	 */
	void addListener(final XMPPContactListener l) {
		logApiId();
		logVariable("l", l);
		Assert.assertNotNull("Cannot add a null contact listener.", l);
		synchronized (LISTENERS) {
			if (LISTENERS.contains(l)) {
                return;
			} else {
			    LISTENERS.add(l);
            }
		}
	}

	/**
	 * Add required LISTENERS to the xmpp connection.
	 * 
	 * @param xmppConnection
	 *            The xmpp connection.
	 */
	void addPacketListeners(final XMPPConnection xmppConnection) {
		// invite contact
		xmppConnection.addPacketListener(
				new PacketListener() {
					public void processPacket(final Packet packet) {
						notifyInvitationExtended((HandleInvitationExtendedIQ) packet);
					}
				},
				new PacketTypeFilter(HandleInvitationExtendedIQ.class));
		// accept invitation
		xmppConnection.addPacketListener(
				new PacketListener() {
					public void processPacket(final Packet packet) {
						notifyInvitationAccepted((HandleInvitationAcceptedIQ) packet);
					}
				},
				new PacketTypeFilter(HandleInvitationAcceptedIQ.class));
        // decline invitation
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        notifyInvitationDeclined((HandleInvitationDeclinedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleInvitationDeclinedIQ.class));
        // delete contact
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        notifyContactDeleted((HandleContactDeletedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleContactDeletedIQ.class));
        // delete invitation
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        notifyInvitationDeleted((HandleInvitationDeletedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleInvitationDeletedIQ.class));
        // update contact
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        notifyContactUpdated((HandleContactUpdatedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleContactUpdatedIQ.class));
	}

	/**
     * Decline a contact invitation.
     * 
     * @param invitedBy
     *            The user who extended the invitation.
     * @throws SmackException
     */
	void decline(final EMail invitedAs, final JabberId invitedBy)
            throws SmackException {
		final XMPPMethod decline = new XMPPMethod("contact:declineinvitation");
        decline.setParameter(Xml.Contact.INVITED_AS, invitedAs);
        decline.setParameter(Xml.Contact.INVITED_BY, invitedBy);
        decline.setParameter(Xml.Contact.DECLINED_BY, xmppCore.getJabberId());
        decline.execute(xmppCore.getConnection());
	}

    /**
     * Delete a contact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    void delete(final JabberId userId, final JabberId contactId) {
        final XMPPMethod delete = new XMPPMethod("contact:delete");
        delete.setParameter("userId", userId);
        delete.setParameter("contactId", contactId);
        execute(delete);
    }

    /**
     * Delete a contact invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedAs
     *            The invitation <code>EMail</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    void deleteInvitation(final JabberId userId, final EMail invitedAs,
            final Calendar deletedOn) {
        logApiId();
        logVariable("userId", userId);
        logVariable("invitedAs", invitedAs);
        logVariable("deletedOn", deletedOn);
        final XMPPMethod deleteInvitation = new XMPPMethod("contact:deleteinvitation");
        deleteInvitation.setParameter("userId", userId);
        deleteInvitation.setParameter("invitedAs", invitedAs);
        deleteInvitation.setParameter("deletedOn", deletedOn);
        execute(deleteInvitation);
    }

	/**
     * Extend an invitation. If the email is registered within the thinkParity
     * community an invitation will be sent via thinkParity otherwise an
     * invitation will be sent via and email.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param extendedTo
     *            A <code>EMail</code> to invite.
     * @param extendedOn
     *            The date <code>Calendar</code> of the invitation.
     */
	void extendInvitation(final JabberId userId, final EMail extendedTo,
            final Calendar extendedOn) {
		logApiId();
        logVariable("userId", userId);
        logVariable("extendedTo", extendedTo);
        logVariable("extendedOn", extendedOn);
        final XMPPMethod extendInvitation = new XMPPMethod("contact:extendinvitation");
        extendInvitation.setParameter("userId", userId);
        extendInvitation.setParameter("extendedTo", extendedTo);
        extendInvitation.setParameter("extendedOn", extendedOn);
        execute(extendInvitation);
	}

	/**
     * Read a user's contacts.
     * 
     * @return The list of contacts for the user.
     */
	List<Contact> read(final JabberId userId) {
		logApiId();
        logVariable("userId", userId);
        assertIsAuthenticatedUser(userId);

		final XMPPMethod readIds = new XMPPMethod("contact:readids");
        readIds.setParameter("userId", userId);
        final XMPPMethodResponse response = execute(readIds, Boolean.TRUE);
        final List<JabberId> contactIds = response.readResultJabberIds("contactIds");
        final List<Contact> contacts = new ArrayList<Contact>(contactIds.size());
        for (final JabberId contactId : contactIds) {
            contacts.add(read(userId, contactId));
        }
        return contacts;
	}

    /**
     * Read a user's contact.
     * 
     * @return A contact.
     */
    Contact read(final JabberId userId, final JabberId contactId) {
        logApiId();
        logVariable("userId", userId);
        assertIsAuthenticatedUser(userId);

        final XMPPMethod read = new XMPPMethod("contact:read");
        read.setParameter("userId", userId);
        read.setParameter("contactId", contactId);
        final XMPPMethodResponse response = execute(read, Boolean.TRUE);

        final Contact contact = new Contact();
        contact.setId(response.readResultJabberId(Xml.Contact.JABBER_ID));
        contact.setName(response.readResultString(Xml.Contact.NAME));
        contact.setOrganization(response.readResultString(Xml.Contact.ORGANIZATION));
        contact.setTitle(response.readResultString("title"));
        contact.setVCard(VCardBuilder.createVCard(response.readResultString(Xml.Contact.VCARD)));
        contact.addAllEmails(response.readResultEMails("emails"));
        return contact;
    }

    /**
     * Fire a local event for the remote contact deleted event.
     * 
     * @param query
     *            The internet event query.
     */
    private void notifyContactDeleted(final HandleContactDeletedIQ query) {
        synchronized (LISTENERS) {
            for(final XMPPContactListener l : LISTENERS) {
                l.handleContactDeleted(query.deletedBy, query.deletedOn);
            }
        }
    }

    /**
     * Fire a local event for the remote contact updated event.
     * 
     * @param query
     *            The internet event query.
     */
    private void notifyContactUpdated(final HandleContactUpdatedIQ query) {
        synchronized (LISTENERS) {
            for(final XMPPContactListener l : LISTENERS) {
                l.handleContactUpdated(query.contactId, query.updatedOn);
            }
        }
    }

	/**
     * Fire a local event for the remote invitation accepted event.
     * 
     * @param query
     *            The internet event query.
     */
    private void notifyInvitationAccepted(final HandleInvitationAcceptedIQ query) {
        synchronized (LISTENERS) {
            for(final XMPPContactListener l : LISTENERS) {
                l.handleInvitationAccepted(query.acceptedBy, query.acceptedOn);
            }
        }
    }

    /**
     * Fire a local event for the remote invitation declined event.
     * 
     * @param query
     *            The internet event query.
     */
    private void notifyInvitationDeclined(final HandleInvitationDeclinedIQ query) {
        synchronized (LISTENERS) {
            for(final XMPPContactListener l : LISTENERS) {
                l.handleInvitationDeclined(query.invitedAs, query.declinedBy,
                        query.declinedOn);
            }
        }
    }

    /**
     * Fire a local event for the remote invitation deleted event.
     * 
     * @param query
     *            The internet event query.
     */
    private void notifyInvitationDeleted(final HandleInvitationDeletedIQ query) {
        synchronized (LISTENERS) {
            for(final XMPPContactListener l : LISTENERS) {
                l.handleInvitationDeleted(query.invitedAs, query.deletedBy,
                        query.deletedOn);
            }
        }
    }

    /**
     * Fire a local event for the remote invitation extended event.
     * 
     * @param query
     *            The internet event query.
     */
	private void notifyInvitationExtended(final HandleInvitationExtendedIQ query) {
		synchronized (LISTENERS) {
			for(final XMPPContactListener l : LISTENERS) {
				l.handleInvitationExtended(query.invitedAs, query.invitedBy,
                        query.invitedOn);
			}
		}
	}

    private static class HandleContactDeletedIQ extends AbstractThinkParityIQ {
        private JabberId deletedBy;
        private Calendar deletedOn;
    }

    private static class HandleContactUpdatedIQ extends AbstractThinkParityIQ {
        private JabberId contactId;
        private Calendar updatedOn;
    }

    private static class HandleInvitationAcceptedIQ extends AbstractThinkParityIQ {
        private JabberId acceptedBy;
        private Calendar acceptedOn;
    }

    private static class HandleInvitationDeclinedIQ extends AbstractThinkParityIQ {
        private JabberId declinedBy;
        private Calendar declinedOn;
        private EMail invitedAs;
    }

    private static class HandleInvitationDeletedIQ extends AbstractThinkParityIQ {
        private JabberId deletedBy;
        private Calendar deletedOn;
        private EMail invitedAs;
    }

    private static class HandleInvitationExtendedIQ extends AbstractThinkParityIQ {
        private EMail invitedAs;
        private JabberId invitedBy;
        private Calendar invitedOn;
    }
}

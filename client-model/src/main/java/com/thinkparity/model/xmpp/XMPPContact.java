/*
 * Feb 28, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.VCardBuilder;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.model.Constants.Xml;
import com.thinkparity.model.Constants.Xml.Service;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.AbstractThinkParityIQ;
import com.thinkparity.model.smackx.packet.AbstractThinkParityIQProvider;
import com.thinkparity.model.smackx.packet.contact.IQReadContacts;
import com.thinkparity.model.smackx.packet.contact.IQReadContactsProvider;
import com.thinkparity.model.smackx.packet.contact.IQReadContactsResult;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.events.XMPPContactListener;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class XMPPContact extends AbstractXMPP {

    /** Contact remote event listeners. */
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
     * @param invitedBy
     *            By whom the invitation was extended.
     * @throws SmackException
     */
	void accept(final JabberId invitedBy)
            throws SmackException {
		logApiId();
        logVariable("invitedBy", invitedBy);
		final XMPPMethod accept = new XMPPMethod("contact:acceptinvitation");
        accept.setParameter(Xml.Contact.INVITED_BY, invitedBy);
        accept.setParameter(Xml.Contact.ACCEPTED_BY, xmppCore.getJabberId());
		accept.execute(xmppCore.getConnection());
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
     * Read the contacts.
     * 
     * @return The list of contacts for the user.
     */
	List<Contact> read() throws SmackException {
		logApiId();
		final IQ iq = new IQReadContacts();
		iq.setType(IQ.Type.GET);
		final IQReadContactsResult result =
			(IQReadContactsResult) xmppCore.sendAndConfirmPacket(iq);
		return result.getContacts();
	}

	/**
     * Read a contact.
     * 
     * @return A contact.
     */
    Contact read(final JabberId contactId) throws XMPPException {
        final XMPPMethod read = new XMPPMethod("contact:read");
        read.setParameter(Xml.Contact.JABBER_ID, contactId);
        final XMPPMethodResponse response = execute(read, Boolean.TRUE);

        final Contact contact = new Contact();
        contact.setId(response.readResultJabberId(Xml.Contact.JABBER_ID));
        contact.setName(response.readResultString(Xml.Contact.NAME));
        contact.setOrganization(response.readResultString(Xml.Contact.ORGANIZATION));
        contact.setVCard(VCardBuilder.createVCard(response.readResultString(Xml.Contact.VCARD)));
        contact.addAllEmails(response.readResultEMails("emails"));
        return contact;
    }

    /**
     * Notify the LISTENERS that an invitation has been accepted.
     * 
     * @param acceptance
     *            The invitation acceptance.
     */
    private void notifyInvitationAccepted(final HandleInvitationAcceptedIQ query) {
        synchronized (LISTENERS) {
            for(final XMPPContactListener l : LISTENERS) {
                l.handleInvitationAccepted(query.acceptedBy, query.acceptedOn);
            }
        }
    }

    /**
     * Notify the LISTENERS that an invitation has been accepted.
     * 
     * @param acceptance
     *            The invitation acceptance.
     */
    private void notifyContactDeleted(final HandleContactDeletedIQ query) {
        synchronized (LISTENERS) {
            for(final XMPPContactListener l : LISTENERS) {
                l.handleContactDeleted(query.deletedBy, query.deletedOn);
            }
        }
    }

	/**
	 * Notify the LISTENERS that an invitation has been declined.
	 * 
	 * @param declination.
	 *            The invitation declination.
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
	 * Notify the LISTENERS that an invitation has been received.
	 * 
	 * @param invitation
	 *            The invitation.
	 */
	private void notifyInvitationExtended(final HandleInvitationExtendedIQ query) {
		synchronized (LISTENERS) {
			for(final XMPPContactListener l : LISTENERS) {
				l.handleInvitationExtended(query.invitedAs, query.invitedBy, query.invitedOn);
			}
		}
	}

    /**
     * <b>Title:</b>thinkParity XMPP Handle Invitation Accepted Query<br>
     * <b>Description:</b>Provides a wrapper fr the invitation accepted remote
     * event.<br>
     */
    private static class HandleInvitationAcceptedIQ extends AbstractThinkParityIQ {

        /** The acceptor. */
        private JabberId acceptedBy;

        /** The acceptance date. */
        private Calendar acceptedOn;
    }

    /**
     * <b>Title:</b>thinkParity XMPP Handle Invitation Declined Query<br>
     * <b>Description:</b>Provides a wrapper fr the invitation declined remote
     * event.<br>
     */
    private static class HandleInvitationDeclinedIQ extends AbstractThinkParityIQ {

        /** The declined user. */
        private JabberId declinedBy;

        /** The declined date. */
        private Calendar declinedOn;

        /** The original invitation e-mail address. */
        private EMail invitedAs;
    }

    /**
     * <b>Title:</b>thinkParity XMPP Handle Invitation Extended Query<br>
     * <b>Description:</b>Provides a wrapper fr the invitation extended remote
     * event.<br>
     */
    private static class HandleInvitationExtendedIQ extends AbstractThinkParityIQ {

        /** The invitation e-mail address. */
        private EMail invitedAs;

        /** The inviter. */
        private JabberId invitedBy;

        /** The invitation date. */
        private Calendar invitedOn;
    }

    private static class HandleContactDeletedIQ extends AbstractThinkParityIQ {

        /** Who deleted the contact. */
        private JabberId deletedBy;

        /** When the contact was deleted. */
        private Calendar deletedOn;
    }
}

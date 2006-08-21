/*
 * Feb 28, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

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
class XMPPContact {

	private static final List<XMPPContactListener> listeners;

	private static final Object listenersLock;

	static {
		listeners = new LinkedList<XMPPContactListener>();
		listenersLock = new Object();

		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:invitationextended", new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser(parser);
                final HandleInvitationExtendedIQ query = new HandleInvitationExtendedIQ();

                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    next(1);
                    if (isStartTag(Xml.Contact.INVITED_AS)) {
                        next(1);
                        query.invitedAs = readString();
                        next(1);
                    } else if (isEndTag(Xml.Contact.INVITED_AS)) {
                        next(1);
                    } else if (isStartTag(Xml.Contact.INVITED_BY)) {
                        next(1);
                        query.invitedBy = readJabberId();
                        next(1);
                    } else if (isEndTag(Xml.Contact.INVITED_BY)) {
                        next(1);
                    } else if (isStartTag(Xml.Contact.INVITED_ON)) {
                        next(1);
                        query.invitedOn = readCalendar();
                        next(1);
                    } else if (isEndTag(Xml.Contact.INVITED_ON)) {
                        next(1);
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:invitationaccepted", new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser(parser);
                final HandleInvitationAcceptedIQ query = new HandleInvitationAcceptedIQ();

                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    next(1);
                    if (isStartTag(Xml.Contact.ACCEPTED_BY)) {
                        next(1);
                        query.acceptedBy = readJabberId();
                        next(1);
                    } else if (isEndTag(Xml.Contact.ACCEPTED_BY)) {
                        next(1);
                    } else if (isStartTag(Xml.Contact.ACCEPTED_ON)) {
                        next(1);
                        query.acceptedOn = readCalendar();
                        next(1);
                    } else if (isEndTag(Xml.Contact.ACCEPTED_ON)) {
                        next(1);
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
                        query.invitedAs = readString();
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
     * @param invitedBy
     *            By whome the invitation was extended.
     * @throws SmackException
     */
	void accept(final JabberId invitedBy)
            throws SmackException {
		logger.info("[XMPP] [CONTACT] [ACCEPT INVITATION]");
        logger.debug(invitedBy);
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
		logger.info("addListener(XMPPContactListener)");
		logger.debug(l);
		Assert.assertNotNull("Cannot add a null contact listener.", l);
		synchronized(listenersLock) {
			if(listeners.contains(l)) { return; }
			listeners.add(l);
		}
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
	}

	/**
     * Decline a contact invitation.
     * 
     * @param invitedBy
     *            The user who extended the invitation.
     * @throws SmackException
     */
	void decline(final String invitedAs, final JabberId invitedBy)
            throws SmackException {
		final XMPPMethod decline = new XMPPMethod("contact:declineinvitation");
        decline.setParameter(Xml.Contact.INVITED_AS, invitedAs);
        decline.setParameter(Xml.Contact.INVITED_BY, invitedBy);
        decline.setParameter(Xml.Contact.DECLINED_BY, xmppCore.getJabberId());
        decline.execute(xmppCore.getConnection());
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
        method.setParameter(Xml.Contact.EMAIL, email);
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
     * Read a contact.
     * 
     * @return A contact.
     */
    Contact read(final JabberId contactId) throws XMPPException {
        final XMPPMethod method = new XMPPMethod("contact:read");
        method.setParameter(Xml.Contact.JABBER_ID, contactId);
        final XMPPMethodResponse response = method.execute(xmppCore.getConnection());
        xmppCore.assertContainsResult("RESPONSE IS EMPTY", response);
        final Contact contact = new Contact();
        contact.setId(response.readResultJabberId(Xml.Contact.JABBER_ID));
        contact.setName(response.readResultString(Xml.Contact.NAME));
        contact.setOrganization(response.readResultString(Xml.Contact.ORGANIZATION));
        contact.setVCard(VCardBuilder.createVCard(response.readResultString(Xml.Contact.VCARD)));
        contact.addAllEmails(response.readResultStrings("emails"));
        return contact;
    }

	/**
	 * Notify the listeners that an invitation has been accepted.
	 * 
	 * @param acceptance
	 *            The invitation acceptance.
	 */
	private void notifyInvitationAccepted(final HandleInvitationAcceptedIQ query) {
		synchronized (listenersLock) {
			for(final XMPPContactListener l : listeners) {
				l.handleInvitationAccepted(query.acceptedBy, query.acceptedOn);
			}
		}
	}

	/**
	 * Notify the listeners that an invitation has been declined.
	 * 
	 * @param declination.
	 *            The invitation declination.
	 */
	private void notifyInvitationDeclined(final HandleInvitationDeclinedIQ query) {
		synchronized (listenersLock) {
			for(final XMPPContactListener l : listeners) {
				l.handleInvitationDeclined(query.invitedAs, query.declinedBy,
                        query.declinedOn);
			}
		}
	}

    /**
	 * Notify the listeners that an invitation has been received.
	 * 
	 * @param invitation
	 *            The invitation.
	 */
	private void notifyInvitationExtended(final HandleInvitationExtendedIQ query) {
		synchronized (listenersLock) {
			for(final XMPPContactListener l : listeners) {
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
        private String invitedAs;
    }

    /**
     * <b>Title:</b>thinkParity XMPP Handle Invitation Extended Query<br>
     * <b>Description:</b>Provides a wrapper fr the invitation extended remote
     * event.<br>
     */
    private static class HandleInvitationExtendedIQ extends AbstractThinkParityIQ {

        /** The invitation e-mail address. */
        private String invitedAs;

        /** The inviter. */
        private JabberId invitedBy;

        /** The invitation date. */
        private Calendar invitedOn;
    }

}

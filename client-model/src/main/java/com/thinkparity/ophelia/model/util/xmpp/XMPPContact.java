/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;

import com.thinkparity.ophelia.model.Constants.Xml;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.util.xmpp.event.ContactListener;
import com.thinkparity.ophelia.model.util.xmpp.event.XMPPEventHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.10
 */
final class XMPPContact extends AbstractXMPP<ContactListener> {

	/**
	 * Create XMPPContact.
	 * 
	 */
	XMPPContact(final XMPPCore xmppCore) {
		super(xmppCore);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#addListener(com.thinkparity.codebase.event.EventListener)
     *
     */
    @Override
    protected boolean addListener(final ContactListener listener) {
        return super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#addEventHandlers()
     *
     */
    @Override
    protected void registerEventHandlers() {
        registerEventHandler(ContactInvitationExtendedEvent.class,
                new XMPPEventHandler<ContactInvitationExtendedEvent>() {
                    public void handleEvent(
                            final ContactInvitationExtendedEvent query) {
                        handleInvitationExtended(query);
                    }});
        registerEventHandler(ContactInvitationAcceptedEvent.class,
                new XMPPEventHandler<ContactInvitationAcceptedEvent>() {
                    public void handleEvent(
                            final ContactInvitationAcceptedEvent query) {
                        handleInvitationAccepted(query);
                    }});
        registerEventHandler(ContactInvitationDeclinedEvent.class,
                new XMPPEventHandler<ContactInvitationDeclinedEvent>() {
                    public void handleEvent(
                            final ContactInvitationDeclinedEvent query) {
                        handleInvitationDeclined(query);
                    }});
        registerEventHandler(ContactDeletedEvent.class,
                new XMPPEventHandler<ContactDeletedEvent>() {
                    public void handleEvent(final ContactDeletedEvent query) {
                        handleContactDeleted(query);
                    }});
        registerEventHandler(ContactInvitationDeletedEvent.class,
                new XMPPEventHandler<ContactInvitationDeletedEvent>() {
                    public void handleEvent(
                            final ContactInvitationDeletedEvent query) {
                        handleInvitationDeleted(query);
                    }});
        registerEventHandler(ContactUpdatedEvent.class,
                new XMPPEventHandler<ContactUpdatedEvent>() {
                    public void handleEvent(final ContactUpdatedEvent query) {
                        handleContactUpdated(query);
                    }});
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#removeListener(com.thinkparity.codebase.event.EventListener)
     *
     */
    @Override
    protected boolean removeListener(final ContactListener listener) {
        return super.removeListener(listener);
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
		logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("invitedBy", invitedBy);
        logger.logVariable("acceptedOn", acceptedOn);
		final XMPPMethod accept = new XMPPMethod("contact:acceptinvitation");
        accept.setParameter("userId", userId);
        accept.setParameter("invitedBy", invitedBy);
        accept.setParameter("acceptedOn", acceptedOn);
		execute(accept);
	}

	/**
     * Decline a contact invitation.
     * 
     * @param invitedBy
     *            The user who extended the invitation.
     * @throws SmackException
     */
	void decline(final EMail invitedAs, final JabberId invitedBy) {
		final XMPPMethod decline = new XMPPMethod("contact:declineinvitation");
        decline.setParameter(Xml.Contact.INVITED_AS, invitedAs);
        decline.setParameter(Xml.Contact.INVITED_BY, invitedBy);
        decline.setParameter(Xml.Contact.DECLINED_BY, xmppCore.getUserId());
        execute(decline);
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
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("invitedAs", invitedAs);
        logger.logVariable("deletedOn", deletedOn);
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
		logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("extendedTo", extendedTo);
        logger.logVariable("extendedOn", extendedOn);
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
		logger.logApiId();
        logger.logVariable("userId", userId);
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
        logger.logApiId();
        logger.logVariable("userId", userId);
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
        contact.addAllEmails(response.readResultEMails("emails"));
        return contact;
    }

    /**
     * Fire a local event for the remote contact deleted event.
     * 
     * @param query
     *            The internet event query.
     */
    private void handleContactDeleted(final ContactDeletedEvent event) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.handleDeleted(event);
            }
        });
    }

    /**
     * Fire a local event for the remote contact updated event.
     * 
     * @param query
     *            The internet event query.
     */
    private void handleContactUpdated(final ContactUpdatedEvent event) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.handleUpdated(event);
            }
        });
    }

	/**
     * Fire a local event for the remote invitation accepted event.
     * 
     * @param query
     *            The internet event query.
     */
    private void handleInvitationAccepted(
            final ContactInvitationAcceptedEvent event) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.handleInvitationAccepted(event);
            }
        });
    }

    /**
     * Fire a local event for the remote invitation declined event.
     * 
     * @param query
     *            The internet event query.
     */
    private void handleInvitationDeclined(
            final ContactInvitationDeclinedEvent event) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.handleInvitationDeclined(event);
            }
        });
    }

    /**
     * Fire a local event for the remote invitation deleted event.
     * 
     * @param query
     *            The internet event query.
     */
    private void handleInvitationDeleted(
            final ContactInvitationDeletedEvent event) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.handleInvitationDeleted(event);
            }
        });
    }

    /**
     * Fire a local event for the remote invitation extended event.
     * 
     * @param query
     *            The internet event query.
     */
	private void handleInvitationExtended(
            final ContactInvitationExtendedEvent event) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
				listener.handleInvitationExtended(event);
			}
		});
	}
}

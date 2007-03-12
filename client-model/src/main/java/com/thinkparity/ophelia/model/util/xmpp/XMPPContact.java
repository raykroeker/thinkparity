/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;

import com.thinkparity.ophelia.model.Constants.Xml;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.xmpp.event.ContactListener;

/**
 * <b>Title:</b>thinkParity OpheliaModel XMPP Contact Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.17
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
     * Accept the contact invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedBy
     *            The invited by user id <code>JabberId</code>.
     * @param acceptedOn
     *            When the user accepted <code>Calendar</code>.
     */
	void acceptIncomingInvitation(final JabberId userId,
            final IncomingInvitation invitation, final Calendar acceptedOn) {
		final XMPPMethod accept = new XMPPMethod("contact:acceptincominginvitation");
        accept.setParameter("userId", userId);
        accept.setParameter("invitation", invitation);
        accept.setParameter("acceptedOn", acceptedOn);
		execute(accept);
	}

    /**
     * Create an outgoing e-mail invitation.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    void createOutgoingEMailInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation) {
        final XMPPMethod extendInvitation = new XMPPMethod("contact:createoutgoingemailinvitation");
        extendInvitation.setParameter("userId", userId);
        extendInvitation.setParameter("invitation", invitation);
        execute(extendInvitation);
    }

    /**
     * Create an outgoing user invitation.
     * 
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    void createOutgoingUserInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation) {
        final XMPPMethod extendInvitation = new XMPPMethod("contact:createoutgoinguserinvitation");
        extendInvitation.setParameter("userId", userId);
        extendInvitation.setParameter("invitation", invitation);
        execute(extendInvitation);
    }

    /**
     * Decline an incoming invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            An <code>IncomingInvitation</code>.
     * @param declinedOn
     *            A declined on <code>Calendar</code>.
     */
    void declineIncomingInvitation(final JabberId userId,
            final IncomingInvitation invitation, final Calendar declinedOn) {
        final XMPPMethod decline = new XMPPMethod("contact:declineincominginvitation");
        decline.setParameter("userId", userId);
        decline.setParameter("invitation", invitation);
        decline.setParameter("declinedOn", declinedOn);
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
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    void deleteOutgoingEMailInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation, final Calendar deletedOn) {
        final XMPPMethod deleteInvitation = new XMPPMethod("contact:deleteoutgoingemailinvitation");
        deleteInvitation.setParameter("userId", userId);
        deleteInvitation.setParameter("invitation", invitation);
        deleteInvitation.setParameter("deletedOn", deletedOn);
        execute(deleteInvitation);
    }

    /**
     * Delete a contact invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    void deleteOutgoingUserInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation, final Calendar deletedOn) {
        final XMPPMethod deleteInvitation = new XMPPMethod("contact:deleteoutgoinguserinvitation");
        deleteInvitation.setParameter("userId", userId);
        deleteInvitation.setParameter("invitation", invitation);
        deleteInvitation.setParameter("deletedOn", deletedOn);
        execute(deleteInvitation);
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
        contact.setVCard(response.readResultContactVCard("vcard"));
        contact.setId(response.readResultJabberId(Xml.Contact.JABBER_ID));
        contact.setName(response.readResultString(Xml.Contact.NAME));
        contact.setOrganization(response.readResultString(Xml.Contact.ORGANIZATION));
        contact.setTitle(response.readResultString("title"));
        contact.addAllEmails(response.readResultEMails("emails"));
        return contact;
    }

    List<JabberId> readIds(final JabberId userId) {
        final XMPPMethod readIds = new XMPPMethod("contact:readids");
        readIds.setParameter("userId", userId);
        return execute(readIds, Boolean.TRUE).readResultJabberIds("contactIds");
    }
}

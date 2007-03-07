/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.ophelia.model.Constants.Xml;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.xmpp.event.ContactListener;

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

	// TODO-javadoc XMPPContact#declineEMailInvitation
    void declineEMailInvitation(final JabberId userId, final EMail invitedAs,
            final JabberId invitedBy, final Calendar declinedOn) {
        final XMPPMethod decline = new XMPPMethod("contact:declineemailinvitation");
        decline.setParameter("userId", userId);
        decline.setParameter("invitedAs", invitedAs);
        decline.setParameter("invitedBy", invitedBy);
        decline.setParameter("declinedOn", declinedOn);
        execute(decline);
    }

    // TODO-javadoc XMPPContact#declineUserInvitation
    void declineUserInvitation(final JabberId userId, final JabberId invitedBy,
            final Calendar declinedOn) {
        final XMPPMethod decline = new XMPPMethod("contact:declineuserinvitation");
        decline.setParameter("userId", userId);
        decline.setParameter("invitedBy", invitedBy);
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
     * Delete a contact e-mail invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedAs
     *            The invitation <code>EMail</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    void deleteEMailInvitation(final JabberId userId, final EMail invitedAs,
            final Calendar deletedOn) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("invitedAs", invitedAs);
        logger.logVariable("deletedOn", deletedOn);
        final XMPPMethod deleteInvitation = new XMPPMethod("contact:deleteemailinvitation");
        deleteInvitation.setParameter("userId", userId);
        deleteInvitation.setParameter("invitedAs", invitedAs);
        deleteInvitation.setParameter("deletedOn", deletedOn);
        execute(deleteInvitation);
    }

    /**
     * Delete a contact user invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedAs
     *            The invitation <code>EMail</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    void deleteUserInvitation(final JabberId userId, final JabberId invitedAs,
            final Calendar deletedOn) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("invitedAs", invitedAs);
        logger.logVariable("deletedOn", deletedOn);
        final XMPPMethod deleteInvitation = new XMPPMethod("contact:deleteuserinvitation");
        deleteInvitation.setParameter("userId", userId);
        deleteInvitation.setParameter("invitedAs", invitedAs);
        deleteInvitation.setParameter("deletedOn", deletedOn);
        execute(deleteInvitation);
    }

    /**
     * Extend an e-mail invitation. If the email is registered within the
     * thinkParity community an invitation will be sent via thinkParity
     * otherwise an invitation will be sent via and email.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param extendTo
     *            A <code>EMail</code> to invite.
     * @param extendedOn
     *            The date <code>Calendar</code> of the invitation.
     */
    void extendEMailInvitation(final JabberId userId, final EMail extendTo,
            final Calendar extendedOn) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("extendTo", extendTo);
        logger.logVariable("extendedOn", extendedOn);
        final XMPPMethod extendInvitation = new XMPPMethod("contact:extendemailinvitation");
        extendInvitation.setParameter("userId", userId);
        extendInvitation.setParameter("extendTo", extendTo);
        extendInvitation.setParameter("extendedOn", extendedOn);
        execute(extendInvitation);
    }

    /**
     * Extend a user invitation. 
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param extendTo
     *            A user id <code>JabberId</code> to invite.
     * @param extendedOn
     *            The date <code>Calendar</code> of the invitation.
     */
    void extendUserInvitation(final JabberId userId, final JabberId extendTo,
            final Calendar extendedOn) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("extendTo", extendTo);
        logger.logVariable("extendedOn", extendedOn);
        final XMPPMethod extendInvitation = new XMPPMethod("contact:extenduserinvitation");
        extendInvitation.setParameter("userId", userId);
        extendInvitation.setParameter("extendTo", extendTo);
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

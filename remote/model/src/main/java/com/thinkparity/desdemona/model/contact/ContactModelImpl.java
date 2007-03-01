/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.desdemona.model.contact;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.ContactVCard;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationExtendedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.ContactSql;
import com.thinkparity.desdemona.model.io.sql.InvitationSql;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.util.smtp.MessageFactory;
import com.thinkparity.desdemona.util.smtp.TransportManager;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.10
 */
class ContactModelImpl extends AbstractModelImpl {

    /** The thinkParity contact io. */
	private final ContactSql contactSql;

    /** The thinkParity invitation io. */
	private final InvitationSql invitationSql;

    /** The thinkParity user io. */
    private final UserSql userSql;

    /**
	 * Create ContactModelImpl.
	 * 
     * @param session
     *            The user's session.
	 */
	ContactModelImpl(final Session session) {
		super(session);
		this.contactSql = new ContactSql();
		this.invitationSql = new InvitationSql();
        this.userSql = new UserSql();
	}

    /**
     * Accept the invitation. Create the contact relationships and notify the
     * invitor.
     * 
     * @param userId
     *            The user id <code>JabberId</code>.
     * @param invitedAs
     *            The original invitation e-mail address.
     * @param invitedBy
     *            The invitor.
     * @param acceptedBy
     *            The invitee.
     * @param acceptedOn
     *            When the acceptance was made.
     */
    void acceptInvitation(final JabberId userId, final JabberId invitedBy,
            final Calendar acceptedOn) {
		logApiId();
		logVariable("userId", userId);
        logVariable("invitedBy", invitedBy);
        logVariable("acceptedOn", acceptedOn);
		try {
            assertIsAuthenticatedUser(userId);
            // delete the invitation
		    invitationSql.delete(invitedBy, userId);
            // check if the inverse invitation exists and delete it as well
            final Invitation invitation = invitationSql.read(userId, invitedBy);
            if (null != invitation)
                invitationSql.delete(userId, invitedBy);
		    // create the contact relationships
            contactSql.create(userId, invitedBy, userId, acceptedOn);
			contactSql.create(invitedBy, userId, userId, acceptedOn);

            final ContactInvitationAcceptedEvent invitationAccepted = new ContactInvitationAcceptedEvent();
            invitationAccepted.setAcceptedBy(userId);
            invitationAccepted.setAcceptedOn(acceptedOn);
            enqueueEvent(userId, invitedBy, invitationAccepted);
		}
		catch(final Throwable t) {
            throw translateError(t);
		}
	}

    /**
     * Decline an invitation. Delete the invitation and send a notification
     * (which will delete that invitation).
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedBy
     *            Who invited the user.
     * @param invitedAs
     *            To which <code>EMail</code> the invitation was sent.
     * @param declinedOn
     *            When the invitation was declined.
     */
	void declineInvitation(final JabberId userId, final JabberId invitedBy,
            final EMail invitedAs, final Calendar declinedOn) {
        logApiId();
        logVariable("userId", userId);
        logVariable("invitedBy", invitedBy);
        logVariable("invitedAs", invitedAs);
        logVariable("declinedOn", declinedOn);
        try {
            // delete the invitation
            invitationSql.delete(invitedBy, userId);
            // send notification
            final ContactInvitationDeclinedEvent invitationDeclined = new ContactInvitationDeclinedEvent();
            invitationDeclined.setDeclinedBy(userId);
            invitationDeclined.setDeclinedOn(declinedOn);
            invitationDeclined.setInvitedAs(invitedAs);
            enqueueEvent(userId, invitedBy, invitationDeclined);
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}

    /**
     * Delete a contact for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    void delete(final JabberId userId, final JabberId contactId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("contactId", contactId);
        try {
            assertIsAuthenticatedUser(userId);
            contactSql.delete(userId, contactId);
            contactSql.delete(contactId, userId);

            final ContactDeletedEvent deleted = new ContactDeletedEvent();
            deleted.setDeletedBy(userId);
            deleted.setDeletedOn(currentDateTime());
            enqueueEvent(userId, contactId, deleted);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Delete a contact invitation.
     * 
     * @param userId
     *            A user id.
     * @param invitedAs
     *            The <code>EMail</code> the invitation was sent to.
     * @param deletedOn
     *            The deletion date\time <code>Calendar<code>.
     */
	void deleteInvitation(final JabberId userId, final EMail invitedAs,
            final Calendar deletedOn) {
        logApiId();
        logVariable("userId", userId);
        logVariable("invitedAs", invitedAs);
        logVariable("deletedOn", deletedOn);
		try {
		    final UserModel userModel = getUserModel();
            final User invitedAsUser = userModel.read(invitedAs);
            if (null == invitedAsUser) {
                // delete remote data
                invitationSql.deleteEmail(userId, invitedAs);
            } else {
                // delete remote data
                invitationSql.delete(userId, invitedAsUser.getId());
                // send notification
                final ContactInvitationDeletedEvent invitationDeleted = new ContactInvitationDeletedEvent();
                invitationDeleted.setInvitedAs(invitedAs);
                invitationDeleted.setDeletedBy(userId);
                invitationDeleted.setDeletedOn(deletedOn);
                enqueueEvent(userId, invitedAsUser.getId(), invitationDeleted);
            }
		} catch(final Throwable t) {
			throw translateError(t);
		}
	}

	/**
     * Extend an invitation for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param extendedTo
     *            An <code>EMail</code> to extend the invitation to.
     * @param extendedOn
     *            The date <code>Calendar</code>.
     */
	void extendInvitation(final JabberId userId, final EMail extendTo,
            final Calendar extendedOn) {
        logApiId();
        logVariable("userId", userId);
        logVariable("extendTo", extendTo);
        logVariable("extendedOn", extendedOn);
        try {
            final UserModel userModel = getUserModel();
            final User extendToUser = userModel.read(extendTo);
            if (null == extendToUser) {
                // create remote data
                invitationSql.createEmail(userId, extendTo, extendedOn);
                // extend the invitation via SMTP
                final MimeMessage mimeMessage = MessageFactory.createMimeMessage();
                createInvitation(mimeMessage, extendTo, userModel.read(userId));
                addRecipient(mimeMessage, extendTo);
                TransportManager.deliver(mimeMessage);
            } else {
                // create remote data
                invitationSql.create(userId, extendToUser.getId(), extendedOn);
                // extend the invitation within thinkParity
                final ContactInvitationExtendedEvent invitationExtended = new ContactInvitationExtendedEvent();
                invitationExtended.setInvitedAs(extendTo);
                invitationExtended.setInvitedBy(userId);
                invitationExtended.setInvitedOn(extendedOn);
                enqueueEvent(userId, extendToUser.getId(), invitationExtended);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<Contact> read(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        assertIsAuthenticatedUser(userId);
		try {
			final List<JabberId> contactIds = contactSql.readIds(userId);
			final List<Contact> contacts = new LinkedList<Contact>();
			for (final JabberId contactId : contactIds) {
				contacts.add(read(userId, contactId));
			}
			return contacts;
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    Contact read(final JabberId userId, final JabberId contactId) {
        logApiId();
        logVariable("userId", userId);
		logVariable("contactId", contactId);
        assertIsAuthenticatedUser(userId);
		try {
		    final Contact contact = inject(new Contact(), getUserModel().read(contactId));
            contact.setVCard(getUserModel().readVCard(contactId, new ContactVCard()));
            contact.addAllEmails(userSql.readEmails(contactId, Boolean.TRUE));
            return contact;
	    } catch (final Throwable t) {
            throw translateError(t);
        }
	}

    Invitation readInvitation(final JabberId from) {
        logApiId();
        logVariable("from", from);
		try {
            return invitationSql.read(from, session.getJabberId());
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

	private void createInvitation(final MimeMessage mimeMessage,
            final EMail email, final User inviter) throws MessagingException {
        final InvitationText text = new InvitationText(getEnvironment(),
                Locale.getDefault(), email, inviter);
	    mimeMessage.setSubject(text.getSubject());

        final MimeBodyPart invitationBody = new MimeBodyPart();
        invitationBody.setContent(text.getBody(), text.getBodyType());

        final Multipart invitation = new MimeMultipart();
        invitation.addBodyPart(invitationBody);
        mimeMessage.setContent(invitation);
    }
}

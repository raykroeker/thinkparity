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
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationExtendedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.ContactSql;
import com.thinkparity.desdemona.model.io.sql.InvitationSql;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.user.InternalUserModel;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.util.smtp.MessageFactory;
import com.thinkparity.desdemona.util.smtp.TransportManager;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Contact Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
final class ContactModelImpl extends AbstractModelImpl {

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
	void acceptInvitation(final JabberId userId,
            final IncomingEMailInvitation invitation, final Calendar acceptedOn) {
		try {
            assertIsAuthenticatedUser(userId);
            final InternalUserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getExtendedBy().getId());

            // delete all invitations
            deleteAllInvitations(userId, user, invitationUser);

            // create contact
            contactSql.create(user, invitationUser, user, acceptedOn);
            contactSql.create(invitationUser, user, user, acceptedOn);

            // fire event
            final ContactInvitationAcceptedEvent event = new ContactInvitationAcceptedEvent();
            event.setAcceptedBy(user.getId());
            event.setAcceptedOn(acceptedOn);
            event.setDate(event.getAcceptedOn());
            enqueueEvent(user.getId(), invitationUser.getId(), event);
		}
		catch(final Throwable t) {
            throw translateError(t);
		}
	}

    void acceptInvitation(final JabberId userId,
            final IncomingUserInvitation invitation, final Calendar acceptedOn) {
        try {
            assertIsAuthenticatedUser(userId);
            final InternalUserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User extendedByUser = userModel.read(
                    invitation.getExtendedBy().getId());

            // delete incoming/outgoing/user/e-mail
            deleteAllInvitations(userId, user, extendedByUser);

            // create contact
            contactSql.create(user, extendedByUser, user, acceptedOn);
            contactSql.create(extendedByUser, user, user, acceptedOn);

            // fire event
            final ContactInvitationAcceptedEvent event = new ContactInvitationAcceptedEvent();
            event.setAcceptedBy(user.getId());
            event.setAcceptedOn(acceptedOn);
            event.setDate(event.getAcceptedOn());
            enqueueEvent(user.getId(), extendedByUser.getId(), event);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    void createInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User createdBy = userModel.read(invitation.getCreatedBy().getId());
            final User user = userModel.read(userId);

            // create outgoing e-mail
            final OutgoingEMailInvitation outgoingEMail = new OutgoingEMailInvitation();
            outgoingEMail.setCreatedBy(createdBy);
            outgoingEMail.setCreatedOn(invitation.getCreatedOn());
            outgoingEMail.setInvitationEMail(invitation.getInvitationEMail());
            invitationSql.create(user, invitation);

            final User invitationUser = userModel.read(invitation.getInvitationEMail());
            if (null == invitationUser) {
                // send e-mail
                final MimeMessage mimeMessage = MessageFactory.createMimeMessage();
                inject(mimeMessage, invitation.getInvitationEMail(), user);
                addRecipient(mimeMessage, invitation.getInvitationEMail());
                TransportManager.deliver(mimeMessage);
            } else {
                // create incoming
                final IncomingEMailInvitation incoming = new IncomingEMailInvitation();
                incoming.setCreatedBy(createdBy);
                incoming.setCreatedOn(invitation.getCreatedOn());
                incoming.setInvitationEMail(invitation.getInvitationEMail());
                incoming.setExtendedBy(user);
                invitationSql.create(invitationUser, incoming);

                // fire event
                final ContactEMailInvitationExtendedEvent event = new ContactEMailInvitationExtendedEvent();
                event.setInvitedAs(incoming.getInvitationEMail());
                event.setInvitedBy(incoming.getExtendedBy().getId());
                event.setInvitedOn(incoming.getCreatedOn());
                enqueueEvent(userId, invitationUser.getId(), event);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Extend an invitation to a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param extendTo
     *            An extend to user id <code>JabberId</code>.
     * @param extendedOn
     *            An extended on <code>Calendar</code>.
     */
    void createInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getInvitationUser().getId());

            // create outgoing user invitation
            final OutgoingUserInvitation outgoingUser = new OutgoingUserInvitation();
            outgoingUser.setCreatedBy(user);
            outgoingUser.setCreatedOn(invitation.getCreatedOn());
            outgoingUser.setInvitationUser(invitationUser);
            invitationSql.create(user, outgoingUser);

            // create incoming
            final IncomingUserInvitation incomingUser = new IncomingUserInvitation();
            incomingUser.setCreatedBy(user);
            incomingUser.setCreatedOn(invitation.getCreatedOn());
            incomingUser.setExtendedBy(incomingUser.getCreatedBy());
            invitationSql.create(invitationUser, incomingUser);

            // fire event
            final ContactUserInvitationExtendedEvent event = new ContactUserInvitationExtendedEvent();
            event.setDate(invitation.getCreatedOn());
            event.setInvitedBy(incomingUser.getExtendedBy().getId());
            event.setInvitedOn(incomingUser.getCreatedOn());
            enqueueEvent(userId, invitationUser.getId(), event);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Decline an e-mail invitation. Delete the invitation and send a
     * notification (which will delete that invitation).
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
    void declineInvitation(final JabberId userId,
            final IncomingEMailInvitation invitation, final Calendar declinedOn) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getExtendedBy().getId());

            // delete incoming e-mail invitation
            final IncomingEMailInvitation incomingEMailInvitation =
                invitationSql.readIncomingEMail(user,
                        invitation.getInvitationEMail(), invitationUser);
            invitationSql.delete(incomingEMailInvitation);

            // delete outgoing e-mail invitation
            final OutgoingEMailInvitation outgoingEMailInvitation =
                invitationSql.readOutgoingEMail(invitationUser,
                        invitation.getInvitationEMail());
            invitationSql.delete(outgoingEMailInvitation);

            // fire event
            final ContactEMailInvitationDeclinedEvent event = new ContactEMailInvitationDeclinedEvent();
            event.setDate(declinedOn);
            event.setDeclinedBy(user.getId());
            event.setDeclinedOn(event.getDate());
            event.setInvitedAs(invitation.getInvitationEMail());
            enqueueEvent(user.getId(), invitationUser.getId(), event);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void declineInvitation(final JabberId userId,
            final IncomingUserInvitation invitation, final Calendar declinedOn) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getExtendedBy().getId());

            // delete incoming user invitation
            final IncomingUserInvitation incomingUserInvitation =
                invitationSql.readIncomingUser(user, invitationUser);
            invitationSql.delete(incomingUserInvitation);

            // delete outgoing user invitation
            final OutgoingUserInvitation outgoingUserInvitation =
                invitationSql.readOutgoingUser(invitationUser, user);
            invitationSql.delete(outgoingUserInvitation);

            // fire event
            final ContactUserInvitationDeclinedEvent event = new ContactUserInvitationDeclinedEvent();
            event.setDate(declinedOn);
            event.setDeclinedBy(user.getId());
            event.setDeclinedOn(event.getDate());
            enqueueEvent(user.getId(), invitationUser.getId(), event);
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
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User contact = userModel.read(contactId);

            // delete contact
            contactSql.delete(user.getLocalId(), contact.getLocalId());

            // fire event
            final ContactDeletedEvent deleted = new ContactDeletedEvent();
            deleted.setDeletedBy(userId);
            deleted.setDeletedOn(currentDateTime());
            enqueueEvent(userId, contactId, deleted);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a contact e-mail invitation.
     * 
     * @param userId
     *            A user id.
     * @param invitedAs
     *            The <code>EMail</code> the invitation was sent to.
     * @param deletedOn
     *            The deletion date\time <code>Calendar<code>.
     */
    void deleteInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation, final Calendar deletedOn) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getInvitationEMail());

            // delete incoming e-mail invitation
            final IncomingEMailInvitation incomingEMailInvitation =
                invitationSql.readIncomingEMail(invitationUser,
                        invitation.getInvitationEMail(), user);
            invitationSql.delete(incomingEMailInvitation);

            // delete outgoing e-mail invitation
            final OutgoingEMailInvitation outgoingEMailInvitation =
                invitationSql.readOutgoingEMail(user,
                        invitation.getInvitationEMail());
            invitationSql.delete(outgoingEMailInvitation);

            // fire event
            final ContactEMailInvitationDeletedEvent event = new ContactEMailInvitationDeletedEvent();
            event.setDate(deletedOn);
            event.setDeletedBy(user.getId());
            event.setDeletedOn(event.getDate());
            event.setInvitedAs(invitation.getInvitationEMail());
            enqueueEvent(user.getId(), invitationUser.getId(), event);
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a contact e-mail invitation.
     * 
     * @param userId
     *            A user id.
     * @param invitedAs
     *            The <code>EMail</code> the invitation was sent to.
     * @param deletedOn
     *            The deletion date\time <code>Calendar<code>.
     */
    void deleteOutgoingUserInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation, final Calendar deletedOn) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getInvitationUser().getId());

            // delete incoming user invitation
            final IncomingUserInvitation incomingUserInvitation =
                invitationSql.readIncomingUser(invitationUser, user);
            invitationSql.delete(incomingUserInvitation);

            // delete outgoing user invitation
            final OutgoingUserInvitation outgoingUserInvitation =
                invitationSql.readOutgoingUser(user, invitationUser);
            invitationSql.delete(outgoingUserInvitation);

            // fire event
            final ContactUserInvitationDeletedEvent event = new ContactUserInvitationDeletedEvent();
            event.setDate(deletedOn);
            event.setDeletedBy(user.getId());
            event.setDeletedOn(event.getDate());
            enqueueEvent(user.getId(), invitationUser.getId(), event);
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    List<Contact> read(final JabberId userId) {
		try {
		    assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

			final List<JabberId> contactIds = contactSql.readIds(user.getLocalId());
			final List<Contact> contacts = new LinkedList<Contact>();
			for (final JabberId contactId : contactIds) {
				contacts.add(read(userId, contactId));
			}
			return contacts;
		} catch (final Throwable t) {
			throw panic(t);
		}
	}

    Contact read(final JabberId userId, final JabberId contactId) {
		try {
		    assertIsAuthenticatedUser(userId);

		    final Contact contact = inject(new Contact(), getUserModel().read(contactId));
            contact.setVCard(getUserModel().readVCard(contactId, new ContactVCard()));
            contact.addAllEmails(userSql.readEmails(contact.getLocalId(), Boolean.TRUE));
            return contact;
	    } catch (final Throwable t) {
            throw panic(t);
        }
	}

	List<IncomingEMailInvitation> readIncomingEMailInvitations(
            final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return invitationSql.readIncomingEMail(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    List<IncomingUserInvitation> readIncomingUserInvitations(
            final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return invitationSql.readIncomingUser(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return invitationSql.readOutgoingEMail(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    List<OutgoingUserInvitation> readOutgoingUserInvitations(
            final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return invitationSql.readOutgoingUser(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Delete all incoming and outgoing user and e-mail invitations between two
     * users.
     * 
     * @param userId
     *            The current user id <code>JabberId</code>.
     * @param user
     *            A <code>User</code>.
     * @param invitationUser
     *            A <code>User</code>.
     */
    private void deleteAllInvitations(final JabberId userId, final User user,
            final User invitationUser) {
        final InternalUserModel userModel = getUserModel();
        final List<EMail> userEMails = userModel.readEMails(userId,
                user.getLocalId());
        final List<EMail> invitationUserEMails = userModel.readEMails(userId,
                invitationUser.getLocalId());

        // delete incoming e-mail invitations
        IncomingEMailInvitation incomingEMailInvitation;
        for (final EMail email : userEMails) {
            incomingEMailInvitation = invitationSql.readIncomingEMail(user,
                    email, invitationUser);
            if (null != incomingEMailInvitation)
                invitationSql.delete(incomingEMailInvitation);
        }
        for (final EMail email : invitationUserEMails) {
            incomingEMailInvitation = invitationSql.readIncomingEMail(
                    invitationUser, email, user);
            if (null != incomingEMailInvitation)
                invitationSql.delete(incomingEMailInvitation);
        }

        // delete outgoing e-mail invitations
        OutgoingEMailInvitation outgoingEMailInvitation;
        for (final EMail email : invitationUserEMails) {
            outgoingEMailInvitation = invitationSql.readOutgoingEMail(user,
                    email);
            if (null != outgoingEMailInvitation)
                invitationSql.delete(outgoingEMailInvitation);
        }
        for (final EMail email : userEMails) {
            outgoingEMailInvitation = invitationSql.readOutgoingEMail(
                    invitationUser, email);
            if (null != outgoingEMailInvitation)
                invitationSql.delete(outgoingEMailInvitation);
        }

        // delete incoming user invitations
        IncomingUserInvitation incomingUserInvitation;
        incomingUserInvitation = invitationSql.readIncomingUser(user,
                invitationUser);
        if (null != incomingUserInvitation)
            invitationSql.delete(incomingUserInvitation);
        incomingUserInvitation = invitationSql.readIncomingUser(
                invitationUser, user);
        if (null != incomingUserInvitation)
            invitationSql.delete(incomingUserInvitation);

        // delete outgoing user invitations
        OutgoingUserInvitation outgoingUserInvitation;
        outgoingUserInvitation = invitationSql.readOutgoingUser(user,
                invitationUser);
        if (null != outgoingUserInvitation)
            invitationSql.delete(outgoingUserInvitation);
        outgoingUserInvitation = invitationSql.readOutgoingUser(
                invitationUser, user);
        if (null != outgoingUserInvitation)
            invitationSql.delete(outgoingUserInvitation);
    }

    /**
     * Inject invitation text into the e-mail mime message.
     * 
     * @param mimeMessage
     *            A <code>MimeMessage</code>.
     * @param email
     *            The <code>EMail</code>.
     * @param invitedBy
     *            The invited by <code>User</code>.
     * @throws MessagingException
     */
	private void inject(final MimeMessage mimeMessage, final EMail email,
            final User invitedBy) throws MessagingException {
        final InvitationText text = new InvitationText(getEnvironment(),
                Locale.getDefault(), email, invitedBy);
	    mimeMessage.setSubject(text.getSubject());

        final MimeBodyPart invitationBody = new MimeBodyPart();
        invitationBody.setContent(text.getBody(), text.getBodyType());

        final Multipart invitation = new MimeMultipart();
        invitation.addBodyPart(invitationBody);
        mimeMessage.setContent(invitation);
    }
}

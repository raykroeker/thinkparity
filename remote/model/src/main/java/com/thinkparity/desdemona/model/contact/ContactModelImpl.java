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
import com.thinkparity.codebase.model.contact.IncomingInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingInvitation;
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
	void acceptIncomingInvitation(final JabberId userId,
            final IncomingInvitation invitation, final Calendar acceptedOn) {
		try {
            assertIsAuthenticatedUser(userId);
            final InternalUserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getInvitedBy().getId());
            // delete incoming
            invitationSql.deleteIncoming(user.getLocalId(), invitationUser.getLocalId());
            // delete outgoing e-mail
            invitationSql.deleteOutgoingEMail(user.getLocalId(), invitationUser.getLocalId());
            // delete outgoing user
            invitationSql.deleteOutgoingUser(user.getLocalId(), invitationUser.getLocalId());
            // create contact
            contactSql.create(user.getLocalId(), invitationUser.getLocalId(),
                    user.getLocalId(), acceptedOn);
            contactSql.create(invitationUser.getLocalId(), user.getLocalId(),
                    user.getLocalId(), acceptedOn);
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

    /**
     * Extend an invitation to an e-mail address. If the e-mail address is
     * known; then a standard invitation is created and sent; otherwise an
     * e-mail is sent.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param extendedTo
     *            An <code>EMail</code> to extend the invitation to.
     * @param extendedOn
     *            The date <code>Calendar</code>.
     */
    void createOutgoingEMailInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            // create outgoing
            invitation.setCreatedBy(user.getLocalId());
            invitationSql.createOutgoingEMail(invitation);
            final User invitationUser = userModel.read(invitation.getEmail());
            if (null == invitationUser) {
                // send e-mail
                final MimeMessage mimeMessage = MessageFactory.createMimeMessage();
                inject(mimeMessage, invitation.getEmail(), user);
                addRecipient(mimeMessage, invitation.getEmail());
                TransportManager.deliver(mimeMessage);
            } else {
                // create incoming
                final IncomingInvitation incoming = new IncomingInvitation();
                incoming.setCreatedBy(user.getLocalId());
                incoming.setCreatedOn(invitation.getCreatedOn());
                incoming.setInvitedAs(invitation.getEmail());
                incoming.setInvitedBy(user);
                invitationSql.createIncoming(invitationUser.getLocalId(), incoming);
                // fire event
                final ContactEMailInvitationExtendedEvent event = new ContactEMailInvitationExtendedEvent();
                event.setInvitedAs(incoming.getInvitedAs());
                event.setInvitedBy(incoming.getInvitedBy().getId());
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
    void createOutgoingUserInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getUser().getId());
            // create outgoing user
            final OutgoingUserInvitation outgoingUser = new OutgoingUserInvitation();
            outgoingUser.setCreatedBy(user.getLocalId());
            outgoingUser.setCreatedOn(invitation.getCreatedOn());
            outgoingUser.setUser(invitationUser);
            invitationSql.createOutgoingUser(outgoingUser);
            // create incoming
            final IncomingInvitation incoming = new IncomingInvitation();
            incoming.setCreatedBy(user.getLocalId());
            incoming.setCreatedOn(invitation.getCreatedOn());
            incoming.setInvitedBy(user);
            invitationSql.createIncoming(invitationUser.getLocalId(), incoming);
            // fire event
            final ContactUserInvitationExtendedEvent event = new ContactUserInvitationExtendedEvent();
            event.setDate(invitation.getCreatedOn());
            event.setInvitedBy(incoming.getInvitedBy().getId());
            event.setInvitedOn(incoming.getCreatedOn());
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
    void declineIncomingInvitation(final JabberId userId,
            final IncomingInvitation invitation, final Calendar declinedOn) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getInvitedBy().getId());
            // delete incoming invitation
            final IncomingInvitation incoming;
            if (invitation.isSetInvitedAs()) {
                incoming = invitationSql.readIncoming(user.getLocalId(),
                        invitationUser.getLocalId(), invitation.getInvitedAs());
            } else {
                incoming = invitationSql.readIncoming(user.getLocalId(),
                        invitationUser.getLocalId());
            }
            invitationSql.deleteIncoming(incoming.getId());
            final OutgoingInvitation outgoing;
            if (invitation.isSetInvitedAs()) {
                // delete outgoing e-mail invitation
                outgoing = invitationSql.readOutgoingEMail(
                        invitationUser.getLocalId(), invitation.getInvitedAs());
                invitationSql.deleteOutgoingEMail(outgoing.getId());
                // fire event
                final ContactEMailInvitationDeclinedEvent event = new ContactEMailInvitationDeclinedEvent();
                event.setDate(declinedOn);
                event.setDeclinedBy(user.getId());
                event.setDeclinedOn(event.getDate());
                event.setInvitedAs(invitation.getInvitedAs());
                enqueueEvent(user.getId(), invitationUser.getId(), event);
            } else {
                // delete outgoing user invitation
                outgoing = invitationSql.readOutgoingUser(
                        invitationUser.getLocalId(), user.getLocalId());
                invitationSql.deleteOutgoingUser(outgoing.getId());
                // fire event
                final ContactUserInvitationDeclinedEvent event = new ContactUserInvitationDeclinedEvent();
                event.setDate(declinedOn);
                event.setDeclinedBy(user.getId());
                event.setDeclinedOn(event.getDate());
                enqueueEvent(user.getId(), invitationUser.getId(), event);
            }
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
    void deleteOutgoingEMailInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation, final Calendar deletedOn) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getEmail());
            // delete the incoming invitation
            final IncomingInvitation incoming = invitationSql.readIncoming(
                    invitationUser.getLocalId(), user.getLocalId(),
                    invitation.getEmail());
            invitationSql.deleteIncoming(incoming.getId());
            // delete the outgoing invitation
            final OutgoingEMailInvitation outgoing =
                invitationSql.readOutgoingEMail(user.getLocalId(),
                        invitation.getEmail());
            invitationSql.deleteOutgoingEMail(outgoing.getId());
            // fire e-mail invitation deleted event
            final ContactEMailInvitationDeletedEvent event = new ContactEMailInvitationDeletedEvent();
            event.setDate(deletedOn);
            event.setDeletedBy(user.getId());
            event.setDeletedOn(event.getDate());
            event.setInvitedAs(invitation.getEmail());
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
            final User invitationUser = userModel.read(invitation.getUser().getId());
            // delete the incoming invitation
            final IncomingInvitation incoming = invitationSql.readIncoming(
                    invitationUser.getLocalId(), user.getLocalId());
            invitationSql.deleteIncoming(incoming.getId());
            // delete the outgoing invitation
            final OutgoingUserInvitation outgoingUser =
                invitationSql.readOutgoingUser(user.getLocalId(),
                        invitationUser.getLocalId());
            invitationSql.deleteOutgoingUser(outgoingUser.getId());
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

    List<IncomingInvitation> readIncomingInvitations(final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return invitationSql.readIncomingInvitations(user.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return invitationSql.readOutgoingEMailInvitations(user.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    List<OutgoingUserInvitation> readOutgoingUserInvitations(
            final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return invitationSql.readOutgoingUserInvitations(user.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
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

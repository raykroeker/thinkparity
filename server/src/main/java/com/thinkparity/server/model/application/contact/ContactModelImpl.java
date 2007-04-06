/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.desdemona.model.contact;

import java.util.*;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.contact.*;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.*;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.artifact.InternalArtifactModel;
import com.thinkparity.desdemona.model.backup.InternalBackupModel;
import com.thinkparity.desdemona.model.contact.invitation.Attachment;
import com.thinkparity.desdemona.model.container.contact.invitation.ContainerVersionAttachment;
import com.thinkparity.desdemona.model.io.sql.ContactSql;
import com.thinkparity.desdemona.model.io.sql.InvitationSql;
import com.thinkparity.desdemona.model.profile.InternalProfileModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.stream.InternalStreamModel;
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
public final class ContactModelImpl extends AbstractModelImpl implements
        ContactModel, InternalContactModel {

    /** The thinkParity contact io. */
	private ContactSql contactSql;

    /** The thinkParity invitation io. */
	private InvitationSql invitationSql;

    /**
	 * Create ContactModelImpl.
	 * 
	 */
	public ContactModelImpl() {
		super();
	}

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#acceptInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.IncomingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
	public void acceptInvitation(final JabberId userId,
            final IncomingEMailInvitation invitation, final Calendar acceptedOn) {
		try {
            assertIsAuthenticatedUser(userId);
            final InternalUserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getExtendedBy().getId());

            // create contact
            contactSql.create(user, invitationUser, user, acceptedOn);
            contactSql.create(invitationUser, user, user, acceptedOn);

            /* check the outgoing e-mail invitation for any attachments, and
             * send to the invitation user if they exist
             */
            final OutgoingEMailInvitation outgoingEMailInvitation =
                invitationSql.readOutgoingEMail(invitationUser,
                        invitation.getInvitationEMail());
            final List<Attachment> attachments = invitationSql.readAttachments(
                    outgoingEMailInvitation);
            for (final Attachment attachment : attachments) {
                invitationSql.deleteAttachment(attachment);
                send(invitationUser.getId(), userId, invitation, attachment);
            }

            // delete all invitations
            deleteAllInvitations(userId, user, invitationUser);

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
     * @see com.thinkparity.desdemona.model.contact.ContactModel#acceptInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.IncomingUserInvitation,
     *      java.util.Calendar)
     * 
     */
    public void acceptInvitation(final JabberId userId,
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

    /**
     * @see com.thinkparity.desdemona.model.contact.InternalContactModel#create(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.model.user.User, com.thinkparity.codebase.model.user.User)
     *
     */
    public void create(final JabberId userId, final User user,
            final User contact) {
        try {
            final User createdBy = getUserModel().read(userId);
            final Calendar createdOn = currentDateTime();
            // create a contact
            contactSql.create(user, contact, createdBy, createdOn);
            contactSql.create(contact, user, createdBy, createdOn);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.InternalContactModel#createInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.IncomingEMailInvitation)
     * 
     */
    public void createInvitation(final JabberId userId,
            final JabberId invitationUserId, final IncomingEMailInvitation invitation) {
        try {
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            // create invitation
            invitationSql.create(user, invitation);
            // fire event
            final ContactEMailInvitationExtendedEvent event = new ContactEMailInvitationExtendedEvent();
            event.setInvitedAs(invitation.getInvitationEMail());
            event.setInvitedBy(invitation.getExtendedBy().getId());
            event.setInvitedOn(invitation.getCreatedOn());
            enqueueEvent(userId, userId, event);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#createInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.OutgoingEMailInvitation)
     * 
     */
    public void createInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation) {
        try {
            final UserModel userModel = getUserModel();
            final User createdBy = userModel.read(invitation.getCreatedBy().getId());
            final User user = userModel.read(userId);

            // create outgoing e-mail
            invitation.setCreatedBy(createdBy);
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
     * @see com.thinkparity.desdemona.model.contact.ContactModel#createInvitation(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.model.contact.OutgoingUserInvitation)
     *
     */
    public void createInvitation(final JabberId userId,
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
     * @see com.thinkparity.desdemona.model.contact.InternalContactModel#createInvitationAttachment(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.desdemona.model.contact.invitation.Attachment)
     * 
     */
    public void createInvitationAttachment(final JabberId userId,
            final Attachment attachment) {
        try {
            invitationSql.createAttachment(attachment);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#declineInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.IncomingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void declineInvitation(final JabberId userId,
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
            invitationSql.deleteAttachments(outgoingEMailInvitation);
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

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#declineInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.IncomingUserInvitation,
     *      java.util.Calendar)
     * 
     */
    public void declineInvitation(final JabberId userId,
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
     * @see com.thinkparity.desdemona.model.contact.ContactModel#delete(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void delete(final JabberId userId, final JabberId contactId) {
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
     * @see com.thinkparity.desdemona.model.contact.ContactModel#deleteInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.OutgoingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void deleteInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation, final Calendar deletedOn) {
        try {
            assertIsAuthenticatedUser(userId);
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final User invitationUser = userModel.read(invitation.getInvitationEMail());

            // delete incoming e-mail invitation
            final IncomingEMailInvitation incomingEMail =
                invitationSql.readIncomingEMail(invitationUser,
                        invitation.getInvitationEMail(), user);
            invitationSql.delete(incomingEMail);

            final OutgoingEMailInvitation outgoingEMail =
                invitationSql.readOutgoingEMail(user,
                        invitation.getInvitationEMail());
            // delete attachments
            final List<Attachment> attachments =
                invitationSql.readAttachments(outgoingEMail);
            for (final Attachment attachment : attachments)
                invitationSql.deleteAttachment(attachment);
            // delete outgoing e-mail invitation
            invitationSql.delete(outgoingEMail);

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
     * @see com.thinkparity.desdemona.model.contact.ContactModel#deleteOutgoingUserInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.OutgoingUserInvitation,
     *      java.util.Calendar)
     * 
     */
    public void deleteOutgoingUserInvitation(final JabberId userId,
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

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#read(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<Contact> read(final JabberId userId) {
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

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#read(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
	public Contact read(final JabberId userId, final JabberId contactId) {
		try {
		    assertIsAuthenticatedUser(userId);

		    final Contact contact = inject(new Contact(), getUserModel().read(contactId));
            contact.setVCard(getUserModel().readVCard(contact.getLocalId(), new ContactVCard()));
            contact.addAllEmails(getProfileModel().readEMails(userId, contact));
            return contact;
	    } catch (final Throwable t) {
            throw panic(t);
        }
	}

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#readIncomingEMailInvitations(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations(
            final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return invitationSql.readIncomingEMail(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#readIncomingUserInvitations(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations(
            final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return invitationSql.readIncomingUser(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.InternalContactModel#readInvitationAttachments(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.ContactInvitation)
     * 
     */
    public List<Attachment> readInvitationAttachments(final JabberId userId,
            final ContactInvitation invitation) {
        try {
            return invitationSql.readAttachments(invitation);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#readOutgoingEMailInvitations(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return invitationSql.readOutgoingEMail(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.InternalContactModel#readOutgoingEMailInvitations(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.email.EMail)
     * 
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final JabberId userId, final EMail email) {
        try {
            return invitationSql.readOutgoingEMail(email);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#readOutgoingUserInvitations(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations(
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
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initializeModel(com.thinkparity.desdemona.model.session.Session)
     *
     */
    @Override
    protected void initializeModel(final Session session) {
        contactSql = new ContactSql();
        invitationSql = new InvitationSql();
    }

    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    protected final User readUser(final JabberId userId) {
        return getUserModel().read(userId);
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
        final InternalProfileModel profileModel = getProfileModel();
        final List<EMail> userEMails = profileModel.readEMails(userId, user);
        final List<EMail> invitationUserEMails = profileModel.readEMails(userId,
                invitationUser);

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
            if (null != outgoingEMailInvitation) {
                invitationSql.deleteAttachments(outgoingEMailInvitation);
                invitationSql.delete(outgoingEMailInvitation);
            }
        }
        for (final EMail email : userEMails) {
            outgoingEMailInvitation = invitationSql.readOutgoingEMail(
                    invitationUser, email);
            if (null != outgoingEMailInvitation) {
                invitationSql.deleteAttachments(outgoingEMailInvitation);
                invitationSql.delete(outgoingEMailInvitation);
            }
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

    /**
     * Send an invitation attachment to a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @param attachment
     *            A <code>Attachment</code>.
     */
    private void send(final JabberId sendAs, final JabberId sendTo,
            final ContactInvitation invitation, final Attachment attachment) {
        switch (attachment.getReferenceType()) {
        case CONTAINER_VERSION:
            final ContainerVersionAttachment containerVersionAttachment =
                new ContainerVersionAttachment();
            containerVersionAttachment.setReferenceId(attachment.getReferenceId());
            send(sendAs, sendTo, invitation, containerVersionAttachment);
            break;
        default:
            Assert.assertUnreachable("Unknown attachment reference type.");
        }
    }

    /**
     * Send a container version invitation attachment to a user.
     * 
     * @param sendAs
     *            A user id <code>JabberId</code>.
     * @param sendTo
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @param attachment
     *            A <code>ContainerVersionAttachment</code>.
     */
    private void send(final JabberId sendAs, final JabberId sendTo,
            final ContactInvitation invitation,
            final ContainerVersionAttachment attachment) {
        final InternalBackupModel backupModel = getBackupModel();
        // grab from backup
        final Container container = backupModel.readContainer(attachment.getUniqueId());
        final ContainerVersion version = backupModel.readContainerVersion(container.getUniqueId(), attachment.getVersionId());
        final ContainerVersion latestVersion = backupModel.readContainerLatestVersion(container.getUniqueId());
        final List<DocumentVersion> documentVersions = backupModel.readContainerDocumentVersions(container.getUniqueId(), version.getVersionId());
        final List<TeamMember> teamMembers = backupModel.readArtifactTeam(container.getUniqueId());
        // upload documents
        final Map<DocumentVersion, String> documentVersionStreams =
            new HashMap<DocumentVersion, String>(documentVersions.size());
        final InternalStreamModel streamModel = getStreamModel();
        final StreamSession streamSession = streamModel.createArchiveSession(sendTo);
        try {
            String streamId;
            for (final DocumentVersion documentVersion : documentVersions) {
                streamId = streamModel.create(sendTo, streamSession.getId());
                backupModel.uploadDocumentVersion(streamId,
                        documentVersion.getArtifactUniqueId(),
                        documentVersion.getVersionId());
                documentVersionStreams.put(documentVersion, streamId);
            }
        } finally {
            streamModel.deleteSession(sendTo, streamSession.getId());
        }
        final JabberId publishedBy = sendAs;
        final Calendar publishedOn = version.getCreatedOn();
        // publish to a single user
        final List<User> publishedToUsers = new ArrayList<User>();
        publishedToUsers.add(readUser(sendTo));
        // container published
        final ContainerPublishedEvent publishedEvent = new ContainerPublishedEvent();
        publishedEvent.setDocumentVersions(documentVersionStreams);
        publishedEvent.setPublishedBy(publishedBy);
        publishedEvent.setPublishedOn(publishedOn);
        publishedEvent.setPublishedTo(publishedToUsers);
        publishedEvent.setVersion(version);
        enqueueEvent(session.getJabberId(), sendTo, publishedEvent);
        // artifact published
        final ArtifactPublishedEvent artifactPublishedEvent = new ArtifactPublishedEvent();
        artifactPublishedEvent.setLatestVersion(version.equals(latestVersion));
        artifactPublishedEvent.setPublishedBy(publishedBy);
        artifactPublishedEvent.setPublishedOn(publishedOn);
        artifactPublishedEvent.setUniqueId(version.getArtifactUniqueId());
        artifactPublishedEvent.setVersionId(version.getVersionId());
        final List<JabberId> teamUserIds = USER_UTIL.getIds(teamMembers,
                new ArrayList<JabberId>(teamMembers.size() + 1));
        teamUserIds.add(sendTo);
        artifactPublishedEvent.setTeamUserIds(teamUserIds);
        enqueueEvent(session.getJabberId(), sendTo, artifactPublishedEvent);
        // add team member
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Artifact artifact = artifactModel.read(version.getArtifactUniqueId());
        getArtifactModel().addTeamMember(session.getJabberId(),
                artifact.getId(), readUser(sendTo).getLocalId());
    }
}

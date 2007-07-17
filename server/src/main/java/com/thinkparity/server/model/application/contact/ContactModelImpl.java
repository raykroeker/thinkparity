/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.desdemona.model.contact;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.*;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.*;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants;
import com.thinkparity.desdemona.model.backup.InternalBackupModel;
import com.thinkparity.desdemona.model.contact.invitation.Attachment;
import com.thinkparity.desdemona.model.contact.invitation.ContainerVersionAttachment;
import com.thinkparity.desdemona.model.io.sql.ContactSql;
import com.thinkparity.desdemona.model.io.sql.InvitationSql;
import com.thinkparity.desdemona.model.profile.InternalProfileModel;
import com.thinkparity.desdemona.model.user.InternalUserModel;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.util.smtp.SMTPService;

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

    /** A default (filters none) invitation attachment filter. */
    private final Filter<Attachment> defaultInvitationAttachmentFilter;

    /** The thinkParity invitation io. */
	private InvitationSql invitationSql;

    /** An instance of <code>SMTPService</code>. */
    private final SMTPService smtpService;

    /**
	 * Create ContactModelImpl.
	 * 
	 */
	public ContactModelImpl() {
		super();
        this.defaultInvitationAttachmentFilter = FilterManager.createDefault();
        this.smtpService = SMTPService.getInstance();
	}

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#acceptInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.IncomingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
	public void acceptInvitation(final IncomingEMailInvitation invitation,
            final Calendar acceptedOn) {
		try {
            final InternalUserModel userModel = getUserModel();
            final User invitationUser = userModel.read(invitation.getExtendedBy().getId());

            // create contact
            contactSql.create(user, invitationUser, user, acceptedOn);
            contactSql.create(invitationUser, user, user, acceptedOn);

            /* check the outgoing e-mail invitation for any attachments, and
             * send to the invitation user if they exist */
            final OutgoingEMailInvitation outgoingEMailInvitation =
                invitationSql.readOutgoingEMail(invitationUser,
                        invitation.getInvitationEMail());
            final List<Attachment> attachments = invitationSql.readAttachments(
                    outgoingEMailInvitation);
            for (final Attachment attachment : attachments) {
                invitationSql.deleteAttachment(attachment);
                send(invitationUser, user, invitation, attachment);
            }
            /* check for an attachments on an outgoing e-mail invitation in the
             * other direction; if it exists */
            final List<EMail> invitationUserEMails =
                getProfileModel().readEMails(user.getId(), invitationUser);
            for (final EMail invitationUserEMail : invitationUserEMails) {
                final OutgoingEMailInvitation outgoingPrime =
                    invitationSql.readOutgoingEMail(user, invitationUserEMail);
                logger.logDebug("outgoingPrime", outgoingPrime);
                if (null != outgoingPrime) {
                    final List<Attachment> attachmentsPrime =
                        invitationSql.readAttachments(outgoingPrime);
                    for (final Attachment attachmentPrime : attachmentsPrime) {
                        invitationSql.deleteAttachment(attachmentPrime);
                        send(user, invitationUser, outgoingPrime, attachmentPrime);
                    }
                }
            }

            // delete all invitations
            deleteAllInvitations(user.getId(), user, invitationUser);

            // fire event
            final ContactInvitationAcceptedEvent event = new ContactInvitationAcceptedEvent();
            event.setAcceptedBy(user.getId());
            event.setAcceptedOn(acceptedOn);
            event.setDate(event.getAcceptedOn());
            enqueueEvent(invitationUser, event);
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
    public void acceptInvitation(final IncomingUserInvitation invitation,
            final Calendar acceptedOn) {
        try {
            final InternalUserModel userModel = getUserModel();
            final User extendedByUser = userModel.read(
                    invitation.getExtendedBy().getId());

            // delete incoming/outgoing/user/e-mail
            deleteAllInvitations(user.getId(), user, extendedByUser);

            // create contact
            contactSql.create(user, extendedByUser, user, acceptedOn);
            contactSql.create(extendedByUser, user, user, acceptedOn);

            // fire event
            final ContactInvitationAcceptedEvent event = new ContactInvitationAcceptedEvent();
            event.setAcceptedBy(user.getId());
            event.setAcceptedOn(acceptedOn);
            event.setDate(event.getAcceptedOn());
            enqueueEvent(extendedByUser, event);
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
            enqueueEvent(userId, event);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#createInvitation(com.thinkparity.codebase.model.contact.OutgoingEMailInvitation)
     * 
     */
    public void createInvitation(final OutgoingEMailInvitation invitation) {
        try {
            final UserModel userModel = getUserModel();
            final User createdBy = userModel.read(invitation.getCreatedBy().getId());

            // create outgoing e-mail
            invitation.setCreatedBy(createdBy);
            invitationSql.create(user, invitation);

            final User invitationUser = userModel.read(invitation.getInvitationEMail());
            if (null == invitationUser) {
                // send e-mail
                final MimeMessage mimeMessage = smtpService.createMessage();
                inject(mimeMessage, invitation.getInvitationEMail(), user);
                addRecipient(mimeMessage, invitation.getInvitationEMail());
                smtpService.deliver(mimeMessage);
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
                enqueueEvent(invitationUser, event);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#createInvitation(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.model.contact.OutgoingUserInvitation)
     *
     */
    public void createInvitation(final OutgoingUserInvitation invitation) {
        try {
            final UserModel userModel = getUserModel();
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
            enqueueEvent(invitationUser, event);
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
     * @see com.thinkparity.desdemona.model.contact.ContactModel#declineInvitation(com.thinkparity.codebase.model.contact.IncomingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void declineInvitation(final IncomingEMailInvitation invitation,
            final Calendar declinedOn) {
        try {
            final UserModel userModel = getUserModel();
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
            enqueueEvent(invitationUser, event);
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
    public void declineInvitation(final IncomingUserInvitation invitation,
            final Calendar declinedOn) {
        try {
            final UserModel userModel = getUserModel();
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
            enqueueEvent(invitationUser, event);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#delete(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void delete(final JabberId id) {
        try {
            delete(user.getId(), id);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#deleteInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.OutgoingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void deleteInvitation(final OutgoingEMailInvitation invitation,
            final Calendar deletedOn) {
        try {
            final UserModel userModel = getUserModel();
            final User invitationUser = userModel.read(invitation.getInvitationEMail());

            // delete incoming e-mail invitation
            if (null == invitationUser) {
                logger.logInfo("No inivation user exists.");
            } else {
                final IncomingEMailInvitation incomingEMail =
                    invitationSql.readIncomingEMail(invitationUser,
                            invitation.getInvitationEMail(), user);
                invitationSql.delete(incomingEMail);
            }

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
            // delete incoming e-mail invitation
            if (null == invitationUser) {
                logger.logInfo("No inivation user exists.");
            } else {
                final ContactEMailInvitationDeletedEvent event = new ContactEMailInvitationDeletedEvent();
                event.setDate(deletedOn);
                event.setDeletedBy(user.getId());
                event.setDeletedOn(event.getDate());
                event.setInvitedAs(invitation.getInvitationEMail());
                enqueueEvent(invitationUser, event);
            }
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
    public void deleteInvitation(final OutgoingUserInvitation invitation,
            final Calendar deletedOn) {
        try {
            final UserModel userModel = getUserModel();
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
            enqueueEvent(invitationUser, event);
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.InternalContactModel#deleteInvitationAttachment(com.thinkparity.desdemona.model.contact.invitation.Attachment)
     *
     */
    public void deleteInvitationAttachment(final Attachment attachment) {
        try {
            invitationSql.deleteAttachment(attachment);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#read(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<Contact> read() {
		try {
			final List<JabberId> contactIds = contactSql.readIds(user.getLocalId());
			final List<Contact> contacts = new LinkedList<Contact>();
			for (final JabberId contactId : contactIds) {
				contacts.add(read(user.getId(), contactId));
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
	public Contact read(final JabberId contactId) {
		try {
            return read(user.getId(), contactId);
	    } catch (final Throwable t) {
            throw panic(t);
        }
	}

    /**
     * @see com.thinkparity.desdemona.model.contact.InternalContactModel#readContainerVersionInvitationAttachments(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.ContactInvitation)
     * 
     */
    public List<ContainerVersionAttachment> readContainerVersionInvitationAttachments(
            final JabberId userId, final ContactInvitation invitation) {
        try {
            final List<Attachment> attachments = readInvitationAttachments(
                    userId, invitation, new Filter<Attachment>() {
                        public Boolean doFilter(final Attachment o) {
                            return Attachment.ReferenceType.CONTAINER_VERSION !=
                                o.getReferenceType();
                        }
                    });
            final List<ContainerVersionAttachment> cvas =
                new ArrayList<ContainerVersionAttachment>(attachments.size());
            for (final Attachment attachment : attachments) {
                final ContainerVersionAttachment cva = new ContainerVersionAttachment();
                cva.setInvitationId(attachment.getInvitationId());
                cva.setReferenceId(attachment.getReferenceId());
                cvas.add(cva);
            }
            return cvas;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#readIncomingEMailInvitations(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations() {
        try {
            return invitationSql.readIncomingEMail(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#readIncomingUserInvitations(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations() {
        try {
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
            return readInvitationAttachments(userId, invitation,
                    defaultInvitationAttachmentFilter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#readOutgoingEMailInvitations(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations() {
        try {
            return readOutgoingEMailInvitations(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactModel#readOutgoingEMailInvitations(com.thinkparity.codebase.model.container.ContainerVersion)
     *
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final ContainerVersion version) {
        try {
            final ContainerVersionAttachment attachment = new ContainerVersionAttachment();
            attachment.setUniqueId(version.getArtifactUniqueId());
            attachment.setVersionId(version.getVersionId());
            return invitationSql.readOutgoingEMail(user, attachment);
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
    public List<OutgoingUserInvitation> readOutgoingUserInvitations() {
        try {
            return invitationSql.readOutgoingUser(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.InternalContactModel#readProxyOutgoingEMailInvitations(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public List<OutgoingEMailInvitation> readProxyOutgoingEMailInvitations(
            final JabberId proxyId) {
        try {
            final User proxyUser = getUserModel().read(proxyId);

            return readOutgoingEMailInvitations(proxyUser);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {
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
     * Delete a contact for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    private void delete(final JabberId userId, final JabberId contactId) {
        final UserModel userModel = getUserModel();
        final User user = userModel.read(userId);
        final User contact = userModel.read(contactId);

        // delete contact
        contactSql.delete(user.getLocalId(), contact.getLocalId());

        // fire event
        final ContactDeletedEvent deleted = new ContactDeletedEvent();
        deleted.setDeletedBy(userId);
        deleted.setDeletedOn(currentDateTime());
        enqueueEvent(contactId, deleted);
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
            final User invitedBy) throws MessagingException,
            UnsupportedEncodingException {
	    final InvitationText text = new InvitationText(Locale.getDefault(),
                invitedBy);

        final InternetAddress fromInternetAddress = new InternetAddress();
        fromInternetAddress.setAddress(Constants.Internet.Mail.FROM_ADDRESS);
        fromInternetAddress.setPersonal(text.getFromPersonal());
        mimeMessage.setFrom(fromInternetAddress);
	    mimeMessage.setSubject(text.getSubject());

        final MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(text.getBody(), text.getBodyType());

        final Multipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(mimeBodyPart);
        mimeMessage.setContent(mimeMultipart);
    }

    /**
     * Read a contact for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     * @return A <code>Contact</code>.
     */
    private Contact read(final JabberId userId, final JabberId contactId) {
        final Contact contact = inject(new Contact(), getUserModel().read(contactId));
        contact.setVCard(getUserModel(contact).readVCard(new ContactVCard()));
        contact.addAllEmails(getProfileModel().readEMails(userId, contact));
        return contact;
    }

    /**
     * Read a list of invitation attachments.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @param filter
     *            A <code>Filter<Attachment></code>.
     * @return A <code>List<Attachment></code>.
     */
    private List<Attachment> readInvitationAttachments(final JabberId userId,
            final ContactInvitation invitation, final Filter<Attachment> filter) {
        try {
            // TODO - Add createdBy criteria
            final List<Attachment> attachments = invitationSql.readAttachments(
                    invitation);
            FilterManager.filter(attachments, filter);
            return attachments;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read the outgoing e-mail address invitations for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>List<OutgoingEMailInvitation></code>.
     */
    private List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final User user) {
        return invitationSql.readOutgoingEMail(user);
    }

    /**
     * Send an invitation attachment to a user.
     * 
     * @param sendAs
     *            A <code>User</code> to send as.
     * @param sendTo
     *            A <code>User</code> to send to.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @param attachment
     *            An <code>Attachment</code>.
     */
    private void send(final User sendAs, final User sendTo,
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
     *            A <code>User</code>.
     * @param sendTo
     *            A <code>User</code>.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @param attachment
     *            A <code>ContainerVersionAttachment</code>.
     */
    private void send(final User sendAs, final User sendTo,
            final ContactInvitation invitation,
            final ContainerVersionAttachment attachment) {
        final InternalBackupModel backupModel = getBackupModel(sendAs);
        // grab from backup
        final Container container = backupModel.readContainer(
                attachment.getUniqueId());
        final ContainerVersion version = backupModel.readContainerVersion(
                container.getUniqueId(), attachment.getVersionId());
        final List<DocumentVersion> documentVersions =
            backupModel.readContainerDocumentVersions(container.getUniqueId(),
                    version.getVersionId());
        final List<ArtifactReceipt> receivedBy = backupModel.readPublishedTo(
                container.getUniqueId(), version.getVersionId());
        final Calendar publishedOn = version.getCreatedOn();
        final List<EMail> publishToEMails = Collections.emptyList();
        final List<User> publishToUsers = new ArrayList<User>(1);
        publishToUsers.add(sendTo);
        // publish version
        getContainerModel(sendAs).publishVersion(version, documentVersions,
                receivedBy, publishedOn, publishToEMails, publishToUsers);
    }
}

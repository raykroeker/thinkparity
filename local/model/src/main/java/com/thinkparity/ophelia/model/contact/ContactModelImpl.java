/*
 * Generated On: Jun 27 06 04:14:53 PM
 */
package com.thinkparity.ophelia.model.contact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterChain;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationExtendedEvent;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.contact.monitor.DownloadStep;
import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ContactIOHandler;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.filter.UserFilterManager;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.util.sort.contact.InvitationIdComparator;
import com.thinkparity.ophelia.model.util.sort.contact.NameComparator;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Contact Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.10
 */
public final class ContactModelImpl extends Model<ContactListener>
        implements ContactModel, InternalContactModel {

    
    /** The contact db io. */
    private ContactIOHandler contactIO;

    /** The default contact comparator. */
    private final Comparator<Contact> defaultComparator;

    /** The default contact filter. */
    private final Filter<? super Contact> defaultFilter;

    /** The default contact invitation comparator. */
    private final Comparator<ContactInvitation> defaultInvitationComparator;

    /** The default contact invitation filter. */
    private final Filter<? super ContactInvitation> defaultInvitationFilter;

    /** A local event generator. */
    private final ContactEventGenerator localEventGenerator;

    /** A remote event generator. */
    private final ContactEventGenerator remoteEventGenerator;

    /**
     * Create ContactModelImpl.
     *
     */
    public ContactModelImpl() {
        super();
        this.defaultComparator = new NameComparator(Boolean.TRUE);
        this.defaultFilter = FilterManager.createDefault();
        this.defaultInvitationComparator = new InvitationIdComparator();
        this.defaultInvitationFilter = FilterManager.createDefault();
        this.localEventGenerator = new ContactEventGenerator(ContactEvent.Source.LOCAL);
        this.remoteEventGenerator = new ContactEventGenerator(ContactEvent.Source.REMOTE);
    }

    /**
     * Accept the incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void acceptIncomingInvitation(final Long invitationId) {
        try {
            assertOnline();
            final IncomingInvitation incoming = readIncomingInvitation(invitationId);
            // delete the invitation
            contactIO.deleteIncomingInvitation(incoming.getId());
            // delete an invitation that might have been sent to the user
            final Contact remoteContact = readRemote(incoming.getInvitedBy().getId());
            final List<OutgoingEMailInvitation> deletedEMailInvitations = new ArrayList<OutgoingEMailInvitation>();
            OutgoingEMailInvitation outgoingEMail;
            // email invitations
            for (final EMail contactEMail : remoteContact.getEmails()) {
                outgoingEMail = contactIO.readOutgoingEMailInvitation(contactEMail);
                if (null != outgoingEMail) {
                    deletedEMailInvitations.add(outgoingEMail);
                    contactIO.deleteOutgoingEMailInvitation(outgoingEMail.getId());
                    contactIO.deleteEMail(contactEMail);
                }
            }
            // user invitation
            final User user = getUserModel().read(remoteContact.getId());
            final OutgoingUserInvitation outgoingUser;
            if (null != user) {
                outgoingUser = contactIO.readOutgoingUserInvitation(user.getId());
                if (null != outgoingUser) {
                    contactIO.deleteOutgoingUserInvitation(outgoingUser.getId());
                }
            } else {
                outgoingUser = null;
            }
            // create contact data
            final Contact localContact = createLocal(remoteContact);
            // accept
            getSessionModel().acceptContactInvitation(localUserId(),
                    incoming.getInvitedBy().getId(), currentDateTime());
            // fire accepted event
            notifyIncomingInvitationAccepted(localContact, incoming,
                    localEventGenerator);
            // fire deleted event
            for (final OutgoingEMailInvitation deletedEMailInvitation : deletedEMailInvitations)
                notifyOutgoingEMailInvitationDeleted(deletedEMailInvitation, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#addListener(com.thinkparity.ophelia.model.util.EventListener)
     * 
     */
    @Override
    public void addListener(final ContactListener listener) {
        super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#createOutgoingEMailInvitation(com.thinkparity.codebase.email.EMail)
     * 
     */
    public OutgoingEMailInvitation createOutgoingEMailInvitation(final EMail email) {
        assertOnline();
        assertDoesNotExist("CONTACT ALREADY EXISTS", email);
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar createdOn = sessionModel.readDateTime();

            final OutgoingEMailInvitation outgoing = new OutgoingEMailInvitation();
            outgoing.setCreatedBy(localUser().getLocalId());
            outgoing.setCreatedOn(createdOn);
            outgoing.setEmail(email);
            contactIO.createOutgoingInvitation(outgoing);

            getIndexModel().indexOutgoingEMailInvitation(outgoing.getId());
            getSessionModel().extendContactEMailInvitation(email, createdOn);
            // fire event
            final OutgoingEMailInvitation postCreation =
                contactIO.readOutgoingEMailInvitation(outgoing.getId());
            notifyOutgoingEMailInvitationCreated(postCreation, localEventGenerator);
            return postCreation;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#createOutgoingUserInvitation(java.lang.Long)
     * 
     */
    public OutgoingUserInvitation createOutgoingUserInvitation(final Long userId) {
        try {
            assertOnline();
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar createdOn = sessionModel.readDateTime();

            final User user = getUserModel().read(userId);
            final OutgoingUserInvitation outgoing = new OutgoingUserInvitation();
            outgoing.setCreatedBy(localUser().getLocalId());
            outgoing.setCreatedOn(createdOn);
            outgoing.setUser(user);
            contactIO.createOutgoingInvitation(outgoing);

            getIndexModel().indexOutgoingUserInvitation(outgoing.getId());
            getSessionModel().extendContactUserInvitation(user.getId(), createdOn);

            // fire event
            final OutgoingUserInvitation postCreation =
                contactIO.readOutgoingUserInvitation(outgoing.getId());
            notifyOutgoingUserInvitationCreated(postCreation, localEventGenerator);
            return postCreation;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Accept the incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void declineIncomingInvitation(final Long invitationId) {
        logger.logApiId();
        logger.logVariable("invitationId", invitationId);
        assertOnline();
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar declinedOn = sessionModel.readDateTime();
            
            final IncomingInvitation invitation = readIncomingInvitation(invitationId);
            // delete invitation data
            contactIO.deleteIncomingInvitation(invitation.getId());
            // decline
            if (invitation.isSetInvitedAs()) {
                sessionModel.declineContactEMailInvitation(
                        invitation.getInvitedAs(),
                        invitation.getInvitedBy().getId(), declinedOn);
            } else {
                sessionModel.declineContactUserInvitation(
                        invitation.getInvitedBy().getId(), declinedOn);
            }
            // fire event
            notifyIncomingInvitationDeclined(invitation, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    public void delete(final JabberId contactId) {
        logger.logApiId();
        logger.logVariable("contactId", contactId);
        assertOnline();
        try {
            final Contact contact = read(contactId);
            // delete data
            deleteLocal(contactId);
            // delete remote
            getSessionModel().deleteContact(localUserId(), contactId);
            // fire event
            notifyContactDeleted(contact, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#deleteOutgoingEMailInvitation(java.lang.Long)
     * 
     */
    public void deleteOutgoingEMailInvitation(final Long invitationId) {
        assertOnline();
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar deletedOn = sessionModel.readDateTime();
            final OutgoingEMailInvitation invitation =
                contactIO.readOutgoingEMailInvitation(invitationId);
            contactIO.deleteOutgoingEMailInvitation(invitationId);
            contactIO.deleteEMail(invitation.getEmail());
            // delete remote
            getSessionModel().deleteContactEMailInvitation(
                    invitation.getEmail(), deletedOn);
            // fire event
            notifyOutgoingEMailInvitationDeleted(invitation, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#deleteOutgoingUserInvitation(java.lang.Long)
     * 
     */
    public void deleteOutgoingUserInvitation(final Long invitationId) {
        assertOnline();
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar deletedOn = sessionModel.readDateTime();
            final OutgoingUserInvitation invitation =
                contactIO.readOutgoingUserInvitation(invitationId);
            contactIO.deleteOutgoingUserInvitation(invitationId);
            // delete remote
            getSessionModel().deleteContactUserInvitation(
                    invitation.getUser().getId(), deletedOn);
            // fire event
            notifyOutgoingUserInvitationDeleted(invitation, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#doesExist(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public Boolean doesExist(final Long contactId) {
        try {
            return contactIO.doesExist(contactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#doesExistOutgoingUserInvitationForUser(java.lang.Long)
     *
     */
    public Boolean doesExistOutgoingUserInvitationForUser(final Long userId) {
        try {
            return contactIO.doesExistOutgoingUserInvitation(userId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#download(com.thinkparity.ophelia.model.util.ProcessMonitor)
     * 
     */
    public void download(final ProcessMonitor monitor) {
        try {
            assertOnline();
            final InternalSessionModel sessionModel = getSessionModel();
            final List<JabberId> contactIds = sessionModel.readContactIds();
            notifyDetermine(monitor, contactIds.size());
            for (final JabberId contactId : contactIds) {
                notifyStepBegin(monitor, DownloadStep.DOWNLOAD);
                createLocal(sessionModel.readContact(localUserId(), contactId));
                notifyStepEnd(monitor, DownloadStep.DOWNLOAD);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Handle the remote event generated when a contact is deleted.
     * 
     * @param deletedBy
     *            By whom the contact was deleted <code>JabberId</code>.
     * @param deletedOn
     *            When the contact was deleted <code>Calendar</code>.
     */
    public void handleContactDeleted(final ContactDeletedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Contact contact = read(event.getDeletedBy());
            // delete data
            deleteLocal(event.getDeletedBy());
            // fire event
            notifyContactDeleted(contact, remoteEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Handle the remote event generated when a contact is updated.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     * @param updatedOn
     *            When the contact was updated <code>Calendar</code>.
     */
    public void handleContactUpdated(final ContactUpdatedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Contact localContact = read(event.getContactId());
            final Contact remoteContact = readRemote(event.getContactId());
            // can only happen if the user re-creates
            // an environment
            if (null == localContact) {
                createLocal(remoteContact);
            } else {
                remoteContact.setLocalId(localContact.getLocalId());
                // delete existing emails
                final List<EMail> emails =
                    contactIO.readEmails(remoteContact.getLocalId());
                for (final EMail email : emails) {
                    contactIO.deleteEmail(remoteContact.getLocalId(), email);
                }
                // update data
                contactIO.update(remoteContact);
                // create emails
                for (final EMail email : remoteContact.getEmails()) {
                    contactIO.createEmail(remoteContact.getLocalId(), email);
                }
            }
            getIndexModel().updateContact(event.getContactId());
            // fire event
            notifyContactUpdated(remoteContact, remoteEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleEMailInvitationDeclined(com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeclinedEvent)
     * 
     */
    public void handleEMailInvitationDeclined(
            final ContactEMailInvitationDeclinedEvent event) {
        try {
            // delete invitation
            final OutgoingEMailInvitation invitation =
                contactIO.readOutgoingEMailInvitation(event.getInvitedAs());
            contactIO.deleteOutgoingEMailInvitation(invitation.getId());
            contactIO.deleteEMail(invitation.getEmail());
            // fire event(s)
            notifyOutgoingEMailInvitationDeclined(invitation, remoteEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleEMailInvitationDeleted(com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeletedEvent)
     * 
     */
    public void handleEMailInvitationDeleted(
            final ContactEMailInvitationDeletedEvent event) {
        try {
            final List<IncomingInvitation> invitations = readIncomingInvitations();
            final List<IncomingInvitation> deletedInvitations =
                new ArrayList<IncomingInvitation>(invitations.size());
            // delete local data
            for (final IncomingInvitation invitation : invitations) {
                if (invitation.isSetInvitedAs()
                        && invitation.getInvitedAs().equals(
                                event.getInvitedAs())) {
                    contactIO.deleteIncomingInvitation(invitation.getId());
                    deletedInvitations.add(invitation);
                }
            }
            // fire event
            for (final IncomingInvitation deletedInvitation : deletedInvitations) {
                notifyIncomingInvitationDeleted(deletedInvitation, remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Handle the invitation extended remote event.
     * 
     * @param invitedBy
     *            By whom the invitation was extended.
     * @param invitedOn
     *            When the invitation was extended.
     */
    public void handleEMailInvitationExtended(
            final ContactEMailInvitationExtendedEvent event) {
        try {
            // create user data
            final InternalUserModel userModel = getUserModel();
            final User extendedByUser = userModel.readLazyCreate(event.getInvitedBy());
            final IncomingInvitation incoming = new IncomingInvitation();
            incoming.setCreatedBy(localUser().getLocalId());
            incoming.setCreatedOn(currentDateTime());
            incoming.setInvitedAs(event.getInvitedAs());
            incoming.setInvitedBy(extendedByUser);
            contactIO.createIncomingInvitation(incoming, extendedByUser);
            getIndexModel().indexIncomingInvitation(incoming.getId());
            // fire event
            final IncomingInvitation postCreation = contactIO.readIncomingInvitation(incoming.getId());
            notifyIncomingInvitationCreated(postCreation, remoteEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleInvitationAccepted(com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent)
     * 
     */
    public void handleInvitationAccepted(
            final ContactInvitationAcceptedEvent event) {
       try {
           final Contact remoteContact = readRemote(event.getAcceptedBy());
           // delete incoming
           final IncomingInvitation incoming = contactIO.readIncomingInvitation(remoteContact.getId());
           if (null != incoming) {
               contactIO.deleteIncomingInvitation(incoming.getId());
           }
           // delete email outgoing
           final List<OutgoingEMailInvitation> acceptedEMailInvitations = new ArrayList<OutgoingEMailInvitation>();
           OutgoingEMailInvitation outgoingEMail;
           for (final EMail email : remoteContact.getEmails()) {
               outgoingEMail = contactIO.readOutgoingEMailInvitation(email);
               if (null != outgoingEMail) {
                   acceptedEMailInvitations.add(outgoingEMail);
                   contactIO.deleteOutgoingEMailInvitation(
                           acceptedEMailInvitations.get(
                                   acceptedEMailInvitations.size() - 1).getId());
                   contactIO.deleteEMail(email);
               }
           }
           // delete user outgoing
           final User user = getUserModel().read(remoteContact.getId());
           final OutgoingUserInvitation acceptedUser;
           if (null != user) {
               acceptedUser = contactIO.readOutgoingUserInvitation(user.getId());
               if (null != acceptedUser) {
                   contactIO.deleteOutgoingUserInvitation(acceptedUser.getId());
               }
           } else {
               acceptedUser = null;
           }
           final Contact localContact = createLocal(remoteContact);
           // fire event
           if (null != incoming)
               notifyIncomingInvitationDeleted(incoming, remoteEventGenerator);
           if (null != acceptedUser)
               notifyOutgoingUserInvitationAccepted(localContact,
                       acceptedUser, remoteEventGenerator);
           // fire events
           for (final OutgoingEMailInvitation acceptedEMailInvitation : acceptedEMailInvitations)
               notifyOutgoingEMailInvitationAccepted(localContact,
                        acceptedEMailInvitation, remoteEventGenerator);
       } catch (final Throwable t) {
           throw translateError(t);
       }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleUserInvitationDeclined(com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeclinedEvent)
     *
     */
    public void handleUserInvitationDeclined(
            final ContactUserInvitationDeclinedEvent event) {
        try {
            // delete invitation
            final OutgoingUserInvitation invitation =
                contactIO.readOutgoingUserInvitation(event.getDeclinedBy());
            contactIO.deleteOutgoingUserInvitation(invitation.getId());
            // fire event(s)
            notifyOutgoingUserInvitationDeclined(invitation, remoteEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleUserInvitationDeleted(com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeletedEvent)
     *
     */
    public void handleUserInvitationDeleted(final ContactUserInvitationDeletedEvent event) {
        try {
            final List<IncomingInvitation> invitations = readIncomingInvitations();
            final List<IncomingInvitation> deletedInvitations =
                new ArrayList<IncomingInvitation>(invitations.size());
            // delete local data
            for (final IncomingInvitation invitation : invitations) {
                if (!invitation.isSetInvitedAs()
                        && invitation.getInvitedBy().equals(
                                event.getDeletedBy())) {
                    contactIO.deleteIncomingInvitation(invitation.getId());
                    deletedInvitations.add(invitation);
                }
            }
            // fire event
            for (final IncomingInvitation deletedInvitation : deletedInvitations) {
                notifyIncomingInvitationDeleted(deletedInvitation, remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleUserInvitationExtended(com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationExtendedEvent)
     *
     */
    public void handleUserInvitationExtended(
            final ContactUserInvitationExtendedEvent event) {
        try {
            // create user data
            final InternalUserModel userModel = getUserModel();
            final User extendedByUser = userModel.readLazyCreate(event.getInvitedBy());
            final IncomingInvitation incoming = new IncomingInvitation();
            incoming.setCreatedBy(localUser().getLocalId());
            incoming.setCreatedOn(currentDateTime());
            incoming.setInvitedBy(extendedByUser);
            contactIO.createIncomingInvitation(incoming, extendedByUser);
            getIndexModel().indexIncomingInvitation(incoming.getId());
            // fire event
            final IncomingInvitation postCreation = contactIO.readIncomingInvitation(incoming.getId());
            notifyIncomingInvitationCreated(postCreation, remoteEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }

    }

    /**
     * Read a list of contacts.
     * 
     * @return A list of contacts.
     */
    public List<Contact> read() {
        logger.logApiId();
        return read(defaultComparator, defaultFilter);
    }

    /**
     * Read a list of contacts.
     * 
     * @param comparator
     *            A user comparator to sort the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Comparator<Contact> comparator) {
        logger.logApiId();
        logger.logVariable("comparator", comparator);
        return read(comparator, defaultFilter);
    }

    /**
     * Read a list of contacts.
     * 
     * @param comparator
     *            A user comparator to sort the contacts.
     * @param filter
     *            A user filter to scope the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Comparator<Contact> comparator,
            final Filter<? super Contact> filter) {
        logger.logApiId();
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final List<Contact> contacts = contactIO.read();
        FilterManager.filter(contacts, filter);
        ModelSorter.sortContacts(contacts, comparator);
        return contacts;
    }

    /**
     * Read a list of contacts.
     * 
     * @param filter
     *            A user filter to scope the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Filter<? super Contact> filter) {
        logger.logApiId();
        logger.logVariable("filter", filter);
        return read(defaultComparator, filter);
    }

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    public Contact read(final JabberId contactId) {
        logger.logApiId();
        return contactIO.read(contactId);
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readPublishTo()
     *
     */
    public List<Contact> readContainerPublishTo() {
        try {
            // filter out the contacts flagged with resitricted publish
            final FilterChain<User> filter = new FilterChain<User>();
            filter.addFilter(UserFilterManager.createContainerPublishTo());
            return read(defaultComparator, filter);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read an incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An incoming invitation.
     */
    public IncomingInvitation readIncomingInvitation(final Long invitationId) {
        try {
            return contactIO.readIncomingInvitation(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations() {
        logger.logApiId();
        return readIncomingInvitations(defaultInvitationComparator, defaultInvitationFilter);
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations(
            final Comparator<ContactInvitation> comparator) {
        logger.logApiId();
        logger.logVariable("comparator",comparator);
        return readIncomingInvitations(comparator, defaultInvitationFilter);
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations(
            final Comparator<ContactInvitation> comparator,
            final Filter<? super ContactInvitation> filter) {
        logger.logApiId();
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final List<IncomingInvitation> invitations =
                contactIO.readIncomingInvitations();
        FilterManager.filter(invitations, filter);
        ModelSorter.sortIncomingContactInvitations(invitations, comparator);
        return invitations;
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations(
            final Filter<? super ContactInvitation> filter) {
        logger.logApiId();
        logger.logVariable("filter", filter);
        return readIncomingInvitations(defaultInvitationComparator, filter);
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readOutgoingEMailInvitation(java.lang.Long)
     * 
     */
    public OutgoingEMailInvitation readOutgoingEMailInvitation(
            final Long invitationId) {
        try {
            return contactIO.readOutgoingEMailInvitation(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readOutgoingEMailInvitations()
     * 
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations() {
        try {
            return contactIO.readOutgoingEMailInvitations();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readOutgoingEMailInvitation(java.lang.Long)
     * 
     */
    public OutgoingUserInvitation readOutgoingUserInvitation(
            final Long invitationId) {
        try {
            return contactIO.readOutgoingUserInvitation(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readOutgoingUserInvitations()
     * 
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations() {
        try {
            return contactIO.readOutgoingUserInvitations();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#removeListener(com.thinkparity.ophelia.model.util.EventListener)
     * 
     */
    @Override
    public void removeListener(final ContactListener listener) {
        super.removeListener(listener);
    }

    /**
     * Search for contacts.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    public List<JabberId> search(final String expression) {
        logger.logApiId();
        logger.logVariable("expression", expression);
        try {
            return getIndexModel().searchContacts(expression);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#searchIncomingInvitations(java.lang.String)
     *
     */
    public List<Long> searchIncomingInvitations(final String expression) {
        try {
            return getIndexModel().searchIncomingInvitations(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#searchOutgoingEMailInvitations(java.lang.String)
     *
     */
    public List<Long> searchOutgoingEMailInvitations(final String expression) {
        try {
            return getIndexModel().searchOutgoingEMailInvitations(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#searchOutgoingUserInvitations(java.lang.String)
     *
     */
    public List<Long> searchOutgoingUserInvitations(String expression) {
        try {
            return getIndexModel().searchOutgoingUserInvitations(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.contactIO = IOFactory.getDefault(workspace).createContactHandler();
    }

    /**
     * Assert that no contact with the given e-mail address exists.
     * 
     * @param assertion
     *            An assertion.
     * @param email
     *            An e-mail address.
     */
    private void assertDoesNotExist(final String assertion, final EMail email) {
        Assert.assertIsNull(assertion, read(email));
    }

    /**
     * Create the local contact data.
     * 
     * @param contact
     *            A contact.
     * @return The new contact.
     */
    private Contact createLocal(final Contact contact) {
        // create the contact data
        final InternalUserModel userModel = getUserModel();
        final User user = userModel.readLazyCreate(contact.getId());
        contact.setLocalId(user.getLocalId());
        contactIO.create(contact);
        for (final EMail email : contact.getEmails()) {
            contactIO.createEmail(contact.getLocalId(), email);
        }
        getIndexModel().indexContact(contact.getId());
        return contactIO.read(contact.getId());
    }

    /**
     * Delete the local contact data.
     * 
     * @param contactId
     *            A contact id.
     */
    private void deleteLocal(final JabberId contactId) {
        // delete index
        getIndexModel().deleteContact(contactId);
        // delete emails
        final Contact contact = read(contactId);
        for (final EMail email : contact.getEmails()) {
            contactIO.deleteEmail(contact.getLocalId(), email);
        }
        // delete local
        contactIO.delete(contact.getLocalId());
    }

    /**
     * Fire a notification event that a contact was deleted.
     * 
     * @param contact
     *            A contact.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyContactDeleted(final Contact contact,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.contactDeleted(eventGenerator.generate(contact));
            }
        });
    }

    /**
     * Fire a notification event that a contact was updated.
     * 
     * @param contact
     *            A contact.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyContactUpdated(final Contact contact,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.contactUpdated(eventGenerator.generate(contact));
            }
        });
    }

    /**
     * Fire a notification event that an incoming invitation has been accepted.
     * 
     * @param contact
     *            A contact.
     * @param incomingInvitation
     *            An incoming invitation.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyIncomingInvitationAccepted(final Contact contact,
            final IncomingInvitation incomingInvitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.incomingInvitationAccepted(eventGenerator.generate(contact,
                        incomingInvitation));
            }
        });
    }

    /**
     * Notify all listeners that an incoming invitation has been created.
     * 
     * @param incomingInvitation
     *            An incoming invitation.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyIncomingInvitationCreated(
            final IncomingInvitation incomingInvitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.incomingInvitationCreated(eventGenerator
                        .generate(incomingInvitation));
            }
        });
    }

    /**
     * Fire a notification event that an incoming invitation has been declined.
     * 
     * @param invitation
     *            An invitation.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyIncomingInvitationDeclined(
            final IncomingInvitation incomingInvitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.incomingInvitationDeclined(eventGenerator
                        .generate(incomingInvitation));
            }
        });
    }

    /**
     * Fire a notification event that an incoming invitation has been deleted.
     * 
     * @param invitation
     *            An invitation.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyIncomingInvitationDeleted(
            final IncomingInvitation incomingInvitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.incomingInvitationDeleted(eventGenerator
                        .generate(incomingInvitation));
            }
        });
    }

    /**
     * Fire a notification event that an outgoing e-mail invitation has been
     * accepted.
     * 
     * @param contact
     *            A contact.
     * @param outgoingInvitation
     *            An outgoing invitation.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyOutgoingEMailInvitationAccepted(final Contact contact,
            final OutgoingEMailInvitation invitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingEMailInvitationAccepted(eventGenerator.generate(
                        contact, invitation));
            }
        });
    }

    /**
     * Notify all listeners that an outgoing e-mail invitation has been created.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyOutgoingEMailInvitationCreated(
            final OutgoingEMailInvitation invitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingEMailInvitationCreated(
                        eventGenerator.generate(invitation));
            }
        });
    }

    /**
     * Notify all listeners that an outgoing e-mail invitation has been declined.
     * 
     * @param invitation
     *            The invitation.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyOutgoingEMailInvitationDeclined(
            final OutgoingEMailInvitation invitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingEMailInvitationDeclined(eventGenerator.generate(invitation));
            }
        });
    }

    /**
     * Notify all listeners that an outgoing e-mail invitation has been deleted.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyOutgoingEMailInvitationDeleted(
            final OutgoingEMailInvitation invitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingEMailInvitationDeleted(
                        eventGenerator.generate(invitation));
            }
        });
    }

    /**
     * Fire a notification event that an outgoing user invitation has been
     * accepted.
     * 
     * @param contact
     *            A contact.
     * @param outgoingInvitation
     *            An outgoing invitation.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyOutgoingUserInvitationAccepted(final Contact contact,
            final OutgoingUserInvitation invitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingUserInvitationAccepted(eventGenerator.generate(
                        contact, invitation));
            }
        });
    }

    /**
     * Notify all listeners that an outgoing user invitation has been created.
     * 
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyOutgoingUserInvitationCreated(
            final OutgoingUserInvitation invitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingUserInvitationCreated(
                        eventGenerator.generate(invitation));
            }
        });
    }

    /**
     * Notify all listeners that an outgoing e-mail invitation has been declined.
     * 
     * @param invitation
     *            The invitation.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyOutgoingUserInvitationDeclined(
            final OutgoingUserInvitation invitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingUserInvitationDeclined(
                        eventGenerator.generate(invitation));
            }
        });
    }

    /**
     * Notify all listeners that an outgoing user invitation has been deleted.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyOutgoingUserInvitationDeleted(
            final OutgoingUserInvitation invitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingUserInvitationDeleted(
                        eventGenerator.generate(invitation));
            }
        });
    }

    /**
     * Read a contact for an e-mail address.
     * 
     * @param email
     *            An e-mail address.
     * @return A contact.
     */
    private Contact read(final EMail email) {
        return null;
    }

    /**
     * Read a contact from the server.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    private Contact readRemote(final JabberId contactId) {
        return getSessionModel().readContact(localUserId(), contactId);
    }
}

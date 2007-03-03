/*
 * Generated On: Jun 27 06 04:14:53 PM
 */
package com.thinkparity.ophelia.model.contact;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.contact.monitor.DownloadStep;
import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ContactIOHandler;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.filter.Filter;
import com.thinkparity.ophelia.model.util.filter.contact.DefaultInvitationFilter;
import com.thinkparity.ophelia.model.util.filter.contact.FilterManager;
import com.thinkparity.ophelia.model.util.filter.user.DefaultFilter;
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
        this.defaultFilter = new DefaultFilter();
        this.defaultInvitationComparator = new InvitationIdComparator();
        this.defaultInvitationFilter = new DefaultInvitationFilter();
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
            final IncomingInvitation invitation = readIncomingInvitation(invitationId);
            // delete the invitation
            contactIO.deleteIncomingInvitation(invitation.getId());
            // delete an invitation that might have been sent to the user
            final Contact remoteContact = readRemote(invitation.getInvitedBy());
            final List<OutgoingInvitation> deletedInvitations =
                new ArrayList<OutgoingInvitation>();
            OutgoingInvitation outgoingInvitation;
            for (final EMail contactEMail : remoteContact.getEmails()) {
                outgoingInvitation = contactIO.readOutgoingInvitation(contactEMail);
                if (null != outgoingInvitation) {
                    deletedInvitations.add(outgoingInvitation);
                    contactIO.deleteOutgoingInvitation(outgoingInvitation.getId());
                    contactIO.deleteEMail(contactEMail);
                }
            }
            // create contact data
            final Contact localContact = createLocal(readRemote(
                    invitation.getInvitedBy()));
            // accept
            getSessionModel().acceptContactInvitation(localUserId(),
                    invitation.getInvitedBy(), currentDateTime());
            // fire accepted event
            notifyIncomingInvitationAccepted(localContact, invitation,
                    localEventGenerator);
            // fire deleted event
            for (final OutgoingInvitation deletedInvitation : deletedInvitations)
                notifyOutgoingInvitationDeleted(deletedInvitation, localEventGenerator);
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
     * Create a contact invitation.
     * 
     * @param email
     *            An e-mail address.
     */
    public OutgoingInvitation createOutgoingInvitation(final EMail email) {
        logger.logApiId();
        logger.logVariable("email", email);
        assertOnline();
        assertDoesNotExist("CONTACT ALREADY EXISTS", email);
        try {
            final OutgoingInvitation outgoing = new OutgoingInvitation();
            outgoing.setCreatedBy(localUser().getLocalId());
            outgoing.setCreatedOn(currentDateTime());
            outgoing.setEmail(email);
            contactIO.createOutgoingInvitation(outgoing);
            getIndexModel().indexOutgoingInvitation(outgoing.getId());
            getSessionModel().extendInvitation(email);
            // fire event
            final OutgoingInvitation postCreation = contactIO.readOutgoingInvitation(email);
            notifyOutgoingInvitationCreated(postCreation, localEventGenerator);
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
            // decline
            final IncomingInvitation invitation = readIncomingInvitation(invitationId);
            getSessionModel().declineInvitation(
                    invitation.getInvitedAs(), invitation.getInvitedBy());
            // delete invitation data
            contactIO.deleteIncomingInvitation(invitation.getId());
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
     * Delete an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void deleteOutgoingInvitation(final Long invitationId) {
        logger.logApiId();
        logger.logVariable("invitationId", invitationId);
        assertOnline();
        try {
            final OutgoingInvitation invitation = readOutgoingInvitation(invitationId);
            contactIO.deleteOutgoingInvitation(invitationId);
            contactIO.deleteEMail(invitation.getEmail());
            // delete remote
            getSessionModel().deleteContactInvitation(localUserId(),
                    invitation.getEmail(), currentDateTime());
            // fire event
            notifyOutgoingInvitationDeleted(invitation, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
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
     * Handle the invitation accepted remote event.
     * 
     * @param acceptedBy
     *            By whom the invitation was accepted.
     * @param acceptedOn
     *            When the invitation was accepted.
     */
    public void handleInvitationAccepted(final ContactInvitationAcceptedEvent event) {
       logger.logApiId();
       logger.logVariable("event", event);
       try {
           final Contact remoteContact = readRemote(event.getAcceptedBy());
           // delete incoming
           final IncomingInvitation incoming = contactIO.readIncomingInvitation(remoteContact.getId());
           if (null != incoming) {
               contactIO.deleteIncomingInvitation(incoming.getId());
           }
           // delete outgoing
           final List<OutgoingInvitation> outgoingInvitations = new ArrayList<OutgoingInvitation>();
           for (final EMail email : remoteContact.getEmails()) {
               outgoingInvitations.add(contactIO.readOutgoingInvitation(email));
               contactIO.deleteOutgoingInvitation(
                       outgoingInvitations.get(
                               outgoingInvitations.size() - 1).getId());
               contactIO.deleteEMail(email);
           }
           final Contact localContact = createLocal(remoteContact);
           // fire event
           if (null != incoming)
               notifyIncomingInvitationDeleted(incoming, remoteEventGenerator);
           // fire events
           for (final OutgoingInvitation outgoingInvitation : outgoingInvitations)
               notifyOutgoingInvitationAccepted(localContact,
                        outgoingInvitation, remoteEventGenerator);
       } catch (final Throwable t) {
           throw translateError(t);
       }
    }

    /**
     * Handle the invitation declined remote event.
     * 
     * @param declinedBy
     *            By whom the invitation was declined.
     * @param declinedOn
     *            When the invitation was declined.
     */
    public void handleInvitationDeclined(final ContactInvitationDeclinedEvent event) {
       logger.logApiId();
       logger.logVariable("event", event);
       try {
           // delete invitation
           final OutgoingInvitation invitation =
                   contactIO.readOutgoingInvitation(event.getInvitedAs());
           contactIO.deleteOutgoingInvitation(invitation.getId());
           contactIO.deleteEMail(invitation.getEmail());
           // fire event(s)
           notifyOutgoingInvitationDeclined(invitation, remoteEventGenerator);
       } catch (final Throwable t) {
           throw translateError(t);
       }
   }

    /**
     * Handle the remote contact invitation deleted remote event.
     * 
     * @param invitedAs
     *            The original invitation e-mail address.
     * @param deletedBy
     *            By whom the invitation was deleted.
     * @param deletedOn
     *            When the invitation was deleted.
     */
    public void handleInvitationDeleted(final ContactInvitationDeletedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final List<IncomingInvitation> invitations = readIncomingInvitations();
            final List<IncomingInvitation> deletedInvitations =
                new ArrayList<IncomingInvitation>(invitations.size());
            // delete local data
            for (final IncomingInvitation invitation : invitations) {
                if (invitation.getInvitedAs().equals(event.getInvitedAs())) {
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
    public void handleInvitationExtended(final ContactInvitationExtendedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            // create user data
            final InternalUserModel userModel = getUserModel();
            final User extendedByUser = userModel.readLazyCreate(event.getInvitedBy());
            final IncomingInvitation incoming = new IncomingInvitation();
            incoming.setCreatedBy(localUser().getLocalId());
            incoming.setCreatedOn(currentDateTime());
            incoming.setInvitedAs(event.getInvitedAs());
            incoming.setInvitedBy(extendedByUser.getId());
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
     * Read an incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An incoming invitation.
     */
    public IncomingInvitation readIncomingInvitation(final Long invitationId) {
        logger.logApiId();
        logger.logVariable("invitationId", invitationId);
        return contactIO.readIncomingInvitation(invitationId);
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
        FilterManager.filterIncomingInvitations(invitations, filter);
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

    public OutgoingInvitation readOutgoingInvitation(final Long invitationId) {
        logger.logApiId();
        logger.logVariable("invitationId", invitationId);
        return contactIO.readOutgoingInvitation(invitationId);
    }

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations() {
        logger.logApiId();
        return readOutgoingInvitations(defaultInvitationComparator, defaultInvitationFilter);
    }

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations(
            final Comparator<ContactInvitation> comparator) {
        logger.logApiId();
        logger.logVariable("comparator",comparator);
        return readOutgoingInvitations(comparator, defaultInvitationFilter);
    }

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations(
            final Comparator<ContactInvitation> comparator,
            final Filter<? super ContactInvitation> filter) {
        logger.logApiId();
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final List<OutgoingInvitation> invitations =
                contactIO.readOutgoingInvitations();
        FilterManager.filterOutgoingInvitations(invitations, filter);
        ModelSorter.sortOutgoingContactInvitations(invitations, comparator);
        return invitations;
    }

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations(
            final Filter<? super ContactInvitation> filter) {
        logger.logApiId();
        logger.logVariable("filter", filter);
        return readOutgoingInvitations(defaultInvitationComparator, filter);
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
     * @see com.thinkparity.ophelia.model.contact.ContactModel#searchOutgoingInvitations(java.lang.String)
     *
     */
    public List<Long> searchOutgoingInvitations(final String expression) {
        try {
            return getIndexModel().searchIncomingInvitations(expression);
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
     * Fire a notification event that an outgoing invitation has been accepted.
     * 
     * @param contact
     *            A contact.
     * @param outgoingInvitation
     *            An outgoing invitation.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyOutgoingInvitationAccepted(final Contact contact,
            final OutgoingInvitation outgoingInvitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingInvitationAccepted(eventGenerator.generate(
                        contact, outgoingInvitation));
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
    private void notifyOutgoingInvitationCreated(
            final OutgoingInvitation outgoingInvitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingInvitationCreated(eventGenerator
                        .generate(outgoingInvitation));
            }
        });
    }

    /**
     * Notify all listeners that an outgoing invitation has been declined.
     * 
     * @param invitation
     *            The invitation.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyOutgoingInvitationDeclined(
            final OutgoingInvitation invitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingInvitationDeclined(eventGenerator.generate(invitation));
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
    private void notifyOutgoingInvitationDeleted(
            final OutgoingInvitation outgoingInvitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingInvitationDeleted(eventGenerator
                        .generate(outgoingInvitation));
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

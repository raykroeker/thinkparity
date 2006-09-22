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
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ContactIOHandler;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.util.filter.Filter;
import com.thinkparity.ophelia.model.util.filter.contact.DefaultInvitationFilter;
import com.thinkparity.ophelia.model.util.filter.contact.FilterManager;
import com.thinkparity.ophelia.model.util.filter.user.DefaultFilter;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.util.sort.contact.InvitationIdComparator;
import com.thinkparity.ophelia.model.util.sort.contact.NameComparator;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Contact Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy; raymond@thinkparity.com
 * @version 1.1.2.12
 */
class ContactModelImpl extends AbstractModelImpl<ContactListener> {

    /** The contact db io. */
    private final ContactIOHandler contactIO;

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
     * @param workspace
     *		The thinkParity workspace.
     */
    ContactModelImpl(final Workspace workspace) {
        super(workspace);
        this.contactIO = IOFactory.getDefault(workspace).createContactHandler();
        this.defaultComparator = new NameComparator(Boolean.TRUE);
        this.defaultFilter = new DefaultFilter();
        this.defaultInvitationComparator = new InvitationIdComparator();
        this.defaultInvitationFilter = new DefaultInvitationFilter();
        this.localEventGenerator = new ContactEventGenerator(ContactEvent.Source.LOCAL);
        this.remoteEventGenerator = new ContactEventGenerator(ContactEvent.Source.REMOTE);
    }

    /**
     * @see com.thinkparity.ophelia.model.AbstractModelImpl#addListener(com.thinkparity.ophelia.model.util.EventListener)
     * 
     */
    @Override
    public boolean addListener(final ContactListener listener) {
        return super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.AbstractModelImpl#removeListener(com.thinkparity.ophelia.model.util.EventListener)
     * 
     */
    @Override
    protected boolean removeListener(final ContactListener listener) {
        return super.removeListener(listener);
    }

    /**
     * Accept the incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    void acceptIncomingInvitation(final Long invitationId) {
        logApiId();
        logVariable("invitationId", invitationId);
        assertOnline();
        try {
            final IncomingInvitation invitation = readIncomingInvitation(invitationId);
            // delete the invitation
            contactIO.deleteIncomingInvitation(invitation.getId());
            // remote accept
            final InternalSessionModel sessionModel = getInternalSessionModel();
            sessionModel.acceptContactInvitation(localUserId(), invitation
                    .getInvitedBy(), currentDateTime());
            // create contact data
            final Contact remoteContact = readRemote(invitation.getInvitedBy());
            final Contact localContact = createLocal(remoteContact);
            // fire event
            notifyIncomingInvitationAccepted(localContact, invitation, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create a contact invitation.
     * 
     * @param email
     *            An e-mail address.
     */
    OutgoingInvitation createOutgoingInvitation(final EMail email) {
        logApiId();
        logVariable("email", email);
        assertOnline();
        assertDoesNotExist("CONTACT ALREADY EXISTS", email);
        try {
            final OutgoingInvitation outgoing = new OutgoingInvitation();
            outgoing.setCreatedBy(localUser().getLocalId());
            outgoing.setCreatedOn(currentDateTime());
            outgoing.setEmail(email);
            contactIO.createOutgoingInvitation(outgoing);
            getInternalSessionModel().extendInvitation(email);
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
    void declineIncomingInvitation(final Long invitationId) {
        logApiId();
        logVariable("invitationId", invitationId);
        assertOnline();
        try {
            // decline
            final IncomingInvitation invitation = readIncomingInvitation(invitationId);
            getInternalSessionModel().declineInvitation(
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
    void delete(final JabberId contactId) {
        logApiId();
        logVariable("contactId", contactId);
        assertOnline();
        try {
            final Contact contact = read(contactId);
            // delete data
            deleteLocal(contactId);
            // delete remote
            getInternalSessionModel().deleteContact(localUserId(), contactId);
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
    void deleteOutgoingInvitation(final Long invitationId) {
        logApiId();
        logVariable("invitationId", invitationId);
        assertOnline();
        try {
            final OutgoingInvitation invitation = readOutgoingInvitation(invitationId);
            contactIO.deleteOutgoingInvitation(invitationId);
            // delete remote
            getInternalSessionModel().deleteContactInvitation(localUserId(),
                    invitation.getEmail(), currentDateTime());
            // fire event
            notifyOutgoingInvitationDeleted(invitation, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Download the contacts from the server and create the local data.
     *
     */
    void download() {
        logApiId();
        assertOnline();
        try {
            final List<Contact> remoteContacts =
                getInternalSessionModel().readContactList(localUserId());
            Contact localContact; 
            for(final Contact remoteContact : remoteContacts) {
                logVariable("remoteContact", remoteContact);
                localContact = read(remoteContact.getId());
                if (null == localContact) {
                    createLocal(remoteContact);
                }
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
    void handleContactDeleted(final JabberId deletedBy, final Calendar deletedOn) {
        final Contact contact = read(deletedBy);
        // delete data
        deleteLocal(deletedBy);
        // fire event
        notifyContactDeleted(contact, remoteEventGenerator);
    }

    /**
     * Handle the remote event generated when a contact is updated.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     * @param updatedOn
     *            When the contact was updated <code>Calendar</code>.
     */
    void handleContactUpdated(final JabberId contactId, final Calendar updatedOn) {
        final Contact localContact = read(contactId);
        final Contact remoteContact = readRemote(contactId);
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
        // fire event
        notifyContactUpdated(remoteContact, remoteEventGenerator);
    }

    /**
     * Handle the invitation accepted remote event.
     * 
     * @param acceptedBy
     *            By whom the invitation was accepted.
     * @param acceptedOn
     *            When the invitation was accepted.
     */
    void handleInvitationAccepted(final JabberId acceptedBy,
            final Calendar acceptedOn) {
       logApiId();
       logVariable("acceptedBy", acceptedBy);
       logVariable("acceptedOn", acceptedOn);
       final Contact remoteContact = readRemote(acceptedBy);
       // delete invitation(s)
       final List<OutgoingInvitation> outgoingInvitations = new ArrayList<OutgoingInvitation>();
       for (final EMail email : remoteContact.getEmails()) {
           outgoingInvitations.add(contactIO.readOutgoingInvitation(email));
           contactIO.deleteOutgoingInvitation(
                   outgoingInvitations.get(
                           outgoingInvitations.size() - 1).getId());
       }
       final Contact localContact = createLocal(remoteContact);
       // fire event(s)
       for (final OutgoingInvitation outgoingInvitation : outgoingInvitations) {
           notifyOutgoingInvitationAccepted(localContact, outgoingInvitation, remoteEventGenerator);
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
    void handleInvitationDeclined(final EMail invitedAs,
            final JabberId declinedBy, final Calendar declinedOn) {
       logApiId();
       logVariable("declinedBy", declinedBy);
       logVariable("declinedOn", declinedOn);
       // delete invitation
       final OutgoingInvitation invitation =
               contactIO.readOutgoingInvitation(invitedAs);
       contactIO.deleteOutgoingInvitation(invitation.getId());
       // fire event(s)
       notifyOutgoingInvitationDeclined(invitation, remoteEventGenerator);
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
    void handleInvitationDeleted(final EMail invitedAs,
            final JabberId deletedBy, final Calendar deletedOn) {
        logApiId();
        logVariable("invitedAs", invitedAs);
        logVariable("deletedBy", deletedBy);
        logVariable("deletedOn", deletedOn);
        try {
            final List<IncomingInvitation> invitations = readIncomingInvitations();
            final List<IncomingInvitation> deletedInvitations =
                new ArrayList<IncomingInvitation>(invitations.size());
            // delete local data
            for (final IncomingInvitation invitation : invitations) {
                if (invitation.getInvitedAs().equals(invitedAs)) {
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
    void handleInvitationExtended(final EMail extendedTo,
            final JabberId extendedBy, final Calendar extendedOn) {
        logApiId();
        logVariable("extendedTo", extendedTo);
        logVariable("extendedBy", extendedBy);
        logVariable("extendedOn", extendedOn);
        // create user data
        final InternalUserModel userModel = getInternalUserModel();
        final User extendedByUser = userModel.readLazyCreate(extendedBy);
        final IncomingInvitation incoming = new IncomingInvitation();
        incoming.setCreatedBy(localUser().getLocalId());
        incoming.setCreatedOn(currentDateTime());
        incoming.setInvitedAs(extendedTo);
        incoming.setInvitedBy(extendedByUser.getId());
        contactIO.createIncomingInvitation(incoming, extendedByUser);
        // fire event
        final IncomingInvitation postCreation = contactIO.readIncomingInvitation(incoming.getId());
        notifyIncomingInvitationCreated(postCreation, localEventGenerator);
    }

    /**
     * Read a list of contacts.
     * 
     * @return A list of contacts.
     */
    List<Contact> read() {
        logApiId();
        return read(defaultComparator, defaultFilter);
    }

    /**
     * Read a list of contacts.
     * 
     * @param comparator
     *            A user comparator to sort the contacts.
     * @return A list of contacts.
     */
    List<Contact> read(final Comparator<Contact> comparator) {
        logApiId();
        logVariable("comparator", comparator);
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
    List<Contact> read(final Comparator<Contact> comparator,
            final Filter<? super Contact> filter) {
        logApiId();
        logVariable("comparator", comparator);
        logVariable("filter", filter);
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
    List<Contact> read(final Filter<? super Contact> filter) {
        logApiId();
        logVariable("filter", filter);
        return read(defaultComparator, filter);
    }

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    Contact read(final JabberId contactId) {
        logApiId();
        return contactIO.read(contactId);
    }

    /**
     * Read an incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An incoming invitation.
     */
    IncomingInvitation readIncomingInvitation(final Long invitationId) {
        logApiId();
        logVariable("invitationId", invitationId);
        return contactIO.readIncomingInvitation(invitationId);
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    List<IncomingInvitation> readIncomingInvitations() {
        logApiId();
        return readIncomingInvitations(defaultInvitationComparator, defaultInvitationFilter);
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    List<IncomingInvitation> readIncomingInvitations(
            final Comparator<ContactInvitation> comparator) {
        logApiId();
        logVariable("comparator",comparator);
        return readIncomingInvitations(comparator, defaultInvitationFilter);
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    List<IncomingInvitation> readIncomingInvitations(
            final Comparator<ContactInvitation> comparator,
            final Filter<? super ContactInvitation> filter) {
        logApiId();
        logVariable("comparator", comparator);
        logVariable("filter", filter);
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
    List<IncomingInvitation> readIncomingInvitations(
            final Filter<? super ContactInvitation> filter) {
        logApiId();
        logVariable("filter", filter);
        return readIncomingInvitations(defaultInvitationComparator, filter);
    }

    OutgoingInvitation readOutgoingInvitation(final Long invitationId) {
        logApiId();
        logVariable("invitationId", invitationId);
        return contactIO.readOutgoingInvitation(invitationId);
    }

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    List<OutgoingInvitation> readOutgoingInvitations() {
        logApiId();
        return readOutgoingInvitations(defaultInvitationComparator, defaultInvitationFilter);
    }

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    List<OutgoingInvitation> readOutgoingInvitations(
            final Comparator<ContactInvitation> comparator) {
        logApiId();
        logVariable("comparator",comparator);
        return readOutgoingInvitations(comparator, defaultInvitationFilter);
    }

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    List<OutgoingInvitation> readOutgoingInvitations(
            final Comparator<ContactInvitation> comparator,
            final Filter<? super ContactInvitation> filter) {
        logApiId();
        logVariable("comparator", comparator);
        logVariable("filter", filter);
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
    List<OutgoingInvitation> readOutgoingInvitations(
            final Filter<? super ContactInvitation> filter) {
        logApiId();
        logVariable("filter", filter);
        return readOutgoingInvitations(defaultInvitationComparator, filter);
    }

    /**
     * Search for contacts.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    List<JabberId> search(final String expression) {
        logApiId();
        logVariable("expression", expression);
        try {
            return getIndexModel().searchContacts(expression);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Assert that no contact with the given e-mail address exists.
     * 
     * @param assertion
     *            An assertion.
     * @param email
     *            An e-mail address.
     */
    private void assertDoesNotExist(final Object assertion, final EMail email) {
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
        final InternalUserModel userModel = getInternalUserModel();
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
        return getInternalSessionModel().readContact(localUserId(), contactId);
    }
}

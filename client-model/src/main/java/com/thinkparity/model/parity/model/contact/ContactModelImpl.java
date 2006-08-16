/*
 * Generated On: Jun 27 06 04:14:53 PM
 */
package com.thinkparity.model.parity.model.contact;

import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.api.events.ContactEvent;
import com.thinkparity.model.parity.api.events.ContactListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.contact.DefaultInvitationFilter;
import com.thinkparity.model.parity.model.filter.contact.FilterManager;
import com.thinkparity.model.parity.model.filter.user.DefaultFilter;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ContactIOHandler;
import com.thinkparity.model.parity.model.session.InternalSessionModel;
import com.thinkparity.model.parity.model.sort.ModelSorter;
import com.thinkparity.model.parity.model.sort.contact.InvitationIdComparator;
import com.thinkparity.model.parity.model.sort.contact.NameComparator;
import com.thinkparity.model.parity.model.user.InternalUserModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * <b>Title:</b>thinkParity Contact Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy; raymond@thinkparity.com
 * @version 1.1.2.12
 */
class ContactModelImpl extends AbstractModelImpl {

    /** A list of contact event listeners. */
    private static final List<ContactListener> LISTENERS = new LinkedList<ContactListener>();

    /**
     * Obtain an apache api log id.
     * 
     * @param api
     *            The api.
     * @return The log id.
     */
    private static StringBuffer getApiId(final String api) {
        return getModelId("[Contact]").append(" ").append(api);
    }

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
        this.contactIO = IOFactory.getDefault().createContactHandler();
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
    void acceptIncomingInvitation(final Long invitationId) {
        logApiId();
        debugVariable("invitationId", invitationId);
        final IncomingInvitation invitation = readIncomingInvitation(invitationId);
        // update contact list
        final InternalSessionModel sessionModel = getInternalSessionModel();
        sessionModel.acceptInvitation(invitation.getUserId());
        // create contact data
        final Contact contact = readRemote(invitation.getUserId());
        contactIO.create(contact);
        // delete invitation
        contactIO.deleteIncomingInvitation(invitation.getId());
        // fire event
        notifyIncomingInvitationAccepted(contact, invitation, localEventGenerator);
    }

    /**
     * Add a contact listener.
     * 
     * @param listener
     *            A contact listener.
     */
    void addListener(final ContactListener listener) {
        logApiId();
        debugVariable("listener", listener);
        Assert.assertNotNull("LISTENER IS NULL", listener);
        synchronized (LISTENERS) {
            if (LISTENERS.contains(listener)) {
                return;
            }
            LISTENERS.add(listener);
        }
    }

    /**
     * Create a contact invitation.
     * 
     * @param email
     *            An e-mail address.
     */
    OutgoingInvitation createOutgoingInvitation(final String email) {
        logApiId();
        debugVariable("email", email);
        assertOnline("USER NOT ONLINE");
        assertDoesNotExist("CONTACT ALREADY EXISTS", email);
//assertInvitationDoesNotExist(getApiId("[CREATE INVITATION] [CONTACT INVITATION ALREADY EXISTS]"), email);

        final OutgoingInvitation outgoing = new OutgoingInvitation();
        outgoing.setCreatedBy(localUser().getLocalId());
        outgoing.setCreatedOn(currentDateTime());
        outgoing.setEmail(email);
        contactIO.createOutgoingInvitation(outgoing);
        getInternalSessionModel().inviteContact(email);
        // fire event
        final OutgoingInvitation postCreation = contactIO.readOutgoingInvitation(email);
        notifyOutgoingInvitationCreated(postCreation, localEventGenerator);
        return postCreation;
    }

    /**
     * Accept the incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    void declineIncomingInvitation(final Long invitationId) {
        logApiId();
        debugVariable("invitationId", invitationId);
        // decline
        final IncomingInvitation incoming = readIncomingInvitation(invitationId);
        getInternalSessionModel().declineInvitation(incoming.getUserId());
        // delete invitation data
        contactIO.deleteIncomingInvitation(incoming.getId());
        // fire event
        notifyIncomingInvitationDeclined(incoming, localEventGenerator);
    }

    /**
     * Delete a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    void delete(final JabberId contactId) {
        logger.info(getApiId("[DELETE]"));
        logger.debug(contactId);
        logger.warn(getApiId("[DELETE] [NOT YET IMPLEMENTED]"));
    }

    /**
     * Delete an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    void deleteOutgoingInvitation(final Long invitationId) {
        logApiId();
        debugVariable("invitationId", invitationId);
        final OutgoingInvitation preDeletion = readOutgoingInvitation(invitationId);
        contactIO.deleteOutgoingInvitation(invitationId);
        // fire event
        notifyOutgoingInvitationDeleted(preDeletion, localEventGenerator);
    }

    /**
     * Download the contacts from the server and create the local data.
     *
     */
    void download() {
        logger.info(getApiId("[DOWNLOAD]"));
        logger.warn(getApiId("[DOWNLOAD]"));
        final List<Contact> remoteContacts = getInternalSessionModel().readContactList();
        for(final Contact remoteContact : remoteContacts) {
            contactIO.create(remoteContact);
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
    void handleInvitationAccepted(final JabberId acceptedBy,
            final Calendar acceptedOn) {
       logApiId();
       debugVariable("acceptedBy", acceptedBy);
       debugVariable("acceptedOn", acceptedOn);
       final Contact contact = readRemote(acceptedBy);
       // delete invitation
       final OutgoingInvitation outgoing =
           contactIO.readOutgoingInvitation(contact.getEmails().get(0));
       contactIO.deleteOutgoingInvitation(outgoing.getId());
       // create the contact data
       contactIO.create(contact);
       // fire event
       notifyOutgoingInvitationAccepted(contact, outgoing, remoteEventGenerator);
   }

    /**
     * Handle the invitation declined remote event.
     * 
     * @param declinedBy
     *            By whom the invitation was declined.
     * @param declinedOn
     *            When the invitation was declined.
     */
    void handleInvitationDeclined(final JabberId declinedBy,
           final Calendar declinedOn) {
       logApiId();
       debugVariable("declinedBy", declinedBy);
       debugVariable("declinedOn", declinedOn);

       // download the user's info
       final InternalUserModel userModel = getInternalUserModel();
       User invitedByUser = userModel.read(declinedBy);
       if (null == invitedByUser) {
           invitedByUser = userModel.create(declinedBy);
       }

       final IncomingInvitation incoming = new IncomingInvitation();
       incoming.setCreatedBy(localUser().getLocalId());
       incoming.setCreatedOn(currentDateTime());
       incoming.setUserId(invitedByUser.getId());
       contactIO.createIncomingInvitation(incoming, invitedByUser);
       // fire event
       final IncomingInvitation postCreation = contactIO.readIncomingInvitation(incoming.getId());
       notifyIncomingInvitationCreated(postCreation, localEventGenerator);
   }

    /**
     * Handle the invitation extended remote event.
     * 
     * @param invitedBy
     *            By whom the invitation was extended.
     * @param invitedOn
     *            When the invitation was extended.
     */
    void handleInvitationExtended(final JabberId invitedBy,
            final Calendar invitedOn) {
        logApiId();
        debugVariable("invitedBy", invitedBy);
        debugVariable("invitedOn", invitedOn);

        // download the user's info
        final InternalUserModel userModel = getInternalUserModel();
        User invitedByUser = userModel.read(invitedBy);
        if (null == invitedByUser) {
            invitedByUser = userModel.create(invitedBy);
        }

        final IncomingInvitation incoming = new IncomingInvitation();
        incoming.setCreatedBy(localUser().getLocalId());
        incoming.setCreatedOn(currentDateTime());
        incoming.setUserId(invitedByUser.getId());
        contactIO.createIncomingInvitation(incoming, invitedByUser);
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
        logger.info(getApiId("[READ]"));
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
        logger.info(getApiId("[READ]"));
        logger.debug(comparator);
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
        logger.info(getApiId("[READ]"));
        logger.debug(comparator);
        logger.debug(filter);
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
        logger.info(getApiId("[READ]"));
        logger.debug(filter);
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
        logger.info(getApiId("[READ]"));
        logger.debug(contactId);
        logger.warn(getApiId("[READ] [NOT YET IMPLEMENTED]"));
        final List<Contact> contacts = getInternalSessionModel().readContactList();
        for(final Contact contact : contacts) {
            if(contact.getId().equals(contactId)) { return contact; }
        }
        return null;
    }

    IncomingInvitation readIncomingInvitation(final Long invitationId) {
        logApiId();
        debugVariable("invitationId", invitationId);
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
        debugVariable("comparator",comparator);
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
        debugVariable("comparator", comparator);
        debugVariable("filter", filter);
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
        debugVariable("filter", filter);
        return readIncomingInvitations(defaultInvitationComparator, filter);
    }

    OutgoingInvitation readOutgoingInvitation(final Long invitationId) {
        logApiId();
        debugVariable("invitationId", invitationId);
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
        debugVariable("comparator",comparator);
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
        debugVariable("comparator", comparator);
        debugVariable("filter", filter);
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
        debugVariable("filter", filter);
        return readOutgoingInvitations(defaultInvitationComparator, filter);
    }

    /**
     * Remove a contact listener.
     * 
     * @param listener
     *            A contact listener.
     */
    void removeListener(final ContactListener listener) {
        logApiId();
        debugVariable("listener", listener);
        Assert.assertNotNull("[LISTENER IS NULL]", listener);
        synchronized (LISTENERS) {
            if (!LISTENERS.contains(listener)) {
                return;
            }
            LISTENERS.remove(listener);
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
    private void assertDoesNotExist(final Object assertion, final String email) {
        Assert.assertIsNull(assertion, read(email));
    }

    /**
     * Assert that no contact invitation with the given e-mail address exists.
     * 
     * @param assertion
     *            An assertion.
     * @param email
     *            An e-mail address.
     */
    private void assertInvitationDoesNotExist(final Object assertion, final String email) {
        throw Assert
                .createNotYetImplemented("ContactModelImpl#assertInvitationDoesNotExist(Object,String)");
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
        synchronized (LISTENERS) {
            for (final ContactListener l : LISTENERS) {
                l.incomingInvitationAccepted(eventGenerator.generate(contact,
                        incomingInvitation));
            }
        }
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
        synchronized (LISTENERS) {
            for (final ContactListener l : LISTENERS) {
                l.incomingInvitationCreated(eventGenerator.generate(incomingInvitation));
            }
        }
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
        synchronized (LISTENERS) {
            for (final ContactListener l : LISTENERS) {
                l.incomingInvitationDeclined(eventGenerator.generate(incomingInvitation));
            }
        }
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
        synchronized (LISTENERS) {
            for (final ContactListener l : LISTENERS) {
                l.outgoingInvitationAccepted(eventGenerator.generate(contact,
                        outgoingInvitation));
            }
        }
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
        synchronized (LISTENERS) {
            for (final ContactListener l : LISTENERS) {
                l.outgoingInvitationCreated(eventGenerator.generate(outgoingInvitation));
            }
        }
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
        synchronized (LISTENERS) {
            for (final ContactListener l : LISTENERS) {
                l.outgoingInvitationDeleted(eventGenerator.generate(outgoingInvitation));
            }
        }
    }

    /**
     * Read a contact for an e-mail address.
     * 
     * @param email
     *            An e-mail address.
     * @return A contact.
     */
    private Contact read(final String email) {
        return null;
    }

    private Contact readRemote(final JabberId contactId) {
        logger.info(getApiId("[READ]"));
        logger.debug(contactId);
        logger.warn(getApiId("[READ] [NOT YET IMPLEMENTED]"));
        final List<Contact> contacts = getInternalSessionModel().readContactList();
        for(final Contact contact : contacts) {
            if(contact.getId().equals(contactId)) { return contact; }
        }
        return null;
    }
}

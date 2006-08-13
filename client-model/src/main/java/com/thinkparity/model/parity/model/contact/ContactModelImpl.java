/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.contact;

import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.contact.FilterManager;
import com.thinkparity.model.parity.model.filter.user.DefaultFilter;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ContactIOHandler;
import com.thinkparity.model.parity.model.sort.ModelSorter;
import com.thinkparity.model.parity.model.sort.contact.NameComparator;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * <b>Title:</b>thinkParity Contact Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version $Revision$
 */
class ContactModelImpl extends AbstractModelImpl {

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
    }

    /**
     * Create a contact invitation.
     * 
     * @param email
     *            An e-mail address.
     */
    ContactInvitation createInvitation(final String email) {
        logger.info(getApiId("[CREATE INVITATION]"));
        logger.debug(email);
        assertOnline(getApiId("[CREATE INVITATION] [USER NOT ONLINE]"));
        assertDoesNotExist(getApiId("[CREATE INVITATION] [CONTACT ALREADY EXISTS]"), email);
        assertInvitationDoesNotExist(getApiId("[CREATE INVITATION] [CONTACT INVITATION ALREADY EXISTS]"), email);

        final ContactInvitation invitation = new ContactInvitation();
        invitation.setCreatedBy(localUserId());
        invitation.setCreatedOn(currentDateTime());
        invitation.setEmail(email);
        contactIO.createInvitation(invitation, localUser());
        getInternalSessionModel().inviteContact(email);
        return contactIO.readInvitation(email);
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
     * Download the contacts from the server and create the local data.
     *
     */
    void download() {
        logger.info(getApiId("[DOWNLOAD]"));
        logger.warn(getApiId("[DOWNLOAD]"));
        try {
            final List<Contact> remoteContacts = getInternalSessionModel().readContactList();
            for(final Contact remoteContact : remoteContacts) {
                contactIO.create(remoteContact);
            }
        }
        catch(final ParityException px) {
            throw translateError(getApiId("[DOWNLOAD]"), px);
        }
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
        try {
            final List<Contact> contacts = getInternalSessionModel().readContactList();
            for(final Contact contact : contacts) {
                if(contact.getId().equals(contactId)) { return contact; }
            }
            return null;
        }
        catch(final ParityException px) {
            logger.error(px);
            return null;
        }
    }

    /**
     * Read a contact invitation for an e-mail address.
     * 
     * @param email
     *            An e-mail address.
     * @return A contact invitation.
     */
    ContactInvitation readInvitation(final String email) {
        logApiId();
        debugVariable("email", email);
        try { return contactIO.readInvitation(email); }
        catch(final Throwable t) { throw translateError("[READ INVITATION]", t); }
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
        Assert.assertIsNull(assertion, readInvitation(email));
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
}

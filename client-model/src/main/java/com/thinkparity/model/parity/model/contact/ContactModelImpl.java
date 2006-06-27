/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.contact;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.user.DefaultFilter;
import com.thinkparity.model.parity.model.sort.user.UserComparatorFactory;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

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

    /** The default contact comparator. */
    private final Comparator<User> defaultComparator;

    /** The default contact filter. */
    private final Filter<? super User> defaultFilter;

    /**
     * Create ContactModelImpl.
     *
     * @param workspace
     *		The thinkParity workspace.
     */
    ContactModelImpl(final Workspace workspace) {
        super(workspace);
        this.defaultComparator = UserComparatorFactory.createName(Boolean.TRUE);
        this.defaultFilter = new DefaultFilter();
    }

    /**
     * Add an e-mail contact.
     * 
     * @param email
     *            An e-mail address.
     */
    Contact create(final String email) {
        logger.info(getApiId("[CREATE]"));
        logger.debug(email);
        logger.warn(getApiId("[CREATE] [NOT YET IMPLEMENTED]"));
        return read((JabberId) null);
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
    List<Contact> read(final Comparator<User> comparator) {
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
    List<Contact> read(final Comparator<User> comparator,
            final Filter<? super User> filter) {
        logger.info(getApiId("[READ]"));
        logger.debug(comparator);
        logger.debug(filter);
        final List<Contact> contactList = new LinkedList<Contact>();
        try { contactList.addAll(getSessionModel().readContacts()); } 
        catch(final ParityException px) { throw new RuntimeException(px); }
        return contactList;
    }

    /**
     * Read a list of contacts.
     * 
     * @param filter
     *            A user filter to scope the contacts.
     * @return A list of contacts.
     */
    List<Contact> read(final Filter<? super User> filter) {
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
        logger.info(getApiId("[DELETE]"));
        logger.debug(contactId);
        logger.warn(getApiId("[DELETE] [NOT YET IMPLEMENTED]"));
        final Contact contact = new Contact();
        contact.setEmail("user@domain.com");
        contact.setId(JabberIdBuilder.parseQualifiedJabberId("user@thinkparity.dyndns.org/parity"));
        contact.setLocalId(new Long(-1));
        contact.setName("Fake User");
        contact.setOrganization("Fake User's Organization");
        return contact;
    }
}

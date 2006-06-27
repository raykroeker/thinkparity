/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.contact;

import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;
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

    /**
     * Create ContactModelImpl.
     *
     * @param workspace
     *		The thinkParity workspace.
     */
    ContactModelImpl(final Workspace workspace) {
        super(workspace);
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
        return read(null);
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

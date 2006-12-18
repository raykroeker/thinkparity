/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadIds extends AbstractHandler {

    /**
     * Create ReadIds.
     *
     */
	public ReadIds() {
        super("contact:readids");
	}

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider,
     *      com.thinkparity.desdemona.util.service.ServiceRequestReader,
     *      com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     * 
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        final List<Contact> contacts = read(provider,
                reader.readJabberId("userId"));

        final List<JabberId> contactIds = new ArrayList<JabberId>(contacts.size());
        for (final Contact contact : contacts) {
            contactIds.add(contact.getId());
        }
        writer.writeJabberIds("contactIds", "contactId", contactIds);
    }

    /**
     * @see ContactModel#readContacts(JabberId)
     */
	private List<Contact> read(final ServiceModelProvider provider,
            final JabberId userId) {
        return provider.getContactModel().read(userId);
    }
}

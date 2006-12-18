/*
 * Created On: Aug 16, 2006 5:21:23 PM
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.desdemona.model.Constants.Xml;
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
public final class Read extends AbstractHandler {

    /**
     * Create Read.
     *
     */
    public Read() { super("contact:read"); }

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
        final Contact contact = read(provider, reader.readJabberId("userId"),
                    reader.readJabberId("contactId"));

        if(null != contact) {
            writer.writeEMails(Xml.Contact.EMAILS, Xml.Contact.EMAIL, contact.getEmails());
            writer.writeJabberId(Xml.Contact.JABBER_ID, contact.getId());
            writer.writeString(Xml.Contact.NAME, contact.getName());
            if (contact.isSetOrganization())
                writer.writeString(Xml.Contact.ORGANIZATION, contact.getOrganization());
            if (contact.isSetTitle())
                writer.writeString("title", contact.getTitle());
            writer.writeVCard("vcard", contact.getVCard());
        }
    }

    /**
     * @see ContactModel#read(JabberId, JabberId)
     */
    private Contact read(final ServiceModelProvider context,
            final JabberId userId, final JabberId contactId) {
        return context.getContactModel().read(userId, contactId);
    }
}

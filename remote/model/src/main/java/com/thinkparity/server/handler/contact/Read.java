/*
 * Created On: Aug 16, 2006 5:21:23 PM
 */
package com.thinkparity.server.handler.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;
import com.thinkparity.server.model.contact.Contact;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Read extends AbstractController {

    /** Create Read. */
    public Read() { super("contact:read"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        final Contact contact = read(readJabberId(Xml.Contact.JABBER_ID));

        if(null != contact) {
            writeEMails(Xml.Contact.EMAILS, Xml.Contact.EMAIL, contact.getEmails());
            writeJabberId(Xml.Contact.JABBER_ID, contact.getId());
            writeString(Xml.Contact.NAME, contact.getName());
            writeString(Xml.Contact.ORGANIZATION, contact.getOrganization());
            writeString(Xml.Contact.VCARD, contact.getVCard().asXML());
        }
    }

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    private Contact read(final JabberId contactId) {
        return getContactModel().readContact(contactId);
    }
}

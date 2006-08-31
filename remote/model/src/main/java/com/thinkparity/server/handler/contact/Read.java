/*
 * Created On: Aug 16, 2006 5:21:23 PM
 */
package com.thinkparity.server.handler.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;
import com.thinkparity.server.model.contact.Contact;
import com.thinkparity.server.model.contact.ContactModel;

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
        final Contact contact = read(readJabberId("userId"),
                readJabberId("contactId"));

        if(null != contact) {
            writeEMails(Xml.Contact.EMAILS, Xml.Contact.EMAIL, contact.getEmails());
            writeJabberId(Xml.Contact.JABBER_ID, contact.getId());
            writeString(Xml.Contact.NAME, contact.getName());
            writeString(Xml.Contact.ORGANIZATION, contact.getOrganization());
            writeString("title", contact.getTitle());
            writeString(Xml.Contact.VCARD, contact.getVCard().asXML());
        }
    }

    /**
     * @see ContactModel#read(JabberId, JabberId)
     */
    private Contact read(final JabberId userId, final JabberId contactId) {
        return getContactModel().read(userId, contactId);
    }
}

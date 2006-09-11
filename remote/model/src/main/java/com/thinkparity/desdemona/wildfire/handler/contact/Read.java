/*
 * Created On: Aug 16, 2006 5:21:23 PM
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.desdemona.model.Constants.Xml;
import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Read extends AbstractHandler {

    /** Create Read. */
    public Read() { super("contact:read"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final Contact contact =
            read(readJabberId("userId"), readJabberId("contactId"));

        if(null != contact) {
            writeEMails(Xml.Contact.EMAILS, Xml.Contact.EMAIL, contact.getEmails());
            writeJabberId(Xml.Contact.JABBER_ID, contact.getId());
            writeString(Xml.Contact.NAME, contact.getName());
            if (contact.isSetOrganization())
                writeString(Xml.Contact.ORGANIZATION, contact.getOrganization());
            if (contact.isSetTitle())
                writeString("title", contact.getTitle());
        }
    }

    /**
     * @see ContactModel#read(JabberId, JabberId)
     */
    private Contact read(final JabberId userId, final JabberId contactId) {
        return getContactModel().read(userId, contactId);
    }
}

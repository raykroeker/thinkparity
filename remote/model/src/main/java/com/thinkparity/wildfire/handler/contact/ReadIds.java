/*
 * Feb 28, 2006
 */
package com.thinkparity.wildfire.handler.contact;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.contact.Contact;
import com.thinkparity.model.contact.ContactModel;

import com.thinkparity.wildfire.handler.AbstractHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadIds extends AbstractHandler {

	/** Create a ReadIds. */
	public ReadIds() { super("contact:readids"); }

	/**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final List<Contact> contacts = read(readJabberId("userId"));

        final List<JabberId> contactIds = new ArrayList<JabberId>(contacts.size());
        for (final Contact contact : contacts) {
            contactIds.add(contact.getId());
        }
        writeJabberIds("contactIds", "contactId", contactIds);
    }

    /**
     * @see ContactModel#readContacts(JabberId)
     */
	private List<Contact> read(final JabberId userId) {
        return getContactModel().read(userId);
    }
}

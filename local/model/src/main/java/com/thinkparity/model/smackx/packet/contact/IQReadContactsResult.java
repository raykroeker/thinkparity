/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.contact;

import java.util.Set;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadContactsResult extends IQ {

	/**
	 * A list of contacts.
	 * 
	 */
	private final Set<Contact> contacts;

	/**
	 * Create a IQReadContacts.
	 * 
	 */
	public IQReadContactsResult(final Set<Contact> contacts) {
		super();
		this.contacts = contacts;
	}

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }

	/**
	 * Obtain the list of contacts.
	 * @return The list of contacts.
	 */
	public Set<Contact> getContacts() { return contacts; }
}

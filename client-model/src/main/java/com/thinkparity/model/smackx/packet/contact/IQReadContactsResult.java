/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.contact;

import java.util.List;

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
	private final List<Contact> contacts;

	/**
	 * Create a IQReadContacts.
	 * 
	 */
	public IQReadContactsResult(final List<Contact> contacts) {
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
	public List<Contact> getContacts() { return contacts; }
}

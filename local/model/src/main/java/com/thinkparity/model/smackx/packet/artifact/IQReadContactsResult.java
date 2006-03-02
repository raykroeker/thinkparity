/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.artifact;

import java.util.List;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadContactsResult extends IQ {

	private final List<Contact> contacts;

	/**
	 * Create a IQReadUsersResult.
	 */
	IQReadContactsResult(final List<Contact> contacts) {
		super();
		this.contacts = contacts;
	}

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }

	public List<Contact> getContacts() { return contacts; }
}

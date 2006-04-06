/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.artifact;

import java.util.Set;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadContactsResult extends IQ {

	private final Set<User> contacts;

	/**
	 * Create a IQReadUsersResult.
	 */
	IQReadContactsResult(final Set<User> contacts) {
		super();
		this.contacts = contacts;
	}

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }

	public Set<User> getContacts() { return contacts; }
}

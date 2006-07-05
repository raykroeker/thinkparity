/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.artifact;

import java.util.List;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadContactsResult extends IQ {

	private final List<User> contacts;

	/**
	 * Create a IQReadUsersResult.
	 */
	IQReadContactsResult(final List<User> contacts) {
		super();
		this.contacts = contacts;
	}

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }

	public List<User> getContacts() { return contacts; }
}

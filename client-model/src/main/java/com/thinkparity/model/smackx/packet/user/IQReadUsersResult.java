/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.user;

import java.util.List;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadUsersResult extends IQ {

	private final List<User> users;

	/**
	 * Create a IQReadUsersResult.
	 */
	public IQReadUsersResult(final List<User> users) {
		super();
		this.users = users;
	}

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }

	public List<User> getUsers() { return users; }
}

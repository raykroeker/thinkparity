/*
 * Mar 1, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet.user;

import java.util.Set;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.codebase.model.user.User;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadUsersResult extends IQ {

	private final Set<User> users;

	/**
	 * Create a IQReadUsersResult.
	 */
	public IQReadUsersResult(final Set<User> users) {
		super();
		this.users = users;
	}

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }

	public Set<User> getUsers() { return users; }
}

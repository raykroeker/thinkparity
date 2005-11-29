/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.user;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class User {

	private final String username;

	/**
	 * Create a User.
	 */
	public User(final String username) {
		super();
		this.username = username;
	}

	/**
	 * Obtain the username.
	 * 
	 * @return The username.
	 */
	public String getUsername() { return username; }

}

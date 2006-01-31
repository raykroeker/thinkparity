/*
 * May 14, 2005
 */
package com.thinkparity.model.xmpp.user;

/**
 * Represents a parity user.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2.2.1
 */
public class User {

	/**
	 * The user's presence types.
	 * 
	 */
	public enum Presence { AVAILABLE, OFFLINE, UNAVAILABLE }

	/**
	 * The user's name.
	 * 
	 */
	private final String name;

	/**
	 * The user's presence info.
	 * 
	 */
	private final Presence presence;

	/**
	 * The fully qualified username. This contains the username; the domain
	 * information as well as the resource information.
	 * 
	 * @see #getUsername()
	 * @see #getSimpleUsername()
	 */
	private final String username;

	/**
	 * Create a User.
	 * 
	 * @param name
	 *            The name of the user.
	 * @param username
	 *            The fully qualified username.
	 * @param presence
	 *            The presence of the user.
	 */
	public User(final String name, final String username,
			final Presence presence) {
		super();
		this.name = name;
		this.username = username;
		this.presence = presence;
	}

	/**
	 * Obtain the name of the user.
	 * 
	 * @return The name of the user.
	 */
	public String getName() { return name; }

	/**
	 * Obtain the presence of the user.
	 * 
	 * @return The presence of the user.
	 */
	public Presence getPresence() { return presence; }

	/**
	 * Obtain the simple username of the user.
	 * 
	 * @return The simple username; without the domain\resource suffix.
	 */
	public String getSimpleUsername() {
		if(username.contains("@")) {
			return username.substring(0, username.indexOf("@"));
		}
		else { return username; }
	}

	/**
	 * Obtain the username of the user.
	 * 
	 * @return The username of the user.
	 */
	public String getUsername() { return username; }
}

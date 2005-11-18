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
	 * User presence.
	 */
	public enum Presence { AVAILABLE, OFFLINE, UNAVAILABLE }

	/**
	 * The name of the user.
	 */
	private final String name;

	/**
	 * The presence of the user.
	 */
	private final Presence presence;

	/**
	 * The username of the user.
	 */
	private final String username;

	/**
	 * Create a UserRenderer
	 * 
	 * @param name
	 *            The name of the user.
	 * @param username
	 *            The username of the user.
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
	 * Obtain the username of the user.
	 * 
	 * @return The username of the user.
	 */
	public String getUsername() { return username; }
}

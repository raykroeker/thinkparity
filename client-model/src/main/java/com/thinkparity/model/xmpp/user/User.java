/*
 * May 14, 2005
 */
package com.thinkparity.model.xmpp.user;

import com.thinkparity.codebase.log4j.Loggable;

/**
 * User Represents the parity model client's interface to all users within the
 * parity world.
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class User implements Loggable {

	/**
	 * State
	 * Potential states for a user.
	 * @author raykroeker@gmail.com
	 * @version 1.0
	 */
	public enum Presence { AVAILABLE, UNAVAILABLE, OFFLINE }

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
	 * Create a User
	 * 
	 * @param name
	 *            The name of the user.
	 * @param username
	 *            The username of the user.
	 * @param presence
	 *            The presence of the user.
	 */
	public User(final String name, final String username, final Presence presence) {
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
	 * Obtain the state of the user.
	 * 
	 * @return The State of the user.
	 */
	public Presence getState() { return presence; }

	/**
	 * Obtain the username of the user.
	 * 
	 * @return The username of the user.
	 */
	public String getUsername() { return username; }

	/**
	 * @see com.thinkparity.codebase.log4j.Loggable#logMe()
	 */
	public StringBuffer logMe() {
		return new StringBuffer("<user>")
			.append("<name>").append(name).append("</name>")
			.append("<presence>").append(presence).append("</presence>")
			.append("<username>").append(username).append("</username>")
			.append("</user>");
	}
}

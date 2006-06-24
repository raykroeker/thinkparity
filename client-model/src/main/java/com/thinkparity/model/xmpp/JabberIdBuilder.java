/*
 * Feb 15, 2006
 */
package com.thinkparity.model.xmpp;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JabberIdBuilder {

	private static final JabberIdBuilder singleton;

	private static final Object singletonLock;

	static {
		singleton = new JabberIdBuilder();
		singletonLock = new Object();
	}

	/**
	 * Parse the qualified jabber id.
	 * 
	 * @param qualifiedJabberId
	 *            The qualified jabber id: user@host/resource
	 * @return The qualified jabber id.
	 */
	public static JabberId parseQualifiedJabberId(final String qualifiedJabberId) {
		synchronized(singletonLock) {
			return singleton.doParseQualifiedJabberId(qualifiedJabberId);
		}
	}

	/**
	 * Parse the qualified username and build a jabber id.
	 * 
	 * @param qualifiedUsername
	 *            The qualified username:  user@host
	 * @return The jabber id.
	 */
	public static JabberId parseQualifiedUsername(final String qualifiedUsername) {
		synchronized(singletonLock) {
			return singleton.doParseQualifiedUsername(qualifiedUsername);
		}
	}

	/**
	 * Parse the username and build a jabber id.
	 * 
	 * @param username
	 *            The username.
	 * @return The jabber id.
	 */
	public static JabberId parseUsername(final String username) {
		synchronized(singletonLock) {
			return singleton.doParseUsername(username);
		}
	}

	private final String defaultHost;

	private final String defaultResource;

	/**
	 * The qualified jabber id.
	 * 
	 */
	private String qualifiedJID;

	/**
	 * The qualified username.
	 * 
	 */
	private String qualifiedUsername;

	public JabberIdBuilder(final String qualifiedJID) {
		this();
		final int indexOfAt = qualifiedJID.indexOf('@');
		final String username = qualifiedJID.substring(0, indexOfAt);
		final int indexOfSlash = qualifiedJID.indexOf('/');
		final String host = qualifiedJID.substring(indexOfAt + 1, indexOfSlash);
		final String resource = qualifiedJID.substring(indexOfSlash + 1);
		setQUalifiedJID(username, host, resource);
		setQualifiedUsername(username, host);
	}

	/**
	 * Create a JabberIdBuilder.
	 * 
	 * @param user
	 *            The parity user.
	 */
	public JabberIdBuilder(final User user) {
		this();
		setQUalifiedJID(user.getSimpleUsername(), defaultHost, defaultResource);
		setQualifiedUsername(user.getSimpleUsername(), defaultHost);
	}

	/**
	 * Create a JabberIdBuilder.
	 * 
	 * <strong>For singleton use only.</strong>
	 */
	private JabberIdBuilder() {
		super();
		this.defaultHost = System.getProperty("parity.serverhost");
		this.defaultResource = IParityModelConstants.PARITY_CONNECTION_RESOURCE;
	}

	/**
	 * Obtain the qualified jabber id:  user@host/resource
	 * 
	 * @return The qualified jabber id.
	 */
	public String getQualifiedJID() { return qualifiedJID; }

	/**
	 * Obtain the qualified username:  user@host
	 * 
	 * @return The qualified username.
	 */
	public String getQualifiedUsername() {
		return qualifiedUsername;
	}

	/**
	 * Parse the qualified jabber id.
	 * 
	 * <strong>For singleton use only.</strong>
	 * 
	 * @param qualifiedJabberId
	 *            The qualified jabber id: user@host/resource
	 * @return The qualified jabber id.
	 */
	private JabberId doParseQualifiedJabberId(final String qualifiedJabberId) {
		final int indexOfAt = qualifiedJabberId.indexOf('@');
		if(-1 == indexOfAt) throw new IllegalArgumentException("Qualified jabber id contains no user\\host separation:  " + qualifiedJabberId);
		final String username = qualifiedJabberId.substring(0, indexOfAt);
		final int indexOfSlash = qualifiedJabberId.indexOf('/');
		if(-1 == indexOfSlash) throw new IllegalArgumentException("Qualified jabber id contains no host\\resource separation:  " + qualifiedJabberId);
		final String host = qualifiedJabberId.substring(indexOfAt + 1, indexOfSlash);
		final String resource = qualifiedJabberId.substring(indexOfSlash + 1);
		if(null == username) throw new IllegalArgumentException("Username cannot be null:  " + qualifiedJabberId);
		if(1 > username.length()) throw new IllegalArgumentException("Username cannot be empty:  " + qualifiedJabberId);
		if(null == host) throw new IllegalArgumentException("Host cannot be null:  " + qualifiedJabberId);
		if(1 > host.length()) throw new IllegalArgumentException("Host cannot be empty:  " + qualifiedJabberId);
		if(null == resource) throw new IllegalArgumentException("Resource cannot be null:  " + qualifiedJabberId);
		if(1 > resource.length()) throw new IllegalArgumentException("Resource cannot be empty:  " + qualifiedJabberId);
		return new JabberId(username, host, resource);
	}

	/**
	 * Parse the qualified username and build a jabber id.
	 * 
	 * <strong>For singleton use only.</strong>
	 * 
	 * @param qualifiedUsername
	 *            The qualified username: user@host
	 * @return The jabber id.
	 */
	private JabberId doParseQualifiedUsername(final String qualifiedUsername) {
		final int indexOfAt = qualifiedUsername.indexOf('@');
		if(-1 == indexOfAt) throw new IllegalArgumentException("Qualified username contains no user\\host separation:  " + qualifiedUsername);
		final String username = qualifiedUsername.substring(0, indexOfAt);
		final String host = qualifiedUsername.substring(indexOfAt + 1);
		if(null == username) throw new IllegalArgumentException("Username cannot be null:  " + qualifiedUsername);
		if(1 > username.length()) throw new IllegalArgumentException("Username cannot be empty:  " + qualifiedUsername);
		if(null == host) throw new IllegalArgumentException("Host cannot be null:  " + qualifiedUsername);
		if(1 > host.length()) throw new IllegalArgumentException("Host cannot be empty:  " + qualifiedUsername);
		if(-1 != host.indexOf('/')) throw new IllegalArgumentException("Host cannot contain '/':  " + qualifiedUsername);
		return new JabberId(username, host, defaultResource);
	}

	/**
	 * Parse the username and build a jabber id.
	 * 
	 * <strong>For singleton use only.</strong>
	 * 
	 * @param username
	 *            The username.
	 * @return A fully qualified jabber id.
	 */
	private JabberId doParseUsername(final String username) {
		if(null == username) throw new IllegalArgumentException("Username cannot be null:  " + username);
		if(1 > username.length()) throw new IllegalArgumentException("Username cannot be empty:  " + username);
		if(-1 != username.indexOf('@')) throw new IllegalArgumentException("Username cannot contain '@':  " + username);
		if(-1 != username.indexOf('/')) throw new IllegalArgumentException("Username cannot contain '/':  " + username);
		return new JabberId(username, defaultHost, defaultResource);
	}

	/**
	 * Set the qualified jabber id.
	 * 
	 * @param username
	 *            The user name.
	 * @param host
	 *            The host.
	 * @param resource
	 *            The resource.
	 */
	private void setQUalifiedJID(final String username, final String host,
			final String resource) {
		qualifiedJID = new StringBuffer(username)
			.append('@')
			.append(host)
			.append('/')
			.append(resource)
			.toString();
	}

	/**
	 * Set the qualified username.
	 * 
	 * @param username
	 *            The username.
	 * @param host
	 *            The host.
	 */
	private void setQualifiedUsername(final String username, final String host) {
		qualifiedUsername = new StringBuffer(username)
			.append('@')
			.append(host)
			.toString();
	}
}

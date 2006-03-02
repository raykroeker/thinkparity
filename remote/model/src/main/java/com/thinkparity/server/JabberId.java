/*
 * Feb 24, 2006
 */
package com.thinkparity.server;

import org.xmpp.packet.JID;

import com.thinkparity.server.org.jivesoftware.messenger.JIDBuilder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JabberId {

	private String host;

	private String resource;

	private String username;

	/**
	 * Create a JabberId.
	 * 
	 * @param username
	 *            The jabber username.
	 * @param host
	 *            The jabber host.
	 * @param resource
	 *            The jabber resource.
	 */
	JabberId(final String username, final String host, final String resource) {
		super();
		this.username = username;
		this.host = host;
		this.resource = resource;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(final Object obj) {
		if(null != obj && obj instanceof JabberId) {
			return toString().equals(obj.toString());
		}
		return false;
	}

	public String getQualifiedJabberId() {
		return new StringBuffer(username)
			.append("@")
			.append(host)
			.append("/")
			.append(resource)
			.toString();
	}

	public String getQualifiedUsername() {
		return new StringBuffer(username)
			.append("@")
			.append(host)
			.toString();
	}

	public String getUsername() { return username; }

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 * 
	 */
	public String toString() {
		return getClass().getName() + "\\" +getQualifiedJabberId();
	}

	public JID getJID() {
		return JIDBuilder.buildQualified(getQualifiedJabberId());
	}
}

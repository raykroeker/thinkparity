/*
 * Dec 7, 2005
 */
package com.thinkparity.server.org.jivesoftware.messenger;

import org.jivesoftware.messenger.XMPPServer;
import org.xmpp.packet.JID;

import com.thinkparity.server.ParityServerConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JIDBuilder {

	private static final JIDBuilder singleton;

	private static final Object singletonLock;

	static {
		singleton = new JIDBuilder();
		singletonLock = new Object();
	}

	/**
	 * Build an xmpp JID based upon a username for this xmpp server.
	 * 
	 * @param username
	 *            The username.
	 * @return The JID for the username for this xmpp server.
	 */
	public static JID build(final String username) {
		synchronized(singletonLock) { return singleton.buildImpl(username); }
	}

	/**
	 * Build an xmpp JID based upon a fully qualified jid.
	 * 
	 * @param qualifiedJID
	 *            The qualified jid.
	 * @return The xmpp JID.
	 */
	public static JID buildQualified(final String qualifiedJID) {
		synchronized(singletonLock) {
			return singleton.buildQualifiedImpl(qualifiedJID);
		}
	}

	/**
	 * Jive Messenger's xmpp server.
	 */
	private final XMPPServer xmppServer;

	/**
	 * Create a JIDBuilder [Singleton]
	 */
	private JIDBuilder() {
		super();
		this.xmppServer = XMPPServer.getInstance();
	}

	/**
	 * Build an xmpp JID based upon a username for this xmpp server.
	 * 
	 * @param username
	 *            The username.
	 * @return The JID for the username for this xmpp server.
	 */
	private JID buildImpl(final String username) {
		return xmppServer.createJID(username, ParityServerConstants.CLIENT_RESOURCE);
	}

	/**
	 * Build an xmpp JID based upon a fully qualified jid.
	 * 
	 * @param qualifiedJID
	 *            The fully qualified jid.
	 * @return The xmpp JID.
	 * @throws IllegalArgumentException
	 *             If qualifiedJID contains an invalid format.
	 * @throws NullPointerException
	 *             If qualifiedJID is null.
	 */
	private JID buildQualifiedImpl(final String qualifiedJID) {
		if(null == qualifiedJID) { throw new NullPointerException(); }
		final Integer indexOfAt = qualifiedJID.indexOf('@');
		// node must exist; and be length > 1
		if(indexOfAt < 1) { throw new IllegalArgumentException(); }
		final Integer indexOfSlash = qualifiedJID.indexOf('/', indexOfAt);
		// resource must exist
		if(indexOfSlash == -1) { throw new IllegalArgumentException(); }
		// resource must be length > 0
		if(indexOfSlash == (qualifiedJID.length() - 1)) { throw new IllegalArgumentException(); }
		// domain must be length > 0
		if(indexOfSlash == (indexOfAt + 1)) { throw new IllegalArgumentException(); }

		return new JID(qualifiedJID);
	}
}

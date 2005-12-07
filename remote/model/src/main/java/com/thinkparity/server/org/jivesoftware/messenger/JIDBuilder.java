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
}

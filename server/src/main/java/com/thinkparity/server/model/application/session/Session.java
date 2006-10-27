/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.session;

import java.net.InetAddress;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Session {

	/**
     * Obtain the session's user id.
     * 
     * @return A <code>JabberId</code>.
     */
	public JabberId getJabberId();

    /**
     * Obtain the session's internet address.
     * 
     * @return An <code>InetAddress</code>.
     */
    public InetAddress getInetAddress();

    /**
     * Obtain the server xmpp domain.
     * 
     * @return The server xmpp domain.
     */
    public String getXmppDomain();
}

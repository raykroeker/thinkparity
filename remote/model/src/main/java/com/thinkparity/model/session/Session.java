/*
 * Dec 1, 2005
 */
package com.thinkparity.model.session;

import org.xmpp.packet.JID;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Session {

	/**
	 * Obtain the jabber id for the user that this session represents.
	 * 
	 * @return The jabber id for the user.
	 */
	public JID getJID();

	/**
	 * Obtain the jabber id for the user that this session represents.
	 * 
	 * @return The jabber id.
	 */
	public JabberId getJabberId();

    /**
     * Obtain the server xmpp domain.
     * 
     * @return The server xmpp domain.
     */
    public String getXmppDomain();
}

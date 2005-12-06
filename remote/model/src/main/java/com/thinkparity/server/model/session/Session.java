/*
 * Dec 1, 2005
 */
package com.thinkparity.server.model.session;

import org.xmpp.packet.JID;

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
}

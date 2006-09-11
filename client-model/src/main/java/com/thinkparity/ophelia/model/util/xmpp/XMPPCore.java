/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;

import com.thinkparity.codebase.jabber.JabberId;


import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.smack.SmackException;

/**
 * Provides core xmpp functionality.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface XMPPCore {
	public XMPPConnection getConnection();
	public JabberId getJabberId();
    public Packet sendAndConfirmPacket(final Packet packet)
			throws SmackException;
    public void assertContainsResult(final Object assertion,
            final XMPPMethodResponse response);
}

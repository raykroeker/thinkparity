/*
 * Feb 28, 2006
 */
package com.thinkparity.model.xmpp;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;

import com.thinkparity.model.smack.SmackException;

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
}

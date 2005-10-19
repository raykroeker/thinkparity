/*
 * May 14, 2005
 */
package com.thinkparity.model.smack.packet;

import org.jivesoftware.smack.PacketListener;

/**
 * XMPPPresenceListener This listener is used to translate presence packets into
 * events required by the parity client. The currently required events are
 * subscription requests by other parity users.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class SmackPresenceListener implements PacketListener {

	/**
	 * Create an XMPPPresenceListener
	 */
	protected SmackPresenceListener() { super(); }
}

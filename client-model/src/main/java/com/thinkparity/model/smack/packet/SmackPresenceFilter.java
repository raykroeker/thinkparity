/*
 * May 14, 2005
 */
package com.thinkparity.model.smack.packet;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * SmackPresenceFilter This will filter all presence packets based upon the
 * parity client's needs. The needs outlined so far are subscription requests
 * for any user.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SmackPresenceFilter implements PacketFilter {

	/**
	 * Create an SmackPresenceFilter
	 */
	public SmackPresenceFilter() { super(); }

	/**
	 * Accept incoming packets only if the packet is a presence packet.
	 * @see org.jivesoftware.smack.filter.PacketFilter#accept(org.jivesoftware.smack.packet.Packet)
	 */
	public boolean accept(final Packet packet) {
		if(Presence.class.isInstance(packet)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}

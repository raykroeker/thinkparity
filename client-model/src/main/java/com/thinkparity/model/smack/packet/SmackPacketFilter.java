/*
 * May 14, 2005
 */
package com.thinkparity.model.smack.packet;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

public class SmackPacketFilter implements PacketFilter {

	/**
	 * Create an SmackPacketFilter
	 */
	public SmackPacketFilter() { super(); }

	/**
	 * @see org.jivesoftware.smack.filter.PacketFilter#accept(org.jivesoftware.smack.packet.Packet)
	 */
	public boolean accept(Packet packet) { return Boolean.TRUE; }
}

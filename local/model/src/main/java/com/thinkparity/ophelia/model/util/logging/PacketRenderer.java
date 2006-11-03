/*
 * 19-Oct-2005
 */
package com.thinkparity.ophelia.model.util.logging;

import org.apache.log4j.or.ObjectRenderer;
import org.jivesoftware.smack.packet.Packet;

/**
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public final class PacketRenderer implements ObjectRenderer {

	/**
	 * Create a PacketRenderer.
	 */
	public PacketRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) { 
			return "null";
		}
		else {
			return ((Packet) o).toXML();
		}
	}
}

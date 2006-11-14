/*
 * 19-Oct-2005
 */
package com.thinkparity.ophelia.model.util.logging.or;

import org.apache.log4j.or.ObjectRenderer;
import org.jivesoftware.smack.packet.Packet;

/**
 * <b>Title:</b>thinkParity Smack Packet Renderer<br>
 * <b>Description:</b>A Log4J renderer of the Smack library's packets.<br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PacketRenderer implements ObjectRenderer {

	/**
	 * Create PacketRenderer.
     * 
	 */
	public PacketRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     * 
	 */
	public String doRender(final Object o) {
		if (null == o) { 
			return "null";
		} else {
			return ((Packet) o).toXML();
		}
	}
}

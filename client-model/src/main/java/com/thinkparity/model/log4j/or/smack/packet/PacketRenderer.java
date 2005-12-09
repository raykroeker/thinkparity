/*
 * 19-Oct-2005
 */
package com.thinkparity.model.log4j.or.smack.packet;

import org.apache.log4j.or.ObjectRenderer;
import org.jivesoftware.smack.packet.Packet;

import com.thinkparity.model.log4j.or.IRendererConstants;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class PacketRenderer implements ObjectRenderer {

	private static final String FROM = ",from:";
	private static final String PREFIX =
		Packet.class.getName() + IRendererConstants.PREFIX_SUFFIX;
	private static final String TO = ",to:";
	private static final String XML = ",xml:";

	/**
	 * Create a PacketRenderer.
	 */
	public PacketRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) { 
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.NULL)
				.append(IRendererConstants.SUFFIX).toString();
		}
		else {
			final Packet p = (Packet) o;
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.ID).append(p.getPacketID())
				.append(TO).append(p.getTo())
				.append(FROM).append(p.getFrom())
				.append(XML).append(p.toXML())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}
}

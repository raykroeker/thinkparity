/*
 * Nov 29, 2005
 */
package com.thinkparity.server.log4j.or.org.xmpp.packet;

import org.apache.log4j.or.ObjectRenderer;
import org.xmpp.packet.Packet;

import com.thinkparity.server.log4j.or.IRendererConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PacketRenderer implements ObjectRenderer {

	private static final String FROM = ",from:";

	private static final String PREFIX =
		Packet.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	private static final String TO = ",to:";

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
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
		else {
			final Packet p = (Packet) o;
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.ID).append(p.getID())
				.append(FROM).append(p.getFrom())
				.append(TO).append(p.getTo())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}
}

/*
 * Dec 7, 2005
 */
package com.thinkparity.server.org.apache.log4j.or.org.xmpp.packet;

import org.apache.log4j.or.ObjectRenderer;
import org.xmpp.packet.JID;

import com.thinkparity.server.org.apache.log4j.or.IRendererConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JIDRenderer implements ObjectRenderer {

	private static final String DOMAIN = "domain:";

	private static final String NODE = ",node:";

	private static final String PREFIX =
		JID.class + IRendererConstants.PREFIX_SUFFIX;

	private static final String RESOURCE = ",resource:";

	/**
	 * Create a JIDRenderer.
	 */
	public JIDRenderer() { super(); }

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
			final JID jid = (JID) o;
			return new StringBuffer(PREFIX)
				.append(DOMAIN).append(jid.getDomain())
				.append(NODE).append(jid.getNode())
				.append(RESOURCE).append(jid.getResource())
				.append(IRendererConstants.SUFFIX).toString();
		}
	}

}

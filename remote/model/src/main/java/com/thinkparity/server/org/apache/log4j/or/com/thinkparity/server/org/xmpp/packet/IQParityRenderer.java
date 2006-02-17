/*
 * Nov 30, 2005
 */
package com.thinkparity.server.org.apache.log4j.or.com.thinkparity.server.org.xmpp.packet;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.server.org.apache.log4j.or.IRendererConstants;
import com.thinkparity.server.org.xmpp.packet.IQParity;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQParityRenderer implements ObjectRenderer {

	private static final String FROM = ",from:";

	private static final String PREFIX =
		IQParity.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	private static final String TO = ",to:";

	private static final String XML = ",xml:";

	/**
	 * Create a IQParityRenderer.
	 */
	public IQParityRenderer() { super(); }

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
			final IQParity iq = (IQParity) o;
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.ID).append(iq.getID())
				.append(FROM).append(iq.getFrom())
				.append(TO).append(iq.getTo())
				.append(XML).append(iq.toXML())
				.append(IRendererConstants.SUFFIX).toString();
		}
	}
}

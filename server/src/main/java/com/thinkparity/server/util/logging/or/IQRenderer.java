/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.util.logging.or;

import org.apache.log4j.or.ObjectRenderer;
import org.xmpp.packet.IQ;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQRenderer implements ObjectRenderer {

	private static final String FROM = ",from:";

	private static final String ID = "id:";

	private static final String PREFIX =
		IQ.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	private static final String TO = ",to:";

	private static final String XML = ",xml:";

	/**
	 * Create a IQRenderer.
	 */
	public IQRenderer() { super(); }

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
			final IQ iq = (IQ) o;
			return new StringBuffer(PREFIX)
				.append(ID).append(iq.getID())
				.append(FROM).append(iq.getFrom())
				.append(TO).append(iq.getTo())
				.append(XML).append(iq.toString())
				.append(IRendererConstants.SUFFIX).toString();
		}
	}
}

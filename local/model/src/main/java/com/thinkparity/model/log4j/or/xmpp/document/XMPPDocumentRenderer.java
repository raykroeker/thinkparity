/*
 * 19-Oct-2005
 */
package com.thinkparity.model.log4j.or.xmpp.document;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.model.log4j.or.IRendererConstants;
import com.thinkparity.model.xmpp.document.XMPPDocument;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class XMPPDocumentRenderer implements ObjectRenderer {

	private static final String NAME = ",name:";
	private static final String PREFIX =
		XMPPDocument.class.getName() + IRendererConstants.PREFIX_SUFFIX;
	private static final String UNIQUE_ID = "uniqueId:";

	/**
	 * Create a XMPPDocumentRenderer.
	 */
	public XMPPDocumentRenderer() { super(); }

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
			final XMPPDocument xmppd = (XMPPDocument) o;
			return new StringBuffer(PREFIX)
				.append(UNIQUE_ID).append(xmppd.getUniqueId())
				.append(NAME).append(xmppd.getName())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}
}

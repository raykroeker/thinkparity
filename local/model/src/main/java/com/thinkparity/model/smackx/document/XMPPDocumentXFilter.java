/*
 * May 31, 2005
 */
package com.thinkparity.model.smackx.document;

import com.thinkparity.model.smackx.XFilter;

/**
 * The xmpp document x filter is an implementation of an xmpp packet filter that
 * filters for xmpp document packet extensions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class XMPPDocumentXFilter extends XFilter {

	/**
	 * Create a new XMPPDocumentXFilter. This will create a filter for xmpp
	 * document packet extensions only.
	 */
	public XMPPDocumentXFilter() {
		super(XMPPDocumentPacketX.getXElementName());
	}
}

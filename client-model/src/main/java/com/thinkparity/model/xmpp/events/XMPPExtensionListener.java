/*
 * May 31, 2005
 */
package com.thinkparity.model.xmpp.events;

import com.thinkparity.model.xmpp.document.XMPPDocument;

/**
 * XMPPExtensionListener
 * @author raykroeker@gmail.com
 *
 */
public interface XMPPExtensionListener {

	public void documentReceived(final XMPPDocument xmppDocument);
}

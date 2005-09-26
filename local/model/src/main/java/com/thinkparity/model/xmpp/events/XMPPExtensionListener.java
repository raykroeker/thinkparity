/*
 * May 31, 2005
 */
package com.thinkparity.model.xmpp.events;

import com.thinkparity.model.parity.api.document.DocumentVersion;

/**
 * XMPPExtensionListener
 * @author raykroeker@gmail.com
 *
 */
public interface XMPPExtensionListener {

	public void documentReceived(final DocumentVersion documentVersion);
}

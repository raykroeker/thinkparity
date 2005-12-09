/*
 * May 31, 2005
 */
package com.thinkparity.model.xmpp.events;

import java.util.UUID;

import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.user.User;

/**
 * XMPPExtensionListener
 * @author raykroeker@gmail.com
 *
 */
public interface XMPPExtensionListener {
	public void documentReceived(final XMPPDocument xmppDocument);
	public void keyRequested(final User user, final UUID artifactUUID);
}

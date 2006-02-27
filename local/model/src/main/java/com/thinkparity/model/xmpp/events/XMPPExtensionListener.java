/*
 * May 31, 2005
 */
package com.thinkparity.model.xmpp.events;

import java.util.UUID;

import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.document.XMPPDocument;

/**
 * XMPPExtensionListener
 * @author raykroeker@gmail.com
 *
 */
public interface XMPPExtensionListener {

	public void artifactClosed(final UUID artifactUniqueId);

	public void documentReceived(final XMPPDocument xmppDocument);

	public void keyRequestAccepted(final UUID artifactUniqueId,
			final JabberId acceptedBy);

	public void keyRequestDenied(final UUID artifactUUID,
			final JabberId deniedBy);

	public void keyRequested(final UUID artifactUniqueId,
			final JabberId requestedBy);
}

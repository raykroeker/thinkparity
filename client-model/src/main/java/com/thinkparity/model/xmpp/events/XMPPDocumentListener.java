/*
 * Apr 5, 2006
 */
package com.thinkparity.model.xmpp.events;

import java.util.UUID;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface XMPPDocumentListener {
    public void documentReceived(final JabberId receivedFrom,
            final UUID uniqueId, final Long versionId, final String name,
            final byte[] content);
}

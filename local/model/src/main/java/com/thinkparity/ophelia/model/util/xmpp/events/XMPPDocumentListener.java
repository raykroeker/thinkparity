/*
 * Apr 5, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp.events;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface XMPPDocumentListener {
    public void documentReceived(final JabberId receivedFrom,
            final UUID uniqueId, final Long versionId, final String name,
            final byte[] content);

    public void documentReactivated(final JabberId reactivatedBy,
            final List<JabberId> team, final UUID uniqueId,
            final Long versionId, final String name, final byte[] content);
}

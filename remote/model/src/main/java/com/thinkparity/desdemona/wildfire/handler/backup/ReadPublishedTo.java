/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.backup;

import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadPublishedTo extends AbstractHandler {

    /**
     * Create ReadDocumentVersionDeltas.
     * 
     */
    public ReadPublishedTo() {
        super("backup:readpublishedto");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        writeUserReceipts("publishedTo", readPublishedTo(
                readJabberId("userId"), readUUID("uniqueId"),
                readLong("versionId")));
    }

    private Map<User, ArtifactReceipt> readPublishedTo(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        return getContainerModel().readBackupPublishedTo(userId, uniqueId,
                versionId);
    }
}

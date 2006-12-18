/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.backup;

import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
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
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider,
     *      com.thinkparity.desdemona.util.service.ServiceRequestReader,
     *      com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     * 
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        writer.writeUserReceipts("publishedTo", readPublishedTo(provider,
                reader.readJabberId("userId"), reader.readUUID("uniqueId"),
                reader.readLong("versionId")));
    }

    private Map<User, ArtifactReceipt> readPublishedTo(
            final ServiceModelProvider provider, final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        return provider.getContainerModel().readBackupPublishedTo(userId,
                uniqueId, versionId);
    }
}

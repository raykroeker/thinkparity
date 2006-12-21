/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.archive;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadDocumentVersion extends AbstractHandler {

    /**
     * Create ReadContainer.
     *
     */
    public ReadDocumentVersion() {
        super("archive:readdocumentversion");
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
        final DocumentVersion version = readDocumentVersion(provider,
                reader.readJabberId("userId"), reader.readUUID("uniqueId"),
                reader.readUUID("documentUniqueId"),
                reader.readLong("documentVersionId"));
        writer.writeDocumentVersion("version", version);
    }

    /**
     * Read a list of containers from the archive.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    private DocumentVersion readDocumentVersion(
            final ServiceModelProvider context, final JabberId userId,
            final UUID uniqueId, final UUID documentUniqueId,
            final Long documentVersionId) {
        return context.getContainerModel().readArchiveDocumentVersion(userId,
                uniqueId, documentUniqueId, documentVersionId);
    }
}

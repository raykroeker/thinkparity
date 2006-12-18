/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.archive;

import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadDocumentVersionDeltas extends AbstractHandler {

    /**
     * Create ReadDocumentVersionDeltas.
     * 
     */
    public ReadDocumentVersionDeltas() {
        super("archive:readdocumentversiondeltas");
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
        final Map<DocumentVersion, Delta> versionDeltas;
        final Long compareToVersionId = reader.readLong("compareToVersionId");
        if (null == compareToVersionId) {
            versionDeltas = readArchiveDocumentVersionDeltas(provider,
                reader.readJabberId("userId"), reader.readUUID("uniqueId"),
                reader.readLong("compareVersionId"));
        } else {
            versionDeltas = readArchiveDocumentVersionDeltas(provider,
                    reader.readJabberId("userId"), reader.readUUID("uniqueId"),
                    reader.readLong("compareVersionId"),
                    reader.readLong("compareToVersionId"));
        }
        writer.writeDocumentVersionDeltas("versionDeltas", versionDeltas);
    }

    private Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final ServiceModelProvider provider, final JabberId userId,
            final UUID uniqueId, final Long compareVersionId) {
        return provider.getContainerModel().readArchiveDocumentVersionDeltas(userId,
                uniqueId, compareVersionId);
    }

    private Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final ServiceModelProvider provider, final JabberId userId,
            final UUID uniqueId, final Long compareVersionId,
            final Long compareToVersionId) {
        return provider.getContainerModel().readArchiveDocumentVersionDeltas(
                userId, uniqueId, compareVersionId, compareToVersionId);
    }
}

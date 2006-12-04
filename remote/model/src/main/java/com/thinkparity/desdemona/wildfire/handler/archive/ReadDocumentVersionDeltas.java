/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.archive;

import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
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
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final Map<DocumentVersion, Delta> versionDeltas;
        final Long compareToVersionId = readLong("compareToVersionId");
        if (null == compareToVersionId) {
            versionDeltas = readArchiveDocumentVersionDeltas(
                readJabberId("userId"), readUUID("uniqueId"),
                readLong("compareVersionId"));
        } else {
            versionDeltas = readArchiveDocumentVersionDeltas(
                    readJabberId("userId"), readUUID("uniqueId"),
                    readLong("compareVersionId"), readLong("compareToVersionId"));
        }
        writeDocumentVersionDeltas("versionDeltas", versionDeltas);
    }

    private Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(final JabberId userId,
            final UUID uniqueId, final Long compareVersionId) {
        return getContainerModel().readArchiveDocumentVersionDeltas(userId,
                uniqueId, compareVersionId);
    }

    private Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(final JabberId userId,
            final UUID uniqueId, final Long compareVersionId, final Long compareToVersionId) {
        return getContainerModel().readArchiveDocumentVersionDeltas(userId,
                uniqueId, compareVersionId, compareToVersionId);
    }
}

/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.archive;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadDocumentVersions extends AbstractHandler {

    /** Create ReadArchiveDocumentVersions. */
    public ReadDocumentVersions() {
        super("archive:readdocumentversions");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final List<DocumentVersion> versions = readArchiveDocumentVersions(
                readJabberId("userId"), readUUID("uniqueId"),
                readLong("versionId"));
        writeDocumentVersions("documentVersions", "documentVersion", versions);
    }

    /**
     * Read a list of document versions from the archive.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param documentUniqueId
     *            A document unique id <code>UUID</code>.
     * @return A <code>List&lt;DocumentVersion&gt;</code>.
     */
    private List<DocumentVersion> readArchiveDocumentVersions(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        return getContainerModel().readArchiveDocumentVersions(userId,
                uniqueId, versionId);
    }
}

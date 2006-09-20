/*
 * Created On: Sep 18, 2006 2:21:12 PM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.xmpp.events.ArchiveListener;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class XMPPArchive extends AbstractXMPP<ArchiveListener> {

    /** Create XMPPArchive. */
    public XMPPArchive(final XMPPCore core) {
        super(core);
    }

    List<Container> readContainers(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        final XMPPMethod readArchive = new XMPPMethod("container:readarchive");
        readArchive.setParameter("userId", userId);
        return execute(readArchive, Boolean.TRUE).readResultContainers("containers");
    }

    List<ContainerVersion> readContainerVersions(final JabberId userId,
            final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        final XMPPMethod readArchiveVersions = new XMPPMethod("container:readarchiveversions");
        readArchiveVersions.setParameter("userId", userId);
        readArchiveVersions.setParameter("uniqueId", uniqueId);
        return execute(readArchiveVersions, Boolean.TRUE).readResultContainerVersions("containerVersions");
    }

    List<Document> readDocuments(final JabberId userId, final UUID uniqueId,
            final Long versionId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        final XMPPMethod readDocuments = new XMPPMethod("container:readarchivedocuments");
        readDocuments.setParameter("userId", userId);
        readDocuments.setParameter("uniqueId", uniqueId);
        readDocuments.setParameter("versionId", versionId);
        return execute(readDocuments, Boolean.TRUE).readResultDocuments("documents");
    }

    List<DocumentVersion> readDocumentVersions(final JabberId userId,
            final UUID uniqueId, final Long versionId,
            final UUID documentUniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("documentUniqueId", documentUniqueId);
        final XMPPMethod readDocumentVersions = new XMPPMethod("container:readarchivedocumentversions");
        readDocumentVersions.setParameter("userId", userId);
        readDocumentVersions.setParameter("uniqueId", uniqueId);
        readDocumentVersions.setParameter("versionId", versionId);
        readDocumentVersions.setParameter("documentUniqueId", documentUniqueId);
        return execute(readDocumentVersions, Boolean.TRUE).readResultDocumentVersions("documentVersions");
    }
}

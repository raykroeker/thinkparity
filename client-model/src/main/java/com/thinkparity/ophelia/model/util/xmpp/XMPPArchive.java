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
import com.thinkparity.ophelia.model.util.xmpp.event.ArchiveListener;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class XMPPArchive extends AbstractXMPP<ArchiveListener> {

    /** Create XMPPArchive. */
    XMPPArchive(final XMPPCore core) {
        super(core);
    }

    /**
     * Archive an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    void archive(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod archive = new XMPPMethod("archive:archive");
        archive.setParameter("userId", userId);
        archive.setParameter("uniqueId", uniqueId);
        execute(archive);
    }

    void createStream(final JabberId userId, final String streamId,
            final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("streamId", streamId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        final XMPPMethod createStream = new XMPPMethod("archive:createstream");
        createStream.setParameter("userId", userId);
        createStream.setParameter("streamId", streamId);
        createStream.setParameter("uniqueId", uniqueId);
        createStream.setParameter("versionId", versionId);
        execute(createStream);
    }

    Container readContainer(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod readArchive = new XMPPMethod("archive:readcontainer");
        readArchive.setParameter("userId", userId);
        readArchive.setParameter("uniqueId", uniqueId);
        return execute(readArchive, Boolean.TRUE).readResultContainer("container");
    }

    List<Container> readContainers(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        final XMPPMethod readContainers = new XMPPMethod("archive:readcontainers");
        readContainers.setParameter("userId", userId);
        return execute(readContainers, Boolean.TRUE).readResultContainers("containers");
    }

    List<ContainerVersion> readContainerVersions(final JabberId userId,
            final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod readContainerVersions = new XMPPMethod("archive:readcontainerversions");
        readContainerVersions.setParameter("userId", userId);
        readContainerVersions.setParameter("uniqueId", uniqueId);
        return execute(readContainerVersions, Boolean.TRUE).readResultContainerVersions("containerVersions");
    }

    List<Document> readDocuments(final JabberId userId, final UUID uniqueId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        final XMPPMethod readDocuments = new XMPPMethod("archive:readdocuments");
        readDocuments.setParameter("userId", userId);
        readDocuments.setParameter("uniqueId", uniqueId);
        readDocuments.setParameter("versionId", versionId);
        return execute(readDocuments, Boolean.TRUE).readResultDocuments("documents");
    }

    List<DocumentVersion> readDocumentVersions(final JabberId userId,
            final UUID uniqueId, final Long versionId,
            final UUID documentUniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        final XMPPMethod readDocumentVersions = new XMPPMethod("archive:readdocumentversions");
        readDocumentVersions.setParameter("userId", userId);
        readDocumentVersions.setParameter("uniqueId", uniqueId);
        readDocumentVersions.setParameter("versionId", versionId);
        readDocumentVersions.setParameter("documentUniqueId", documentUniqueId);
        return execute(readDocumentVersions, Boolean.TRUE).readResultDocumentVersions("documentVersions");
    }

    List<JabberId> readTeamIds(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod readTeam = new XMPPMethod("archive:readteamids");
        readTeam.setParameter("userId", userId);
        readTeam.setParameter("uniqueId", uniqueId);
        return execute(readTeam, Boolean.TRUE).readResultJabberIds("teamIds");
    }

    /**
     * Restore an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    void restore(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod archive = new XMPPMethod("archive:restore");
        archive.setParameter("userId", userId);
        archive.setParameter("uniqueId", uniqueId);
        execute(archive);
    }
}

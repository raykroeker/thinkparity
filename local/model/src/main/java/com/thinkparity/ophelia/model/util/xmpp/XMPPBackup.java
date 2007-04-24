/*
 * Created On: Sep 18, 2006 2:21:12 PM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.xmpp.event.BackupListener;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class XMPPBackup extends AbstractXMPP<BackupListener> {

    /**
     * Create XMPPBackup.
     * 
     * @param core
     *            The <code>XMPPCore</code>.
     */
    XMPPBackup(final XMPPCore core) {
        super(core);
    }

    /**
     * Archive an artifact. This will simply apply the archived flag within the
     * backup.
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
        final XMPPMethod archive = xmppCore.createMethod("backup:archive");
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
        final XMPPMethod createStream = xmppCore.createMethod("backup:createstream");
        createStream.setParameter("userId", userId);
        createStream.setParameter("streamId", streamId);
        createStream.setParameter("uniqueId", uniqueId);
        createStream.setParameter("versionId", versionId);
        execute(createStream);
    }

    /**
     * Delete an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    void delete(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod delete = xmppCore.createMethod("backup:delete");
        delete.setParameter("userId", userId);
        delete.setParameter("uniqueId", uniqueId);
        execute(delete);
    }

    Statistics readStatistics(final JabberId userId) {
        final XMPPMethod readStatistics = xmppCore.createMethod("backup:readstatistics");
        readStatistics.setParameter("userId", userId);
        return execute(readStatistics, Boolean.TRUE).readResultStatistics("statistics");
    }

    Container readContainer(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod readContainer = xmppCore.createMethod("backup:readcontainer");
        readContainer.setParameter("userId", userId);
        readContainer.setParameter("uniqueId", uniqueId);
        return execute(readContainer, Boolean.TRUE).readResultContainer("container");
    }

    List<Container> readContainers(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        final XMPPMethod readContainers = xmppCore.createMethod("backup:readcontainers");
        readContainers.setParameter("userId", userId);
        return execute(readContainers, Boolean.TRUE).readResultContainers("containers");
    }

    List<ContainerVersion> readContainerVersions(final JabberId userId,
            final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod readContainerVersions = xmppCore.createMethod("backup:readcontainerversions");
        readContainerVersions.setParameter("userId", userId);
        readContainerVersions.setParameter("uniqueId", uniqueId);
        return execute(readContainerVersions, Boolean.TRUE).readResultContainerVersions("containerVersions");
    }

    Document readDocument(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("unqueId", uniqueId);
        final XMPPMethod readDocument = xmppCore.createMethod("backup:readdocument");
        readDocument.setParameter("userId", userId);
        readDocument.setParameter("uniqueId", uniqueId);
        return execute(readDocument, Boolean.TRUE).readResultDocument("document");
    }

    List<Document> readDocuments(final JabberId userId, final UUID uniqueId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        final XMPPMethod readDocuments = xmppCore.createMethod("backup:readdocuments");
        readDocuments.setParameter("userId", userId);
        readDocuments.setParameter("uniqueId", uniqueId);
        readDocuments.setParameter("versionId", versionId);
        return execute(readDocuments, Boolean.TRUE).readResultDocuments("documents");
    }

    List<DocumentVersion> readDocumentVersions(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        final XMPPMethod readDocumentVersions = xmppCore.createMethod("backup:readdocumentversions");
        readDocumentVersions.setParameter("userId", userId);
        readDocumentVersions.setParameter("uniqueId", uniqueId);
        readDocumentVersions.setParameter("versionId", versionId);
        return execute(readDocumentVersions, Boolean.TRUE).readResultDocumentVersions("documentVersions");
    }

    List<ArtifactReceipt> readPublishedTo(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        final XMPPMethod readDocumentVersions = xmppCore.createMethod("backup:readpublishedto");
        readDocumentVersions.setParameter("userId", userId);
        readDocumentVersions.setParameter("uniqueId", uniqueId);
        readDocumentVersions.setParameter("versionId", versionId);
        return execute(readDocumentVersions, Boolean.TRUE).readResultUserArtifactReceipts("publishedTo");
    }

    List<JabberId> readTeamIds(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod readTeam = xmppCore.createMethod("backup:readteamids");
        readTeam.setParameter("userId", userId);
        readTeam.setParameter("uniqueId", uniqueId);
        return execute(readTeam, Boolean.TRUE).readResultJabberIds("teamIds");
    }

    /**
     * Restore an artifact. This will simply remove the archived flag within the
     * backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>Long</code>.
     */
    void restore(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod archive = xmppCore.createMethod("backup:restore");
        archive.setParameter("userId", userId);
        archive.setParameter("uniqueId", uniqueId);
        execute(archive);
    }

}

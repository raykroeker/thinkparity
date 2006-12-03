/*
 * Generated On: Oct 04 06 10:14:14 AM
 */
package com.thinkparity.desdemona.model.backup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.archive.ClientModelFactory;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.stream.InternalStreamModel;

/**
 * <b>Title:</b>thinkParity Backup Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
final class BackupModelImpl extends AbstractModelImpl {

    /**
     * Create BackupModelImpl.
     *
     * @param session
     *		The user's session.
     */
    BackupModelImpl(final Session session) {
        super(session);
    }

    /**
     * Obtain a container reader for the archive.
     * 
     * @return A container archive reader.
     */
    BackupReader<Container, ContainerVersion> getContainerReader(
            final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            return createContainerReader(userId);
        } catch (final Throwable t) {
            throw translateError(t);
        }

    }

    /**
     * Obtain a document archive reader.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return An <code>ArchiveReader&lt;Document, DocumentVersion&gt;</code>.
     */
    BackupReader<Document, DocumentVersion> getDocumentReader(
            final JabberId userId, final UUID containerUniqueId,
            final Long containerVersionId) {
        try {
            assertIsAuthenticatedUser(userId);
            return createDocumentReader(userId, containerUniqueId, containerVersionId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
    /**
     * Open a document version's input stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A document unique id <code>UUID</code>.
     * @param versionId
     *            A document version id <code>Long</code>.
     * @return An <code>InputStream</code>.
     */
    void createStream(final JabberId userId, final String streamId,
            final UUID uniqueId, final Long versionId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("streamId", streamId);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        try {
            assertIsAuthenticatedUser(userId);
            final JabberId archiveId = readArchiveId(userId);
            if (null == archiveId) {
                logWarning("No archive exists for user {0}.", userId);
            } else {
                final ClientModelFactory modelFactory = getModelFactory(archiveId);
                final InternalArtifactModel artifactModel = modelFactory.getArtifactModel(getClass());
                final InternalDocumentModel documentModel = modelFactory.getDocumentModel(getClass());

                final Long documentId = artifactModel.readId(uniqueId);
                final InputStream stream = documentModel.openVersionStream(documentId, versionId);
                final Long streamSize = documentModel.readVersionSize(documentId, versionId);

                final InternalStreamModel streamModel = getStreamModel();
                final StreamSession streamSession = streamModel.createSession(userId);
                final StreamWriter writer = new StreamWriter(streamSession);
                writer.open();
                try {
                    writer.write(streamId, stream, streamSize);
                } finally {
                    try {
                        stream.close();
                    } finally {
                        writer.close();
                    }
                }
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the team for a backup artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    List<JabberId> readTeam(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);
            final JabberId archiveId = readArchiveId(userId);
            if (null == archiveId) {
                logInfo("No archive exists for user {0}.", userId);
                return Collections.emptyList();
            } else {
                final InternalArtifactModel artifactModel =
                    getModelFactory(archiveId).getArtifactModel(getClass());
                final Long artifactId = artifactModel.readId(uniqueId);
                final List<TeamMember> teamMembers = artifactModel.readTeam2(artifactId);
                final List<JabberId> teamIds = new ArrayList<JabberId>(teamMembers.size());
                for (final TeamMember teamMember : teamMembers) {
                    teamIds.add(teamMember.getId());
                }
                return teamIds;
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }

    }

    /**
     * Create a container backup reader for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>BackupReader&lt;Container, ContainerVersion&gt;</code>.
     */
    private BackupReader<Container, ContainerVersion> createContainerReader(
            final JabberId userId) {
        final JabberId archiveId = readArchiveId(userId);
        if (null == archiveId) {
            logInfo("No archive exists for user {0}.", userId);
            return BackupReader.emptyReader();
        } else {
            return new ContainerReader(getModelFactory(archiveId));
        }
    }

    /**
     * Create a container backup reader for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param containerUniqueId
     *            A container unique id <code>UUID</code>.
     * @param containerVersionId
     *            A container version id <code>Long</code>.
     * @return A <code>BackupReader&lt;Document, DocumentVersion&gt;<code>.
     */
    private BackupReader<Document, DocumentVersion> createDocumentReader(
            final JabberId userId, final UUID containerUniqueId,
            final Long containerVersionId) {
        final JabberId archiveId = readArchiveId(userId);
        if (null == archiveId) {
            logInfo("No archive exists for user {0}.", userId);
            return BackupReader.emptyReader();
        } else {
            return new DocumentReader(getModelFactory(archiveId),
                    containerUniqueId, containerVersionId);
        }
    }

    /**
     * Obtain the archive's model factory.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @return An archive's <code>ClientModelFactory</code>.
     */
    private ClientModelFactory getModelFactory(final JabberId archiveId) {
        return getArchiveModel().getModelFactory(archiveId);
    }

    /**
     * Read an archive id for a user id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return An archive id <code>JabberId</code>.
     */
    private JabberId readArchiveId(final JabberId userId) {
        return getUserModel().readArchiveId(userId);
    }
}

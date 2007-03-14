/*
 * Generated On: Oct 04 06 10:14:14 AM
 */
package com.thinkparity.desdemona.model.backup;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.UploadMonitor;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamUploader;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
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

    private static final Statistics EMPTY_STATISTICS;

    static {
        EMPTY_STATISTICS = new Statistics();
        EMPTY_STATISTICS.setDiskUsage(0L);
    }
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
     * Archive an artifact. This will simply apply the archived flag within the
     * backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>Long</code>.
     */
    void archive(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);

            final JabberId archiveId = readArchiveId(userId);
            if (null == archiveId) {
                logWarning("No archive exists for user {0}.", userId);
            } else {
                final InternalArtifactModel artifactModel =
                    getModelFactory(archiveId).getArtifactModel(getClass());
                Assert.assertTrue(artifactModel.doesExist(uniqueId),
                        "Artifact {0} does not exist for user {1} in archive {2}.",
                        uniqueId, userId.getUsername(),
                        archiveId.getUsername());
                final Long artifactId = artifactModel.readId(uniqueId);
                artifactModel.applyFlagArchived(artifactId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Upload a document version to the stream server using the provided stream
     * id. The stream can then be downloaded by a client.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @param uniqueId
     *            A document unique id <code>UUID</code>.
     * @param versionId
     *            A document version id <code>Long</code>.
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
                final Long streamSize = documentModel.readVersionSize(documentId, versionId);
                logger.logVariable("documentId", documentId);
                logger.logVariable("streamSize", streamSize);

                final InternalStreamModel streamModel = getStreamModel();
                final StreamSession streamSession =
                    streamModel.createArchiveSession(archiveId);
                documentModel.uploadVersion(documentId, versionId, new StreamUploader() {
                    public void upload(final InputStream stream) throws IOException {
                        final InputStream bufferedStream =
                            new BufferedInputStream(stream, getDefaultBufferSize());
                        /* NOTE the underlying stream is closed by the document
                         * io handler through the document model and is thus not
                         * closed here */
                        BackupModelImpl.this.upload(new UploadMonitor() {
                            public void chunkUploaded(final int chunkSize) {
                                logger.logApiId();
                                logger.logVariable("chunkSize", chunkSize);
                            }
                        }, streamId, streamSession, bufferedStream, streamSize);
                    }
                });
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    void delete(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);

            if (getUserModel().isArchive(userId)) {
                logInfo("Ignoring archive user {0}.", userId);
            } else {
                final JabberId archiveId = readArchiveId(userId);
                if (null == archiveId) {
                    logger.logInfo("No archive exists for user \"{0}\".", userId.getUsername());
                } else {
                    final InternalArtifactModel artifactModel =
                        getModelFactory(archiveId).getArtifactModel(getClass());
                    Assert.assertTrue(artifactModel.doesExist(uniqueId),
                            "Artifact {0} does not exist for user {1} in archive {2}.",
                            uniqueId, userId.getUsername(),
                            archiveId.getUsername());
                    final Long artifactId = artifactModel.readId(uniqueId);
                    final InternalContainerModel containerModel =
                        getModelFactory(archiveId).getContainerModel(getClass());
                    containerModel.delete(artifactId);
                }
                // remove user from the team
                final com.thinkparity.desdemona.model.artifact.InternalArtifactModel
                        artifactModel = getArtifactModel();
                final Artifact artifact = getArtifactModel().read(uniqueId);
                final User user = getUserModel().read(userId);
                artifactModel.removeTeamMember(userId, artifact.getId(), user.getLocalId());
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
        
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

    Boolean isBackupOnline(final JabberId userId) {
        try {
            return getArchiveModel().isArchiveOnline(userId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    Statistics readStatisitcs(final JabberId userId) {
        try {
            final JabberId backupId = readArchiveId(userId);
            if (null == backupId) {
                logger.logWarning("");
                return emptyStatistics();
            } else {
                final Statistics statistics = new Statistics();
                statistics.setDiskUsage(0L);
                return statistics;
            }
        } catch (final Throwable t) {
            throw panic(t);
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
     * Restore an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    void restore(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);

            final JabberId archiveId = readArchiveId(userId);
            if (null == archiveId) {
                logWarning("No archive exists for user {0}.", userId);
            } else {
                final InternalArtifactModel artifactModel =
                    getModelFactory(archiveId).getArtifactModel(getClass());
                final Long artifactId = artifactModel.readId(uniqueId);
                Assert.assertTrue(artifactModel.doesExist(uniqueId),
                        "Artifact {0} does not exist for user {1} in archive {2}.",
                        uniqueId, userId.getUsername(),
                        archiveId.getUsername());
                artifactModel.removeFlagArchived(artifactId);
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

    private Statistics emptyStatistics() {
        return EMPTY_STATISTICS;
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

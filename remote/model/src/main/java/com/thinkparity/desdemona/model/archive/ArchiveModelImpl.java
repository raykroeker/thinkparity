/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.desdemona.model.archive;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.session.DefaultLoginMonitor;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.stream.InternalStreamModel;

import org.jivesoftware.util.JiveProperties;

/**
 * <b>Title:</b>thinkParity Archive Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ArchiveModelImpl extends AbstractModelImpl {

    /**
     * A map of the archive contexts. The archive context contains the
     * appropriate accessors to the archive client.
     * 
     */
    private static final Map<JabberId, ArchiveContext> ARCHIVE_CONTEXT_LOOKUP;

    static {
        ARCHIVE_CONTEXT_LOOKUP = new HashMap<JabberId, ArchiveContext>();
    }

    /** The archive file system. */
    private final FileSystem fileSystem;

    /** A <code>JiveProperties</code> instance. */
    private final JiveProperties jiveProperties;

    /** Create ArchiveModelImpl. */
    ArchiveModelImpl() {
        this(null);
    }

    /**
     * Create ArchiveModelImpl.
     *
     * @param session
     *      The user's session.
     */
    ArchiveModelImpl(final Session session) {
        super(session);
        this.jiveProperties = JiveProperties.getInstance();
        this.fileSystem = readFileSystem();
    }

    /**
     * Archive an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    void archive(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);
            final JabberId archiveId = readArchiveId(userId);
            final InternalArtifactModel artifactModel =
                getModelFactory(archiveId).getArtifactModel(getClass());
            Assert.assertTrue(artifactModel.doesExist(uniqueId),
                    "Artifact {0} does not exist for user {1} in archive {2}.",
                    uniqueId, userId.getUsername(),
                    archiveId.getUsername());
            final Long artifactId = artifactModel.readId(uniqueId);
            artifactModel.applyFlagArchived(artifactId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
        
    }

    /**
     * Obtain a container reader for the archive.
     * 
     * @return A container archive reader.
     */
    ArchiveReader<Container, ContainerVersion> getContainerReader(
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
    ArchiveReader<Document, DocumentVersion> getDocumentReader(
            final JabberId userId, final UUID containerUniqueId,
            final Long containerVersionId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("containerUniqueId", containerUniqueId);
        logVariable("containerVersionId", containerVersionId);
        try {
            assertIsAuthenticatedUser(userId);
            return createDocumentReader(userId, containerUniqueId, containerVersionId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Obtain the archive's model factory.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @return An archive's <code>ClientModelFactory</code>.
     */
    ClientModelFactory getModelFactory(final JabberId archiveId) {
        synchronized (ARCHIVE_CONTEXT_LOOKUP) {
            return ((ArchiveContext) ARCHIVE_CONTEXT_LOOKUP.get(archiveId)).modelFactory;
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
                logger.logWarning("User {0} has no archive.", userId);
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

    List<TeamMember> readTeam(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);
            final JabberId archiveId = readArchiveId(userId);
            if (null == archiveId) {
                return Collections.emptyList();
            } else {
                final InternalArtifactModel artifactModel =
                    getModelFactory(archiveId).getArtifactModel(getClass());
                final Long artifactId = artifactModel.readId(uniqueId);
                if (null == artifactId) {
                    return Collections.emptyList();
                } else {
                    return artifactModel.readTeam2(artifactId);
                }
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<JabberId> readTeamIds(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);
            final JabberId archiveId = readArchiveId(userId);
            if (null == archiveId) {
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
                artifactModel.removeFlagArchived(artifactId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
        
    }

    /**
     * Start the archive.  This involves starting all of the archive
     * databases.
     *
     */
    void start() {
        logApiId();
        try {
            final List<User> users = getUserModel().read();
            JabberId archiveId;
            for (final User user : users) {
                archiveId = readArchiveId(user.getId());
                if (null != archiveId) {
                    logger.logInfo("Starting archive {0} for user {1}.",
                            archiveId, user.getId());
                    try {
                        start(archiveId, readArchiveCredentials(archiveId));
                    } catch (final Throwable t) {
                        logger.logFatal(t, "Failed to start archive {0}.", archiveId);
                        throw translateError(t);
                    }
                    logger.logInfo("Archive {0} is online.", archiveId);
                }
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Stop the archive.  This involves stopping all of the archive
     * databases.
     *
     */
    void stop() {
        logApiId();
        try {
            final List<User> users = getUserModel().read();
            JabberId archiveId;
            for (final User user : users) {
                archiveId = readArchiveId(user.getId());
                if (null != archiveId) {
                    stop(readArchiveId(user.getId()));
                }
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create a container archive reader.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return An <code>ArchiveReader&lt;Container, ContainerVersion&gt;</code>.
     */
    private ArchiveReader<Container, ContainerVersion> createContainerReader(final JabberId userId) {
        final JabberId archiveId = readArchiveId(userId);
        if (null == archiveId) {
            logInfo("No archive exists for user {0}.", userId);
            return ArchiveReader.emptyReader();
        } else {
            return new ContainerReader(getModelFactory(readArchiveId(userId)));
        }
    }

    /**
     * Add a context from the archive context map.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     */
    private void createContext(final JabberId archiveId,
            final Environment environment, final Workspace workspace) {
        synchronized (ARCHIVE_CONTEXT_LOOKUP) {
            Assert.assertNotTrue(ARCHIVE_CONTEXT_LOOKUP.containsKey(archiveId),
                    "Archive context for {0} already exists.", archiveId);
            ARCHIVE_CONTEXT_LOOKUP.put(archiveId, new ArchiveContext(
                    getContext(), environment, workspace));
        }
    }

    /**
     * Create a container archive reader.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param containerUniqueId
     *            A container unique id <code>UUID</code>.
     * @param containerVersionId
     *            A container version id <code>Long</code>.
     * @return An <code>ArchiveReader&lt;Document, DocumentVersion&gt;</code>.
     */
    private ArchiveReader<Document, DocumentVersion> createDocumentReader(
            final JabberId userId, final UUID containerUniqueId,
            final Long containerVersionId) {
        final JabberId archiveId = readArchiveId(userId);
        if (null == archiveId) {
            logInfo("No archive exists for user {0}.", userId);
            return ArchiveReader.emptyReader();
        } else {
            return new DocumentReader(getModelFactory(readArchiveId(userId)),
                    containerUniqueId, containerVersionId);
        }
    }

    /**
     * Remove a context from the archive context map.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     */
    private void deleteContext(final JabberId archiveId) {
        synchronized (ARCHIVE_CONTEXT_LOOKUP) {
            Assert.assertTrue(ARCHIVE_CONTEXT_LOOKUP.containsKey(archiveId),
                    "Archive context for {0} does not exist.", archiveId);
            ARCHIVE_CONTEXT_LOOKUP.remove(archiveId);
        }
    }

    /**
     * Read the login credentials for an archive.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @return The archive's login <code>Credentials</code>.
     */
    private Credentials readArchiveCredentials(final JabberId archiveId) {
        return getUserModel().readArchiveCredentials(archiveId);
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

    /**
     * Read the file system from the jive properties.
     * 
     * @return A <code>FileSystem</code>.
     */
    private FileSystem readFileSystem() {
        final String thinkParityArchiveRoot =
            (String) jiveProperties.get(JivePropertyNames.THINKPARITY_ARCHIVE_ROOT);
        return new FileSystem(new File(thinkParityArchiveRoot));
    }

    /**
     * Read the archive file system for an archive id.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @return A <code>FileSystem</code>.
     */
    private FileSystem readFileSystem(final JabberId archiveId) {
        final String path = archiveId.getQualifiedUsername();
        if (!fileSystem.pathExists(path)) {
            fileSystem.createDirectory(path);
        }
        return fileSystem.cloneChild(path);
    }

    /**
     * Read an archive workspace.
     * 
     * @param archiveFileSystem
     *            An archive file system.
     * @return An archive <code>Workspace</code>.
     */
    private Workspace readWorkspace(final Environment environment,
            final FileSystem archiveFileSystem) {
        final WorkspaceModel workspaceModel = WorkspaceModel.getModel(environment);
        return workspaceModel.getWorkspace(archiveFileSystem.getRoot());
    }

    /**
     * Start an individual archive.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @param credentials
     *            An archive's <code>Credentials</code>.
     */
    private void start(final JabberId archiveId, final Credentials credentials) {
        final FileSystem archiveFileSystem = readFileSystem(archiveId);
        final Environment environment = readEnvironment();
        final Workspace workspace = readWorkspace(environment, archiveFileSystem);
        createContext(archiveId, environment, workspace);
        final WorkspaceModel workspaceModel = WorkspaceModel.getModel(environment);
        if (!workspaceModel.isInitialized(workspace)) {
            workspaceModel.initialize(workspace, new DefaultLoginMonitor() {
                @Override
                public Boolean confirmSynchronize() {
                    return Boolean.TRUE;
                }
            }, credentials);
        } else {
            getModelFactory(archiveId).getSessionModel(getClass()).login(new DefaultLoginMonitor() {
                @Override
                public Boolean confirmSynchronize() {
                    return Boolean.TRUE;
                }
            }, credentials);
        }
    }

    /**
     * Start an individual archive.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     */
    private void stop(final JabberId archiveId) {
        getModelFactory(archiveId).getSessionModel(getClass()).logout();
        deleteContext(archiveId);
    }

    private static class ArchiveContext {
        private final ClientModelFactory modelFactory;
        private ArchiveContext(final Context context,
                final Environment environment, final Workspace workspace) {
            super();
            this.modelFactory = new ClientModelFactory(context, environment, workspace);
        }
    }

}

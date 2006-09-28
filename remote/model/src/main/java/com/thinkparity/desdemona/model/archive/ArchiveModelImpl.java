/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.desdemona.model.archive;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jivesoftware.util.JiveProperties;

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
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.user.TeamMember;
import com.thinkparity.ophelia.model.workspace.Preferences;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

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
            return new ContainerReader(getModelFactory(readArchiveId(userId)));
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
            return new DocumentReader(getModelFactory(readArchiveId(userId)),
                containerUniqueId, containerVersionId);
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
    InputStream openDocumentVersionStream(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        try {
            assertIsAuthenticatedUser(userId);
            final JabberId archiveId = readArchiveId(userId);
            final ClientModelFactory modelFactory = getModelFactory(archiveId);
            final InternalArtifactModel artifactModel = modelFactory.getArtifactModel(getClass());
            final InternalDocumentModel documentModel = modelFactory.getDocumentModel(getClass());
            final Long documentId = artifactModel.readId(uniqueId);
            return documentModel.openVersionStream(documentId, versionId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<JabberId> readTeam(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);
            final JabberId archiveId = readArchiveId(userId);
            final InternalArtifactModel artifactModel =
                getModelFactory(archiveId).getArtifactModel(getClass());
            final Long artifactId = artifactModel.readId(uniqueId);
            final List<TeamMember> teamMembers = artifactModel.readTeam2(artifactId);
            final List<JabberId> teamIds = new ArrayList<JabberId>(teamMembers.size());
            for (final TeamMember teamMember : teamMembers) {
                teamIds.add(teamMember.getId());
            }
            return teamIds;
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
            final InternalArtifactModel artifactModel =
                getModelFactory(archiveId).getArtifactModel(getClass());
            final Long artifactId = artifactModel.readId(uniqueId);
            artifactModel.removeFlagArchived(artifactId);
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
                    start(archiveId, readArchiveCredentials(archiveId));
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
     * Obtain the archive's model factory.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @return An archive's <code>ClientModelFactory</code>.
     */
    private ClientModelFactory getModelFactory(final JabberId archiveId) {
        synchronized (ARCHIVE_CONTEXT_LOOKUP) {
            return ((ArchiveContext) ARCHIVE_CONTEXT_LOOKUP.get(archiveId)).modelFactory;
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
     * Read the archive file system for an archive id.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @return A <code>FileSystem</code>.
     */
    private FileSystem readFileSystem(final JabberId archiveId) {
        final String path = archiveId.getQualifiedJabberId();
        if (!fileSystem.pathExists(path)) {
            fileSystem.createDirectory(path);
        }
        return fileSystem.cloneChild(path);
    }

    /**
     * Read an archive workspace.
     * 
     * @param archiveId
     *            An archive id.
     * @param archiveFileSystem
     *            An archive file system.
     * @return An archive <code>Workspace</code>.
     */
    private Workspace readWorkspace(final JabberId archiveId,
            final FileSystem archiveFileSystem) {
        final WorkspaceModel workspaceModel = WorkspaceModel.getModel();
        return workspaceModel.getWorkspace(archiveFileSystem.getRoot());
    }

    /**
     * Read the jive property for the environment.
     * 
     * @return An environment.
     */
    private Environment readEnvironment() {
        final Environment environment;
        final String thinkParityEnvironment =
            (String) JiveProperties.getInstance().get(JivePropertyNames.THINKPARITY_ENVIRONMENT);
        if ("development.localhost".equals(thinkParityEnvironment)) {
            environment = Environment.DEVELOPMENT_LOCALHOST;
        } else if ("development.raymond".equals(thinkParityEnvironment)) {
            environment = Environment.DEVELOPMENT_RAYMOND;
        } else if ("development.robert".equals(thinkParityEnvironment)) {
            environment = Environment.DEVELOPMENT_ROBERT;
        } else if ("production".equals(thinkParityEnvironment)) {
            environment = Environment.PRODUCTION;
        } else if ("testing".equals(thinkParityEnvironment)) {
            environment = Environment.TESTING;
        } else if ("testing.localhost".equals(thinkParityEnvironment)) {
            environment = Environment.TESTING_LOCALHOST;
        } else if ("production".equals(thinkParityEnvironment)) {
            environment = Environment.PRODUCTION;
        } else {
            throw Assert.createUnreachable("UNKNOWN ENVIRONMENT");
        }
        return environment;
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
        final Workspace workspace = readWorkspace(archiveId, archiveFileSystem);
        final Environment environment = readEnvironment();
        createContext(archiveId, environment, workspace);
        getModelFactory(archiveId).getSessionModel(getClass()).login(credentials);
        final WorkspaceModel workspaceModel = WorkspaceModel.getModel();
        if (workspaceModel.isFirstRun(workspace)) {
            final Preferences preferences = workspace.getPreferences();
            preferences.setUsername(archiveId.getUsername());
            getModelFactory(archiveId).getProfileModel(getClass()).read();
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

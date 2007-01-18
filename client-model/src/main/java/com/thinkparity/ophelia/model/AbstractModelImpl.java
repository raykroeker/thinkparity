/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.ophelia.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.Assertion;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.l10n.L18n;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamReader;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.Constants.Versioning;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.audit.InternalAuditModel;
import com.thinkparity.ophelia.model.backup.InternalBackupModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.UserUtils;
import com.thinkparity.ophelia.model.util.Base64;
import com.thinkparity.ophelia.model.util.MD5Util;
import com.thinkparity.ophelia.model.util.localization.Localization;
import com.thinkparity.ophelia.model.util.localization.LocalizationContext;
import com.thinkparity.ophelia.model.workspace.InternalWorkspaceModel;
import com.thinkparity.ophelia.model.workspace.Preferences;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Abstract Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.39
 * @param <T>
 */
public abstract class AbstractModelImpl<T extends EventListener>
    extends com.thinkparity.codebase.model.AbstractModelImpl {

    /**
	 * Assertion message to be displayed if the username is not set in the
	 * parity preferences.
	 */
	private static final String ASSERT_IS_SET_USERNAME = new StringBuffer()
		.append("Before you can create the first parity artifact; you will ")
		.append("need to establish a parity session.").toString();

    /** A set of user utilitiy methods. */
    private static final UserUtils USER_UTILS;

    static {
        USER_UTILS = UserUtils.getInstance();
    }

    /**
	 * Obtain the current date\time.
	 * 
	 * @return The current date\time.
	 */
	protected static Calendar currentDateTime() {
        return DateUtil.getInstance();
	}

	/** The configuration io. */
    protected ConfigurationIOHandler configurationIO;

	/** A thinkParity <code>Environment</code>. */
    protected Environment environment;

    /** A localization interface. */
	protected final L18n l18n;

	/** An apache logger. */
	protected final Log4JWrapper logger;

	/** A thinkParity <code>InternalModelFactory</code>. */
    protected InternalModelFactory modelFactory;

    /** The thinkParity workspace <code>Preferences</code>. */
	protected Preferences preferences;

    /** A thinkParity <code>Workspace</code>. */
	protected Workspace workspace;

    /** The decryption cipher. */
    private transient Cipher decryptionCipher;

	/** The encryption cipher. */
    private transient Cipher encryptionCipher;

    /** A quick-lookup for the local user id. */
    private JabberId localUserId;

    /** The secret key spec. */
    private transient SecretKeySpec secretKeySpec;

    /**
     * Create an AbstractModelImpl
     * 
     */
	protected AbstractModelImpl() {
		super();
        this.l18n = new Localization(LocalizationContext.MODEL);
        this.logger = new Log4JWrapper(getClass());
	}

    /**
     * Add a thinkParity event listener.
     * 
     * @param listener
     *            A thinkParity <code>EventListener</code>.
     * @return Whether or not the listener list was modified as a result of
     *         calling add.
     */
    protected void addListener(final T listener) {
        final Context context = new Context();
        InternalWorkspaceModel.getInstance(context, environment).addListener(
                workspace, this, listener);
    }

    /**
     * Assert that the artifact does not exist.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @param assertMessage
     *            An assertion message <code>String</code>.
     * @param assertArguments
     *            The assertion message arguments <code>Object...</code>.
     */
    protected void assertArtifactDoesNotExist(final UUID uniqueId,
            final String assertMessage, final Object... assertArguments) {
        Assert.assertNotTrue(doesArtifactExist(uniqueId), assertMessage,
                assertArguments);
    }

    /**
     * Assert a draft doesn't exist for the container.
     * 
     * @param containerId
     *            A container id.
     */
    protected void assertContainerDraftDoesNotExist(final Long containerId) {
        Assert.assertIsNull(getContainerModel().readDraft(containerId),
                "Draft for container {0} already exists.", containerId);
    }

	/**
     * Assert a draft exists for the container.
     * 
     * @param assertion
     *            An assertion.
     * @param containerId
     *            A container id.
     */
    protected void assertContainerDraftExists(final Object assertion, final Long containerId) {
        Assert.assertNotNull(assertion, getContainerModel().readDraft(containerId));
    }

    /**
     * Assert that a latest version exists.
     * 
     * @param assertion
     *            An assertion <code>Object</code>.
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @see #doesExistLatestVersion(Long)
     */
    protected void assertDoesExistLatestVersion(final Object assertion,
            final Long artifactId) {
        Assert.assertTrue(assertion, doesExistLatestVersion(artifactId));
    }

    /**
     * Assert that a version exists.
     * 
     * @param assertion
     *            An assertion <code>Object</code>.
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @see #doesExistLatestVersion(Long)
     */
    protected void assertDoesExistVersion(final Object assertion,
            final Long artifactId, final Long versionId) {
        Assert.assertTrue(assertion, doesExistVersion(artifactId, versionId));
    }

	/**
     * Assert that the list of team members does not contain the user.
     * 
     * 
     * @param assertion
     *            An assertion.
     * @param teamMembers
     *            A list of team members.
     * @param user
     *            A user.
     */
    protected void assertDoesNotContain(final Object assertion,
            final List<TeamMember> teamMembers, final User user) {
        Assert.assertNotTrue(assertion, contains(teamMembers, user));
    }

    /**
     * Assert that the artifact is closed.
     * 
     * @param assertion
     *            The assertion.
     * @param artifact
     *            The artifact.
     */
    protected void assertIsClosed(final String assertion,
            final Artifact artifact) {
        Assert.assertTrue(assertion, isClosed(artifact));
    }

    /**
     * Assert the user is the key holder. An assertion that the user is online
     * is also made.
     * 
     * @param assertion
     *            The assertion message.
     * @param artifactId
     *            The artifact id.
     * @see #isKeyHolder(Long)
     */
    protected void assertIsKeyHolder(final Object assertion,
            final Long artifactId) throws ParityException {
        Assert.assertTrue(assertion, isKeyHolder(artifactId));
    }

	/**
     * Assert that the logged in user is not the key holder.
     * 
     * @param assertion
     *            The assertion message.
     * @param artifactId
     *            The artifact id.
     * @see #isKeyHolder(Long)
     */
	protected void assertIsNotKeyHolder(final String assertion,
            final Long artifactId) throws ParityException {
		Assert.assertNotTrue(assertion, isKeyHolder(artifactId));
	}

    /**
     * Assert the user id does not match the local user id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    protected void assertIsNotLocalUserId(final JabberId userId) {
        Assert.assertNotTrue("USER ID MATCHES LOCAL USER ID", isLocalUserId(userId));
    }

    /**
	 * Assert that the model framework is initialized to a state where the user
	 * can start to create artifacts. This requires:
	 * <ol>
	 * <li>The user has logged in at least once.</li>
	 * </ol>
	 * 
	 */
	protected void assertIsSetCredentials() {
		Assert.assertTrue(ASSERT_IS_SET_USERNAME, isSetCredentials());
	}

    /**
     * Ensure the user is not online.
     * 
     * @param assertion
     *            The assertion.
     */
    protected void assertNotIsOnline() {
        Assert.assertNotTrue("USER IS ONLINE", isOnline());
    }

    /**
     * Assert that the reference is not null.
     * 
     * @param assertion
     *            An assertion.
     * @param reference
     *            An object reference.
     */
    protected void assertNotNull(final Object assertion, final Object reference) {
        Assert.assertNotNull(assertion, reference);
    }

	/**
     * Assert that the user is not a team member.
     * 
     * @param assertion
     *            An assertion.
     * @param artifactId
     *            An artifact id.
     * @param userId
     *            A user id.
     */
    protected void assertNotTeamMember(final Object assertion, final Long artifactId, final JabberId userId) {
        final List<TeamMember> team = getArtifactModel().readTeam2(artifactId);
        final User user = getUserModel().read(userId);
        if (null != user)
            Assert.assertNotTrue(assertion, contains(team, user));
    }

	/**
     * Assert the user is online.
     *
     */
    protected void assertOnline() {
        assertOnline("USER NOT ONLINE");
    }

    /**
     * Assert the user is online.
     *
     * @param assertion
     *      The assertion.
     */
    protected void assertOnline(final String assertion) {
        Assert.assertTrue(assertion, isOnline());
    }

    protected void assertOnline(final StringBuffer api) {
        assertOnline(api.toString());
    }

	/**
	 * Assert that the state transition from currentState to newState can be
	 * made safely.
	 * 
	 * @param currentState The artifact's current state.
	 * @param intentedState
	 *            The artifact's intended state.
	 * 
	 * @throws NotTrueAssertion
	 *             If the state cannot be moved.
	 */
	protected void assertStateTransition(final ArtifactState currentState,
			final ArtifactState intendedState) {
		switch(currentState) {
		case ACTIVE:
			// i can close it
			Assert.assertTrue(
					formatAssertion(currentState, intendedState, new ArtifactState[] {ArtifactState.CLOSED}),
						ArtifactState.CLOSED == intendedState);
			break;
		case CLOSED:
			// i can reactivate it
			Assert.assertTrue(
					formatAssertion(currentState, intendedState, new ArtifactState[] {ArtifactState.ACTIVE}),
						ArtifactState.ACTIVE == intendedState);
			break;
		default: Assert.assertUnreachable("Unknown artifact state:  " + currentState);
		}
	}

    /**
     * Assert that the user is a team member.
     * 
     * @param assertion
     *            An assertion.
     * @param artifactId
     *            An artifact id.
     * @param userId
     *            A user id.
     */
    protected void assertTeamMember(final Object assertion, final Long artifactId, final JabberId userId) {
        final List<TeamMember> team = getArtifactModel().readTeam2(artifactId);
        Assert.assertNotTrue(assertion, contains(team, getUserModel().read(userId)));
    }

    /**
     * Assert that the xmpp service is online.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     */
    protected void assertXMPPIsReachable(final Environment environment) {
        Assert.assertTrue(environment.isXMPPReachable(),
                "XMPP environment {0} is not reachable.",
                environment.getXMPPService());
    }

    /**
     * Determine if the list of team members contains the user id..
     * 
     * @param team
     *            A <code>List&lt;TeamMember&gt;</code>.
     * @param user
     *            A user id <code>JabberId</code>.
     * @return True if the user id matches one of the team members.
     */
    protected <U extends User> Boolean contains(final List<U> users,
            final JabberId userId) {
        return USER_UTILS.contains(users, userId);
    }

    /**
     * Determine if the list of team members contains the user.
     * 
     * @param team
     *            A list of team members.
     * @param user
     *            A user.
     * @return True if the id of the user matches one of the team members.
     */
    protected <U extends User, V extends User> Boolean contains(
            final List<U> users, final V user) {
        return USER_UTILS.contains(users, user);
    }

    /**
     * Determine if a list of user ids contains a reference to a user.
     * 
     * @param userIds
     *            A list of user ids <code>JabberId</code>.
     * @param user
     *            A <code>User</code>.
     * @return True if the list contains a reference to a user.
     */
    protected <U extends User> Boolean containsUser(final List<JabberId> userIds,
            final U user) {
        return USER_UTILS.containsUser(userIds, user);
    }

    /**
     * Create the user's credentials.
     * 
     * @param credentials
     *            A user's <code>Credentials</code>.
     * @return The user's <code>Credentials</code>.
     */
    protected Credentials createCredentials(final Credentials credentials) {
        final String cipherKey = getCipherKey();
        try {
            getConfigurationHandler().create(ConfigurationKeys.PASSWORD, encrypt(cipherKey, credentials.getPassword()));
            getConfigurationHandler().create(ConfigurationKeys.RESOURCE, encrypt(cipherKey, credentials.getResource()));
            getConfigurationHandler().create(ConfigurationKeys.USERNAME, encrypt(cipherKey, credentials.getUsername()));
            return readCredentials();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create the user's token.
     * 
     * @param token
     *            A user's <code>Token</code>.
     * @return The user's <code>Token</code>.
     */
    protected Token createToken(final Token token) {
        try {
            getConfigurationHandler().create(ConfigurationKeys.TOKEN, encrypt(getCipherKey(), token.getValue()));
            return readToken();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete the user credentials.
     *
     */
    protected void deleteCredentials() {
        assertIsSetCredentials();
        try {
            getConfigurationHandler().delete(ConfigurationKeys.PASSWORD);
            getConfigurationHandler().delete(ConfigurationKeys.RESOURCE);
            getConfigurationHandler().delete(ConfigurationKeys.USERNAME);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Determine whether or not the artifact exists.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return True if the artifact exists; false otherwise.
     */
    protected Boolean doesArtifactExist(final UUID uniqueId) {
        return getArtifactModel().doesExist(uniqueId);
    }

    /**
     * Determine whether or not a latest version exists.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @return True if a version exists; false otherwise.
     */
    protected Boolean doesExistLatestVersion(final Long artifactId) {
        return getArtifactModel().doesVersionExist(artifactId);
    }

    /**
     * Determine whether or not a version exists.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return True if a version exists; false otherwise.
     */
    protected Boolean doesExistVersion(final Long artifactId, final Long versionId) {
        return getArtifactModel().doesVersionExist(artifactId, versionId);
    }

    /**
     * Download from the stream server.
     * 
     * @param downloadMonitor
     *            A download monitor.
     * @param streamMonitor
     *            A <code>StreamMonitor</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @return The downloaded <code>File</code>.
     * @throws IOException
     */
    protected final File downloadStream(final DownloadMonitor downloadMonitor,
            final StreamMonitor streamMonitor, final String streamId,
            final Long streamOffset) throws IOException {
        final File streamFile = buildStreamFile(streamId);
        final Long actualStreamOffset;
        if (streamFile.length() < streamOffset) {
            logger.logWarning("Cannot resume download for {0} at {1}.  Starting over.",
                    streamId, streamOffset);
            actualStreamOffset = 0L;
        } else {
            actualStreamOffset = streamOffset;
        }
        final FileOutputStream stream;
        if (0 == actualStreamOffset) {
            stream = new FileOutputStream(streamFile);
            logger.logInfo("Starting download for {0}.", streamId);
        } else {
            stream = new FileOutputStream(streamFile, true);
            logger.logInfo("Resuming download for {0} at {1}.", streamId,
                    actualStreamOffset);
        }

        final StreamSession session = getSessionModel().createStreamSession();
        final StreamReader reader = new StreamReader(streamMonitor, session);
        try {
            reader.open();
            reader.read(streamId, stream, actualStreamOffset);
        } finally {
            try {
                stream.close();
            } finally {
                reader.close();
            }
        }
        return streamFile;
    }

    /**
     * Start a download from the stream server.
     * 
     * @param downloadMonitor
     *            A download monitor.
     * @param streamId
     *            A stream id <code>String</code>.
     * @return The downloaded <code>File</code>.
     * @throws IOException
     */
    protected final File downloadStream(final DownloadMonitor downloadMonitor,
            final String streamId) throws IOException {
        final StreamMonitor streamMonitor = new StreamMonitor() {
            long recoverChunkOffset = 0;
            long totalChunks = 0;
            public void chunkReceived(final int chunkSize) {
                logger.logApiId();
                logger.logVariable("chunkSize", chunkSize);
                totalChunks += chunkSize;
                downloadMonitor.chunkDownloaded(chunkSize);
            }
            public void chunkSent(final int chunkSize) {}
            public void headerReceived(final String header) {}
            public void headerSent(final String header) {}
            public void streamError(final StreamException error) {
                if (error.isRecoverable()) {
                    if (recoverChunkOffset <= totalChunks) {
                        logger.logWarning(error, "Network error.");
                        recoverChunkOffset = totalChunks;
                        try {
                            // attempt to resume the download
                            downloadStream(downloadMonitor, this, streamId,
                                    Long.valueOf(recoverChunkOffset));
                        } catch (final IOException iox) {
                            throw translateError(iox);
                        }
                    } else {
                        throw translateError(error);
                    }
                } else {
                    throw translateError(error);
                }
            }
        };
        return downloadStream(downloadMonitor, streamMonitor, streamId, 0L);
    }

    /**
     * Find the user in a team.
     * 
     * @param team
     *            The team.
     * @param user
     *            The user to look for.
     * @return The team member.
     */
    protected TeamMember get(final List<TeamMember> team, final User user) {
        return team.get(indexOf(team, user));
    }

    /**
     * Obtain an internal artifact model.
     * 
     * @return An <code>InternalArtifactModel</code>.
     */
    protected final InternalArtifactModel getArtifactModel() {
        return modelFactory.getArtifactModel();
    }

    /**
     * Obtain an internal audit model.
     * 
     * @return An instance of <code>InternalAuditModel</code>.
     */
    protected final InternalAuditModel getAuditModel() {
		return modelFactory.getAuditModel();
	}

    /**
     * Obtain an internal backup model.
     * 
     * @return An instance of <code>InternalBackupModel</code>.
     */
    protected final InternalBackupModel getBackupModel() {
        return modelFactory.getBackupModel();
    }

    /**
     * Obtain an internal contact model.
     * 
     * @return An instance of <code>InternalContactModel</code>.
     */
    protected final InternalContactModel getContactModel() {
        return modelFactory.getContactModel();
    }

    /**
     * Obtain an internal container model.
     * 
     * @return An instance of <code>InternalContainerModel</code>.
     */
    protected final InternalContainerModel getContainerModel() {
        return modelFactory.getContainerModel();
    }

    /**
     * Obtain an internal document model.
     * 
     * @return An instance of <code>InternalDocumentModel</code>.
     */
	protected final InternalDocumentModel getDocumentModel() {
		return modelFactory.getDocumentModel();
	}

    /**
     * Obtain an internal index model.
     * 
     * @return An instance of <code>InternalIndexModel</code>.
     */
    protected final InternalIndexModel getIndexModel() {
        return modelFactory.getIndexModel();
    }

	/**
	 * Obtain the model's localization.
	 * 
	 * @return The model's localization.
	 */
	protected L18n getL18n() { return l18n; }

    protected StringBuffer getLogId(final Library library) {
        if(null == library) { return new StringBuffer("null"); }
        else {
            return new StringBuffer()
                .append(library.getId())
                .append(":").append(library.getGroupId())
                .append(":").append(library.getArtifactId())
                .append(":").append(library.getVersion())
                .append(":").append(DateUtil.format(
                        library.getCreatedOn(), DateUtil.DateImage.ISO));
        }
    }

    protected StringBuffer getLogId(final Release release) {
        if(null == release) { return new StringBuffer("null"); }
        else {
            return new StringBuffer()
                .append(release.getId())
                .append(":").append(release.getGroupId())
                .append(":").append(release.getArtifactId())
                .append(":").append(release.getVersion())
                .append(":").append(DateUtil.format(
                        release.getCreatedOn(), DateUtil.DateImage.ISO));
        }
    }

    /**
     * Obtain an internal profile model.
     * 
     * @return An instance of <code>InternalProfileModel</code>.
     */
    protected final InternalProfileModel getProfileModel() {
        return modelFactory.getProfileModel();
    }

    /**
     * Obtain an internal session model.
     * 
     * @return An instance of <code>InternalSessionModel</code>.
     */
	protected final InternalSessionModel getSessionModel() {
		return modelFactory.getSessionModel();
	}

    /**
	 * @see Localization#getString(String)
	 * 
	 */
	protected String getString(final String localKey) {
		return l18n.getString(localKey);
	}

    /**
	 * @see Localization#getString(String, Object[])
	 * 
	 */
	protected String getString(final String localKey, final Object[] arguments) {
		return l18n.getString(localKey, arguments);
	}

    /**
     * Obtain an internal user model.
     * 
     * @return An instance of <code>InternalUserModel</code>.
     */
    protected final InternalUserModel getUserModel() {
        return modelFactory.getUserModel();
    }

    /**
     * Obtain the index of a user id in a list of users.
     * 
     * @param users
     *            A list of <code>User</code>s.
     * @param userId
     *            A user id.
     * @return The index of the user id in the list; or -1 if the user does not
     *         exist in the list.
     */
    protected <U extends User> int indexOf(final List<U> users,
            final JabberId userId) {
        return USER_UTILS.indexOf(users, userId);
    }

    /**
     * Initialize the model.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
    protected final void initialize(final Environment environment,
            final Workspace workspace) {
        this.environment = environment;
        this.modelFactory = InternalModelFactory.getInstance(
                getContext(), environment, workspace);
        this.workspace = workspace;
        this.preferences = (null == workspace ? null : workspace.getPreferences());
    }

    /**
     * Intialize the model.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
    protected abstract void initializeModel(final Environment environment,
            final Workspace workspace);

    /**
     * Determine whether or not the artifact is closed.
     * 
     * @param artifact
     *            The artifact.
     * @return True if the artifact is closed; false otherwise.
     */
    protected Boolean isClosed(final Artifact artifact) {
        return ArtifactState.CLOSED == artifact.getState();
    }

    /**
     * Check the local flag; as well as the key holder on the server.
     * 
     * @param artifactId
     *            The artifact id.
     * @return True if the user is the keyholder; false otherwise.
     */
    protected Boolean isKeyHolder(final Long artifactId) throws ParityException {
        assertOnline("USER NOT ONLINE");
        final InternalArtifactModel artifactModel = getArtifactModel();
        return artifactModel.isFlagApplied(artifactId, ArtifactFlag.KEY) &&
            isRemoteKeyHolder(artifactModel.readUniqueId(artifactId));
    }

    /**
     * Determine if the user id matched the local user id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the local user id matches the user id.
     */
    protected Boolean isLocalUserId(final JabberId userId) {
        return localUserId().equals(userId);
    }

    /**
     * Determine whether or not the user is online.
     *
     * @return True if the user is online; false otherwise.
     */
    protected Boolean isOnline() {
        return getSessionModel().isLoggedIn();
    }

    protected final Boolean isStreamDownloadComplete(final String streamId,
            final Long versionSize) throws IOException {
        final File streamFile = buildStreamFile(streamId);
        return streamFile.exists()
                && streamFile.length() == versionSize.longValue();
    }

    /**
     * Obtain the team member for the local user.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The team member.
     */
    protected TeamMember localTeamMember(final Long artifactId) {
        final List<TeamMember> team = getArtifactModel().readTeam2(artifactId);
        return get(team, localUser());
    }

    /**
     * Obtain the local user.
     *
     * @return The current user.
     */
    protected User localUser() {
        final JabberId currentUserId = localUserId();
        if (null == currentUserId) {
            return null;
        } else {
            return getUserModel().read(currentUserId);
        }
    }

    /**
	 * Obtain the local user id.
	 * 
	 * @return The jabber id of the local user.
	 */
	protected JabberId localUserId() {
        if (null == localUserId) {
            final Credentials credentials = readCredentials();
            if (null != credentials) {
                localUserId = JabberIdBuilder.parseUsername(credentials.getUsername());
            }
        }
        return localUserId;
	}

    protected final File locateStreamFile(final String streamId)
            throws IOException {
        return buildStreamFile(streamId);
    }

    /**
     * Notify all event listeners.
     * 
     * @param notifier
     *            A thinkParity <code>EventNotifier</code>.
     */
    protected void notifyListeners(final EventNotifier<T> notifier) {
        final Context context = new Context();
        final List<T> listeners = InternalWorkspaceModel.getInstance(context,
                environment).getListeners(workspace, this);
        for (final T listener : listeners) {
            try {
                notifier.notifyListener(listener);
            } catch (final Throwable t) {
                logger.logError(t, "Event listener {0} failed.", listener);
            }
        }
    }

    /**
     * Panic. Nothing can be done about the error that has been generated. An
     * appropriate error is constructed suitable for throwing beyond the model
     * interface.
     * 
     * @param t
     *            A <code>Throwable</code>.
     * @return A <code>RuntimeException</code>.
     */
    protected RuntimeException panic(final Throwable t) {
        return translateError(t);
    }

    /**
     * Read the artifact unique id.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact unique id.
     */
    protected UUID readArtifactUniqueId(final Long artifactId) {
        return getArtifactModel().readUniqueId(artifactId);
    }

    /**
     * Read the credentials from the configuration.
     * 
     * @return The user's credentials.
     */
    protected Credentials readCredentials() {
        final String cipherKey = getCipherKey();
        final String password = getConfigurationHandler().read(ConfigurationKeys.PASSWORD);
        final String resource = getConfigurationHandler().read(ConfigurationKeys.RESOURCE);
        final String username = getConfigurationHandler().read(ConfigurationKeys.USERNAME);

        if (null == username || null == password || null == resource) {
            return null;
        } else {
            final Credentials credentials = new Credentials();
            try {
                credentials.setPassword(decrypt(cipherKey, password));
                credentials.setResource(decrypt(cipherKey, resource));
                credentials.setUsername(decrypt(cipherKey, username));
                return credentials;
            } catch (final Throwable t) {
                throw translateError(t);
            }
        }
    }

    /**
     * Read the next version id.
     * 
     * @param containerId
     *            A container id.
     * @return The next version id.
     */
    protected Long readNextVersionId(final Long artifactId) {
        final Long latestVersionId = getArtifactModel().readLatestVersionId(artifactId);
        return null == latestVersionId ? Versioning.START : latestVersionId + Versioning.INCREMENT;
    }

    /**
     * Read the user's token.
     * 
     * @return The user's <code>Token</code>.
     */
    protected Token readToken() {
        final String tokenValue = getConfigurationHandler().read(ConfigurationKeys.TOKEN);
        if (null == tokenValue) {
            return null;
        } else {
            try {
                final Token token = new Token();
                token.setValue(decrypt(getCipherKey(), tokenValue));
                return token;
            } catch (final Throwable t) {
                throw translateError(t);
            }
        }
    }

    /**
     * Remove a thinkParity event listener.
     * 
     * @param listener
     *            A thinkParity <code>EventListener</code>.
     * @return Whether or not the listener list was modified as a result of
     *         calling remove.
     */
    protected void removeListener(final T listener) {
        final Context context = new Context();
        InternalWorkspaceModel.getInstance(context, environment).removeListener(
                workspace, this, listener);
    }

    /**
     * Create a stream id for an artifact version.
     * 
     * @param version
     *            An <code>ArtifactVersion</code>.
     * @return A stream id <code>String</code>.
     */
    protected String streamId(final ArtifactVersion version) {
        return new StringBuffer()
            .append(version.getArtifactId())
            .append("-").append(version.getVersionId())
            .toString();
    }

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     */
    protected RuntimeException translateError(final Throwable t) {
        if (ParityUncheckedException.class.isAssignableFrom(t.getClass())) {
            return (ParityUncheckedException) t;
        } else if (Assertion.class.isAssignableFrom(t.getClass())) {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, "{0}", errorId);
            return (Assertion) t;
        }
        else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, "{0}", errorId);
            return ParityErrorTranslator.translateUnchecked(getContext(), errorId, t);
        }
    }

    /**
     * Update the credentials from the configuration.
     * 
     * @param credentials
     *            The user's credentials.
     */
    protected void updateCredentials(final Credentials credentials) {
        final String cipherKey = "18273-4897-12-53974-816523-49-81623-95-4-91-8723-56974812-63498-612395-498-7125-349871265-47892-1539784-1523954-19-287356-4";
        try {
            getConfigurationHandler().update(ConfigurationKeys.PASSWORD, encrypt(cipherKey, credentials.getPassword()));
            getConfigurationHandler().update(ConfigurationKeys.RESOURCE, encrypt(cipherKey, credentials.getResource()));
            getConfigurationHandler().update(ConfigurationKeys.USERNAME, encrypt(cipherKey, credentials.getUsername()));
        }
        catch(final Throwable t) { throw translateError(t); }
    }

    /**
     * Upload a stream to the stream server using an existing session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param iStream
     *            A <code>Iterable</code> series of <code>InputStream</code>.
     * @throws IOException
     */
    protected final String uploadStream(final UploadMonitor uploadMonitor,
            final StreamMonitor streamMonitor, final StreamSession session,
            final InputStream stream, final Long streamSize,
            final Long streamOffset) throws IOException {
        stream.reset();
        long skipped = stream.skip(streamOffset);
        while (skipped < streamOffset && 0 < skipped) {
            skipped += stream.skip(streamOffset.longValue() - skipped);
        }
        final Long actualStreamOffset;
        if (skipped == streamOffset.longValue()) {
            logger.logInfo("Resuming download for {0} at {1}.",
                    session, streamOffset);
            actualStreamOffset = streamOffset;
        } else {
            logger.logWarning("Could not resume download for {0} at {1}.  Starting over.",
                    session, streamOffset);
            actualStreamOffset = 0L;
        }
        final InternalSessionModel sessionModel = getSessionModel();
        final StreamWriter writer = new StreamWriter(streamMonitor, session);
        writer.open();
        try {
            final String streamId = sessionModel.createStream(session);
            writer.write(streamId, stream, streamSize, actualStreamOffset);
            return streamId;
        } finally {
            writer.close();
        }
    }

    /**
     * Upload a stream to the stream server using an existing session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param iStream
     *            A <code>Iterable</code> series of <code>InputStream</code>.
     * @throws IOException
     */
    protected final String uploadStream(final UploadMonitor uploadMonitor,
            final StreamSession session, final InputStream stream,
            final Long streamSize) throws IOException {
        final StreamMonitor streamMonitor = new StreamMonitor() {
            long recoverChunkOffset = 0;
            long totalChunks = 0;
            public void chunkReceived(final int chunkSize) {}
            public void chunkSent(final int chunkSize) {
                totalChunks += chunkSize;
                uploadMonitor.chunkUploaded(chunkSize);
            }
            public void headerReceived(final String header) {}
            public void headerSent(final String header) {}
            public void streamError(final StreamException error) {
                if (error.isRecoverable()) {
                    if (recoverChunkOffset <= totalChunks) {
                        // attempt to resume the upload
                        recoverChunkOffset = totalChunks;
                        try {
                            uploadStream(uploadMonitor, this, session, stream,
                                    streamSize, Long.valueOf(recoverChunkOffset));
                        } catch (final IOException iox) {
                            throw translateError(iox);
                        }
                    } else {
                        throw error;
                    }
                } else {
                    throw error;
                }
            }
        };
        return uploadStream(uploadMonitor, streamMonitor, session, stream,
                streamSize, 0L);
    }

    /**
     * Build a local file to back a stream. Note that the file is transient in
     * nature and will be deleted when thinkParity is shutdown or the next time
     * it is started up.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return A <code>File</code>.
     * @throws IOException
     */
    private File buildStreamFile(final String streamId) throws IOException {
        return workspace.createTempFile(streamId);
    }

    /**
     * Decrypt the cipher text into clear text using the cipher key.
     * 
     * @param cipherKey
     *            The cipher key.
     * @param cipherText
     *            The cipher text.
     * @return The clear text.
     * @throws BadPaddingException
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private String decrypt(final String cipherKey, final String cipherText)
            throws BadPaddingException, IOException, IllegalBlockSizeException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        final Cipher cipher = getDecryptionCipher();
        return new String(cipher.doFinal(Base64.decodeBytes(cipherText)));
    }
    /**
     * Encrypt clear text into a base 64 encoded cipher text.
     * 
     * @param cipherKey
     *            The cipher key
     * @param clearText
     *            The clean text to encrypt.
     * @return The cipher text.
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private String encrypt(final String cipherKey, final String clearText)
            throws BadPaddingException, IOException, IllegalBlockSizeException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        final Cipher cipher = getEncryptionCipher();
        return Base64.encodeBytes(cipher.doFinal(clearText.getBytes()));
    }

    private String formatAssertion(final ArtifactState currentState,
			final ArtifactState intendedState,
			final ArtifactState[] allowedStates) {
		final StringBuffer assertion =
			new StringBuffer("Cannot move artifact state.  ")
			.append("Current State:  ").append(currentState)
			.append("  Attempted State:  ").append(intendedState)
			.append("  Allowed State(s):  ");
		int index = 0;
		for(final ArtifactState allowedState: allowedStates) {
			if(0 != index++) { assertion.append(","); }
			assertion.append(allowedState.toString());
		}
		return assertion.toString();
	}

    /**
     * Obtain the cipher key used to encrypt configuration information.
     * 
     * @return A cipher key <code>String</code>.
     */
    private String getCipherKey() {
        return "18273-4897-12-53974-816523-49-81623-95-4-91-8723-56974812-63498-612395-498-7125-349871265-47892-1539784-1523954-19-287356-4";
    }

    /**
     * Obtain the configuration io interface.
     * 
     * @return The configuraion io interface.
     */
    private ConfigurationIOHandler getConfigurationHandler() {
        if(null == configurationIO) {
            configurationIO = IOFactory.getDefault(workspace).createConfigurationHandler();
        }
        return configurationIO;
    }

    /**
     * Obtain the decryption cipher; creating it if necessary.
     * 
     * @return A decryption cipher.
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private Cipher getDecryptionCipher() throws IOException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        if(null == decryptionCipher) {
            decryptionCipher = Cipher.getInstance("AES");
            decryptionCipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec());
        }
        return decryptionCipher;
    }

    /**
     * Obtain the encryption cipher; creating it if need be.
     * 
     * @return The encryption cipher.
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private Cipher getEncryptionCipher() throws IOException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        if(null == encryptionCipher) {
            encryptionCipher = Cipher.getInstance("AES");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec());
        }
        return encryptionCipher;
    }

    /**
     * Obtain the secret key; creating it if necessary.
     * 
     * @return The secret key.
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private SecretKeySpec getSecretKeySpec() throws IOException,
            NoSuchAlgorithmException {
        if(null == secretKeySpec) {
            final byte[] rawKey = MD5Util.md5("010932671-023769081237450981735098127-1280397-181-2387-6581972689-1728-9671-8276-892173-5971283-751-239875-182735-98712-85971-2897-867-9823-56823165-8365-89236-987-214981265-9-9-65623-5896-35-3296-289-65893-983-932-5928734-302894719825-99181-28497612-8375".getBytes());
            secretKeySpec = new SecretKeySpec(rawKey, "AES");
        }
        return secretKeySpec;
    }

    /**
     * Obtain the index of a team member in a user list.
     * 
     * @param users
     *            A user list.
     * @param teamMember
     *            A team member.
     * @return The index of the team member in the users list or -1 if the team
     *         member does not exist in the list.
     */
    private <U extends User, V extends User> int indexOf(final List<U> users,
            final V user) {
        return USER_UTILS.indexOf(users, user);
    }

    /**
     * Determine whether or not the local user is the remote key holder.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return True if the user is the artifact key holder.
     */
    private Boolean isRemoteKeyHolder(final UUID uniqueId) {
        return localUserId().equals(
                getSessionModel().readKeyHolder(localUserId(), uniqueId));
    }

    /**
     * Determine whether or not the user's credentials have been set.
     * 
     * @return True if the credentials have been set; false otherwise.
     */
    private Boolean isSetCredentials() { return null != readCredentials(); }

    /** Configuration keys. */
    private static class ConfigurationKeys {
        private static final String PASSWORD = "PASSWORD";
        private static final String RESOURCE = "RESOURCE";
        private static final String TOKEN = "TOKEN";
        private static final String USERNAME = "USERNAME";
    }
}
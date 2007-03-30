/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.ophelia.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.Constants.ChecksumAlgorithm;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.l10n.L18n;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.DownloadMonitor;
import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.Token;
import com.thinkparity.codebase.model.util.codec.MD5Util;

import com.thinkparity.ophelia.model.Constants.Versioning;
import com.thinkparity.ophelia.model.artifact.ArtifactUtil;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.audit.InternalAuditModel;
import com.thinkparity.ophelia.model.backup.InternalBackupModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler;
import com.thinkparity.ophelia.model.migrator.InternalMigratorModel;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.UserUtils;
import com.thinkparity.ophelia.model.util.Base64;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.util.localization.Localization;
import com.thinkparity.ophelia.model.util.localization.LocalizationContext;
import com.thinkparity.ophelia.model.workspace.InternalWorkspaceModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.40
 * @param <T>
 */
public abstract class Model<T extends EventListener> extends
        com.thinkparity.codebase.model.AbstractModelImpl {

    /** An instance of <code>ArtifactUtil<code>. */
    protected static final ArtifactUtil ARTIFACT_UTIL;

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
        ARTIFACT_UTIL = ArtifactUtil.getInstance();
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

    /**
     * Notify a process monitor that a given number of steps is upcoming.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param steps
     *            An <code>Integer</code> number of steps.
     */
    protected static final void notifyDetermine(final ProcessMonitor monitor,
            final Integer steps) {
        monitor.determineSteps(steps);
    }

    /**
     * Notify a process monitor that a given process will begin.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param steps
     *            An <code>Integer</code> number of steps.
     */
    protected static final void notifyProcessBegin(final ProcessMonitor monitor) {
        monitor.beginProcess();
    }

    /**
     * Notify a process monitor that a given process will end.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param steps
     *            An <code>Integer</code> number of steps.
     */
    protected static final void notifyProcessEnd(final ProcessMonitor monitor) {
        monitor.endProcess();
    }

    /**
     * Notify a process monitor that a given step will begin.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param step
     *            A <code>Step</code>.
     */
    protected static final void notifyStepBegin(final ProcessMonitor monitor,
            final Step step) {
        notifyStepBegin(monitor, step, null);
    }

    /**
     * Notify a process monitor that a given step will begin.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param step
     *            A <code>Step</code>.
     * @param data
     *            Any extra step data.
     */
    protected static final void notifyStepBegin(final ProcessMonitor monitor,
            final Step step, final Object data) {
        monitor.beginStep(step, data);
    }

    /**
     * Notify a process monitor that a given step will end.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param step
     *            A <code>Step</code>.
     */
    protected static final void notifyStepEnd(final ProcessMonitor monitor,
            final Step step) {
        monitor.endStep(step);
    }

	/** The configuration io. */
    protected ConfigurationIOHandler configurationIO;

	/** A thinkParity <code>Environment</code>. */
    protected Environment environment;

    /** A localization interface. */
	protected final L18n l18n;

	/** A thinkParity <code>InternalModelFactory</code>. */
    protected InternalModelFactory modelFactory;

    /** A thinkParity <code>Workspace</code>. */
	protected Workspace workspace;

    /** The decryption cipher. */
    private transient Cipher decryptionCipher;

    /** The encryption cipher. */
    private transient Cipher encryptionCipher;

    /** The <code>ModelInvocationContext</code>. */
    private ModelInvocationContext invocationContext;

    /** A list of all pending <code>EventNotifier</code>s of <code>T</code>. */
    private final List<EventNotifier<T>> notifiers;

    /** The secret key spec. */
    private transient SecretKeySpec secretKeySpec;

	/**
     * Create an Model
     * 
     */
	protected Model() {
		super();
        this.l18n = new Localization(LocalizationContext.MODEL);
        this.notifiers = new Vector<EventNotifier<T>>(3);
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
     * Assert that a container draft does not exist.
     * 
     * @param containerId
     *            A container draft id <code>Long</code>.
     * @param assertion
     *            An assertion message <code>String</code>.
     * @param assertionArguments
     *            The assertion message arguments <code>Object[]</code>.
     */
    protected void assertContainerDraftDoesNotExist(final Long containerId,
            final String assertion, final Object... assertionArguments) {
        Assert.assertNotTrue(getContainerModel().doesExistDraft(containerId),
                assertion, assertionArguments);
    }

    /**
     * Assert that a container draft exists.
     * 
     * @param containerId
     *            A container draft id <code>Long</code>.
     * @param assertion
     *            An assertion message <code>String</code>.
     * @param assertionArguments
     *            The assertion message arguments <code>Object[]</code>.
     */
    protected void assertContainerDraftExists(final Long containerId,
            final String assertion, final Object... assertionArguments) {
        Assert.assertTrue(getContainerModel().doesExistDraft(containerId),
                assertion, assertionArguments);
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
            final Long artifactId) {
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
            final Long artifactId) {
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
                "XMPP environment {0}:{1} is not reachable.",
                environment.getXMPPHost(), environment.getXMPPPort());
    }

    /**
     * Calculate a checksum for a file's contents.
     * 
     * @param file
     *            A <code>File</code>.
     * @param buffer
     *            The <code>Integer</code> size of a buffer to use.
     * @return An MD5 checksum <code>String</code>.
     */
    protected final String checksum(final ByteChannel byteChannel,
            final Integer buffer) throws IOException {
        return MD5Util.md5Hex(byteChannel, buffer);
    }

    /**
     * Calculate a checksum for a file's contents.
     * 
     * @param file
     *            A <code>File</code>.
     * @param buffer
     *            The <code>Integer</code> size of a buffer to use.
     * @return An MD5 checksum <code>String</code>.
     */
    protected final String checksum(final File file, final ByteBuffer buffer)
            throws IOException {
        final InputStream stream = new FileInputStream(file);
        try {
            return MD5Util.md5Hex(stream, buffer);
        } finally {
            stream.close();
        }
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
            configurationIO.create(ConfigurationKeys.Credentials.PASSWORD, encrypt(cipherKey, credentials.getPassword()));
            configurationIO.create(ConfigurationKeys.Credentials.RESOURCE, encrypt(cipherKey, credentials.getResource()));
            configurationIO.create(ConfigurationKeys.Credentials.USERNAME, encrypt(cipherKey, credentials.getUsername()));
            return readCredentials();
        } catch (final Throwable t) {
            throw panic(t);
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
            configurationIO.create(ConfigurationKeys.TOKEN, encrypt(getCipherKey(), token.getValue()));
            return readToken();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Delete the user credentials.
     *
     */
    protected void deleteCredentials() {
        assertIsSetCredentials();
        try {
            configurationIO.delete(ConfigurationKeys.Credentials.PASSWORD);
            configurationIO.delete(ConfigurationKeys.Credentials.RESOURCE);
            configurationIO.delete(ConfigurationKeys.Credentials.USERNAME);
        } catch (final Throwable t) {
            throw panic(t);
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
        final File streamFile = buildStreamFile(streamId);
        final StreamSession streamSession = getSessionModel().createStreamSession();
        logger.logVariable("streamSession.getBufferSize()", streamSession.getBufferSize());
        logger.logVariable("streamSession.getCharset()", streamSession.getCharset());
        logger.logVariable("streamSession.getId()", streamSession.getId());
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
                            downloadStream(downloadMonitor, this,
                                    streamSession, streamId, streamFile,
                                    Long.valueOf(recoverChunkOffset));
                        } catch (final IOException iox) {
                            throw panic(iox);
                        }
                    } else {
                        throw panic(error);
                    }
                } else {
                    throw panic(error);
                }
            }
        };
        downloadStream(downloadMonitor, streamMonitor, streamSession,
                streamId, streamFile, 0L);
        return streamFile;
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

    protected final String getChecksumAlgorithm() {
        return ChecksumAlgorithm.MD5.name();
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
     * Obtain the default buffer size.
     * 
     * @return An <code>Integer</code> default buffer size.
     */
    protected Integer getDefaultBufferSize() {
        return workspace.getDefaultBufferSize();
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

    /**
     * Obtain an internal migrator model.
     * 
     * @return An <code>InternalMigratorModel</code>.
     */
    protected final InternalMigratorModel getMigratorModel() {
        return modelFactory.getMigratorModel();
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
        this.workspace = workspace;

        this.configurationIO = IOFactory.getDefault(workspace).createConfigurationHandler();
        this.modelFactory = InternalModelFactory.getInstance(getContext(), environment, workspace);
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
    protected Boolean isKeyHolder(final Long artifactId) {
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
        if (workspace.isSetAttribute("localUserId")) {
            return (JabberId) workspace.getAttribute("localUserId");
        } else {
            final Credentials credentials = readCredentials();
            workspace.setAttribute("localUserId", JabberIdBuilder.build(
                    credentials.getUsername(), environment.getXMPPService(),
                    credentials.getResource()));
            return (JabberId) workspace.getAttribute("localUserId");
        }
	}

    protected final File locateStreamFile(final String streamId)
            throws IOException {
        return buildStreamFile(streamId);
    }

    /**
     * Notify all event listeners.
     *
     */
    protected void notifyListeners() {
        final Context context = new Context();
        final List<T> listeners = InternalWorkspaceModel.getInstance(context,
                environment).getListeners(workspace, this);
        try {
            for (final EventNotifier<T> notifier : notifiers) {
                for (final T listener : listeners) {
                    try {
                        notifier.notifyListener(listener);
                    } catch (final Throwable t) {
                        logger.logError(t, "Event notification {0}:{1} failed.",
                            notifier, listener);
                    }
                }
            }
        } finally {
            notifiers.clear();
        }
    }

    /**
     * Notify all event listeners.
     * 
     * @param notifier
     *            A thinkParity <code>EventNotifier</code>.
     */
    protected void notifyListeners(final EventNotifier<T> notifier) {
        notifiers.add(notifier);
    }

    /**
     * @see com.thinkparity.codebase.model.AbstractModelImpl#panic(java.lang.Throwable)
     * 
     */
	@Override
    protected ThinkParityException panic(final Throwable t) {
        final ThinkParityException tpx = super.panic(t);
        /* NOTE An attempt is made to log the error that has just occured on the
         * server.  In order to prevent recursion; a check is made to determine
         * if a remote error logging attempt is in progress before performing
         * the operation. */
        final Object workspaceLock = workspace.getAttribute("panicLock");
        if (null == workspaceLock) {
            workspace.setAttribute("panicLock", DateUtil.getInstance());
            try {
                getMigratorModel().logError(tpx, invocationContext.getMethod(),
                        invocationContext.getArguments());
            } catch (final Throwable t2) {
                logger.logError(t2, "Could not log error.");
            } finally {
                workspace.removeAttribute("panicLock");
            }
        }
        return tpx;
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
        final String password = configurationIO.read(ConfigurationKeys.Credentials.PASSWORD);
        final String resource = configurationIO.read(ConfigurationKeys.Credentials.RESOURCE);
        final String username = configurationIO.read(ConfigurationKeys.Credentials.USERNAME);

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
                throw panic(t);
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
        final String tokenValue = configurationIO.read(ConfigurationKeys.TOKEN);
        if (null == tokenValue) {
            return null;
        } else {
            try {
                final Token token = new Token();
                token.setValue(decrypt(getCipherKey(), tokenValue));
                return token;
            } catch (final Throwable t) {
                throw panic(t);
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
     * Update the credentials from the configuration.
     * 
     * @param credentials
     *            The user's credentials.
     */
    protected void updateCredentials(final Credentials credentials) {
        final String cipherKey = "18273-4897-12-53974-816523-49-81623-95-4-91-8723-56974812-63498-612395-498-7125-349871265-47892-1539784-1523954-19-287356-4";
        try {
            configurationIO.update(ConfigurationKeys.Credentials.PASSWORD, encrypt(cipherKey, credentials.getPassword()));
            configurationIO.update(ConfigurationKeys.Credentials.RESOURCE, encrypt(cipherKey, credentials.getResource()));
            configurationIO.update(ConfigurationKeys.Credentials.USERNAME, encrypt(cipherKey, credentials.getUsername()));
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Set the current execution.
     * 
     * @param invocationContext
     *            A <code>ModelInvocationContext</code>.
     */
    void setInvocationContext(final ModelInvocationContext invocationContext) {
        this.invocationContext = invocationContext;
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
        private static final String TOKEN = "TOKEN";
        private class Credentials {
            private static final String PASSWORD = "Credentials.PASSWORD";
            private static final String RESOURCE = "Credentials.RESOURCE";
            private static final String USERNAME = "Credentials.USERNAME";
        }
    }
}

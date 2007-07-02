/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.ophelia.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.Constants.ChecksumAlgorithm;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.nio.ChannelUtil;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamReader;
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
import com.thinkparity.ophelia.model.help.InternalHelpModel;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler;
import com.thinkparity.ophelia.model.migrator.InternalMigratorModel;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.queue.InternalQueueModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.session.OfflineException;
import com.thinkparity.ophelia.model.stream.InternalStreamModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.UserUtils;
import com.thinkparity.ophelia.model.util.Base64;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;
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

    /** A set of user utilitiy methods. */
    protected static final UserUtils USER_UTILS;

    /**
	 * Assertion message to be displayed if the username is not set in the
	 * parity preferences.
	 */
	private static final String ASSERT_IS_SET_USERNAME = new StringBuffer()
		.append("Before you can create the first parity artifact; you will ")
		.append("need to establish a parity session.").toString();

    static {
        ARTIFACT_UTIL = ArtifactUtil.getInstance();
        USER_UTILS = UserUtils.getInstance();
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
    protected static final <U extends User> Boolean contains(
            final List<U> users, final JabberId userId) {
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
    protected static final <U extends User, V extends User> Boolean contains(
            final List<U> users, final V user) {
        return USER_UTILS.contains(users, user);
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
     * Extract the user's ids into a list.
     * 
     * @param <T>
     *            A user type.
     * @param users
     *            A <code>List</code> of <code>T</code>.
     * @param userIds
     *            A <code>List</code> to populate.
     * @return A <code>List</code> of <code>JabberId</code>s.
     */
    protected static final <U extends User> List<JabberId> getIds(
            final List<U> users, final List<JabberId> userIds) {
        return USER_UTILS.getIds(users, userIds);
    }

    protected static ResourceBundle getLocalizationBundle(final String baseName) {
        return ResourceBundle.getBundle(baseName);
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
    protected static final <U extends User> int indexOf(final List<U> users,
            final JabberId userId) {
        return USER_UTILS.indexOf(users, userId);
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
    protected static final <U extends User, V extends User> int indexOf(
            final List<U> users, final V user) {
        return USER_UTILS.indexOf(users, user);
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
     * Assert that the xmpp service is online.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     */
    protected void assertServiceIsReachable(final Environment environment) {
        Assert.assertTrue(environment.isServiceReachable(),
                "Service environment {0}:{1} is not reachable.",
                environment.getServiceHost(), environment.getServicePort());
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
        Assert.assertTrue(assertion, contains(team, getUserModel().read(userId)));
    }

    /**
     * Copy the content of a channel to a file. Create a channel to write to the
     * file.
     * 
     * @param channel
     *            A <code>ReadableByteChannel</code>.
     * @param file
     *            A <code>File</code>.
     * @throws IOException
     */
    protected final void channelToFile(final ReadableByteChannel channel,
            final File file) throws IOException {
        final WritableByteChannel writeChannel = ChannelUtil.openWriteChannel(file);
        try {
            channelToChannel(channel, writeChannel);
        } finally {
            writeChannel.close();
        }
    }

    /**
     * Calculate a checksum for a file's contents. Create a channel to read the
     * file.
     * 
     * @param file
     *            A <code>File</code>.
     * @return An MD5 checksum <code>String</code>.
     */
    protected final String checksum(final File file) throws IOException {
        final ReadableByteChannel channel = ChannelUtil.openReadChannel(file);
        try {
            return checksum(channel);
        } finally {
            channel.close();
        }
    }

    /**
     * Calculate a checksum for a readable byte channel. Use the workspace
     * buffer as an intermediary.
     * 
     * @param channel
     *            A <code>ReadableByteChannel</code>.
     * @return An MD5 checksum <code>String</code>.
     */
    protected final String checksum(final ReadableByteChannel channel)
            throws IOException {
        synchronized (workspace.getBufferLock()) {
            return MD5Util.md5Base64(channel, workspace.getBufferArray());
        }
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
            configurationIO.create(ConfigurationKeys.Credentials.USERNAME, encrypt(cipherKey, credentials.getUsername()));
            return readCredentials();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Create a temporary file.
     * 
     * @return A <code>File</code>.
     */
    protected final File createTempFile() throws IOException {
        return workspace.createTempFile();
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
     * Determine whether or not a earliest version exists.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @return True if a version exists; false otherwise.
     */
    protected Boolean doesExistEarliestVersion(final Long artifactId) {
        return getArtifactModel().doesVersionExist(artifactId);
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
     * Assert the session is online. We are throwing a specific error here in
     * order to allow a client of the model an opportunity to display an
     * appropriate message.
     * 
     */
    protected final void ensureOnline() {
        if (!getSessionModel().isOnline().booleanValue())
            throw new OfflineException();
    }

    /**
     * Copy the content of a file to a channel. Create a channel to read the
     * file.
     * 
     * @param file
     *            A <code>File</code>.
     * @param channel
     *            A <code>WritableByteChannel</code>.
     * @throws IOException
     */
    protected final void fileToChannel(final File file,
            final WritableByteChannel channel) throws IOException {
        final ReadableByteChannel readChannel = ChannelUtil.openReadChannel(file);
        try {
            channelToChannel(readChannel, channel);
        } finally {
            readChannel.close();
        }
    }

    /**
     * Copy the content of a file to another file. Create a channel to read the
     * file.
     * 
     * @param readFile
     *            A <code>File</code>.
     * @param writeFile
     *            A <code>File</code>.
     * @throws IOException
     */
    protected final void fileToFile(final File readFile, final File writeFile)
            throws IOException {
        final ReadableByteChannel readChannel = ChannelUtil.openReadChannel(readFile);
        try {
            channelToFile(readChannel, writeFile);
        } finally {
            readChannel.close();
        }
    }

    /**
     * Copy the content of a file to a stream. Create a channel to read the
     * file.
     * 
     * @param file
     *            A <code>File</code>.
     * @param stream
     *            An <code>OutputStream</code>.
     * @throws IOException
     */
    protected final void fileToStream(final File file, final OutputStream stream)
            throws IOException {
        final ReadableByteChannel channel = ChannelUtil.openReadChannel(file);
        try {
            channelToStream(channel, stream);
        } finally {
            channel.close();
        }
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
     * Obtain the workspace buffer size.
     * 
     * @return An <code>Integer</code> default buffer size.
     */
    protected Integer getBufferSize() {
        return workspace.getBufferSize();
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
     * Obtain an internal document model.
     * 
     * @return An instance of <code>InternalDocumentModel</code>.
     */
	protected final InternalDocumentModel getDocumentModel() {
		return modelFactory.getDocumentModel();
	}

    /**
     * Obtain an internal help model.
     * 
     * @return An instance of <code>InternalHelpModel</code>.
     */
    protected final InternalHelpModel getHelpModel() {
        return modelFactory.getHelpModel();
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
     * Obtain an internal queue model.
     * 
     * @return An instance of <code>InternalQueueModel</code>.
     */
    protected InternalQueueModel getQueueModel() {
        return modelFactory.getQueueModel();
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
     * Obtain an internal stream model.
     * 
     * @return An instance of <code>InternalStreamModel</code>.
     */
    protected final InternalStreamModel getStreamModel() {
        return modelFactory.getStreamModel();
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
     * Determine if the user id matched the local user id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the local user id matches the user id.
     */
    protected Boolean isLocalUserId(final JabberId userId) {
        return localUserId().equals(userId);
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
            // TODO - Model#localUserId - rework user ids similar to artifact ids
            workspace.setAttribute("localUserId", JabberIdBuilder.build(
                    credentials.getUsername(), "thinkparity.net"));
            return (JabberId) workspace.getAttribute("localUserId");
        }
	}

    protected final File locateStreamFile(final String streamId)
            throws IOException {
        return buildStreamFile(streamId);
    }

    /**
     * Create a new download helper.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @return A <code>DownloadHelper</code>.
     */
    protected final DownloadHelper newDownloadHelper(final StreamSession session) {
        final BytesFormat bytesFormat = new BytesFormat();

        return new DownloadHelper() {

            /**
             * @see com.thinkparity.ophelia.model.DownloadHelper#download(java.io.File)
             * 
             */
            public void download(final File target) throws IOException {
                ensureDownload(target);
                attemptDownload(target);
            }
        
            /**
             * Attempt a download of the version. Create a new stream reader using the
             * session; and download the version to the temp file, then return the temp
             * file.
             * 
             * @param target
             *            A <code>File</code> to download to.
             * @throws IOException
             */
            private void attemptDownload(final File target) throws IOException {
                final StreamReader reader = new StreamReader(new StreamMonitor () {

                    /**
                     * @see com.thinkparity.codebase.model.stream.StreamMonitor#chunkReceived(int)
                     *
                     */
                    public void chunkReceived(final int chunkSize) {
                        logger.logDebug("Downloaded {0}.",
                                bytesFormat.format(new Long(chunkSize)));
                    }
        
                    /**
                     * @see com.thinkparity.codebase.model.stream.StreamMonitor#chunkSent(int)
                     *
                     */
                    public void chunkSent(final int chunkSize) {
                        // not possbile in download
                        Assert.assertUnreachable("");
                    }
        
                    /**
                     * @see com.thinkparity.codebase.model.stream.StreamMonitor#getName()
                     *
                     */
                    public String getName() {
                        return "Model#newDownloadHelper";
                    }

                }, session);
                final OutputStream output = new FileOutputStream(target);
                try {
                    reader.read(output);
                } finally {
                    output.close();
                }
            }

            /**
             * Ensure a download is possible by checking the target file for validity as
             * well as the session.
             * 
             * @param target
             *            A <code>File</code>.
             */
            private void ensureDownload(final File target) {
                final String error;
                if (null == target) {
                    error = "Target must not be null.";
                } else if (!target.exists()) {
                    error = "Target {0} must exist.";
                } else if (!target.isFile()) {
                    error = "Target {0} must be a file.";
                } else if (session == null) {
                    error = "Stream ession must exist.";
                } else {
                    error = null;
                }
                if (null != error) {
                    throw new IllegalArgumentException(MessageFormat.format(error,
                            target, session));
                }
            }
        };
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
        final String username = configurationIO.read(ConfigurationKeys.Credentials.USERNAME);

        if (null == username || null == password) {
            return null;
        } else {
            final Credentials credentials = new Credentials();
            try {
                credentials.setPassword(decrypt(cipherKey, password));
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
     * Copy the content of a stream to a channel. Use the workspace byte buffer
     * as an intermediary.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @param channel
     *            A <code>WritableByteChannel</code>.
     * @throws IOException
     */
    protected final void streamToChannel(final InputStream stream,
            final WritableByteChannel channel) throws IOException {
        synchronized (workspace.getBufferLock()) {
            StreamUtil.copy(stream, channel, workspace.getBuffer());
        }
    }

    /**
     * Copy the content of a stream to a file. Use a channel to write to the
     * file.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @param file
     *            A <code>File</code>.
     * @throws IOException
     */
    protected final void streamToFile(final InputStream stream, final File file)
            throws IOException {
        final WritableByteChannel channel = ChannelUtil.openWriteChannel(file);
        try {
            streamToChannel(stream, channel);
        } finally {
            channel.close();
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
            configurationIO.update(ConfigurationKeys.Credentials.PASSWORD, encrypt(cipherKey, credentials.getPassword()));
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
     * Copy the content of one channel to another. Use the workspace buffer as
     * an intermediary.
     * 
     * @param readChannel
     *            A <code>ReadableByteChannel</code>.
     * @param writeChannel
     *            A <code>WritableByteChannel</code>.
     * @throws IOException
     */
    private void channelToChannel(final ReadableByteChannel readChannel,
            final WritableByteChannel writeChannel) throws IOException {
        synchronized (workspace.getBufferLock()) {
            ChannelUtil.copy(readChannel, writeChannel, workspace.getBuffer());
        }
    }

    /**
     * Copy the content of a channel to a stream. Use the workspace buffer as an
     * intermediary.
     * 
     * @param channel
     *            A <code>ReadableByteChannel</code>.
     * @param stream
     *            An <code>OutputStream</code>.
     * @throws IOException
     */
    private void channelToStream(final ReadableByteChannel channel,
            final OutputStream stream) throws IOException {
        synchronized (workspace.getBufferLock()) {
            StreamUtil.copy(channel, stream, workspace.getBuffer());
        }
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
     * Determine whether or not the user's credentials have been set.
     * 
     * @return True if the credentials have been set; false otherwise.
     */
    private Boolean isSetCredentials() {
        return null != readCredentials();
    }

    /** Configuration keys. */
    private static class ConfigurationKeys {
        private static final String TOKEN = "TOKEN";
        private class Credentials {
            private static final String PASSWORD = "Credentials.PASSWORD";
            private static final String USERNAME = "Credentials.USERNAME";
        }
    }
}

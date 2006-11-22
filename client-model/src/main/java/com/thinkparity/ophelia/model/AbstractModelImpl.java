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

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamReader;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.Constants.Versioning;
import com.thinkparity.ophelia.model.archive.ArchiveModel;
import com.thinkparity.ophelia.model.archive.InternalArchiveModel;
import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.audit.AuditModel;
import com.thinkparity.ophelia.model.audit.InternalAuditModel;
import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.backup.InternalBackupModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.download.DownloadModel;
import com.thinkparity.ophelia.model.download.InternalDownloadModel;
import com.thinkparity.ophelia.model.index.IndexModel;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler;
import com.thinkparity.ophelia.model.message.InternalSystemMessageModel;
import com.thinkparity.ophelia.model.message.SystemMessageModel;
import com.thinkparity.ophelia.model.migrator.InternalLibraryModel;
import com.thinkparity.ophelia.model.migrator.InternalReleaseModel;
import com.thinkparity.ophelia.model.migrator.LibraryModel;
import com.thinkparity.ophelia.model.migrator.ReleaseModel;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.TeamMember;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.util.Base64;
import com.thinkparity.ophelia.model.util.MD5Util;
import com.thinkparity.ophelia.model.util.localization.Localization;
import com.thinkparity.ophelia.model.util.localization.LocalizationContext;
import com.thinkparity.ophelia.model.workspace.InternalWorkspaceModel;
import com.thinkparity.ophelia.model.workspace.Preferences;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * AbstractModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
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
    protected final Environment environment;

	/** An internal model factory. */
    protected final InternalModelFactory internalModelFactory;

	/** A localization interface. */
	protected final L18n l18n;

    /** An apache logger. */
	protected final Log4JWrapper logger;

	/** The thinkParity workspace <code>Preferences</code>. */
	protected final Preferences preferences;

	/** A thinkParity <code>Workspace</code>. */
	protected final Workspace workspace;

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
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
	protected AbstractModelImpl(final Environment environment,
            final Workspace workspace) {
		super();
        this.environment = environment;
        this.internalModelFactory = new InternalModelFactory(getContext(),
                environment, workspace);
		this.l18n = new Localization(LocalizationContext.MODEL);
        this.logger = new Log4JWrapper(getClass());
		this.workspace = workspace;
		this.preferences = (null == workspace ? null : workspace.getPreferences());
	}

	/**
     * Add a thinkParity event listener.
     * 
     * @param listener
     *            A thinkParity <code>EventListener</code>.
     * @return Whether or not the listener list was modified as a result of
     *         calling add.
     */
    protected boolean addListener(final T listener) {
        return getWorkspaceModel().addListener(workspace, this, listener);
    }

    /**
     * Assert a draft doesn't exist for the container.
     * 
     * @param assertion
     *            An assertion.
     * @param containerId
     *            A container id.
     */
    protected void assertContainerDraftDoesNotExist(final Object assertion, final Long containerId) {
        Assert.assertIsNull(assertion, getContainerModel().readDraft(containerId));
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
        final List<TeamMember> team = getInternalArtifactModel().readTeam2(artifactId);
        final User user = getInternalUserModel().read(userId);
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
        final List<TeamMember> team = getInternalArtifactModel().readTeam2(artifactId);
        Assert.assertNotTrue(assertion, contains(team, getInternalUserModel().read(userId)));
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
    protected Boolean contains(final List<TeamMember> team,
            final JabberId userId) {
        return -1 != indexOf(team, userId);
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
    protected Boolean contains(final List<TeamMember> team, final User user) {
        return -1 != indexOf(team, user);
    }

    /**
     * Determine if the list of users contains the team member.
     * 
     * @param users
     *            A list of users.
     * @param contact
     *            A contact.
     * @return True if the id of the team member matches one of the users.
     */
    protected Boolean contains(final List<User> users, final Contact contact) {
        return -1 != indexOf(users, contact);
    }

    /**
     * Determine if the list of users contains the team member.
     * 
     * @param users
     *            A list of users.
     * @param teamMember
     *            A team member.
     * @return True if the id of the team member matches one of the users.
     */
    protected Boolean contains(final List<User> users, final TeamMember teamMember) {
        return -1 != indexOf(users, teamMember);
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
     * Determine whether or not a latest version exists.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @return True if a version exists; false otherwise.
     */
    protected Boolean doesExistLatestVersion(final Long artifactId) {
        return doesExistVersion(artifactId, Versioning.START);
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
        return getInternalArtifactModel().doesVersionExist(artifactId, versionId);
    }

    /**
     * Download a stream from the stream server.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return A stream <code>File</code>.
     */
    protected final File downloadStream(final String streamId)
            throws IOException {
        final File streamFile = workspace.createTempFile(streamId);
        final FileOutputStream stream = new FileOutputStream(streamFile);
        final StreamSession session = getSessionModel().createStreamSession();
        final StreamReader reader = new StreamReader(session);
        try {
            reader.open();
            reader.read(streamId, stream);
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

    protected InternalArchiveModel getArchiveModel() {
        return ArchiveModel.getInternalModel(getContext(), environment, workspace);
    }

    protected InternalBackupModel getBackupModel() {
        return BackupModel.getInternalModel(getContext(), environment, workspace);
    }

    /**
     * Obtain the internal thinkParity container interface.
     * 
     * @return The internal thinkParity container interface.
     */
    protected InternalContainerModel getContainerModel() {
        return ContainerModel.getInternalModel(getContext(), environment, workspace);
    }

    protected InternalIndexModel getIndexModel() {
        return IndexModel.getInternalModel(getContext(), environment, workspace);
    }

    /**
     * Obtain the internal parity artifact interface.
     * 
     * @return The internal parity artifact interface.
     */
	protected InternalArtifactModel getInternalArtifactModel() {
		return ArtifactModel.getInternalModel(getContext(), environment, workspace);
	}

	/**
     * Obtain the internal parity audit interface.
     * 
     * @return The internal parity audit interface.
     */
    protected InternalAuditModel getInternalAuditModel() {
		return AuditModel.getInternalModel(getContext(), environment, workspace);
	}

    /**
     * Obtain the internal thinkParity contact interface.
     * 
     * @return The internal thinkParity contact interface.
     */
    protected InternalContactModel getInternalContactModel() {
        return ContactModel.getInternalModel(getContext(), environment, workspace);
    }

	/**
     * Obtain the internal parity document interface.
     * 
     * @return The internal parity document interface.
     */
	protected InternalDocumentModel getInternalDocumentModel() {
		return DocumentModel.getInternalModel(getContext(), environment, workspace);
	};

	/**
     * Obtain the internal parity download interface.
     *
     * @return The internal parity download interface.
     */
    protected InternalDownloadModel getInternalDownloadModel() {
        return DownloadModel.getInternalModel(getContext(), environment, workspace);
    }

	/**
     * Obtain the internal parity library interface.
     *
     * @return The internal parity library interface.
     */
    protected InternalLibraryModel getInternalLibraryModel() {
        return LibraryModel.getInternalModel(getContext(), environment, workspace);
    }

	/**
     * Obtain the thinkParity internal message interface.
     * 
     * @return The thinkParity internal message interface.
     */
    protected InternalSystemMessageModel getInternalMessageModel() {
        return getInternalSystemMessageModel();
    }

    /**
     * Obtain the internal parity release interface.
     *
     * @return The internal parity release interface.
     */
    protected InternalReleaseModel getInternalReleaseModel() {
        return ReleaseModel.getInternalModel(getContext(), environment, workspace);
    }

    /**
     * Obtain the internal parity system message interface.
     * 
     * @return The internal parity system message interface.
     */
	protected InternalSystemMessageModel getInternalSystemMessageModel() {
		return SystemMessageModel.getInternalModel(getContext(), environment, workspace);
	}

    /**
     * Obtain the internal parity user interface.
     * 
     * @return The internal parity user interface.
     */
    protected InternalUserModel getInternalUserModel() {
        return UserModel.getInternalModel(getContext(), environment, workspace);
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
     * Obtain the thinkParity internal profile interface.
     * 
     * @return A thinkParity internal profile interface.
     */
    protected InternalProfileModel getProfileModel() {
        return ProfileModel.getInternalModel(getContext(), environment, workspace);
    }

    /**
     * Obtain the internal parity session interface.
     * 
     * @return The internal parity session interface.
     */
	protected InternalSessionModel getSessionModel() {
		return SessionModel.getInternalModel(getContext(), environment, workspace);
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
     * Obtain the index of a user id in a team.
     * 
     * @param team
     *            A team.
     * @param userId
     *            A user id.
     * @return The index of the user id in the list; or -1 if the user does not
     *         exist in the list.
     */
    protected int indexOf(final List<TeamMember> team, final JabberId userId) {
        for (int i = 0; i < team.size(); i++) {
            if (team.get(i).getId().equals(userId)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Obtain the index of the user in the team.
     * 
     * @param team
     *            The team.
     * @param user
     *            A user.
     * @return The index of the user in the team; or -1 if the user does not
     *         exist in the list.
     */
    protected int indexOf(final List<TeamMember> team, final User user) {
        for (int i = 0; i < team.size(); i++) {
            if (team.get(i).getId().equals(user.getId())) {
                return i;
            }
        }
        return -1;
    }

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
        final InternalArtifactModel artifactModel = getInternalArtifactModel();
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

    /**
     * Obtain the team member for the local user.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The team member.
     */
    protected TeamMember localTeamMember(final Long artifactId) {
        final List<TeamMember> team = getInternalArtifactModel().readTeam2(artifactId);
        return get(team, localUser());
    }

    /**
     * Obtain the local user.
     *
     * @return The current user.
     */
    protected User localUser() {
        final JabberId currentUserId = localUserId();
        if(null == currentUserId) { return null; }
        else { return getInternalUserModel().read(currentUserId); }
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
                localUserId = JabberIdBuilder.build(
                        credentials.getUsername(), Constants.Jabber.DOMAIN,
                        credentials.getResource());
            }
        }
        return localUserId;
	}

    /**
     * Notify all event listeners.
     * 
     * @param notifier
     *            A thinkParity <code>EventNotifier</code>.
     */
    protected void notifyListeners(final EventNotifier<T> notifier) {
        final List<T> listeners = getWorkspaceModel().getListeners(workspace, this);
        for (final T listener : listeners) {
            notifier.notifyListener(listener);
        }
    }

    /**
     * Read the artifact unique id.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact unique id.
     */
    protected UUID readArtifactUniqueId(final Long artifactId) {
        return getInternalArtifactModel().readUniqueId(artifactId);
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
        final Long latestVersionId = getInternalArtifactModel().readLatestVersionId(artifactId);
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
    protected boolean removeListener(final T listener) {
        return getWorkspaceModel().removeListener(workspace, this, listener);
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
    protected String uploadStream(final StreamMonitor monitor,
            final StreamSession session, final InputStream stream,
            final Long streamSize) throws IOException {
        final InternalSessionModel sessionModel = getSessionModel();
        final StreamWriter writer = new StreamWriter(monitor, session);
        writer.open();
        try {
            final String streamId = sessionModel.createStream(session);
            writer.write(streamId, stream, streamSize);
            return streamId;
        } finally {
            writer.close();
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
     * Obtain an internal thinkParity workspace interface.
     * 
     * @return An <code>InternalWorkspaceModel</code>.
     */
    private InternalWorkspaceModel getWorkspaceModel() {
        return WorkspaceModel.getInternalModel(getContext(), environment);
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
    private int indexOf(final List<User> users, final Contact contact) {
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getId().equals(contact.getId())) { return i; }
        }
        return -1;
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
    private int indexOf(final List<User> users, final TeamMember teamMember) {
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getId().equals(teamMember.getId())) { return i; }
        }
        return -1;
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
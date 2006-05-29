/*
 * Created On: Aug 6, 2005
 * $Id$
 */
package com.thinkparity.model.parity.model;

import java.util.Calendar;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.l10n.L18n;
import com.thinkparity.codebase.l10n.L18nContext;

import com.thinkparity.model.LoggerFactory;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.artifact.InternalArtifactModel;
import com.thinkparity.model.parity.model.audit.AuditModel;
import com.thinkparity.model.parity.model.audit.InternalAuditModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.InternalDocumentModel;
import com.thinkparity.model.parity.model.download.DownloadModel;
import com.thinkparity.model.parity.model.download.InternalDownloadModel;
import com.thinkparity.model.parity.model.library.InternalLibraryModel;
import com.thinkparity.model.parity.model.library.LibraryModel;
import com.thinkparity.model.parity.model.message.system.InternalSystemMessageModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.release.InternalReleaseModel;
import com.thinkparity.model.parity.model.release.ReleaseModel;
import com.thinkparity.model.parity.model.session.InternalSessionModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.user.InternalUserModel;
import com.thinkparity.model.parity.model.user.UserModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.util.l10n.ModelL18n;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.user.User;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * AbstractModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl {

	/**
	 * Assertion message to be displayed if the username is not set in the
	 * parity preferences.
	 */
	private static final String ASSERT_IS_SET_USERNAME = new StringBuffer()
		.append("Before you can create the first parity artifact; you will ")
		.append("need to establish a parity session.").toString();

	/**
	 * The session model context
	 * 
	 * @see #getSessionModelContext()
	 */
	private static Context sessionModelContext;

	/**
	 * Obtain the current date\time.
	 * 
	 * @return The current date\time.
	 */
	protected static Calendar currentDateTime() { return DateUtil.getInstance(); }

	/**
	 * Obtain the session model context.
	 * 
	 * @return The session model context.
	 */
	protected static Context getSessionModelContext() {
		if(null == sessionModelContext) {
			sessionModelContext = new Context(SessionModel.class);
		}
		return sessionModelContext;
	}

	/**
	 * The parity model context.
	 * 
	 */
	protected final Context context;

	/**
	 * The model's l18n.
	 * 
	 */
	protected final L18n l18n;

	/**
	 * Apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * Handle to the parity model preferences.
	 */
	protected final Preferences preferences;

	/**
	 * Handle to the parity model workspace.
	 */
	protected final Workspace workspace;

	/**
	 * Create a AbstractModelImpl.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	protected AbstractModelImpl(final Workspace workspace) {
		this(workspace, null);
	}

	/**
	 * Create an AbstractModelImpl
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	protected AbstractModelImpl(final Workspace workspace,
			final L18nContext l18nContext) {
		super();
		this.context = new Context(getClass());
		this.l18n = null == l18nContext ? null : new ModelL18n(l18nContext);
        this.logger = LoggerFactory.getLogger(getClass());
		this.workspace = workspace;
		this.preferences = (null == workspace ? null : workspace.getPreferences());
	}

	/**
	 * Assert that the model framework is initialized to a state where the user
	 * can start to create artifacts. This requires:
	 * <ol>
	 * <li>The user has logged in at least once.</li>
	 * </ol>
	 * 
	 */
	protected void assertCanCreateArtifacts() {
		Assert.assertTrue(ASSERT_IS_SET_USERNAME, preferences.isSetUsername());
	}

    /**
     * Assert the user is the key holder. An assertion that the user is online
     * is also made.
     * 
     * @param assertion
     *            The assertion message.
     * @param artifactId
     *            The artifact id.
     */
    protected void assertIsKeyHolder(final String assertion,
            final Long artifactId) throws ParityException {
        Assert.assertTrue(assertion, isOnline());
        Assert.assertTrue(assertion, isKeyHolder(artifactId));
    }

    /**
     * Determine whether or not the logged in user is the artifact key holder.
     *
     * @param artifactId
     *      The artifact id.
     * @return True if the user is the keyholder; false otherwise.
     */
    protected Boolean isKeyHolder(final Long artifactId) throws ParityException {
        return getInternalSessionModel().isLoggedInUserKeyHolder(artifactId);
    }

    /**
     * Assert that the logged in user is not the key holder.
     * 
     * @param assertion
     *            The assertion message.
     * @param artifactId
     *            The artifact id.
     */
	protected void assertIsNotKeyHolder(final String assertion,
            final Long artifactId) throws ParityException {
		Assert.assertNotTrue(assertion, isKeyHolder(artifactId));
	}

	/**
	 * Assert that the calling method has not yet been implemented.
	 *
	 */
	protected void assertNYI() {
		Assert.assertNotYetImplemented("The calling method has not yet been implemented.");
	}

	/**
     * Assert the user is online.
     *
     * @param assertion
     *      The assertion message.
     */
    protected void assertOnline(final String assertion) {
        Assert.assertTrue(assertion, isOnline());
    }

    /**
     * Determine whether or not the user is online.
     *
     * @return True if the user is online; false otherwise.
     */
    protected Boolean isOnline() {
        return getInternalSessionModel().isLoggedIn();
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
			// i can close it or delete it
			Assert.assertTrue(
					formatAssertion(currentState, intendedState, new ArtifactState[] {ArtifactState.CLOSED}),
						ArtifactState.CLOSED == intendedState);
			break;
		case CLOSED:
			// i can delete it
			Assert.assertTrue(
					formatAssertion(currentState, intendedState, new ArtifactState[] {ArtifactState.DELETED}),
						ArtifactState.DELETED == intendedState);
			break;
		default: Assert.assertUnreachable("Unknown artifact state:  " + currentState);
		}
	}

	/**
	 * Build a jabber id from a parity user.
	 * 
	 * @param user
	 *            The parity user.
	 * @return The jabber id.
	 */
	protected JabberId buildJabberId(final User user) {
		JabberId jabberId = null;
		try { jabberId =
			JabberIdBuilder.parseQualifiedJabberId(user.getUsername()); }
		catch(final IllegalArgumentException iax) {}
		if(null != jabberId) {
			try {
				jabberId =
					JabberIdBuilder.parseQualifiedUsername(user.getUsername());
			}
			catch(final IllegalArgumentException iax) {}
			if(null != jabberId)
				jabberId = JabberIdBuilder.parseUsername(user.getUsername());
		}
		return jabberId;
	}

	/**
     * Obtain the current user.
     *
     * @return The current user.
     */
    protected User currentUser() throws ParityException {
        assertOnline("[LMODEL] [ABSTRACTION] [CURRENT USER] [NOT ONLINE]");
        return getInternalSessionModel().getLoggedInUser();
    }

    /**
	 * Obtain the current user id.
	 * 
	 * @return The jabber id of the current user.
	 */
	protected JabberId currentUserId() {
		return JabberIdBuilder.parseUsername(preferences.getUsername());
	}

	protected Long getArtifactId(final UUID artifactUniqueId)
			throws ParityException {
		// NOTE I'm assuming document
		final InternalDocumentModel iDModel = getInternalDocumentModel();
		final Document d = iDModel.get(artifactUniqueId);
		if(null == d) {
			logger.warn("Local document:  " + artifactUniqueId + " does not exist.");
			return null;
		}
		else { return d.getId(); }
	}

	protected UUID getArtifactUniqueId(final Long artifactId)
			throws ParityException {
		// NOTE I'm assuming document :)
		final InternalDocumentModel iDocumentModel = getInternalDocumentModel();
		return iDocumentModel.get(artifactId).getUniqueId();
	}

	protected UUID getArtifactUniqueId(final Long artifactId,
			final ArtifactType artifactType) throws ParityException {
		switch(artifactType) {
		case DOCUMENT:
			final InternalDocumentModel iDocumentModel = getInternalDocumentModel();
			return iDocumentModel.get(artifactId).getUniqueId();
		default:
			throw Assert.createUnreachable("");
		}
	}

	/**
	 * Obtain the model's context.
	 * 
	 * @return The model's context.
	 */
	protected Context getContext() { return context; }

	/**
	 * Obtain a the parity document interface.
	 * 
	 * @return The parity document interface.
	 */
	protected DocumentModel getDocumentModel() { return DocumentModel.getModel(); }

    /**
     * Obtain the internal parity artifact interface.
     * 
     * @return The internal parity artifact interface.
     */
	protected InternalArtifactModel getInternalArtifactModel() {
		return ArtifactModel.getInternalModel(context);
	}

    /**
     * Obtain the internal parity audit interface.
     * 
     * @return The internal parity audit interface.
     */
    protected InternalAuditModel getInternalAuditModel() {
		return AuditModel.getInternalModel(context);
	}

    /**
     * Obtain the internal parity document interface.
     * 
     * @return The internal parity document interface.
     */
	protected InternalDocumentModel getInternalDocumentModel() {
		return DocumentModel.getInternalModel(context);
	}

    /**
     * Obtain the internal parity download interface.
     *
     * @return The internal parity download interface.
     */
    protected InternalDownloadModel getInternalDownloadModel() {
        return DownloadModel.getInternalModel(context);
    }

    /**
     * Obtain the internal parity library interface.
     *
     * @return The internal parity library interface.
     */
    protected InternalLibraryModel getInternalLibraryModel() {
        return LibraryModel.getInternalModel(context);
    }

	/**
     * Obtain the internal parity session interface.
     * 
     * @return The internal parity session interface.
     */
	protected InternalSessionModel getInternalSessionModel() {
		return SessionModel.getInternalModel(getContext());
	}

    /**
     * Obtain the internal parity release interface.
     *
     * @return The internal parity release interface.
     */
    protected InternalReleaseModel getInternalReleaseModel() {
        return ReleaseModel.getInternalModel(getContext());
    }

    /**
     * Obtain the internal parity system message interface.
     * 
     * @return The internal parity system message interface.
     */
	protected InternalSystemMessageModel getInternalSystemMessageModel() {
		return SystemMessageModel.getInternalModel(context);
	}

    /**
     * Obtain the internal parity user interface.
     * 
     * @return The internal parity user interface.
     */
    protected InternalUserModel getInternalUserModel() {
        return UserModel.getInternalModel(context);
    }

	/**
	 * Obtain the model's localization.
	 * 
	 * @return The model's localization.
	 */
	protected L18n getL18n() { return l18n; }

	/**
	 * Obtain a handle to the session model.
	 * 
	 * @return Obtain a handle to the session model.
	 */
	protected SessionModel getSessionModel() { return SessionModel.getModel(); };

	/**
	 * @see ModelL18n#getString(String)
	 * 
	 */
	protected String getString(final String localKey) {
		return l18n.getString(localKey);
	}

	/**
	 * @see ModelL18n#getString(String, Object[])
	 * 
	 */
	protected String getString(final String localKey, final Object[] arguments) {
		return l18n.getString(localKey, arguments);
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
}
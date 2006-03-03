/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model;

import java.util.Calendar;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.artifact.InternalArtifactModel;
import com.thinkparity.model.parity.model.audit.InternalAuditModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.InternalDocumentModel;
import com.thinkparity.model.parity.model.session.InternalSessionModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.user.User;


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
	 * Apache logger.
	 * 
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(getClass());

	/**
	 * Handle to the parity model preferences.
	 */
	protected final Preferences preferences;

	/**
	 * Handle to the parity model workspace.
	 */
	protected final Workspace workspace;

	/**
	 * Create an AbstractModelImpl
	 * 
	 * @param workspace
	 *            Handle to an existing parity model workspace.
	 */
	protected AbstractModelImpl(final Workspace workspace) {
		super();
		this.context = new Context(getClass());
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
	 * Ensure that the user's parity session is valid.
	 *
	 */
	protected void assertIsSessionValid() {
		final SessionModel sessionModel = getSessionModel();
		Assert.assertTrue(
				"Current session is not valid:  " +
					StackUtil.getCallerClassAndMethodName(),
				sessionModel.isLoggedIn());
	}

	/**
	 * Assert that the logged in user is the key holder for the artifact id.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user is offline.
	 *             <li>If the logged in user is not the key holder.
	 *             </ul>
	 * @throws ParityException
	 */
	protected void assertLoggedInUserIsKeyHolder(final Long artifactId)
			throws ParityException {
		final InternalSessionModel iSModel = getInternalSessionModel();
		Assert.assertTrue("Logged in user is not the key holder.",
				iSModel.isLoggedInUserKeyHolder(artifactId));
	}

	/**
	 * Assert that the calling method has not yet been implemented.
	 *
	 */
	protected void assertNYI() {
		Assert.assertNotYetImplemented("The calling method has not yet been implemented.");
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
					formatAssertion(currentState, intendedState, new ArtifactState[] {ArtifactState.CLOSED, ArtifactState.DELETED}),
						ArtifactState.CLOSED == intendedState || ArtifactState.DELETED == intendedState);
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
	 * Obtain a handle to the project model.
	 * 
	 * @return A handle to the project model.
	 */
	protected DocumentModel getDocumentModel() { return DocumentModel.getModel(); }

	protected InternalArtifactModel getInternalArtifactModel() {
		return ArtifactModel.getInternalModel(context);
	}

	protected InternalAuditModel getInternalAuditModel() {
		return InternalAuditModel.getInternalModel(context);
	}

	protected InternalDocumentModel getInternalDocumentModel() {
		return DocumentModel.getInternalModel(context);
	}

	/**
	 * Obtain an internal session model.
	 * 
	 * @return The internal session model.
	 */
	protected InternalSessionModel getInternalSessionModel() {
		return SessionModel.getInternalModel(getContext());
	}

	/**
	 * Obtain a handle to the session model.
	 * 
	 * @return Obtain a handle to the session model.
	 */
	protected SessionModel getSessionModel() { return SessionModel.getModel(); }

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
}
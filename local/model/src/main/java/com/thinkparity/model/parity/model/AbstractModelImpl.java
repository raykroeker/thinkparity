/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.InternalDocumentModel;
import com.thinkparity.model.parity.model.session.InternalSessionModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.user.User;


/**
 * AbstractModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl {

	/**
	 * Empty list of parity object flags.
	 */
	protected static final Collection<ArtifactFlag> NO_FLAGS =
		Collections.unmodifiableCollection(new Vector<ArtifactFlag>(0));

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
	 *             If the logged in user is not the key holder.
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
	 * Flag the object as not having been seen. This will remove the seen flag
	 * from the object, and remove the seen flag from the parent objects all the
	 * way up the tree.
	 * 
	 * @param parityObject
	 *            The object to flag.
	 * @throws ParityException
	 */
	protected void flagAsNotSEEN(final Artifact parityObject)
			throws ParityException {
		switch(parityObject.getType()) {
		case DOCUMENT:
			flagAsNotSEEN((Document) parityObject);
			break;
		default:
			throw Assert.createUnreachable(
					"removeFlag(Artifact,ArtifactFlag)");
		}
	}

	/**
	 * Flag the object as having been seen. This will add the SEEN flag to the
	 * object; then check the parent to see if all of the siblings of the object
	 * contain the SEEN flag; and if they do, the parent will be flagged as
	 * well.
	 * 
	 * @param parityObject
	 *            The object to flag.
	 * @throws ParityException
	 */
	protected void flagAsSEEN(final Artifact parityObject) throws ParityException {
		switch(parityObject.getType()) {
		case DOCUMENT:
			flagAsSEEN((Document) parityObject);
			break;
		default:
			throw Assert.createUnreachable("flagAsSEEN(Artifact)");
		}
	}

	protected Long getArtifactId(final UUID artifactUniqueId)
			throws ParityException {
		// NOTE I'm assuming document
		final InternalDocumentModel iDModel = getInternalDocumentModel();
		return iDModel.get(artifactUniqueId).getId();
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

	protected InternalDocumentModel getInternalDocumentModel() {
		final InternalDocumentModel iDocumentModel =
			DocumentModel.getInternalModel(getContext());
		return iDocumentModel;
	}

	/**
	 * Obtain an internal session model.
	 * 
	 * @return The internal session model.
	 */
	protected InternalSessionModel getInternalSessionModel() {
		final InternalSessionModel iSessionModel =
			SessionModel.getInternalModel(getContext());
		return iSessionModel;
	}

	/**
	 * Obtain a handle to the session model.
	 * 
	 * @return Obtain a handle to the session model.
	 */
	protected SessionModel getSessionModel() { return SessionModel.getModel(); }

	/**
	 * Obtain a timestamp representing now.
	 * @return The timestamp.
	 */
	protected Calendar getTimestamp() {
		return DateUtil.getInstance();
	}

	/**
	 * Obtain a user reference for a given username. This will obtain the user's
	 * roster and compare the username. If a match is found; the user will be
	 * returned; otherwise null will be returned. This api assumes that the
	 * user's session is established.
	 * 
	 * @param simpleUsername
	 *            The simple username to find.
	 * @return The user reference.
	 * @throws ParityException
	 * 
	 * @see User#getSimpleUsername()
	 */
	protected User getUser(final String simpleUsername) throws ParityException {
		final SessionModel sessionModel = getSessionModel();
		Assert.assertTrue("getUser(String)", sessionModel.isLoggedIn());
		final Collection<User> rosterEntries = sessionModel.getRosterEntries();
		for(User user : rosterEntries) {
			if(user.getSimpleUsername().equals(simpleUsername)) { return user; }
		}
		return null;
	}

	/**
	 * Remove the seen flag from the document. This will also update the parent
	 * so that its seen flag is removed.
	 * 
	 * @param document
	 *            The document to flag.
	 * @throws ParityException
	 */
	private void flagAsNotSEEN(final Document document) throws ParityException {
		if(document.contains(ArtifactFlag.SEEN)) {
			// remove the seen flag
			document.remove(ArtifactFlag.SEEN);
			getDocumentModel().update(document);
		}
	}

	/**
	 * Flag a document as having been seen. If the document's sibling objects
	 * have all been seen as well, the parent will be updated.
	 * 
	 * @param document
	 *            The document to flag.
	 * @throws ParityException
	 */
	private void flagAsSEEN(final Document document) throws ParityException {
		if(!document.contains(ArtifactFlag.SEEN)) {
			document.add(ArtifactFlag.SEEN);
			getDocumentModel().update(document);
		}
	}
}
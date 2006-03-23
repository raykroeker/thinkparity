/*
 * Mar 7, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.KeyEvent;
import com.thinkparity.model.parity.api.events.KeyListener;
import com.thinkparity.model.parity.api.events.PresenceListener;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.InternalArtifactModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.document.InternalDocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.user.User;

/**
 * SessionModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * TODO Updates\new documents are BOLD - Includes receive of history items as 
 * well as ownership request; send ownership.  This means the SEEN flag must be
 * removed once a request\request response has been processed.
 */
class SessionModelImpl extends AbstractModelImpl {

	/**
	 * Assertion used to compare the username provided with the username in
	 * the preferences.
	 */
	private static final String ASSERT_USERNAME_EQUALS_PREFS =
		"The username supplied \"{0}\" does not match the preferences \"{1}\".";

	/**
	 * List of all of the registered parity key listeners.
	 * 
	 * @see SessionModelImpl#keyListenersLock
	 */
	private static final Collection<KeyListener> keyListeners;

	/**
	 * Synchronization lock for the key listeners list.
	 * 
	 * @see SessionModelImpl#keyListeners
	 */
	private static final Object keyListenersLock;

	/**
	 * List of all of the registered parity presence listeners.
	 * 
	 * @see SessionModelImpl#presenceListenersLock
	 */
	private static final Collection<PresenceListener> presenceListeners;

	/**
	 * Lock used to synchronize the collection access.
	 * 
	 * @see SessionModelImpl#presenceListeners
	 */
	private static final Object presenceListenersLock;

	/**
	 * Static handle to the session model's auditor.
	 * 
	 */
	private static final SessionModelAuditor sAuditor;

	/**
	 * The static session model context.
	 * 
	 */
	private static final Context sContext;

	/**
	 * List of all of the registered parity session listeners.
	 * 
	 * @see SessionModelImpl#sessionListenersLock
	 */
	private static final Collection<SessionListener> sessionListeners;

	/**
	 * Lock used to synchronize the collection access.
	 * 
	 * @see SessionModelImpl#sessionListeners
	 */
	private static final Object sessionListenersLock;

	/**
	 * Helper wrapper class for xmpp calls.
	 * 
	 * @see SessionModelImpl#xmppHelperLock
	 */
	private static final SessionModelXMPPHelper xmppHelper;

	/**
	 * Helper wrapper's synchronization lock.
	 * 
	 * @see SessionModelImpl#xmppHelper
	 */
	private static final Object xmppHelperLock;

	static {
		// create the key listener list & sync lock
		keyListeners = new Vector<KeyListener>(3);
		keyListenersLock = new Object();
		// create the presence listener list & sync lock
		presenceListeners = new Vector<PresenceListener>(3);
		presenceListenersLock = new Object();
		// create the session listener list & sync lock
		sessionListeners = new Vector<SessionListener>(3);
		sessionListenersLock = new Object();
		// session context
		sContext = getSessionModelContext();
		sAuditor = new SessionModelAuditor(sContext);
		// create the xmpp helper
		xmppHelper = new SessionModelXMPPHelper();
		xmppHelperLock = new Object();
	}

	/**
	 * Handle the event generated by the server to close an artifact.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 */
	static void notifyArtifactClosed(final UUID artifactUniqueId,
			final JabberId artifactClosedBy) throws ParityException {
		final InternalDocumentModel iDModel = DocumentModel.getInternalModel(sContext);
		iDModel.close(artifactUniqueId, artifactClosedBy);
	}

	/**
	 * Handle the event generated by xmppExtensionListenerImpl.  Here we create
	 * a new document based upon the document version.
	 * 
	 * @param xmppDocument
	 *            The xmpp document that has been received.
	 */
	static void notifyDocumentReceived(final XMPPDocument xmppDocument)
			throws ParityException {
		DocumentModel.getInternalModel(sContext).receive(xmppDocument);
	}

	static void notifyInvitationAccepted(final JabberId acceptedBy) {
		SystemMessageModel.getInternalModel(sContext).createContactInvitationResponse(acceptedBy, Boolean.TRUE);
	}

	static void notifyInvitationDeclined(final JabberId declinedBy) {
		SystemMessageModel.getInternalModel(sContext).createContactInvitationResponse(declinedBy, Boolean.FALSE);
	}

	static void notifyInvitationExtended(final JabberId invitedBy) {
		SystemMessageModel.getInternalModel(sContext).createContactInvitation(invitedBy);
	}

	/**
	 * Handle the event generated by the xmpp extension listener. Here we pass
	 * on the event to the key listener.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @param user
	 *            The user.
	 */
	static void notifyKeyRequestAccepted(final UUID artifactUniqueId,
			final JabberId acceptedBy) throws SmackException, ParityException {
		// unlock
		final InternalDocumentModel iDModel =
			DocumentModel.getInternalModel(sContext);
		final Document document = iDModel.get(artifactUniqueId);
		iDModel.unlock(document.getId());

		// apply key flag
		final InternalArtifactModel iAModel =
			ArtifactModel.getInternalModel(sContext);
		iAModel.applyFlagKey(document.getId());
		// remove seen flag
		iAModel.removeFlagSeen(document.getId());

		// create system message
		SystemMessageModel.getInternalModel(sContext).
			createKeyResponse(document.getId(), Boolean.TRUE, acceptedBy);

		// audit receive key
		final User loggedInUser;
		synchronized(xmppHelperLock) {
			loggedInUser =  xmppHelper.getUser();
		}
		iDModel.auditRecieveKey(document.getId(), loggedInUser.getId(), currentDateTime(), acceptedBy);
	}

	/**
	 * Handle the event generated by the xmpp extension listener. Here we pass
	 * on the notification to the key listener.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 * @param user
	 *            The user.
	 */
	static void notifyKeyRequestDenied(final UUID artifactUniqueId,
			final JabberId deniedBy) throws ParityException, SmackException {
		final Document document =
			DocumentModel.getInternalModel(sContext).get(artifactUniqueId);

		// remove seen flag
		final InternalArtifactModel iAModel = ArtifactModel.getInternalModel(sContext);
		iAModel.removeFlagSeen(document.getId());

		// create system message
		SystemMessageModel.getInternalModel(sContext).
			createKeyResponse(document.getId(), Boolean.FALSE, deniedBy);

		// audit key request denied
		final User loggedInUser;
		synchronized(xmppHelperLock) { loggedInUser = xmppHelper.getUser(); }
		iAModel.auditKeyRequestDenied(document.getId(), loggedInUser.getId(),
				currentDateTime(), deniedBy);
	}

	/**
	 * Notify all of the registered key listeners that a user has requested the
	 * key for a given artifact.
	 * 
	 * @param user
	 *            The requesting user.
	 * @param artifactUUID
	 *            The artifact.
	 */
	static void notifyKeyRequested(final UUID artifactUniqueId,
			final JabberId requestedBy) throws ParityException, SmackException {
		final Document document = DocumentModel.getInternalModel(sContext).get(artifactUniqueId);

		// remove seen flag
		final InternalArtifactModel iAModel = ArtifactModel.getInternalModel(sContext);
		iAModel.removeFlagSeen(document.getId());

		// create system message
		SystemMessageModel.getInternalModel(sContext).createKeyRequest(document.getId(), requestedBy);

		// audit key request
		final User loggedInUser;
		synchronized(xmppHelperLock) { loggedInUser =  xmppHelper.getUser(); }
		sAuditor.requestKey(document.getId(), loggedInUser.getId(), currentDateTime(), requestedBy, loggedInUser.getId());
	}

	/**
	 * Notify all of the registered session listeners that the session has been
	 * established.
	 *
	 */
	static void notifySessionEstablished() {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionEstablished();
			}
		}
	}

	/**
	 * Notify all of the registered session listeners that the session has been
	 * terminated.
	 *
	 */
	static void notifySessionTerminated() {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionTerminated();
			}
		}
	}

	/**
	 * Notify all of the registered session listeners that the session has been
	 * terminated due to an error.
	 * 
	 * @param x
	 *            The cause of the session termination.
	 */
	static void notifySessionTerminated(final Exception x) {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionTerminated(x);
			}
		}
	}

	/**
	 * The session model auditor.
	 * 
	 */
	private final SessionModelAuditor auditor;

	/**
	 * Create a SessionModelImpl
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	SessionModelImpl(final Workspace workspace) {
		super(workspace);
		this.auditor = new SessionModelAuditor(getContext());
	}

	/**
	 * Accept an invitation to the user's contact list.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 * @throws ParityException
	 */
	void acceptInvitation(final JabberId jabberId) throws ParityException {
		synchronized(xmppHelperLock) {
			try { xmppHelper.acceptInvitation(jabberId); }
			catch(SmackException sx) {
				logger.error("acceptPresence(User)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("acceptPresence(User)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Add a key listener to the session.
	 * 
	 * @param keyListener
	 *            The key listener to add.
	 */
	void addListener(final KeyListener keyListener) {
		Assert.assertNotNull("Cannot register a null key listener.", keyListener);
		synchronized(SessionModelImpl.keyListenersLock) {
			Assert.assertNotTrue(
					"Cannot re-registry the same key listener.",
					SessionModelImpl.keyListeners.contains(keyListener));
			SessionModelImpl.keyListeners.add(keyListener);
		}
	}

	/**
	 * Add a presence listener to the session.
	 * 
	 * @param presenceListener
	 *            The presence listener to add.
	 */
	void addListener(final PresenceListener presenceListener) {
		Assert.assertNotNull("Cannot register a null presence listener.",
				presenceListener);
		synchronized(SessionModelImpl.presenceListenersLock) {
			Assert.assertTrue("Cannot re-register the same presence listener.",
					!SessionModelImpl.presenceListeners.contains(presenceListener));
			SessionModelImpl.presenceListeners.add(presenceListener);
		}
	}

	/**
	 * Add a session listener to the session.
	 * 
	 * @param sessionListener
	 *            The session listener to add.
	 */
	void addListener(final SessionListener sessionListener) {
		Assert.assertNotNull("Cannot register a null session listener.",
				sessionListener);
		Assert.assertTrue("Cannot re-register the same session listener.",
				!sessionListeners.contains(sessionListener));
		sessionListeners.add(sessionListener);
	}

	/**
	 * Decline a user's invitation to their contact list.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 * @throws ParityException
	 */
	void declineInvitation(final JabberId jabberId) throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("Cannot decline invitation while offline.", xmppHelper);
			try { xmppHelper.declineInvitation(jabberId); }
			catch(SmackException sx) {
				logger.error("Could not decline invitation:  " + jabberId, sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("Could not decline invitation:  " + jabberId, rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Obtain the artifact key holder.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return The artifact key holder.
	 * @throws ParityException
	 */
	User getArtifactKeyHolder(final Long artifactId) throws ParityException {
		logger.info("getArtifactKeyHolder(Long)");
		logger.debug(artifactId);
		synchronized(xmppHelper) {
			assertIsLoggedIn("Cannot obtain artifact key holder while offline.", xmppHelper);
			try {
				final UUID artifactUniqueId = getArtifactUniqueId(artifactId);
				return xmppHelper.getArtifactKeyHolder(artifactUniqueId);
			}
			catch(final SmackException sx) {
				logger.error("Cannot obtain artifact key holder.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Cannot obtain artifact key holder.", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Obtain the currently logged in user.
	 * 
	 * @return The currently logged in user.
	 * @throws ParityException
	 */
	User getLoggedInUser() throws ParityException {
		logger.info("getLoggedInUser()");
		synchronized(xmppHelper) {
			assertIsLoggedIn("Cannot obtain logged in user while offline.", xmppHelper);
			try { return xmppHelper.getUser(); }
			catch(final SmackException sx) {
				logger.error("Cannot obtain logged in user.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Cannot obtain logged in user.", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Add a roster entry for the user. This will send a presence request to
	 * user.
	 * 
	 * @param jabberId
	 *            The jabber id of the contact to invite.
	 * @throws ParityException
	 */
	void inviteContact(final JabberId jabberId) throws ParityException {
		synchronized(xmppHelperLock) {
			try { xmppHelper.inviteContact(jabberId); }
			catch(SmackException sx) {
				logger.error("addRosterEntry(User)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("addRosterEntry(User)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Determine whether or not a user is logged in.
	 * 
	 * @return True if the user is logged in, false otherwise.
	 */
	Boolean isLoggedIn() {
		synchronized(xmppHelperLock) { return xmppHelper.isLoggedIn(); }
	}

	/**
	 * Determine whether or not the logged in user is the artifact key holder.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return True if the logged in user is the artifact key holder; false
	 *         otherwise.
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             If the user is offline.
	 */
	Boolean isLoggedInUserKeyHolder(final Long artifactId) throws ParityException {
		logger.info("isLoggedInUserKeyHolder(Long)");
		logger.debug(artifactId);
		final UUID artifactUniqueId = getArtifactUniqueId(artifactId);
		synchronized(xmppHelper) {
			assertIsLoggedIn(
					"Cannot determine whether the logged in user is the key holder while offline.",
					xmppHelper);
			final User loggedInUser = getLoggedInUser();
			try {
				final User keyHolder =
					xmppHelper.getArtifactKeyHolder(artifactUniqueId);
				return keyHolder.getSimpleUsername().equals(loggedInUser.getSimpleUsername());
			}
			catch(final SmackException sx) {
				logger.error("Cannot determine whether the logged in user is the key holder.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Cannot determine whether the logged in user is the key holder.", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Establish a new xmpp session.
	 * 
	 * @param username
	 *            The login.
	 * @param password
	 *            The login password.
	 * @throws ParityException
	 */
	void login(final String username, final String password) throws ParityException {
		logger.info("login(String,String)");
		logger.debug(username);
		logger.debug(mask(password));
		final String host = preferences.getServerHost();
		final Integer port = preferences.getServerPort();
		synchronized(xmppHelperLock) {
			try {
				// check that the preferences username matches the username
				// supplied
				if(preferences.isSetUsername()) {
					Assert.assertTrue(
							formatAssertUsernameEqualsPreferences(username),
							username.equals(preferences.getUsername()));
				}
				// login
				xmppHelper.login(host, port, username, password);
				// set the username@host in the preferences
				if(!preferences.isSetUsername()) {
					preferences.setUsername(username);
				}
				xmppHelper.processOfflineQueue();
			}
			catch(SmackException sx) {
				logger.error("login(String,String)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("login(String,String)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Terminate the current session.
	 * 
	 * @throws ParityException
	 */
	void logout() throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("logout", xmppHelper);
			try { xmppHelper.logout(); }
			catch(SmackException sx) {
				logger.error("logout()", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("logout()", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Read a list of contacts for an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return A list of contacts.
	 * @throws ParityException
	 */
	List<Contact> readArtifactContacts(final Long artifactId)
			throws ParityException {
		logger.info("READING ARTIFACT CONTACTS");
		logger.debug(artifactId);
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("Cannot read artifact contacts while offline.",
					xmppHelper);
			try {
				final UUID artifactUniqueId = getArtifactUniqueId(artifactId);
				return xmppHelper.readArtifactContacts(artifactUniqueId);
			}
			catch(final SmackException sx) {
				logger.error("getSubscriptions(Long)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("getSubscriptions(Long)", rx);
				throw ParityErrorTranslator.translate(rx);

			}
		}
	}

	/**
	 * Obtain a list of roster entries.
	 * 
	 * @return The list of roster entries.
	 * @throws ParityException
	 */
	List<Contact> readContacts() throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("Cannot read contact list while offline.", xmppHelper);
			try { return xmppHelper.readContacts(); }
			catch(SmackException sx) {
				logger.error("getRosterEntries()", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("getRosterEntries()", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	User readUser(final JabberId jabberId) throws ParityException {
		final List<JabberId> jabberIds = new LinkedList<JabberId>();
		jabberIds.add(jabberId);
		return readUsers(jabberIds).get(0);
	}

	List<User> readUsers(final List<JabberId> jabberIds) throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("Cannot read user list while offline.", xmppHelper);
			try { return xmppHelper.readUsers(jabberIds); }
			catch(final SmackException sx) {
				logger.error("Cannot read users.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Cannot read users.", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Remove a key listener from the session.
	 * 
	 * @param keyListener
	 *            The key listener to remove.
	 */
	void removeListener(final KeyListener keyListener) {
		logger.info("removeListener(KeyListener)");
		logger.debug(keyListener);
		Assert.assertNotNull("Cannot remove a null key listener.", keyListener);
		synchronized(SessionModelImpl.keyListenersLock) {
			Assert.assertTrue(
					"Cannot remove a non-registered listener.",
					SessionModelImpl.keyListeners.contains(keyListener));
			SessionModelImpl.keyListeners.remove(keyListener);
		}
	}

	/**
	 * Remove a presence listener from the session.
	 * 
	 * @param presenceListener
	 *            The presence listener to remove.
	 */
	void removeListener(final PresenceListener presenceListener) {
		logger.info("removeListener(PresenceListener)");
		logger.debug(presenceListener);
		Assert.assertNotNull("Cannot remove a null presence listener.", presenceListener);
		synchronized(SessionModelImpl.presenceListenersLock) {
			Assert.assertTrue(
					"Cannot remove a non-registered listener.",
					SessionModelImpl.presenceListeners.contains(presenceListener));
			SessionModelImpl.presenceListeners.remove(presenceListener);
		}
	}

	/**
	 * Remove a session listener from the session.
	 * 
	 * @param sessionListener
	 *            The session listener to remove.
	 */
	void removeListener(final SessionListener sessionListener) {
		logger.info("removeListener(SessionListener)");
		logger.debug(sessionListener);
		Assert.assertNotNull("Cannot remove a null session listener.", sessionListener);
		synchronized(SessionModelImpl.sessionListenersLock) {
			Assert.assertTrue(
					"Cannot remove a non-registered listener.",
					SessionModelImpl.sessionListeners.contains(sessionListener));
			SessionModelImpl.sessionListeners.remove(sessionListener);
		}
	}

	/**
	 * Send the working copy of a document to a list of users. Here we create a
	 * new version; and send that version to the list of users.
	 * 
	 * @param users
	 *            The list of parity users to send to.
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             If the logged in user is not the artifact key holder.
	 */
	void send(final Collection<User> users, final Long documentId)
			throws ParityException {
		final List<JabberId> jabberIds = new LinkedList<JabberId>();
		for(final User user : users) { jabberIds.add(user.getId()); }
		send(jabberIds, documentId);
	}

	/**
	 * Send a particular revision to a list of users. The version is obtained
	 * from the document model; and streamed to the list of users.
	 * 
	 * @param users
	 *            The list of users to send the document version to.
	 * @param documentId
	 *            The document id.
	 * @param versionId
	 *            The version id.
	 * @throws ParityException
	 */
	void send(final Collection<User> users, final Long documentId,
			final Long versionId) throws ParityException {
		logger.info("send(Collection<User>,Long,Long)");
		logger.debug(users);
		logger.debug(documentId);
		logger.debug(versionId);
		synchronized(xmppHelper) {
			try {
				final InternalDocumentModel iDocumentModel =
					getInternalDocumentModel();
				final DocumentVersion version =
					iDocumentModel.getVersion(documentId, versionId);
				final DocumentVersionContent versionContent =
					iDocumentModel.getVersionContent(documentId, versionId);

				final XMPPDocument xmppDocument = new XMPPDocument();
				xmppDocument.setContent(versionContent.getDocumentContent().getContent());
				xmppDocument.setCreatedBy(version.getCreatedBy());
				xmppDocument.setCreatedOn(version.getCreatedOn());
				xmppDocument.setName(version.getName());
				xmppDocument.setReceivedFrom(getLoggedInUser().getSimpleUsername());
				xmppDocument.setUniqueId(version.getArtifactUniqueId());
				xmppDocument.setUpdatedBy(version.getUpdatedBy());
				xmppDocument.setUpdatedOn(version.getUpdatedOn());
				xmppDocument.setVersionId(version.getVersionId());

				// send the document version
				xmppHelper.send(users, xmppDocument);

				// audit the send
				final Calendar now = DateUtil.getInstance();
				final DocumentVersion dv = iDocumentModel.getVersion(documentId, versionId);
				auditor.send(dv.getArtifactId(), dv.getVersionId(),
						xmppHelper.getUser().getId(), now, users);
			}
			catch(final SmackException sx) {
				logger.error("Could not send document version.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Could not send document version.", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	void send(final JabberId jabberId, final Long documentId)
			throws ParityException {
		final List<JabberId> jabberIds = new LinkedList<JabberId>();
		jabberIds.add(jabberId);
		send(jabberIds, documentId);
	}

	/**
	 * Send a message to a list of parity users.
	 * 
	 * @param jabberId
	 *            The user id.
	 * @param message
	 *            The message to send.
	 * @throws ParityException
	 */
	void send(final JabberId jabberId, final String message)
			throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("send(Collection<User>,String)", xmppHelper);
			try {
				final Collection<User> users = new Vector<User>(0);
				users.add(new User(jabberId));
				xmppHelper.send(users, message);
			}
			catch(SmackException sx) {
				logger.error("send(Collection<User>,String)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("send(Collection<User>,String)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	void send(final List<JabberId> jabberIds, final Long documentId)
			throws ParityException {
		assertLoggedInUserIsKeyHolder(documentId);

		final InternalDocumentModel iDModel = getInternalDocumentModel();
		final DocumentVersion version;
		if(!iDModel.isWorkingVersionEqual(documentId)) {
			version = iDModel.createVersion(documentId);
		}
		else { version = iDModel.getLatestVersion(documentId); }
		send(jabberIds, documentId, version.getVersionId());
	}

	void send(final List<JabberId> jabberIds, final Long documentId,
			final Long versionId) throws ParityException {
		final Collection<User> users = new Vector<User>(jabberIds.size());
		for(final JabberId jabberId : jabberIds) { users.add(new User(jabberId)); }
		send(users, documentId, versionId);
	}

	/**
	 * Send a close packet to the server.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @throws ParityException
	 */
	void sendClose(final Long artifactId) throws ParityException {
		logger.info("sendClose(Long)");
		logger.debug(artifactId);
		assertLoggedInUserIsKeyHolder(artifactId);
		synchronized(xmppHelper) {
			try {
				final UUID artifactUniqueId = getArtifactUniqueId(artifactId);
				xmppHelper.sendClose(artifactUniqueId);
			}
			catch(final SmackException sx) {
				logger.error("Cannot call remote close api.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Cannot close artifact:  " + artifactId, rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Send an artifact creation packet to the parity server.
	 * 
	 * @param artifact
	 *            The document.
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user is offline.
	 *             <li>If the logged in user is not the key holder.
	 *             </ul>
	 * @throws ParityException
	 */
	void sendCreate(final Artifact artifact) throws ParityException {
		logger.info("sendCreate(Artifact)");
		logger.debug(artifact);
		synchronized(SessionModelImpl.xmppHelperLock) {
			assertIsLoggedIn("sendCreate(Artifact)", SessionModelImpl.xmppHelper);
			try { SessionModelImpl.xmppHelper.sendCreate(artifact.getUniqueId()); }
			catch(SmackException sx) {
				logger.error("sendCreate(Artifact)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("sendCreate(Artifact)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Send a delete packet to the parity server.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user if offline.
	 *             </ul>
	 */
	void sendDelete(final Long artifactId) throws ParityException {
		logger.info("sendDelete(Long)");
		logger.debug(artifactId);
		synchronized(SessionModelImpl.xmppHelperLock) {
			assertIsLoggedIn("Must be online to delete.", xmppHelper);
			try {
				final UUID artifactUniqueId = getArtifactUniqueId(artifactId);
				xmppHelper.sendDelete(artifactUniqueId);
			}
			catch(final SmackException sx) {
				logger.error("Could not delete artifact:  " + artifactId, sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Could not delete artifact:  " + artifactId, rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Send a reqest for a document key to the parity server.
	 * 
	 * @param artifactId
	 *            The artifact unique id.
	 * @throws ParityException
	 * @see KeyListener#keyRequested(KeyEvent)
	 */
	void sendKeyRequest(final Long artifactId) throws ParityException {
		logger.info("sendKeyRequest(Long)");
		logger.debug(artifactId);
		synchronized(SessionModelImpl.xmppHelperLock) {
			assertIsLoggedIn("Cannot request key while offline.", xmppHelper);
			try {
				final UUID artifactUniqueId = getArtifactUniqueId(artifactId);
				xmppHelper.sendKeyRequest(artifactUniqueId);

				// audit key request
				final User loggedInUser = xmppHelper.getUser();
				final User keyHolder = getArtifactKeyHolder(artifactId);
				auditor.requestKey(artifactId, loggedInUser.getId(), currentDateTime(), loggedInUser.getId(), keyHolder.getId());
			}
			catch(SmackException sx) {
				logger.error("Cannot send key request.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("Cannot send key request.", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Send the response to a key request. It is assumed that the logged in user
	 * is the key holder. It is assumed that all key information is stored on
	 * the parity server and never locally. It is assumed that they key will be
	 * accompanied by the working document.
	 * 
	 * @param documentId
	 *            The document id.
	 * @param requestedBy
	 *            The jabber id of the user requesting the key.
	 * @param keyResponse
	 *            The response.
	 */
	void sendKeyResponse(final Long documentId, final JabberId requestedBy,
			final KeyResponse keyResponse) throws ParityException {
		logger.info("sendKeyResponse(UUID,User,KeyResponse)");
		logger.debug(documentId);
		logger.debug(requestedBy);
		logger.debug(keyResponse);
		assertLoggedInUserIsKeyHolder(documentId);
		synchronized(SessionModelImpl.xmppHelperLock) {
			try {
				// NOTE This should be refactored when the session can be changed.
				final User requestedByUser = new User(requestedBy.getQualifiedJabberId());

				final InternalDocumentModel iDModel = getInternalDocumentModel();
				final Document document = iDModel.get(documentId);

				// if the user sends an acceptance of the key request; we
				// want to send the latest version to the requesting user
				switch(keyResponse) {
				case ACCEPT:
					// check if a new version is needed
					final DocumentVersion version;
					if(iDModel.isWorkingVersionEqual(documentId)) {
						version = iDModel.getLatestVersion(documentId);
					}
					else { version = iDModel.createVersion(documentId); }

					// send the key change to the server
					xmppHelper.sendKeyResponse(
							document.getUniqueId(), keyResponse, requestedByUser);

					// send new version
					final Collection<User> users = new Vector<User>(1);
					users.add(requestedByUser);
					send(users, documentId, version.getVersionId());

					// remove flag key
					final InternalArtifactModel iAModel = getInternalArtifactModel();
					iAModel.removeFlagKey(documentId);

					// lock the local document
					iDModel.lock(documentId);

					// audit send key
					final DocumentVersion dv = iDModel.getVersion(
							version.getArtifactId(), version.getVersionId());
					auditor.sendKey(dv.getArtifactId(), dv.getVersionId(),
							xmppHelper.getUser().getId(), currentDateTime(),
							requestedBy);
					break;
				case DENY:
					// send the declination to the server
					xmppHelper.sendKeyResponse(
							document.getUniqueId(), KeyResponse.DENY, requestedByUser);

					// audit send key denied
					auditor.keyResponseDenied(documentId,
							xmppHelper.getUser().getId(), currentDateTime(),
							requestedBy);
					break;
				default: throw Assert.createUnreachable("Unknown key response:  " + keyResponse);
				}
			}
			catch(final SmackException sx) {
				logger.error("sendKeyResponse(Document,User,KeyResponse)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("sendKeyResponse(Document,User,KeyResponse)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Send the parity log file. To be used in order to troubleshoot remote
	 * problems.
	 * 
	 * @throws ParityException
	 */
	void sendLogFileArchive() throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("sendLogFile()", xmppHelper);
			try {
				xmppHelper.sendLogFileArchive(
						workspace.getLogArchive(), preferences.getSystemUser());
			}
			catch(final SmackException sx) {
				logger.error("sendLogFile()", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("sendLogFile()", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Subscribe to a document. The parity server is notified and will create a
	 * subscription entry for the logged in user.
	 * 
	 * @param document
	 *            The document to subscribe to.
	 * @throws ParityException
	 */
	void sendSubscribe(final Document document) throws ParityException {
		logger.info("sendSubscribe(Document)");
		logger.debug(document);
		synchronized(SessionModelImpl.xmppHelperLock) {
			assertIsLoggedIn("sendSubscribe(Document)", SessionModelImpl.xmppHelper);
			try { SessionModelImpl.xmppHelper.sendSubscribe(document.getUniqueId()); }
			catch(SmackException sx) {
				logger.error("sendSubscribe(Document)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("sendSubscribe(Document)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Assert that the user is currently logged in.
	 * 
	 * @param message
	 *            Message to display in the assertion.
	 * @param xmppHelper
	 *            A handle to the xmpp helper in order to determine logged in
	 *            status.
	 */
	private void assertIsLoggedIn(final String message,
			final SessionModelXMPPHelper xmppHelper) {
		Assert.assertTrue(message, xmppHelper.isLoggedIn());
	}

	/**
	 * Create an asssertion message.
	 * 
	 * @param username
	 *            The username.
	 * @return The assertion message.
	 */
	private String formatAssertUsernameEqualsPreferences(final String username) {
		final MessageFormat f = new MessageFormat(ASSERT_USERNAME_EQUALS_PREFS);
		return f.format(new Object[] {username, preferences.getUsername()});
	}

	/**
	 * Mask the password for logging statements.
	 * 
	 * @param password
	 *            The password.
	 * @return The password mask.
	 */
	private String mask(final String password) { return "XXXXXXXXXX"; }
}

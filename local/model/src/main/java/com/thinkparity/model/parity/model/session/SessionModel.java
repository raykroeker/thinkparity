/*
 * Mar 7, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.Collection;
import java.util.List;

import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.KeyEvent;
import com.thinkparity.model.parity.api.events.KeyListener;
import com.thinkparity.model.parity.api.events.PresenceListener;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * SessionModel
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * TODO The RFO should display the document's history info.
 *   The details of the System messages should be displayed in the info panel.
 */
public class SessionModel extends AbstractModel {

	/**
	 * Obtain an internal interface to the session model.
	 * 
	 * @param context
	 *            The model context.
	 * @return The internal interface.
	 */
	public static InternalSessionModel getInternalModel(final Context context) {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		final InternalSessionModel internalModel = new InternalSessionModel(workspace, context);
		return internalModel;
	}

	/**
	 * Obtain a handle to a session model interface.
	 * @return A handle to the session model interface.
	 */
	public static SessionModel getModel() {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		final SessionModel sessionModel = new SessionModel(workspace);
		return sessionModel;
	}

	/**
	 * Instance implementation.
	 */
	private final SessionModelImpl impl;

	/**
	 * Synchronization lock for impl.
	 */
	private final Object implLock;

	/**
	 * Create a SessionModel
	 */
	protected SessionModel(final Workspace workspace) {
		super();
		this.impl = new SessionModelImpl(workspace);
		this.implLock = new Object();
	}

	/**
	 * Accept an invitation to the user's contact list.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 * @throws ParityException
	 */
	public void acceptInvitation(final JabberId jabberId) throws ParityException {
		synchronized(implLock) { impl.acceptInvitation(jabberId); }
	}

	/**
	 * Add a key listener to the session.
	 * 
	 * @param keyListener
	 *            The key listener to add.
	 */
	public void addListener(final KeyListener keyListener) {
		synchronized(implLock) { impl.addListener(keyListener); }
	}

	/**
	 * Add a presence listener to the session.
	 * 
	 * @param presenceListener
	 *            The presence listener to add.
	 */
	public void addListener(final PresenceListener presenceListener) {
		synchronized(implLock) { impl.addListener(presenceListener); }
	}

	/**
	 * Add a session listener to the session.
	 * 
	 * @param sessionListener
	 *            The session listener to add.
	 */
	public void addListener(final SessionListener sessionListener) {
		synchronized(implLock) { impl.addListener(sessionListener); }
	}

	/**
	 * Deny the presence visibility request from user to the currently logged
	 * in user.
	 * 
	 * @param user
	 *            The user who's presence request the currently logged in user
	 *            will deny.
	 * @see SessionModel#acceptPresence(User)
	 * @throws ParityException
	 */
	public void declineInvitation(final JabberId jabberId) throws ParityException {
		synchronized(implLock) { impl.declineInvitation(jabberId); }
	}

	/**
	 * Obtain a list of artifact ids for which the logged in user has the key.
	 * 
	 * @return A list of artifact ids.
	 * @throws ParityException
	 */
	public List<Long> getArtifactKeys() throws ParityException {
		synchronized(implLock) { return impl.getArtifactKeys(); }
	}

	/**
	 * Add a roster entry for the user. This will send a presence request to
	 * user.
	 * 
	 * @param user
	 *            The user to add to the roster.
	 * @throws ParityException
	 */
	public void inviteContact(final JabberId jabberId) throws ParityException {
		synchronized(implLock) { impl.inviteContact(jabberId); }
	}

	/**
	 * Determine whether or not the parity session has been established.
	 * @return Boolean
	 */
	public Boolean isLoggedIn() {
		synchronized(implLock) { return impl.isLoggedIn(); }
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
	 *             If the user is currently offline.
	 */
	public Boolean isLoggedInUserKeyHolder(final Long artifactId)
			throws ParityException {
		synchronized(implLock) {
			return impl.isLoggedInUserKeyHolder(artifactId);
		}
	}

	/**
	 * Login to parity. This will create a new singleton instance of a parity
	 * session.
	 * 
	 * @param username
	 *            The username used to login.
	 * @param password
	 *            The password used to login.
	 * @throws ParityException
	 */
	public void login(final String username, final String password)
			throws ParityException {
		synchronized(implLock) { impl.login(username, password); }
	}

	/**
	 * Terminate the current parity session.
	 * @throws ParityException
	 */
	public void logout() throws ParityException {
		synchronized(implLock) { impl.logout(); }
	}

	/**
	 * Obtain a list of contacts for an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return A list of contacts for the artifact.
	 * @throws ParityException
	 */
	public List<Contact> readArtifactContacts(final Long artifactId)
			throws ParityException {
		synchronized(implLock) { return impl.readArtifactContacts(artifactId); }
	}

	/**
	 * Obtain a list of contacts.
	 * 
	 * @return A list of contacts.
	 * @throws ParityException
	 */
	public List<Contact> readContacts() throws ParityException {
		synchronized(implLock) { return impl.readContacts(); }
	}

	/**
	 * Remove a key listener from the session.
	 * 
	 * @param keyListener
	 *            The key listener to remove.
	 */
	public void removeListener(final KeyListener keyListener) {
		synchronized(implLock) { impl.removeListener(keyListener); }
	}

	/**
	 * Remove a presence listener from the session.
	 * 
	 * @param presenceListener
	 *            The presence listener to remove.
	 */
	public void removeListener(final PresenceListener presenceListener) {
		synchronized(implLock) { impl.removeListener(presenceListener); }
	}

	/**
	 * Remove a session listener from the session.
	 * 
	 * @param sessionListener
	 *            The registered session listener to remove.
	 */
	public void removeListener(final SessionListener sessionListener) {
		synchronized(implLock) { impl.removeListener(sessionListener); }
	}

	/**
	 * Send the working document to the list of users. A new version is created;
	 * which is then sent to each of the users in the list.
	 * 
	 * @param users
	 *            The list of parity users to send to.
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             If the logged in user is not the key holder.
	 */
	public void send(final Collection<User> users, final Long documentId)
			throws ParityException {
		synchronized(implLock) { impl.send(users, documentId); }
	}

	/**
	 * Send a specific document version to the list of users.
	 * 
	 * @param users
	 *            The list of users.
	 * @param documentId
	 *            The document id.
	 * @param versionId
	 *            The version id.
	 * @throws ParityException
	 */
	public void send(final Collection<User> users, final Long documentId,
			final Long versionId) throws ParityException {
		synchronized(implLock) { impl.send(users, documentId, versionId); }
	}

	/**
	 * Send the working version of the document to the user.
	 * 
	 * @param jabberId
	 *            The user id.
	 * @param documentId
	 *            The document id.
	 * @throws ParityException
	 */
	public void send(final JabberId jabberId, final Long documentId)
			throws ParityException {
		synchronized(implLock) { impl.send(jabberId, documentId); }
	}

	/**
	 * Send a message to a user.
	 * 
	 * @param jabberId
	 *            The user id.
	 * @param message
	 *            The message.
	 * @throws ParityException
	 */
	public void send(final JabberId jabberId, final String message)
		throws ParityException {
		synchronized(implLock) { impl.send(jabberId, message); }
	}

	/**
	 * Send a reqest for a document key to the parity server.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @throws ParityException
	 * @see KeyListener#keyRequested(KeyEvent)
	 */
	public void sendKeyRequest(final Long artifactId) throws ParityException {
		synchronized(implLock) { impl.sendKeyRequest(artifactId); }
	}

	/**
	 * Send the response to a key request.
	 * 
	 * @param artifactId
	 *            The document unique id.
	 * @param requestedBy
	 *            The jabber id of the user requesting the key.
	 * @param keyResponse
	 *            The response.
	 * 
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             If the logged in user is not the key holder.
	 */
	public void sendKeyResponse(final Long artifactId,
			final JabberId requestedBy, final KeyResponse keyResponse)
			throws ParityException {
		synchronized(implLock) {
			impl.sendKeyResponse(artifactId, requestedBy, keyResponse);
		}
	}

	/**
	 * Send the parity log file. To be used in order to troubleshoot remote
	 * problems.
	 * 
	 * @throws ParityException
	 */
	public void sendLogFileArchive() throws ParityException {
		synchronized(implLock) { impl.sendLogFileArchive(); }
	}

	/**
	 * Subscribe to a document. The parity server is notified and will create a
	 * subscription entry for the logged in user.
	 * 
	 * @param document
	 *            The document to subscribe to.
	 * @throws ParityException
	 */
	public void sendSubscribe(final Document document) throws ParityException {
		synchronized(implLock) { impl.sendSubscribe(document); }
	}

	/**
	 * Obtain the session model implementation.
	 * 
	 * @return The session model implementation.
	 */
	protected SessionModelImpl getImpl() { return impl; }

	/**
	 * Obtain the session model implementation lock.
	 * 
	 * @return The session model implemenation synchronization lock.
	 */
	protected Object getImplLock() { return implLock; }
}

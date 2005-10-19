/*
 * Mar 7, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.Collection;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.PresenceListener;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * SessionModel
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionModel extends AbstractModel {

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
	private SessionModel(final Workspace workspace) {
		super();
		this.impl = new SessionModelImpl(workspace);
		this.implLock = new Object();
	}

	/**
	 * Accept the presence visibility request from user to the currently logged
	 * in user.
	 * 
	 * @param user
	 *            The user who's presence request the currently logged in user
	 *            will accept.
	 * @see SessionModel#denyPresence(UserRenderer)
	 * @throws ParityException
	 */
	public void acceptPresence(final User user) throws ParityException {
		synchronized(implLock) { impl.acceptPresence(user); }
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
	 * Add a roster entry for the user. This will send a presence request to
	 * user.
	 * 
	 * @param user
	 *            The user to add to the roster.
	 * @throws ParityException
	 */
	public void addRosterEntry(final User user) throws ParityException {
		synchronized(implLock) { impl.addRosterEntry(user); }
	}

	/**
	 * Deny the presence visibility request from user to the currently logged
	 * in user.
	 * 
	 * @param user
	 *            The user who's presence request the currently logged in user
	 *            will deny.
	 * @see SessionModel#acceptPresence(UserRenderer)
	 * @throws ParityException
	 */
	public void denyPresence(final User user) throws ParityException {
		synchronized(implLock) { impl.denyPresence(user); }
	}

	/**
	 * Obtain a list of roster entries.
	 * 
	 * @return The list of roster entries.
	 * @throws ParityException
	 */
	public Collection<User> getRosterEntries() throws ParityException {
		synchronized(implLock) { return impl.getRosterEntries(); }
	}

	/**
	 * Determine whether or not the parity session has been established.
	 * @return Boolean
	 */
	public Boolean isLoggedIn() {
		synchronized(implLock) { return impl.isLoggedIn(); }
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
	 * Remove a session listener from the session.
	 * 
	 * @param sessionListener
	 *            The registered session listener to remove.
	 */
	public void removeListener(final SessionListener sessionListener) {
		synchronized(implLock) { impl.removeListener(sessionListener); }
	}

	/**
	 * Send a document to a list of parity users.
	 * 
	 * @param users
	 *            The list of parity users to send to.
	 * @param document
	 *            The document to send.
	 * @throws ParityException
	 */
	public void send(final Collection<User> users, final Document document)
			throws ParityException {
		synchronized(implLock) { impl.send(users, document); }
	}

	/**
	 * Update the roster entry to the values found in user.
	 * @param user
	 * @throws ParityException
	 */
	public void updateRosterEntry(final User user) throws ParityException {
		synchronized(implLock) { impl.updateRosterEntry(user); }
	}
}

/*
 * Created On: Mar 7, 2005
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.User;


import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.events.KeyListener;
import com.thinkparity.ophelia.model.events.PresenceListener;
import com.thinkparity.ophelia.model.events.SessionListener;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * The parity session interface.
 * 
 * @author raykroeker@gmail.com
 * @version 1.3.2.25
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
	public void declineInvitation(final EMail invitedAs, final JabberId invitedBy) {
		synchronized(implLock) { impl.declineInvitation(invitedAs, invitedBy); }
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
     * @throws ParityException
     */
    public void login() {
        synchronized(implLock) { impl.login(); }
    }

	/**
	 * Login to parity. This will create a new singleton instance of a parity
	 * session.
	 * 
	 * @param credentials
	 *            The user's credentials.
	 * @throws ParityException
	 */
	public void login(final Credentials credentials) {
		synchronized(implLock) { impl.login(credentials); }
	}

	/**
	 * Terminate the current parity session.
	 * @throws ParityException
	 */
	public void logout() {
		synchronized(implLock) { impl.logout(); }
	}

	/**
     * Read the logged in user's session.
     * 
     * @return The logged in user's session.
     * @throws ParityException
     */
    public Session readSession() {
        synchronized(implLock) { return impl.readSession(); }
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
	 * Send the parity log file. To be used in order to troubleshoot remote
	 * problems.
	 * 
	 * @throws ParityException
	 */
	public void sendLogFileArchive() throws ParityException {
		synchronized(implLock) { impl.sendLogFileArchive(); }
	}

	/**
     * Update the session user's contact info.
     * 
     * @param contact
     *            The user's contact info.
     */
    public void updateContact(final Contact contact) {
        synchronized(implLock) { impl.updateContact(contact); }
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

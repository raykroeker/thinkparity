/*
 * Created On: Mar 7, 2005
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Session;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.events.SessionListener;
import com.thinkparity.ophelia.model.workspace.Workspace;

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
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @param context
	 *            The model context.
	 * @return The internal interface.
	 */
	public static InternalSessionModel getInternalModel(final Context context,
            final Workspace workspace) {
	    return new InternalSessionModel(workspace, context);
	}

	/**
     * Obtain a handle to a session model interface.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @return A handle to the session model interface.
     */
	public static SessionModel getModel(final Workspace workspace) {
	    return new SessionModel(workspace);
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

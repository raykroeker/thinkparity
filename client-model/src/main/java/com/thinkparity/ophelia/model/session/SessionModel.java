/*
 * Created On: Mar 7, 2005
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.Session;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.events.SessionListener;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The parity session interface.
 * 
 * @author raykroeker@gmail.com
 * @version 1.3.2.25
 */
public class SessionModel extends AbstractModel<SessionModelImpl> {

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
            final Environment environment, final Workspace workspace) {
	    return new InternalSessionModel(environment, workspace, context);
	}

	/**
     * Obtain a handle to a session model interface.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @return A handle to the session model interface.
     */
	public static SessionModel getModel(final Environment environment,
            final Workspace workspace) {
	    return new SessionModel(environment, workspace);
	}

	/**
	 * Create a SessionModel
	 */
	protected SessionModel(final Environment environment,
            final Workspace workspace) {
		super(new SessionModelImpl(environment, workspace));
	}

	/**
	 * Add a session listener to the session.
	 * 
	 * @param sessionListener
	 *            The session listener to add.
	 */
	public void addListener(final SessionListener sessionListener) {
		synchronized(getImplLock()) { getImpl().addListener(sessionListener); }
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
		synchronized(getImplLock()) { getImpl().declineInvitation(invitedAs, invitedBy); }
	}

	/**
	 * Determine whether or not the parity session has been established.
	 * @return Boolean
	 */
	public Boolean isLoggedIn() {
		synchronized(getImplLock()) { return getImpl().isLoggedIn(); }
	}

    /**
     * Login to parity. This will create a new singleton instance of a parity
     * session.
     * 
     */
    public void login() {
        synchronized(getImplLock()) { getImpl().login(); }
    }

	/**
	 * Login to parity. This will create a new singleton instance of a parity
	 * session.
	 * 
	 * @param credentials
	 *            The user's credentials.
	 */
	public void login(final Credentials credentials) {
		synchronized(getImplLock()) { getImpl().login(credentials); }
	}

	/**
	 * Terminate the current parity session.
	 * @throws ParityException
	 */
	public void logout() {
		synchronized(getImplLock()) { getImpl().logout(); }
	}

	/**
     * Read the logged in user's session.
     * 
     * @return The logged in user's session.
     * @throws ParityException
     */
    public Session readSession() {
        synchronized(getImplLock()) { return getImpl().readSession(); }
    }

	/**
	 * Remove a session listener from the session.
	 * 
	 * @param sessionListener
	 *            The registered session listener to remove.
	 */
	public void removeListener(final SessionListener sessionListener) {
		synchronized(getImplLock()) { getImpl().removeListener(sessionListener); }
	}

    /**
	 * Send the parity log file. To be used in order to troubleshoot remote
	 * problems.
	 * 
	 * @throws ParityException
	 */
	public void sendLogFileArchive() throws ParityException {
		synchronized(getImplLock()) { getImpl().sendLogFileArchive(); }
	}

	/**
     * Update the session user's contact info.
     * 
     * @param contact
     *            The user's contact info.
     */
    public void updateContact(final Contact contact) {
        synchronized(getImplLock()) { getImpl().updateContact(contact); }
    }
}

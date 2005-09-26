/*
 * Mar 7, 2005
 */
package com.thinkparity.model.parity.model.session;


import java.util.Collection;


import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.api.events.PresenceListener;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.model.AbstractModel;
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
	 * Handle to the implementation of this session api.
	 */
	private static SessionModelImpl impl = new SessionModelImpl();

	public static void acceptPresence(final User xmppUser) throws ParityException {
		impl.acceptPresence(xmppUser);
	}

	public static void debugRoster() {
		impl.debugRoster();
	}

	public static void denyPresence(final User xmppUser) throws ParityException {
		impl.denyPresence(xmppUser);
	}

	/**
	 * Obtain a handle to a session model interface.
	 * @return SessionModel
	 */
	public static SessionModel getModel() { return new SessionModel(); }

	/**
	 * Terminate the current parity session.
	 * @throws ParityException
	 */
	public static void logout() throws ParityException { impl.logout(); }

	/**
	 * Deregister an instance of a listener for parity session events.
	 * 
	 * @param sessionListener
	 *            <code>com.thinkparity.model.parity.api.session.SessionListener</code>
	 */
	public static void removeListener(final SessionListener sessionListener) {
		impl.removeListener(sessionListener);
	}

	public static void send(final User user,
			final DocumentVersion documentVersion) throws ParityException {
		impl.send(user, documentVersion);
	}

	/**
	 * Instance implementation.
	 */
	private final SessionModelImpl impl2;

	/**
	 * Handle to the parity workspace.
	 */
	private final Workspace workspace;

	/**
	 * Create a SessionModel
	 */
	private SessionModel() {
		super();
		this.workspace = WorkspaceModel.getModel().getWorkspace();
		this.impl2 = new SessionModelImpl(workspace);
	}

	public void addListener(final PresenceListener presenceListener) {
		impl2.addListener(presenceListener);
	}

	/**
	 * Register an instance of a listener for parity session events.
	 * 
	 * @param sessionListener
	 *            <code>com.thinkparity.model.parity.api.session.SessionListener</code>
	 */
	public void addListener(final SessionListener sessionListener) {
		impl2.addListener(sessionListener);
	}

	public void addRosterEntry(final User xmppUser)
			throws ParityException {
		impl2.addRosterEntry(xmppUser);
	}

	public Collection<User> getRosterEntries() throws ParityException {
		return impl2.getRosterEntries();
	}

	/**
	 * Determine whether or not the parity session has been established.
	 * @return Boolean
	 */
	public Boolean isLoggedIn() { return impl2.isLoggedIn(); }

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
		impl2.login(username, password);
	}
}

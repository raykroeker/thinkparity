/*
 * Created On: Mar 7, 2005
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.events.SessionListener;

/**
 * <b>Title:</b>thinkParity Session Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.12
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface SessionModel {

    /**
	 * Add a session listener to the session.
	 * 
	 * @param sessionListener
	 *            The session listener to add.
	 */
	public void addListener(final SessionListener sessionListener);

    /**
	 * Determine whether or not the parity session has been established.
	 * @return Boolean
	 */
	public Boolean isLoggedIn();

    /**
     * Login to parity. This will create a new singleton instance of a parity
     * session.
     * 
     */
    public void login(final LoginMonitor monitor);

	/**
     * Login to parity. This will create a new singleton instance of a parity
     * session.
     * 
     * @param monitor
     *            A <code>LoginMonitor</code>.
     * @param credentials
     *            The user's credentials.
     */
	public void login(final LoginMonitor monitor, final Credentials credentials);

	/**
	 * Terminate the current parity session.
	 * @throws ParityException
	 */
	public void logout();

	/**
	 * Remove a session listener from the session.
	 * 
	 * @param sessionListener
	 *            The registered session listener to remove.
	 */
	public void removeListener(final SessionListener sessionListener);
}

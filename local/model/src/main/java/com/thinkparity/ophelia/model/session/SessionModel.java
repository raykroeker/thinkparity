/*
 * Created On: Mar 7, 2005
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.InvalidLocationException;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.events.SessionListener;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

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
    @ThinkParityTransaction(TransactionType.NEVER)
	public void addListener(final SessionListener sessionListener);

    /**
	 * Determine whether or not the parity session has been established.
	 * @return Boolean
	 */
	public Boolean isLoggedIn();

    /**
     * Login.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @throws InvalidCredentialsException
     *             if the stored credentials do not match the actual credentials
     */
    public void login(final ProcessMonitor monitor)
            throws InvalidCredentialsException, InvalidLocationException;

    /**
     * Logout.
     *
     */
	public void logout();

	/**
	 * Remove a session listener from the session.
	 * 
	 * @param sessionListener
	 *            The registered session listener to remove.
	 */
    @ThinkParityTransaction(TransactionType.NEVER)
	public void removeListener(final SessionListener sessionListener);
}

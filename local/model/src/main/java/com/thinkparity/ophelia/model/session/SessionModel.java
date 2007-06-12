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
     * Obtain an offline explanation.
     * 
     * @return An <code>OfflineCode</code>, or null if we are currently
     *         online.
     */
    @ThinkParityTransaction(TransactionType.SUPPORTED)
    public OfflineCode getOfflineCode();

    /**
     * Determine if the user is logged in.
     * 
     * @return True if the user is logged in.
     */
    public Boolean isLoggedIn();

    /**
     * Determine whether or not the session is online.
     * 
     * @return True if the session is online; false otherwise.
     */
    @ThinkParityTransaction(TransactionType.SUPPORTED)
    public Boolean isOnline();

    /**
     * Login.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @throws InvalidCredentialsException
     *             if the stored credentials do not match the actual credentials
     */
    @ThinkParityTransaction(TransactionType.NEVER)
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

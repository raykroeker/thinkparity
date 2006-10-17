/**
 * 
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.codebase.model.session.Credentials;

/**
 * <b>Title:</b>thinkParity Login Monitor<br>
 * <b>Description:</b>The thinkParity login monitor provides a client of the
 * login process the ability to provide feedback during the login process.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface LoginMonitor {

    /**
     * Confirm the synchronization of the local database.
     * 
     * @return True if the client confirms.
     */
    public Boolean confirmSynchronize();

    /**
     * Notify that the credentials used to login are invalid.
     * 
     * @param credentials
     *            The invalid <code>Credentials</code>.
     */
    public void notifyInvalidCredentials(final Credentials credentials);
}

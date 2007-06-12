/*
 * Created On:  29-May-07 8:06:17 PM
 */
package com.thinkparity.desdemona.model.session;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface SessionModel {

    /**
     * Login.
     * 
     * @param credentials
     *            A user's <code>Credentials</code>.
     * @return A session id <code>String</code>.
     * @throws InvalidCredentialsException
     */
    public String login(final Credentials credentials)
            throws InvalidCredentialsException;

    /**
     * Logout.
     * 
     * @param sessionId
     *            A user's session id <code>String</code>.
     */
    public void logout(final String sessionId);

    /**
     * Read a session.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A <code>Session</code>.
     */
    public Session readSession(final String sessionId);

    /**
     * Read a session user.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A <code>User</code>.
     */
    public User readUser(final String sessionId);
}

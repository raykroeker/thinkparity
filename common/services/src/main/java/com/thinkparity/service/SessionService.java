/*
 * Created On:  29-May-07 8:02:32 PM
 */
package com.thinkparity.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.model.session.Configuration;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

/**
 * <b>Title:</b>thinkParity Session Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Session")
public interface SessionService {

    /**
     * Login.
     * 
     * @param credentials
     *            The user's <code>Credentials</code>.
     * @return An <code>AuthToken</code>.
     */
    @WebMethod
    AuthToken login(Credentials credentials) throws InvalidCredentialsException;

    /**
     * Logout.
     *
     */
    @WebMethod
    void logout(AuthToken authToken);

    /**
     * Read the configuration.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return A <code>Configuration</code>.
     */
    @WebMethod
    Configuration readConfiguration(AuthToken authToken);
}

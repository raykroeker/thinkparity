/*
 * Created On:  29-May-07 8:03:57 PM
 */
package com.thinkparity.desdemona.web.service;

import javax.jws.WebService;

import com.thinkparity.codebase.model.session.Configuration;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.desdemona.model.ModelFactory;
import com.thinkparity.desdemona.model.session.SessionModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.SessionService;

/**
 * <b>Title:</b>thinkParity Desdemona Session Service Endpoint Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.SessionService")
public class SessionSEI extends ServiceSEI implements SessionService {

    /**
     * Create SessionSEI.
     *
     */
    public SessionSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.SessionService#login(com.thinkparity.codebase.model.session.Credentials)
     * 
     */
    public AuthToken login(final Credentials credentials)
            throws InvalidCredentialsException {
        try {
            return getModel().login(credentials);
        } catch (final InvalidCredentialsException icx) {
            logger.logError(icx, "Cannot login as {0}.", credentials);
            throw icx;
        }
    }

    /**
     * @see com.thinkparity.service.SessionService#logout()
     *
     */
    public void logout(final AuthToken authToken) {
        getModel(authToken).logout(authToken.getSessionId());
    }

    /**
     * @see com.thinkparity.service.SessionService#readConfiguration(com.thinkparity.service.AuthToken)
     * 
     */
    @Override
    public Configuration readConfiguration(final AuthToken authToken) {
        return getModel(authToken).readConfiguration(authToken);
    }

    /**
     * Obtain a session model.
     * 
     * @return An instance of <code>SessionModel</code>.
     */
    private final SessionModel getModel() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return ModelFactory.getInstance(loader).getSessionModel();
    }

    /**
     * Obtain a session model for an authenticated session.
     * 
     * @param authToken
     *            A session <code>AuthToken</code>.
     * @return An instance of <code>SessionModel</code>.
     */
    private final SessionModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getSessionModel();
    }
}

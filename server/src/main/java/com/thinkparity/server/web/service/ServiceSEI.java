/*
 * Created On:  29-May-07 8:05:15 PM
 */
package com.thinkparity.desdemona.web.service;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.ModelFactory;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.session.SessionModel;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Web Services Service Endpoint Abstration<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ServiceSEI {

    /** A <code>Log4JWrapper</code>. */
    protected final Log4JWrapper logger;

    /** The service endpoint <code>ClassLoader</code>. */
    private ClassLoader loader;

    /** An instance of <code>SessionModel</code>. */
    private final SessionModel sessionModel;

    /**
     * Create ServiceEndpoint.
     *
     */
    protected ServiceSEI() {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.sessionModel = ModelFactory.getInstance(getLoader()).getSessionModel();
    }

    /**
     * Obtain a model factory.
     * 
     * @return An instance of <code>ModelFactory</code>.
     */
    protected final ModelFactory getModelFactory(final AuthToken authToken) {
        return ModelFactory.getInstance(readSessionUser(authToken), getLoader());
    }

    /**
     * Obtain the service endpoint loader.
     * 
     * @return A <code>ClassLoader</code>.
     */
    private ClassLoader getLoader() {
        if (null == loader) {
            loader = Thread.currentThread().getContextClassLoader();
        }
        return loader;
    }

    /**
     * Create a new authentication specific service exception.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return A <code>ServiceException</code>.
     */
    private AuthException newAuthException(final AuthToken authToken) {
        return new AuthException(authToken);
    }

    /**
     * Read a session user for an authenticated session.
     * 
     * @param authToken
     *            A session <code>AuthToken</code>.
     * @return A <code>User</code>.
     */
    private User readSessionUser(final AuthToken authToken) {
        final Session session;
        if (null == authToken) {
            throw newAuthException(authToken);
        } else {
            session = sessionModel.readSession(authToken.getSessionId());
            if (null == session) {
                throw newAuthException(authToken);
            }
        }
        return sessionModel.readUser(session.getId());
    }
}

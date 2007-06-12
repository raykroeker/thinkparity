/*
 * Created On:  3-Jun-07 2:57:55 PM
 */
package com.thinkparity.desdemona.web.service;

import java.util.Calendar;

import javax.jws.WebService;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.ModelFactory;
import com.thinkparity.desdemona.model.system.SystemModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.SystemService;

/**
 * <b>Title:</b>thinkParity Desdemona System Service Endpoint Implementation<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.SystemService")
public class SystemSEI extends ServiceSEI implements SystemService {

    /**
     * Create SystemSEI.
     *
     */
    public SystemSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.SystemService#readDateTime(com.thinkparity.service.AuthToken)
     *
     */
    public Calendar readDateTime(final AuthToken authToken) {
        return getModel().readDateTime();
    }

    /**
     * @see com.thinkparity.service.SystemService#readVersion(com.thinkparity.service.AuthToken)
     *
     */
    public String readVersion(final AuthToken authToken) {
        return getModel(authToken).readVersion();
    }

    /**
     * Obtain a system model for an authenticated session.
     * 
     * @param authToken
     *            A session <code>AuthToken</code>.
     * @return An instance of <code>SystemModel</code>.
     */
    private SystemModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getSystemModel();
    }

    /**
     * Obtain a system model for an administrative user.
     * 
     * @return An instance of <code>SystemModel</code>.
     */
    private SystemModel getModel() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return ModelFactory.getInstance(User.THINKPARITY, loader).getSystemModel();
    }
}

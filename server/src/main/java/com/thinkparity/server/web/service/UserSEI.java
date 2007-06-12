/*
 * Created On:  3-Jun-07 2:58:11 PM
 */
package com.thinkparity.desdemona.web.service;

import javax.jws.WebService;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.user.UserModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.UserService;

/**
 * <b>Title:</b>thinkParity Desdemona User Service Endpoint Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.UserService")
public class UserSEI extends ServiceSEI implements UserService {

    /**
     * Create UserSEI.
     *
     */
    public UserSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.UserService#read(com.thinkparity.service.AuthToken, com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public User read(final AuthToken authToken, final JabberId userId) {
        return getModel(authToken).read(userId);
    }

    /**
     * Obtain a user model for an authenticated session.
     * 
     * @param authToken
     *            A session <code>AuthToken</code>.
     * @return An instance of <code>UserModel</code>.
     */
    private UserModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getUserModel();
    }
}

/*
 * Created On:  30-May-07 10:19:40 AM
 */
package com.thinkparity.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity User Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name="User")
public interface UserService {

    @WebMethod
    User read(AuthToken authToken, JabberId userId);
}

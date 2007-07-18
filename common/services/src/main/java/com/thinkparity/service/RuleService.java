/*
 * Created On:  30-May-07 10:05:56 AM
 */
package com.thinkparity.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity Rule Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Rule")
public interface RuleService {

    @WebMethod
    Boolean isInviteRestricted(AuthToken authToken, User user);

    @WebMethod
    Boolean isPublishRestricted(AuthToken authToken,
            JabberId publishTo);

    @WebMethod
    Boolean isPublishRestricted(AuthToken authToken, List<EMail> emails,
            List<User> users);
}

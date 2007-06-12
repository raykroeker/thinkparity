/*
 * Created On:  30-May-07 10:05:56 AM
 */
package com.thinkparity.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.jabber.JabberId;

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
    Boolean isPublishRestricted(AuthToken authToken,
            JabberId publishTo);
}

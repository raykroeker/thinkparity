/*
 * Created On:  30-May-07 10:07:03 AM
 */
package com.thinkparity.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * <b>Title:</b>thinkParity Stream Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Stream")
public interface StreamService {

    @WebMethod
    String create(AuthToken authToken, StreamSession session);

    @WebMethod
    StreamSession createSession(AuthToken authToken);

    @WebMethod
    void delete(AuthToken authToken, String sessionId, String streamId);

    @WebMethod
    void deleteSession(AuthToken authToken, StreamSession sessionId);

    @WebMethod
    StreamSession readSession(AuthToken authToken, String sessionId);
}

/*
 * Created On:  3-Jun-07 2:57:37 PM
 */
package com.thinkparity.desdemona.web.service;

import javax.jws.WebService;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.model.stream.StreamModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.StreamService;

/**
 * <b>Title:</b>thinkParity Desdemona Stream Service Endpoint Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.StreamService")
public class StreamSEI extends ServiceSEI implements StreamService {

    /**
     * Create StreamSEI.
     *
     */
    public StreamSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.StreamService#create(com.thinkparity.service.AuthToken, java.lang.String)
     *
     */
    public String create(final AuthToken authToken, final StreamSession session) {
        return getModel(authToken).create(session.getId());
    }

    /**
     * @see com.thinkparity.service.StreamService#createSession(com.thinkparity.service.AuthToken)
     *
     */
    public StreamSession createSession(final AuthToken authToken) {
        return getModel(authToken).createSession();
    }

    /**
     * @see com.thinkparity.service.StreamService#delete(com.thinkparity.service.AuthToken, java.lang.String, java.lang.String)
     *
     */
    public void delete(final AuthToken authToken, final String sessionId,
            final String streamId) {
        getModel(authToken).delete(sessionId, streamId);
    }

    /**
     * @see com.thinkparity.service.StreamService#deleteSession(com.thinkparity.service.AuthToken, java.lang.String)
     *
     */
    public void deleteSession(final AuthToken authToken, final StreamSession session) {
        getModel(authToken).deleteSession(session.getId());
    }

    /**
     * @see com.thinkparity.service.StreamService#readSession(com.thinkparity.service.AuthToken, java.lang.String)
     *
     */
    public StreamSession readSession(final AuthToken authToken,
            final String sessionId) {
        return getModel(authToken).readSession(sessionId);
    }

    /**
     * Obtain a stream model for an authenticated session.
     * 
     * @param authToken
     *            A session <code>AuthToken</code>.
     * @return A <code>StreamModel</code>.
     */
    private StreamModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getStreamModel();
    }
}

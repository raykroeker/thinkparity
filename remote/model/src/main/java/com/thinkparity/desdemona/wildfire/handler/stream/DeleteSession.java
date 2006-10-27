/*
 * Created On: Sun Oct 22 2006 10:23 PDT
 */
package com.thinkparity.desdemona.wildfire.handler.stream;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeleteSession extends AbstractHandler {

    /** Create DeleteSession. */
    public DeleteSession() {
        super("stream:deletesession");
    }

    /**
     * Delete a stream session.
     *
     */
    @Override
    public void service() {
        delete(readJabberId("userId"), readString("sessionId"));
    }

    /**
     * Delete a session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A session id <code>String</code>.
     */
    private void delete(final JabberId userId, final String sessionId) {
        getStreamModel().deleteSession(userId, sessionId);
    }
}
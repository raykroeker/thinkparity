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
public final class Delete extends AbstractHandler {

    /** Create Delete. */
    public Delete() {
        super("stream:delete");
    }

    /**
     * Delete a stream.
     *
     */
    @Override
    public void service() {
        delete(readJabberId("userId"), readString("sessionId"), readString("streamId"));
    }

    /**
     * Delete a stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A session id <code>String</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    private void delete(final JabberId userId, final String sessionId,
            final String streamId) {
        getStreamModel().delete(userId, sessionId, streamId);
    }
}
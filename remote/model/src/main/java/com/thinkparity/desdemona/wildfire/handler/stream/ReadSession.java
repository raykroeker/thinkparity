/*
 * Created On: Sun Oct 22 2006 10:23 PDT
 */
package com.thinkparity.desdemona.wildfire.handler.stream;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadSession extends AbstractHandler {

    /** Create CreateSession. */
    public ReadSession() {
        super("stream:readsession");
    }

    /**
     * Initialize a stream session.
     *
     */
    @Override
    public void service() {
        final StreamSession streamSession = read(
                readJabberId("userId"), readString("sessionId"));
        writeStreamSession("session", streamSession);
    }

    /**
     * Read an existing stream session.
     *
     * @return A <code>StreamSession</code>.
     */
    private StreamSession read(final JabberId userId, final String sessionId) {
        return getStreamModel().readSession(userId, sessionId);
    }
}
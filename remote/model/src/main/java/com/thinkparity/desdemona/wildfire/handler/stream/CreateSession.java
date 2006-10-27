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
public final class CreateSession extends AbstractHandler {

    /** Create CreateSession. */
    public CreateSession() {
        super("stream:createsession");
    }

    /**
     * Create a stream session.
     *
     */
    @Override
    public void service() {
        final StreamSession streamSession = create(readJabberId("userId"));
        writeStreamSession("session", streamSession);
    }

    /**
     * Create a new stream session.
     *
     * @return A <code>StreamSession</code>.
     */
    private StreamSession create(final JabberId userId) {
        return getStreamModel().createSession(userId);
    }
}
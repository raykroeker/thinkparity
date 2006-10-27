/*
 * Created On: Sun Oct 22 2006 10:23 PDT
 */
package com.thinkparity.desdemona.wildfire.handler.stream;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Initialize extends AbstractHandler {

    /** Create Initialize. */
    public Initialize() {
        super("stream:initialize");
    }

    /**
     * Initialize a stream session.
     *
     */
    @Override
    public void service() {
        final StreamSession streamSession = initialize(readJabberId("userId"));
        writeStreamSession("session", streamSession);
    }

    /**
     * Create a new stream session.
     *
     * @return A <code>Session</code>.
     */
    private StreamSession initialize(final JabberId userId) {
        return getStreamModel().initializeSession(userId);
    }
}
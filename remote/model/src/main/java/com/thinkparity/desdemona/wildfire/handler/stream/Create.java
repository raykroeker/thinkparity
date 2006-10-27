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
public final class Create extends AbstractHandler {

    /** Create Create. */
    public Create() {
        super("stream:create");
    }

    /**
     * Create a stream.
     *
     */
    @Override
    public void service() {
        final String streamId = create(
                readJabberId("userId"), readString("sessionId"));
        writeString("streamId", streamId);
    }

    /**
     * Create a new stream.
     * 
     * @return A stream id <code>String</code>.
     */
    private String create(final JabberId userId, final String sessionId) {
        return getStreamModel().create(userId, sessionId);
    }
}
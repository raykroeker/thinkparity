/*
 * Created On: Sun Oct 22 2006 10:23 PDT
 */
package com.thinkparity.desdemona.wildfire.handler.stream;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadSession extends AbstractHandler {

    /**
     * Create ReadSession.
     *
     */
    public ReadSession() {
        super("stream:readsession");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider,
     *      com.thinkparity.desdemona.util.service.ServiceRequestReader,
     *      com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     * 
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        final StreamSession streamSession = read(provider,
                reader.readJabberId("userId"), reader.readString("sessionId"));
        writer.writeStreamSession("session", streamSession);
    }

    /**
     * Read an existing stream session.
     *
     * @return A <code>StreamSession</code>.
     */
    private StreamSession read(final ServiceModelProvider provider,
            final JabberId userId, final String sessionId) {
        return provider.getStreamModel().readSession(userId, sessionId);
    }
}
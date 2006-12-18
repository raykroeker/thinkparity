/*
 * Created On: Sun Oct 22 2006 10:23 PDT
 */
package com.thinkparity.desdemona.wildfire.handler.stream;

import com.thinkparity.codebase.jabber.JabberId;

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
public final class Delete extends AbstractHandler {

    /**
     * Create Delete.
     *
     */
    public Delete() {
        super("stream:delete");
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
        delete(provider, reader.readJabberId("userId"),
                reader.readString("sessionId"), reader.readString("streamId"));
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
    private void delete(final ServiceModelProvider context,
            final JabberId userId, final String sessionId, final String streamId) {
        context.getStreamModel().delete(userId, sessionId, streamId);
    }
}
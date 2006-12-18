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
public final class Create extends AbstractHandler {

    /**
     * Create Create.
     *
     */
    public Create() {
        super("stream:create");
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
        final String streamId = create(provider, reader.readJabberId("userId"),
                reader.readString("sessionId"));
        writer.writeString("streamId", streamId);
    }

    /**
     * Create a new stream.
     * 
     * @return A stream id <code>String</code>.
     */
    private String create(final ServiceModelProvider context,
            final JabberId userId, final String sessionId) {
        return context.getStreamModel().create(userId, sessionId);
    }
}
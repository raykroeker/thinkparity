/*
 * Created On:  23-Jan-07 6:13:18 PM
 */
package com.thinkparity.desdemona.wildfire.handler.migrator;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Resource;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AuthenticatedHandler;

/**
 * <b>Title:</b>thinkParity Migrator Read Latest Release Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateStream extends AuthenticatedHandler {

    /**
     * Create CreateStream.
     *
     */
    public CreateStream() {
        super("migrator:createstream");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        createStream(provider, reader.readJabberId("userId"),
                reader.readString("streamId"),
                reader.readResources("resources"));
    }

    /**
     * Read a release.
     * 
     * @param provider
     *            A <code>ServiceModelProvider</code>.
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Release</code>.
     */
    private void createStream(final ServiceModelProvider provider,
            final JabberId userId, final String streamId,
            final List<Resource> resources) {
        provider.getMigratorModel().createStream(userId, streamId, resources);
    }
}

/*
 * Created On: Sep 17, 2006 2:28:00 PM
 */
package com.thinkparity.desdemona.wildfire.handler.archive;

import java.util.UUID;

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
public final class Archive extends AbstractHandler {

    /**
     * Create Archive.
     *
     */
    public Archive() {
        super("archive:archive");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        archive(provider, reader.readJabberId("userId"),
                reader.readUUID("uniqueId"));
    }

    /**
     * Archive a container.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    private void archive(final ServiceModelProvider provider,
            final JabberId userId, final UUID uniqueId) {
        provider.getArtifactModel().archive(userId, uniqueId);
    }
}

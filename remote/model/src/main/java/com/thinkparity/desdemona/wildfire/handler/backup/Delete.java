/*
 * Created On: 2007-01-17 13:37:00
 */
package com.thinkparity.desdemona.wildfire.handler.backup;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b>thinkParity Backup Archive Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Delete extends AbstractHandler {

    /**
     * Create Archive.
     *
     */
    public Delete() {
        super("backup:delete");
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
        delete(provider, reader.readJabberId("userId"),
                reader.readUUID("uniqueId"));
    }

    /**
     * Delete an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    private void delete(final ServiceModelProvider provider,
            final JabberId userId, final UUID uniqueId) {
        provider.getBackupModel().delete(userId, uniqueId);
    }
}

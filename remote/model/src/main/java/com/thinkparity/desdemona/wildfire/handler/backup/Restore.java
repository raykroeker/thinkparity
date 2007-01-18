/*
 * Created On: Sep 17, 2006 2:28:00 PM
 */
package com.thinkparity.desdemona.wildfire.handler.backup;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b>thinkParity Backup Restore Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Restore extends AbstractHandler {

    /**
     * Create Restore.
     *
     */
    public Restore() {
        super("backup:restore");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        archive(provider, reader.readJabberId("userId"), reader.readUUID("uniqueId"));
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
        provider.getBackupModel().restore(userId, uniqueId);
    }
}

/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.backup;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.ContainerVersion;

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
public final class ReadContainerVersions extends AbstractHandler {

    /**
     * Create ReadContainerVersions.
     *
     */
    public ReadContainerVersions() {
        super("backup:readcontainerversions");
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
        final List<ContainerVersion> versions =
            readContainerVersions(provider, reader.readJabberId("userId"),
                    reader.readUUID("uniqueId"));
        writer.writeContainerVersions("containerVersions", "containerVersions", versions);
    }

    private List<ContainerVersion> readContainerVersions(
            final ServiceModelProvider context, final JabberId userId,
            final UUID uniqueId) {
        return context.getBackupModel().readContainerVersions(userId, uniqueId);
    }
}

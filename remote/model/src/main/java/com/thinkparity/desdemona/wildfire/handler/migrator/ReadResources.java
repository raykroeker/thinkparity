/*
 * Created On:  23-Jan-07 6:13:18 PM
 */
package com.thinkparity.desdemona.wildfire.handler.migrator;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Resource;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AuthenticatedHandler;

/**
 * <b>Title:</b>thinkParity Migrator Read Resources Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadResources extends AuthenticatedHandler {

    /**
     * Create ReadResources.
     *
     */
    public ReadResources() {
        super("migrator:readresources");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        writer.writeResources("resources", readResources(provider,
                reader.readJabberId("userId"),
                reader.readUUID("productUniqueId"),
                reader.readString("releaseName"), reader.readOs("os")));
    }

    /**
     * Read resources.
     * 
     * @param provider
     *            A <code>ServiceModelProvider</code>.
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param releaseName
     *            A release name <code>String</code>.
     * @return A <code>List</code> of <code>Resource</code>.
     */
    private List<Resource> readResources(final ServiceModelProvider provider,
            final JabberId userId, final UUID productUniqueId,
            final String releaseName, final OS os) {
        return provider.getMigratorModel().readResources(userId,
                productUniqueId, releaseName, os);
    }
}

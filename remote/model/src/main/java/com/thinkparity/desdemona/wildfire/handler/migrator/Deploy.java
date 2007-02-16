/*
 * Created On:  23-Jan-07 6:13:18 PM
 */
package com.thinkparity.desdemona.wildfire.handler.migrator;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AuthenticatedHandler;

/**
 * <b>Title:</b>thinkParity Migrator Deploy Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Deploy extends AuthenticatedHandler {

    /**
     * Create DeployRelease.
     *
     */
    public Deploy() {
        super("migrator:deploy");
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
        deploy(provider, reader.readJabberId("userId"),
                reader.readProduct("product"), reader.readRelease("release"),
                reader.readResources("resources"),
                reader.readString("streamId"));
    }

    /**
     * Deploy a product release.
     * 
     * @param provider
     *            A <code>ServiceModelProvider</code>.
     * @param userId
     *            A user id <code>UUID</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    private void deploy(final ServiceModelProvider provider,
            final JabberId userId, final Product product, final Release release,
            final List<Resource> resources, final String streamId) {
        provider.getMigratorModel().deploy(userId, product, release, resources,
                streamId);
    }
}

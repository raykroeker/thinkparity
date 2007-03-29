/*
 * Created On:  23-Jan-07 6:13:18 PM
 */
package com.thinkparity.desdemona.wildfire.handler.migrator;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Release;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AuthenticatedHandler;

/**
 * <b>Title:</b>thinkParity Migrator Read Release Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadRelease extends AuthenticatedHandler {

    /**
     * Create ReadRelease.
     *
     */
    public ReadRelease() {
        super("migrator:readrelease");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        writer.write("release", readRelease(provider,
                reader.readJabberId("userId"), reader.readString("productName"),
                reader.readString("name"), reader.readOs("os")));
    }

    /**
     * Read a release.
     * 
     * @param provider
     *            A <code>ServiceModelProvider</code>.
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param name
     *            A release name <code>String</code>.
     * @return A <code>Release</code>.
     */
    private Release readRelease(final ServiceModelProvider provider,
            final JabberId userId, final String productName,
            final String name, final OS os) {
        return provider.getMigratorModel().readRelease(userId, productName,
                name, os);
    }
}

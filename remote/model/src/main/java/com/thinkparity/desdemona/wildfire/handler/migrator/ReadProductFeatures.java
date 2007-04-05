/*
 * Created On:  23-Jan-07 6:13:18 PM
 */
package com.thinkparity.desdemona.wildfire.handler.migrator;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Feature;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AuthenticatedHandler;

/**
 * <b>Title:</b>thinkParity Migrator Read Product Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadProductFeatures extends AuthenticatedHandler {

    /**
     * Create ReadProduct.
     *
     */
    public ReadProductFeatures() {
        super("migrator:readproductfeatures");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        writer.writeFeatures("features", readProductFeatures(provider, reader
                .readJabberId("userId"), reader.readString("name")));
    }

    private List<Feature> readProductFeatures(
            final ServiceModelProvider provider, final JabberId userId,
            final String name) {
        return provider.getMigratorModel().readProductFeatures(userId, name);
    }
}

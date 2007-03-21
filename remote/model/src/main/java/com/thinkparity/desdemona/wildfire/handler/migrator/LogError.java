/*
 * Created On: Sep 28, 2006 8:41:26 AM
 */
package com.thinkparity.desdemona.wildfire.handler.migrator;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Product;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b>thinkParity DesdemonaModel System Log Error Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class LogError extends AbstractHandler {

    /**
     * Create LogError.
     * 
     */
    public LogError() {
        super("migrator:logerror");
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
        logError(provider, reader.readJabberId("userId"),
                reader.readProduct("product"), reader.readError("error"));
    }

    private void logError(final ServiceModelProvider provider,
            final JabberId userId, final Product product, final Error error) {
        provider.getMigratorModel().logError(userId, product, error);
    }
}

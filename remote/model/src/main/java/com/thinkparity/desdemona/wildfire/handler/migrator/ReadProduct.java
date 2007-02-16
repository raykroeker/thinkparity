/*
 * Created On:  23-Jan-07 6:13:18 PM
 */
package com.thinkparity.desdemona.wildfire.handler.migrator;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Product;

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
public final class ReadProduct extends AuthenticatedHandler {

    /**
     * Create ReadProduct.
     *
     */
    public ReadProduct() {
        super("migrator:readproduct");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        writer.write("product", readProduct(provider, reader.readJabberId("userId"),
                reader.readString("name")));
    }

    /**
     * Read a product.
     * 
     * @param provider
     *            A <code>ServiceModelProvider</code>.
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param name
     *            A product name <code>String</code>.
     * @return A <code>Product</code>.
     */
    private Product readProduct(final ServiceModelProvider provider,
            final JabberId userId, final String name) {
        return provider.getMigratorModel().readProduct(userId, name);
    }
}

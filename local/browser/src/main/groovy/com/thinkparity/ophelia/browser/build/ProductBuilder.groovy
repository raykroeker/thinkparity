/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.codebase.jabber.JabberId

import com.thinkparity.codebase.model.migrator.Product

import com.thinkparity.service.AuthToken
import com.thinkparity.service.MigratorService

import com.thinkparity.ophelia.model.util.UUIDGenerator


/**
 * <b>Title:</b>thinkParity OpheliaUI Build Task Product Builder<br>
 * <b>Description:</b><br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ProductBuilder {

    /** An <code>AntBuilder</code>. */
    AntBuilder ant

    /** A migrator web-service. */
    MigratorService migratorService

    /** An authentication token. */
    AuthToken authToken

    /** A build task configuration <code>Map</code>. */
    Map configuration

    /**
     * Initialize the product builder.
     *
     */
    void init() {
        if (null == migratorService)
            migratorService = configuration["thinkparity.service-migrator"]
        if (null == authToken)
            authToken = configuration["thinkparity.auth-token"]
    }

    /**
     * Create an instance of a product.
     *
     * @param name
     *      A product name <code>String</code>.
     * @return A <code>Product</code>.
     */
    Product create(String name) {
        init()
        def product = migratorService.readProduct(authToken,
            configuration["thinkparity.product-name"])
        if (null == product) {
            ant.fail("Product ${configuration["thinkparity.product-name"]} does not exist.")
            return null
        } else {
            return product
        }
    }
}

/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.codebase.model.migrator.Product

import com.thinkparity.ophelia.model.util.UUIDGenerator

/**
 * <b>Title:</b>thinkParity OpheliaUI Build Task Product Builder<br>
 * <b>Description:</b><br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ProductBuilder {

    /** A build task configuration <code>Map</code>. */
    Map configuration

    /**
     * Create an instance of a product.
     *
     * @param name
     *      A product name <code>String</code>.
     * @return A <code>Product</code>.
     */
    Product create(String name) {
        def product = new Product()
        product.setCreatedBy(configuration["thinkparity.userid"])
        product.setCreatedOn(configuration["thinkparity.now"])
        product.setName(configuration["thinkparity.product-name"])
        product.setUniqueId(UUIDGenerator.nextUUID());
        product.setUpdatedBy(product.getCreatedBy());
        product.setUpdatedOn(product.getCreatedOn());
        return product
    }
}

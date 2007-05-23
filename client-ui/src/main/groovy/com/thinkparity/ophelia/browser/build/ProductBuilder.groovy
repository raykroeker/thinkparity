/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.codebase.jabber.JabberId

import com.thinkparity.codebase.model.migrator.Product

import com.thinkparity.ophelia.model.util.UUIDGenerator
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession

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

    /** An <code>XMPPSession</code>. */
    XMPPSession xmppSession

    /** A build task configuration <code>Map</code>. */
    Map configuration

    /** The user id <code>JabberId</code>. */
    JabberId userId

    /**
     * Initialize the product builder.
     *
     */
    void init() {
        if (null == xmppSession)
            xmppSession = configuration["thinkparity.xmpp-session"]
        if (null == userId)
            userId = configuration["thinkparity.userid"] 
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
        def product = xmppSession.readMigratorProduct(userId,
            configuration["thinkparity.product-name"])
        if (null == product) {
            ant.fail("Product ${configuration["thinkparity.product-name"]} does not exist.")
            return null
        } else {
            return product
        }
    }
}

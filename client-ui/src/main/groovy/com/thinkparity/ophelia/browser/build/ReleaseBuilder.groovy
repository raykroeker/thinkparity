/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.codebase.jabber.JabberId

import com.thinkparity.codebase.model.migrator.Product
import com.thinkparity.codebase.model.migrator.Release
import com.thinkparity.codebase.model.util.codec.MD5Util

import com.thinkparity.ophelia.model.util.UUIDGenerator
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession

/**
 * <b>Title:</b>thinkParity OpheliaUI Build Task Release Builder<br>
 * <b>Description:</b><br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ReleaseBuilder {

    /** An <code>AntBuilder</code>. */
    AntBuilder ant

    /** A build task configuration <code>Map</code>. */
    Map configuration

    /** An <code>XMPPSession</code>. */
    XMPPSession xmppSession

    /** The <code>Product</code>. */
    Product product

    /** The user id <code>JabberId</code>. */
    JabberId userId

    /**
     * Initialize the release builder.
     *
     */
    void init() {
        if (null == xmppSession)
            xmppSession = configuration["thinkparity.xmpp-session"]
        if (null == userId)
            userId = configuration["thinkparity.userid"] 
    }

    /**
     * Create a release.
     *
     * @param name
     *      A release name.
     * @return A <code>Release</code>.
     */
    Release create() {
        init()

        def existing = xmppSession.readMigratorRelease(userId,
            product.getName(), configuration["thinkparity.release-name"],
            configuration["thinkparity.os"])
        if (null == existing) {
            def release = new Release()
            release.setDate(configuration["thinkparity.now"])
            release.setName(configuration["thinkparity.release-name"])
            release.setOs(configuration["thinkparity.os"])
            return release
        } else {
            ant.fail("Release ${existing.getName()} for ${product.getName()} already exists.")
            return null
        }
    }
}

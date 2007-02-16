/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.codebase.model.migrator.Product
import com.thinkparity.codebase.model.migrator.Release
import com.thinkparity.codebase.model.util.codec.MD5Util

import com.thinkparity.ophelia.model.util.UUIDGenerator

/**
 * <b>Title:</b>thinkParity OpheliaUI Build Task Release Builder<br>
 * <b>Description:</b><br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ReleaseBuilder {

    /** A build task configuration <code>Map</code>. */
    Map configuration

    /**
     * Create a release.
     *
     * @param name
     *      A release name.
     * @return A <code>Release</code>.
     */
    Release create() {
        def release = new Release()
        release.setDate(configuration["thinkparity.now"])
        release.setName(configuration["thinkparity.release-name"])
        release.setOs(configuration["thinkparity.os"])
        return release
    }
}

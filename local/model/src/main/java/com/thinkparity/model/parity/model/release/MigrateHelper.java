/*
 * Created On: Fri May 12 2006 09:11 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import com.thinkparity.migrator.Release;

/**
 * The migrate helper implements a command pattern in order to contain state and
 * perform the various steps required by the migration models' migrate api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class MigrateHelper {

    /** A release. */
    private final Release release;

    /**
     * Create MigrateHelper.
     *
     * @param release
     *      A release.
     */
    MigrateHelper(final Release release) {
        super();
        this.release = release;
    }
}

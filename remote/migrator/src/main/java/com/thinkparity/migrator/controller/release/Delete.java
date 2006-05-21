/*
 * Created On: Fri May 19 2006 10:01 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Delete extends AbstractController {

    /** Create Delete. */
    public Delete() { super("release:delete"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [CONTROLLER] [RELEASE] [DELETE]");
        delete(readLong(Xml.Release.ID));
    }

    /**
     * Delete a release.
     * 
     * @param releaseId
     *      A release id.
     */
    private void delete(final Long releaseId) {
        getReleaseModel(getClass()).delete(releaseId);
    }
}

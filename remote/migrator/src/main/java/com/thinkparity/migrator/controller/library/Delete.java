/*
 * Created On: Fri May 19 2006 10:06 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.AbstractController;

/**
 * Library delete controller.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public final class Delete extends AbstractController {

    /** Create a Delete. */
    public Delete() { super("library:delete"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [CONTROLLER] [LIBRARY] [DELETE]");
        delete(readLong(Xml.Library.ID));
    }

    /**
     * Delete a library.
     * 
     * @param libraryId
     *      A library id.
     */
    private void delete(final Long libraryId) {
        getLibraryModel(getClass()).delete(libraryId);
    }
}

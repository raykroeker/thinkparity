/*
 * May 11, 2006 1:56:22 PM
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class ReadLibraries extends AbstractController {

    /** Create ReadLibraries. */
    public ReadLibraries() { super("release:readlibraries"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LIBRARIES]");
        final List<Library> libraries = readLibraries(readLong(Xml.Release.ID));
        writeLibraries(Xml.Release.LIBRARIES, Xml.Release.LIBRARY, libraries);
    }

    /**
     * Read a release.
     *
     * @param releaseId
     *      A release id.
     * @return A list of libraries.
     */
    private List<Library> readLibraries(final Long releaseId) {
        return getReleaseModel(getClass()).readLibraries(releaseId);
    }
}

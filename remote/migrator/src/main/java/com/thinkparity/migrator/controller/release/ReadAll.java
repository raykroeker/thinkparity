/*
 * May 11, 2006 1:56:22 PM
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import java.util.List;

import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class ReadAll extends AbstractController {

    /** Create ReadLibraries. */
    public ReadAll() { super("release:readall"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ ALL]");
        final List<Release> releases = readAll();
        writeReleases(Xml.Release.RELEASES, Xml.Release.RELEASE, releases);
    }

    /**
     * Read all releases.
     *
     * @return A list of releases.
     */
    private List<Release> readAll() {
        return getReleaseModel(getClass()).readAll();
    }
}

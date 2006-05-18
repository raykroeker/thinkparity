/*
 * May 11, 2006 1:56:22 PM
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class Read extends AbstractController {

    /** Create Read. */
    public Read() { super("release:read"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ]");
        final Release release = read(readString(Xml.Release.NAME));

        if(null != release) {
            writeString(Xml.Release.ARTIFACT_ID, release.getArtifactId());
            writeString(Xml.Release.GROUP_ID, release.getGroupId());
            writeLong(Xml.Release.ID, release.getId());
            writeLibraries(Xml.Release.LIBRARIES, Xml.Release.LIBRARY, release.getLibraries());
            writeString(Xml.Release.NAME, release.getName());
            writeString(Xml.Release.VERSION, release.getVersion());
        }
    }

    /**
     * Create a release.
     * 
     * @param releaseId
     *            The release id.
     * @return The release.
     */
    private Release read(final String releaseName) {
        return getReleaseModel(getClass()).read(releaseName);
    }
}

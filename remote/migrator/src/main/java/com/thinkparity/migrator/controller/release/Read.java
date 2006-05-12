/*
 * May 11, 2006 1:56:22 PM
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import com.thinkparity.migrator.Release;
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
        logger.info("[RMIGRATOR] [RELEASE] [READ]");
        final Release release = read(readString("releaseName"));

        writeString("artifactId", release.getArtifactId());
        writeString("groupId", release.getGroupId());
        writeLong("id", release.getId());
        writeLibraryIds("libraryIds", "libraryId", release.getLibraries());
        writeString("name", release.getName());
        writeString("version", release.getVersion());
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

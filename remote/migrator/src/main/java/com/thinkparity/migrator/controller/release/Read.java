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
        final Long releaseId = readLong(Xml.Release.ID);
        final Release release;
        if(null == releaseId) {
            release = read(readString(Xml.Release.ARTIFACT_ID),
                    readString(Xml.Release.GROUP_ID),
                    readString(Xml.Release.VERSION));
        }
        else { release = read(readLong(Xml.Release.ID)); }
        
        if(null != release) {
            writeString(Xml.Release.ARTIFACT_ID, release.getArtifactId());
            writeCalendar(Xml.Release.CREATED_ON, release.getCreatedOn());
            writeString(Xml.Release.GROUP_ID, release.getGroupId());
            writeLong(Xml.Release.ID, release.getId());
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
    private Release read(final Long releaseId) {
        return getReleaseModel(getClass()).read(releaseId);
    }

    /**
     * Read a release.
     *
     * @param artifactId
     *      An artifact id.
     * @param groupId
     *      A group id.
     * @param version
     *      A version.
     * @return A release.
     */
    private Release read(final String artifactId, final String groupId,
            final String version) {
        return getReleaseModel(getClass()).read(artifactId, groupId, version);
    }
}

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
public class ReadLatest extends AbstractController {

    /** Create ReadLatest. */
    public ReadLatest() { super("release:readlatest"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LATEST]");
        final Release release = readLatest(
                readString(Xml.Release.ARTIFACT_ID),
                readString(Xml.Release.GROUP_ID));

        if(null == release) {}
        else {
            writeString(Xml.Release.ARTIFACT_ID, release.getArtifactId());
            writeCalendar(Xml.Release.CREATED_ON, release.getCreatedOn());
            writeString(Xml.Release.GROUP_ID, release.getGroupId());
            writeLong(Xml.Release.ID, release.getId());
            writeString(Xml.Release.VERSION, release.getVersion());
        }
    }

    /**
     * Read the latest release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @return The release.
     */
    private Release readLatest(final String artifactId, final String groupId) {
        return getReleaseModel(getClass()).readLatest(artifactId, groupId);
    }
}

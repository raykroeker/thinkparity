/*
 * May 11, 2006 1:56:22 PM
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class Create extends AbstractController {

    /** Create Create. */
    public Create() { super("release:create"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE]");
        final Release release = create(readString(Xml.Release.ARTIFACT_ID),
                readString(Xml.Release.GROUP_ID), readString(Xml.Release.VERSION),
                readLibraryIds(Xml.Release.LIBRARY_IDS, Xml.Release.LIBRARY_ID));

        writeString(Xml.Release.ARTIFACT_ID, release.getArtifactId());
        writeCalendar(Xml.Release.CREATED_ON, release.getCreatedOn());
        writeString(Xml.Release.GROUP_ID, release.getGroupId());
        writeLong(Xml.Release.ID, release.getId());
        writeString(Xml.Release.VERSION, release.getVersion());
    }

    /**
     * Create a release.
     * 
     * @param artifactId
     *            The artifact id.
     * @param groupId
     *            The group id.
     * @param version
     *            The version.
     * @param libraries
     *            The libraries.
     * @return The release.
     */
    private Release create(final String artifactId, final String groupId,
            final String version, final List<Library> libraries) {
        return getReleaseModel(getClass()).create(
                artifactId, groupId, version, libraries);
    }
}

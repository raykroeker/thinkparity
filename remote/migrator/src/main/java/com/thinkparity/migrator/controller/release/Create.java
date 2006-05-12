/*
 * May 11, 2006 1:56:22 PM
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
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
        logger.info("[RMIGRATOR] [RELEASE] [CREATE]");
        final Release release = create(readString("artifactId"),
                readString("groupId"), readString("name"),
                readString("version"), readLibraryIds("libraryIds", "libraryId"));

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
     * @param artifactId
     *            The artifact id.
     * @param groupId
     *            The group id.
     * @param name
     *            The name.
     * @param version
     *            The version.
     * @param libraries
     *            The libraries.
     * @return The release.
     */
    private Release create(final String artifactId, final String groupId,
            final String name, final String version,
            final List<Library> libraries) {
        return getReleaseModel(getClass()).create(
                artifactId, groupId, name, version, libraries);
    }
}

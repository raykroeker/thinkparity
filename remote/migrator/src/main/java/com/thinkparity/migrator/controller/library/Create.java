/*
 * Created On: Wed May 10 2006 13:02 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.controller.AbstractController;

/**
 * Library create controller.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.
 */
public final class Create extends AbstractController {

    /** Create a Create. */
    public Create() { super("library:create"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [LIBRARY] [CREATE]");
        final Library library = create(
                readString("artifactId"), readString("groupId"),
                readLibraryType("type"), readString("version"));

        writeString("artifactId", library.getArtifactId());
        writeString("groupId", library.getGroupId());
        writeLong("id", library.getId());
        writeLibraryType("type", library.getType());
        writeString("version", library.getVersion());
    }

    /**
     * Create a library.
     * 
     * @param artifactId
     *            A artifact id.
     * @param groupId
     *            A group id.
     * @param type
     *            A library type.
     * @param version
     *            A version.
     * @return A library.
     */
    private Library create(final String artifactId, final String groupId,
            final Library.Type type, final String version) {
        return getLibraryModel(getClass()).create(
                artifactId, groupId, type, version);
    }
}

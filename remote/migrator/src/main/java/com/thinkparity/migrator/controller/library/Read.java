/*
 * Created On: Thu May 11 2006 08:14 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.controller.AbstractController;

/**
 * Library read controller.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.
 */
public final class Read extends AbstractController {

    /** Create a Read. */
    public Read() { super("library:read"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [LIBRARY] [READ]");
        final Library library = read(readLong("libraryId"));
        
        writeString("artifactId", library.getArtifactId());
        writeString("groupId", library.getGroupId());
        writeLong("id", library.getId());
        writeLibraryType("type", library.getType());
        writeString("version", library.getVersion());
    }

    /**
     * Read a library.
     * 
     * @param libraryId
     *            A library id.
     * @return A library.
     */
    private Library read(final Long libraryId) {
        return getLibraryModel(getClass()).read(libraryId);
    }
}

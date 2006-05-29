/*
 * Created On: Thu May 11 2006 08:14 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Constants.Xml;
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
        logger.info("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ]");
        final Long libraryId = readLong(Xml.Library.ID);
        final Library library;
        if(null == libraryId) {
            library = read(readString(Xml.Library.ARTIFACT_ID),
                    readString(Xml.Library.GROUP_ID),
                    readLibraryType(Xml.Library.TYPE),
                    readString(Xml.Library.VERSION));
        }
        else { library = read(readLong(Xml.Library.ID)); }

        if(null != library) {
            writeString(Xml.Library.ARTIFACT_ID, library.getArtifactId());
            writeCalendar(Xml.Library.CREATED_ON, library.getCreatedOn());
            writeString(Xml.Library.GROUP_ID, library.getGroupId());
            writeLong(Xml.Library.ID, library.getId());
            writeString(Xml.Library.PATH, library.getPath());
            writeLibraryType(Xml.Library.TYPE, library.getType());
            writeString(Xml.Library.VERSION, library.getVersion());
        }
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

    /**
     * Read a library.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param type
     *            A type.
     * @param version
     *            A version.
     * @return A library.
     */
    private Library read(final String artifactId, final String groupId,
            final Library.Type type, final String version) {
        return getLibraryModel(getClass()).read(artifactId, groupId, type, version);
    }
}

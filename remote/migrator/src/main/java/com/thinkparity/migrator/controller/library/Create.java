/*
 * Created On: Wed May 10 2006 13:02 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Constants.Xml;
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
        logger.info("[RMIGRATOR] [CONTROLLER] [LIBRARY] [CREATE]");
        final Library library = create(
                readString(Xml.Library.ARTIFACT_ID), readString(Xml.Library.GROUP_ID),
                readLibraryType(Xml.Library.TYPE), readString(Xml.Library.VERSION));

        writeString(Xml.Library.ARTIFACT_ID, library.getArtifactId());
        writeString(Xml.Library.GROUP_ID, library.getGroupId());
        writeLong(Xml.Library.ID, library.getId());
        writeLibraryType(Xml.Library.TYPE, library.getType());
        writeString(Xml.Library.VERSION, library.getVersion());
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

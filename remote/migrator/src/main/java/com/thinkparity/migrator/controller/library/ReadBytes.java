/*
 * Created On: Thu May 11 2006 08:15 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.AbstractController;

/**
 * Library read bytes controller.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.
 */
public final class ReadBytes extends AbstractController {

    /** Create a ReadBytes. */
    public ReadBytes() { super("library:readbytes"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ BYTES]");
        writeBytes(Xml.Library.BYTES, readBytes(readLong(Xml.Library.ID)));
    }

    /**
     * Read a library's bytes.
     * 
     * @param libraryId
     *            A library id.
     * @return The library's bytes.
     */
    private Byte[] readBytes(final Long libraryId) {
        return getLibraryModel(getClass()).readBytes(libraryId);
    }
}

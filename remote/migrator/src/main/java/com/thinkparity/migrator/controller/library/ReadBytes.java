/*
 * Created On: Thu May 11 2006 08:15 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import com.thinkparity.migrator.LibraryBytes;
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
        final LibraryBytes bytes = readBytes(readLong(Xml.Library.ID));

        writeBytes(Xml.Library.BYTES, bytes.getBytes());
        writeString(Xml.Library.CHECKSUM, bytes.getChecksum());
        writeLong(Xml.Library.ID, bytes.getLibraryId());
    }

    /**
     * Read a library's bytes.
     * 
     * @param libraryId
     *            A library id.
     * @return The library's bytes.
     */
    private LibraryBytes readBytes(final Long libraryId) {
        return getLibraryModel(getClass()).readBytes(libraryId);
    }
}

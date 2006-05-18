/*
 * Created On: Thu May 11 2006 08:14 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import com.thinkparity.migrator.controller.AbstractController;
import com.thinkparity.migrator.Constants.Xml;

/**
 * Library create bytes controller.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.
 */
public final class CreateBytes extends AbstractController {

    /** Create a CreateBytes. */
    public CreateBytes() { super("library:createbytes"); }

    /** @see com.thinkparity.migrator.controller.AbstractController#service() */
    public void service() {
        logger.info("[RMIGRATOR] [CONTROLLER] [LIBRARY] [CREATE BYTES]");
        createBytes(readLong(Xml.Library.ID), readByteArray(Xml.Library.BYTES));
    }

    /**
     * Create a library's bytes.
     * 
     * @param libraryId
     *            A library id.
     * @param bytes
     *            The library's bytes.
     */
    private void createBytes(final Long libraryId, final Byte[] bytes) {
        getLibraryModel(getClass()).createBytes(libraryId, bytes);
    }
}

/*
 * Created On: Wed May 10 2006 08:04 PDT
 * $Id$
 */
package com.thinkparity.migrator.model.library;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LibraryBytes;
import com.thinkparity.migrator.MigratorTestCase;

/**
 * Test the parity library interface's read bytes api.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class ReadBytesTest extends MigratorTestCase {

    /** Test data. */
    private List<Fixture> data;

    /** Create ReadBytesTest. */
    public ReadBytesTest() {
        super("[RMIGRATOR] [LIBRARY] [READ BYTES TEST]");
    }

    /** Test the read bytes api. */
    public void testReadBytes() {
        LibraryBytes libraryBytes;
        for(final Fixture datum : data) {
            libraryBytes = datum.lModel.readBytes(datum.libraryId);

            assertNotNull("[RMIGRATOR] [LIBRARY] [READ  BYTES TEST] [LIBRARY BYTES ARE NULL]", libraryBytes);
            assertEquals("[RMIGRATOR] [LIBRARY] [READ BYTES TEST] [LIBRARY BYTES DO NOT EQUAL EXPECTATION]", datum.eLibraryBytes, libraryBytes);
        }
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final LibraryModel lModel = getLibraryModel(getClass());
        data = new LinkedList<Fixture>();

        // 1 scenario
        final Library eLibrary = createJavaLibrary();
        final LibraryBytes eLibraryBytes = new LibraryBytes();
        eLibraryBytes.setBytes(getJavaLibraryBytes(eLibrary));
        eLibraryBytes.setChecksum(getJavaLibraryChecksum(eLibraryBytes.getBytes()));
        eLibraryBytes.setLibraryId(eLibrary.getId());
        data.add(new Fixture(eLibraryBytes, lModel, eLibrary.getId()));
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        data.clear();
        data = null;

        super.tearDown();
    }

    private class Fixture {
        private final LibraryBytes eLibraryBytes;
        private final Long libraryId;
        private final LibraryModel lModel;
        private Fixture(final LibraryBytes eLibraryBytes, final LibraryModel lModel, final Long libraryId) {
            this.eLibraryBytes = eLibraryBytes;
            this.lModel = lModel;
            this.libraryId = libraryId;
        }
    }
}

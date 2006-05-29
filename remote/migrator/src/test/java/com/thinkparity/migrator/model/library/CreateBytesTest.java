/*
 * Created On: May 9, 2006 2:45:39 PM
 * $Id$
 */
package com.thinkparity.migrator.model.library;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LibraryBytes;
import com.thinkparity.migrator.MigratorTestCase;

/**
 * Test the parity library interface's create bytes api.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class CreateBytesTest extends MigratorTestCase {

    /** Test data. */
    private List<Fixture> data;

    /** Create CreateBytesTest. */
    public CreateBytesTest() {
        super("[RMIGRATOR] [LIBRARY] [CREATE BYTES TEST]");
    }

    /** Test the create bytes api. */
    public void testCreateBytes() {
        LibraryBytes libraryBytes;
        for(final Fixture datum : data) {
            datum.lModel.createBytes(datum.libraryId, datum.eLibraryBytes.getBytes(), datum.eLibraryBytes.getChecksum());

            libraryBytes = datum.lModel.readBytes(datum.libraryId);

            assertNotNull("[RMIGRATOR] [LIBRARY] [CREATE TEST] [LIBRARY BYTES ARE NULL]", libraryBytes);
            assertEquals("[RMIGRATOR] [LIBRARY] [CREATE TEST]", datum.eLibraryBytes, libraryBytes);
        }
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final LibraryModel lModel = getLibraryModel(getClass());
        data = new LinkedList<Fixture>();

        final Library javaLibrary = getJavaLibrary();
        final Library eLibrary = lModel.create(javaLibrary.getArtifactId(),
                javaLibrary.getGroupId(), javaLibrary.getPath(),
                javaLibrary.getType(), javaLibrary.getVersion());
        final LibraryBytes eLibraryBytes = new LibraryBytes();
        eLibraryBytes.setBytes(getJavaLibraryBytes(javaLibrary));
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
        private Fixture(final LibraryBytes eLibraryBytes,
                final LibraryModel lModel, final Long libraryId) {
            this.eLibraryBytes = eLibraryBytes;
            this.lModel = lModel;
            this.libraryId = libraryId;
        }
    }
}

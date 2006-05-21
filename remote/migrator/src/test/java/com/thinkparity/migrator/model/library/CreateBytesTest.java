/*
 * Created On: May 9, 2006 2:45:39 PM
 * $Id$
 */
package com.thinkparity.migrator.model.library;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.migrator.Library;
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
        Byte[] aBytes;
        for(final Fixture datum : data) {
            datum.lModel.createBytes(datum.libraryId, datum.eBytes);

            aBytes = datum.lModel.readBytes(datum.libraryId);

            assertNotNull("[RMIGRATOR] [LIBRARY] [CREATE TEST] [LIBRARY BYTES ARE NULL]", aBytes);
            assertEquals("[RMIGRATOR] [LIBRARY] [CREATE TEST] [LIBRARY BYTES DO NOT EQUAL EXPECTATION]", datum.eBytes.length, aBytes.length);
            for(int i = 0; i < datum.eBytes.length; i++) {
                assertEquals("[RMIGRATOR] [LIBRARY] [CREATE TEST] [LIBRARY BYTES DO NOT EQUAL EXPECTATION]", datum.eBytes[i], aBytes[i]);
            }
        }
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final LibraryModel lModel = getLibraryModel(getClass());
        data = new LinkedList<Fixture>();

        final Library javaLibrary = getJavaLibrary();
        final Library eLibrary = lModel.create(javaLibrary.getArtifactId(),
                javaLibrary.getGroupId(), javaLibrary.getType(),
                javaLibrary.getVersion());
        data.add(new Fixture(getJavaLibraryBytes(), lModel, eLibrary.getId()));
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        data.clear();
        data = null;

        super.tearDown();
    }

    private class Fixture {
        private final Byte[] eBytes;
        private final LibraryModel lModel;
        private final Long libraryId;
        private Fixture(final Byte[] eBytes, final LibraryModel lModel, final Long libraryId) {
            this.eBytes = eBytes;
            this.lModel = lModel;
            this.libraryId = libraryId;
        }
    }
}

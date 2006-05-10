/*
 * Created On: Wed May 10 2006 08:04 PDT
 * $Id$
 */
package com.thinkparity.migrator.model.library;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MigratorTestCase;
import com.thinkparity.migrator.MockLibrary;

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
        Byte[] bytes;
        for(final Fixture datum : data) {
            bytes = datum.lModel.readBytes(datum.libraryId);

            assertNotNull("[RMIGRATOR] [LIBRARY] [READ  BYTES TEST] [LIBRARY BYTES ARE NULL]", bytes);
            assertEquals("[RMIGRATOR] [LIBRARY] [READ BYTES TEST] [LIBRARY BYTES DO NOT EQUAL EXPECTATION]", datum.eBytes.length, bytes.length);
            for(int i = 0; i < datum.eBytes.length; i++) {
                assertEquals("[RMIGRATOR] [LIBRARY] [READ BYTES TEST] [LIBRARY BYTES DO NOT EQUAL EXPECTATION]", datum.eBytes[i], bytes[i]);
            }
        }
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final LibraryModel lModel = getLibraryModel(getClass());
        data = new LinkedList<Fixture>();

        // 1 scenario
        final MockLibrary eLibrary = MockLibrary.create(this);
        lModel.create(eLibrary.getArtifactId(), eLibrary.getGroupId(),
            eLibrary.getType(), eLibrary.getVersion());
        lModel.createBytes(eLibrary.getId(), eLibrary.getBytes());
        final Byte[] eBytes = eLibrary.getBytes();
        data.add(new Fixture(eBytes, lModel, eLibrary.getId()));
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

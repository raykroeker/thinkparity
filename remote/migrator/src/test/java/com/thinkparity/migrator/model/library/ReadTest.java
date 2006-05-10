/*
 * Created On: Tue May 09 2006 11:09 PDT
 * $Id$
 */
package com.thinkparity.migrator.model.library;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MigratorTestCase;
import com.thinkparity.migrator.MockLibrary;

/**
 * Test the parity library interface's read api.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class ReadTest extends MigratorTestCase {

    /** Test data. */
    private List<Fixture> data;

    /** Create ReadTest. */
    public ReadTest() {
        super("[RMIGRATOR] [LIBRARY] [READ TEST]");
    }

    /** Test the read api. */
    public void testRead() {
        Library library;
        for(final Fixture datum : data) {
            library = datum.lModel.read(datum.libraryId);

            assertNotNull("[RMIGRATOR] [LIBRARY] [READ TEST] [LIBRARY IS NULL]", library);
            assertEquals("[RMIGRATOR] [LIBRARY] [READ TEST] [LIBRARY DOES NOT EQUAL EXPECTATION]", datum.eLibrary, library);
        }
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final LibraryModel lModel = getLibraryModel(getClass());
        data = new LinkedList<Fixture>();

        // 2 scenarios
        
        // 1:  java library
        final Library eLibrary0 = MockLibrary.create(this);
        lModel.create(eLibrary0.getArtifactId(), eLibrary0.getGroupId(),
                eLibrary0.getType(), eLibrary0.getVersion());
        data.add(new Fixture(eLibrary0, lModel, eLibrary0.getId()));

        // 2:  native library
        final Library eLibrary1 = MockLibrary.createNative(this);
        lModel.create(eLibrary1.getArtifactId(), eLibrary1.getGroupId(),
                eLibrary1.getType(), eLibrary1.getVersion());
        data.add(new Fixture(eLibrary1, lModel, eLibrary1.getId()));
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        data.clear();
        data = null;

        super.tearDown();
    }

    private class Fixture {
        private final Library eLibrary;
        private final Long libraryId;
        private final LibraryModel lModel;
        private Fixture(final Library eLibrary, final LibraryModel lModel,
                final Long libraryId) {
            this.eLibrary = eLibrary;
            this.lModel = lModel;
            this.libraryId = libraryId;
        }
    }
}

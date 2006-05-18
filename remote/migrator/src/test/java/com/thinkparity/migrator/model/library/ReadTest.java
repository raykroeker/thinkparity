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
            if(null != datum.libraryId) {
                library = datum.lModel.read(datum.libraryId);
            }
            else {
                library = datum.lModel.read(datum.artifactId, datum.groupId,
                        datum.type, datum.version);
            }

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
        data.add(new Fixture(eLibrary0.getArtifactId(), eLibrary0,
                eLibrary0.getGroupId(), lModel, eLibrary0.getType(),
                eLibrary0.getVersion()));

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
        private final String artifactId;
        private final Library eLibrary;
        private final String groupId;
        private final Long libraryId;
        private final LibraryModel lModel;
        private final Library.Type type;
        private final String version;
        private Fixture(final String artifactId, final Library eLibrary,
                final String groupId, final LibraryModel lModel,
                final Library.Type type, final String version) {
            this.artifactId = artifactId;
            this.eLibrary = eLibrary;
            this.groupId = groupId;
            this.lModel = lModel;
            this.type = type;
            this.libraryId = null;
            this.version = version;
        }
        private Fixture(final Library eLibrary, final LibraryModel lModel,
                final Long libraryId) {
            this.artifactId = null;
            this.eLibrary = eLibrary;
            this.groupId = null;
            this.lModel = lModel;
            this.type = null;
            this.libraryId = libraryId;
            this.version = null;
        }
    }
}

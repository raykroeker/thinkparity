/*
 * Created On: May 9, 2006 2:45:39 PM
 * $Id$
 */
package com.thinkparity.migrator.model.library;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MigratorTestCase;
import com.thinkparity.migrator.MockLibrary;

/**
 * Test the parity library interface's create api.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class CreateTest extends MigratorTestCase {

    /** Test data. */
    private List<Fixture> data;

    /** Create CreateTest. */
    public CreateTest() {
        super("[RMIGRATOR] [LIBRARY] [CREATE TEST]");
    }

    /** Test the create api. */
    public void testCreate() {
        Library library;
        for(final Fixture datum : data) {
            testLogger.debug(datum.eLibrary.getId());
            library = datum.lModel.create(datum.artifactId, datum.groupId, datum.type, datum.version);
            testLogger.debug(library.getId());

            assertNotNull("[RMIGRATOR] [LIBRARY] [CREATE TEST] [LIBRARY IS NULL]", library);
            assertEquals("[RMIGRATOR] [LIBRARY] [CREATE TEST] [LIBRARY DOES NOT EQUAL EXPECTATION]", datum.eLibrary, library);
            assertEquals("[RMIGRATOR] [LIBRARY] [CREATE TEST] [LIBRARY ARTIFACT ID DOES NOT EQUAL EXPECTATION]", datum.eLibrary.getArtifactId(), library.getArtifactId());
            assertEquals("[RMIGRATOR] [LIBRARY] [CREATE TEST] [LIBRARY GROUP ID DOES NOT EQUAL EXPECTATION]", datum.eLibrary.getGroupId(), library.getGroupId());
            assertEquals("[RMIGRATOR] [LIBRARY] [CREATE TEST] [LIBRARY TYPE DOES NOT EQUAL EXPECTATION]", datum.eLibrary.getType(), library.getType());
            assertEquals("[RMIGRATOR] [LIBRARY] [CREATE TEST] [LIBRARY VERSION NOT EQUAL EXPECTATION]", datum.eLibrary.getVersion(), library.getVersion());
        }
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final LibraryModel lModel = getLibraryModel(getClass());
        data = new LinkedList<Fixture>();

        // 1 scenario
        final Library eLibrary = MockLibrary.create(this);
        data.add(new Fixture(eLibrary.getArtifactId(), eLibrary,
                eLibrary.getGroupId(), lModel, eLibrary.getType(),
                eLibrary.getVersion()));
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
        private final LibraryModel lModel;
        private final Library.Type type;
        private final String version;
        private Fixture(final String artifactId, final Library eLibrary,
                final String groupId, final LibraryModel lModel,
                final Library.Type type, final String version) {
            this.artifactId = artifactId;
            this.eLibrary= eLibrary;
            this.groupId = groupId;
            this.lModel = lModel;
            this.type = type;
            this.version = version;
        }
    }
}

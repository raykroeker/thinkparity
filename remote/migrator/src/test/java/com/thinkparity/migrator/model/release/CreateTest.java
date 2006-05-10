/*
 * Created On: May 9, 2006 2:45:39 PM
 * $Id$
 */
package com.thinkparity.migrator.model.release;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MigratorTestCase;
import com.thinkparity.migrator.MockRelease;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.model.library.LibraryModel;

/**
 * Test the parity release interface's create api.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class CreateTest extends MigratorTestCase {

    /** Test data. */
    private List<Fixture> data;

    /** Create CreateTest. */
    public CreateTest() {
        super("[RMIGRATOR] [RELEASE] [CREATE TEST]");
    }

    /** Test the create api. */
    public void testCreate() {
        Release release;
        List<Library> libraries;
        for(final Fixture datum : data) {
            release = datum.rModel.create(datum.artifactId, datum.groupId, datum.name, datum.version, datum.libraries);

            assertNotNull("[RMIGRATOR] [RELEASE] [CREATE TEST] [RELEASE IS NULL]", release);
            assertEquals("[RMIGRATOR] [RELEASE] [CREATE TEST] [RELEASE DOES NOT EQUAL EXPECTATION]", datum.eRelease, release);
            assertEquals("[RMIGRATOR] [RELEASE] [CREATE TEST] [RELEASE ARTIFACT ID DOES NOT EQUAL EXPECTATION]", datum.eRelease.getArtifactId(), release.getArtifactId());
            assertEquals("[RMIGRATOR] [RELEASE] [CREATE TEST] [RELEASE GROUP ID DOES NOT EQUAL EXPECTATION]", datum.eRelease.getGroupId(), release.getGroupId());
            assertEquals("[RMIGRATOR] [RELEASE] [CREATE TEST] [RELEASE ID DOES NOT EQUAL EXPECTATION]", datum.eRelease.getId(), release.getId());
            assertEquals("[RMIGRATOR] [RELEASE] [CREATE TEST] [RELEASE VERSION DOES NOT EQUAL EXPECTATION]", datum.eRelease.getVersion(), release.getVersion());
        }
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final LibraryModel lModel = getLibraryModel(getClass());
        final ReleaseModel rModel = getReleaseModel(getClass());
        data = new LinkedList<Fixture>();

        // 1 scenario
        final Release eRelease = MockRelease.create(this);
        for(final Library library : eRelease.getLibraries()) {
            lModel.create(library.getArtifactId(), library.getGroupId(),
                    library.getType(), library.getVersion());
        }
        data.add(new Fixture(eRelease.getArtifactId(), eRelease,
                eRelease.getGroupId(), eRelease.getLibraries(),
                eRelease.getName(), rModel, eRelease.getVersion()));
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        data.clear();
        data = null;

        super.tearDown();
    }

    private class Fixture {
        private final String artifactId;
        private final Release eRelease;
        private final String groupId;
        private final List<Library> libraries;
        private final String name;
        private final ReleaseModel rModel;
        private final String version;
        private Fixture(final String artifactId, final Release eRelease,
                final String groupId, final List<Library> libraries,
                final String name, final ReleaseModel rModel,
                final String version) {
            this.artifactId = artifactId;
            this.eRelease = eRelease;
            this.groupId = groupId;
            this.libraries = libraries;
            this.name = name;
            this.rModel = rModel;
            this.version = version;
        }
    }
}

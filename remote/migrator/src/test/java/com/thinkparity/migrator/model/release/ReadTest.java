/*
 * Created On: Tue May 09 2006 11:09 PDT
 * $Id$
 */
package com.thinkparity.migrator.model.release;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.migrator.MigratorTestCase;
import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MockLibrary;
import com.thinkparity.migrator.MockRelease;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.model.library.LibraryModel;

/**
 * Test the parity release interface's read api.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class ReadTest extends MigratorTestCase {

    /** Test data. */
    private List<Fixture> data;

    /** Create ReadTest. */
    public ReadTest() {
        super("[RMIGRATOR] [RELEASE] [READ TEST]");
    }

    /** Test the read api. */
    public void testRead() {
        Release release;
        for(final Fixture datum : data) {
            release = datum.rModel.read(datum.releaseName);

            assertNotNull("[RMIGRATOR] [RELEASE] [READ TEST] [RELEASE IS NULL]", release);
            assertEquals("[RMIGRATOR] [RELEASE] [READ TEST] [RELEASE DOES NOT EQUAL EXPECTATION]", datum.eRelease, release);
        }
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final LibraryModel lModel = getLibraryModel(getClass());
        final ReleaseModel rModel = getReleaseModel(getClass());
        data = new LinkedList<Fixture>();

        // 1 scenario
        final MockRelease eRelease = MockRelease.create(this);
        for(final MockLibrary library : eRelease.getMockLibraries()) {
            lModel.create(library.getArtifactId(), library.getGroupId(),
                library.getType(), library.getVersion());
            lModel.createBytes(library.getId(), library.getBytes());
        }
        rModel.create(eRelease.getArtifactId(), eRelease.getGroupId(),
                eRelease.getName(), eRelease.getVersion(), eRelease.getLibraries());
        data.add(new Fixture(eRelease, rModel, eRelease.getName()));
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private class Fixture {
        private final Release eRelease;
        private final ReleaseModel rModel;
        private final String releaseName;
        private Fixture(final Release eRelease, final ReleaseModel rModel,
                final String releaseName) {
            this.eRelease = eRelease;
            this.rModel = rModel;
            this.releaseName = releaseName;
        }
    }
}

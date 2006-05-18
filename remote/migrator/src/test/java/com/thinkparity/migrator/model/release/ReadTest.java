/*
 * Created On: Tue May 09 2006 11:09 PDT
 * $Id$
 */
package com.thinkparity.migrator.model.release;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MigratorTestCase;
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
    private Fixture data;

    /** Create ReadTest. */
    public ReadTest() {
        super("[RMIGRATOR] [RELEASE] [READ TEST]");
    }

    /** Test the read api. */
    public void testRead() {
        final Release release = data.rModel.read(data.releaseName);

        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE DOES NOT EQUAL EXPECTATION]", data.eRelease, release);
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE ARTIFACT ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getArtifactId(), release.getArtifactId());
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE GROUP ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getGroupId(), release.getGroupId());
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getId(), release.getId());
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE VERSION DOES NOT EQUAL EXPECTATION]", data.eRelease.getVersion(), release.getVersion());
        for(final Library library : data.eRelease.getLibraries()) {
            assertTrue("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE LIBRARES DO NOT CONTAIN EXPECTATION]", release.getLibraries().contains(library));
        }
        for(final Library library : release.getLibraries()) {
            assertTrue("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE EXPECTATION LIBRARIES DO NOT CONTAIN LIBRARY]", data.eRelease.getLibraries().contains(library));
        }
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final LibraryModel lModel = getLibraryModel(getClass());
        final ReleaseModel rModel = getReleaseModel(getClass());

        // 1 scenario
        final MockRelease eRelease = MockRelease.create(this);
        for(final MockLibrary library : eRelease.getMockLibraries()) {
            lModel.create(library.getArtifactId(), library.getGroupId(),
                library.getType(), library.getVersion());
            lModel.createBytes(library.getId(), library.getBytes());
        }
        rModel.create(eRelease.getArtifactId(), eRelease.getGroupId(),
                eRelease.getName(), eRelease.getVersion(), eRelease.getLibraries());
        data = new Fixture(eRelease, rModel, eRelease.getName());
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        super.tearDown();
        data = null;
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

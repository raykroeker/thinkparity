/*
 * Created On: Tue May 09 2006 11:09 PDT
 * $Id$
 */
package com.thinkparity.migrator.model.release;

import com.thinkparity.migrator.MigratorTestCase;
import com.thinkparity.migrator.Release;

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
        final Release release = data.rModel.read(data.releaseId);

        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE DOES NOT EQUAL EXPECTATION]", data.eRelease, release);
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE ARTIFACT ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getArtifactId(), release.getArtifactId());
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE GROUP ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getGroupId(), release.getGroupId());
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getId(), release.getId());
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE VERSION DOES NOT EQUAL EXPECTATION]", data.eRelease.getVersion(), release.getVersion());
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final Release eRelease = createRelease();
        final ReleaseModel rModel = getReleaseModel(getClass());
        data = new Fixture(eRelease, rModel, eRelease.getId());
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        super.tearDown();
        data = null;
    }

    private class Fixture {
        private final Release eRelease;
        private final ReleaseModel rModel;
        private final Long releaseId;
        private Fixture(final Release eRelease, final ReleaseModel rModel,
                final Long releaseId) {
            this.eRelease = eRelease;
            this.rModel = rModel;
            this.releaseId = releaseId;
        }
    }
}

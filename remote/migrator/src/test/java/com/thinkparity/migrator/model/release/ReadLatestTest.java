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
public class ReadLatestTest extends MigratorTestCase {

    /** Test data. */
    private Fixture data;

    /** Create ReadLatestTest. */
    public ReadLatestTest() {
        super("[RMIGRATOR] [RELEASE] [READ LATEST TEST]");
    }

    /** Test the read api. */
    public void testReadLatest() {
        final Release release = data.rModel.readLatest(data.artifactId, data.groupId);

        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE DOES NOT EQUAL EXPECTATION]", data.eRelease, release);
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE ARTIFACT ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getArtifactId(), release.getArtifactId());
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE GROUP ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getGroupId(), release.getGroupId());
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getId(), release.getId());
        assertEquals("[RMIGRATOR] [RELEASE] [READ LATEST TEST] [RELEASE VERSION DOES NOT EQUAL EXPECTATION]", data.eRelease.getVersion(), release.getVersion());
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        createRelease();
        createRelease();
        final Release eRelease = createRelease();
        data = new Fixture(eRelease.getArtifactId(), eRelease,
                eRelease.getGroupId(), getReleaseModel(getClass()));
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private class Fixture {
        private final String artifactId;
        private final Release eRelease;
        private final String groupId;
        private final ReleaseModel rModel;
        private Fixture(final String artifactId, final Release eRelease,
                final String groupId, final ReleaseModel rModel) {
            this.artifactId = artifactId;
            this.eRelease = eRelease;
            this.groupId = groupId;
            this.rModel = rModel;
        }
    }
}

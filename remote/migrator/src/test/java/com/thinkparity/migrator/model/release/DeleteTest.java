/*
 * Created On: May 9, 2006 2:45:39 PM
 * $Id$
 */
package com.thinkparity.migrator.model.release;

import com.thinkparity.migrator.MigratorTestCase;
import com.thinkparity.migrator.Release;

/**
 * Test the parity release interface's delete api.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class DeleteTest extends MigratorTestCase {

    /** Test data. */
    private Fixture datum;

    /** Create DeleteTest. */
    public DeleteTest() {
        super("[RMIGRATOR] [RELEASE] [DELETE TEST]");
    }

    /** Test the create api. */
    public void testDelete() {
        datum.rModel.delete(datum.releaseId);

        final Release release = datum.rModel.read(datum.releaseId);
        assertNull("[RMIGRATOR] [RELEASE] [DELETE TEST] [RELEASE IS NOT NULL]", release);
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final Release eRelease = createRelease();
        datum = new Fixture(eRelease.getId(), getReleaseModel(getClass()));
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }

    private class Fixture {
        private final Long releaseId;
        private final ReleaseModel rModel;
        private Fixture(final Long releaseId, final ReleaseModel rModel) {
            this.releaseId = releaseId;
            this.rModel = rModel;
        }
    }
}

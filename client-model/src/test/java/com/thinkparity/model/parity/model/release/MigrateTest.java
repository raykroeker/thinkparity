/*
 * May 19, 2006 1:52:56 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.ModelTestCase;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class MigrateTest extends ModelTestCase {

    /** Test data. */
    private Fixture datum;

    /** Create MigrateTest. */
    public MigrateTest() { super("[RMODEL] [TEST] [MODEL] [RELEASE] [MIGRATE]"); }

    /** Test the release interface's download api. */
    public void testMigrate() {
        try { datum.rModel.migrate(datum.artifactId, datum.groupId, datum.version); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
    }

    /** @see com.thinkparity.model.parity.model.ModelTestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final InternalReleaseModel irModel = getInternalReleaseModel();
        irModel.download("lBrowser", "com.thinkparity.parity");

        datum = new Fixture("lBrowser", "com.thinkparity.parity", irModel, "1.0.0.1000");
    }

    /** @see com.thinkparity.model.parity.model.ModelTestCase#tearDown() */
    protected void tearDown() throws Exception {
        super.tearDown();
        datum = null;
    }

    private class Fixture {
        private final String artifactId;
        private final String groupId;
        private final ReleaseModel rModel;
        private final String version;
        private Fixture(final String artifactId, final String groupId,
                final ReleaseModel rModel, final String version) {
            this.artifactId = artifactId;
            this.groupId = groupId;
            this.rModel = rModel;
            this.version = version;
        }
    }
}

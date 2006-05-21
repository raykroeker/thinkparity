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
public class DownloadTest extends ModelTestCase {

    /** Test data. */
    private Fixture datum;

    /** Create DownloadTest. */
    public DownloadTest() { super("[RMODEL] [TEST] [MODEL] [RELEASE] [DOWNLOAD]"); }

    /** Test the release interface's download api. */
    public void testDownload() {
        try { datum.rModel.download(datum.artifactId, datum.groupId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
    }

    /** @see com.thinkparity.model.parity.model.ModelTestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture("tRelease", "com.thinkparity.parity",
                getInternalReleaseModel());
    }

    /** @see com.thinkparity.model.parity.model.ModelTestCase#tearDown() */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private class Fixture {
        private final String artifactId;
        private final String groupId;
        private final ReleaseModel rModel;
        private Fixture(final String artifactId, final String groupId,
                final ReleaseModel rModel) {
            this.artifactId = artifactId;
            this.groupId = groupId;
            this.rModel = rModel;
        }
    }
}

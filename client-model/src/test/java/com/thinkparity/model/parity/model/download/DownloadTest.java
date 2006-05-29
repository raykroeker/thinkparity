/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.model.parity.model.download;

import com.thinkparity.model.Constants;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.ModelTestCase;

import com.thinkparity.migrator.Release;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class DownloadTest extends ModelTestCase {

    /** Create DownloadTest. */
    public DownloadTest() { super("[LBROWSER BOOTSTRAP] [MODEL] [DOWNLOAD] [DOWNLOAD TEST]"); }

    public void testDownload() {
        try { datum.dModel.download(datum.release); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
    }

    /** @see com.thinkparity.browser.bootstrap.model.ModelTestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final Release release = getInternalReleaseModel().readLatest(
                Constants.Release.ARTIFACT_ID, Constants.Release.GROUP_ID);
        datum = new Fixture(DownloadModel.getModel(), release);
    }

    /** @see com.thinkparity.browser.bootstrap.model.ModelTestCase#tearDown() */
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }

    /** Test data. */
    private Fixture datum;

    /** Test data definition. */
    private class Fixture {
        private final Release release;
        private final DownloadModel dModel;
        private Fixture(final DownloadModel dModel, final Release release) {
            this.release = release;
            this.dModel = dModel;
        }
    }
}

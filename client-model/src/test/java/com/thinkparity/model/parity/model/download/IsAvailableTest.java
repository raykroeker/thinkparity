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
public class IsAvailableTest extends ModelTestCase {

    /** Test data. */
    private Fixture datum;

    /** Create IsAvailableTest. */
    public IsAvailableTest() { super(""); }

    /** Test the is available api. */
    public void testIsComplete() {
        try { datum.dModel.isComplete(datum.release); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
    }

    /** @see com.thinkparity.browser.bootstrap.model.ModelTestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final Release release = getInternalReleaseModel().readLatest(
                Constants.Release.ARTIFACT_ID,
                Constants.Release.GROUP_ID);
        datum = new Fixture(DownloadModel.getModel(), release);
    }

    /** @see com.thinkparity.browser.bootstrap.model.ModelTestCase#tearDown() */
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture {
        private final DownloadModel dModel;
        private final Release release;
        private Fixture(final DownloadModel dModel, final Release release) {
            this.dModel = dModel;
            this.release = release;
        }
    }
}

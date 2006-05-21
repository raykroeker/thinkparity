/*
 * Created On: Fri May 19 2006 09:57 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.ControllerTestCase;

/**
 * A test for the library delete controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class DeleteTest extends ControllerTestCase {

    /** The test data. */
    private Fixture datum;

    /** Create DeleteTest. */
    public DeleteTest() {
        super("[RMIGRATOR] [CONTROLLER] [RELEASE] [DELETE TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        IQ response = null;
        try { response = datum.delete.handleIQ(datum.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [DELETE TEST] [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        final Release eRelease = createRelease();

        final IQ iq = createGetIQ();
        writeLong(iq, Xml.Release.ID, eRelease.getId());
        datum = new Fixture(new Delete(), iq);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { datum = null; }

    /** The test data fixture. */
    private class Fixture {
        private final Delete delete;
        private final IQ iq;
        private Fixture(final Delete delete, final IQ iq) {
            this.delete = delete;
            this.iq = iq;
        }
    }
}

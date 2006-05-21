/*
 * Created On: Fri May 19 2006 10:03 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Constants;
import com.thinkparity.migrator.Library;
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
        super("[RMIGRATOR] [CONTROLLER] [LIBRARY] [DELETE TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        try { datum.delete.handleIQ(datum.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        final Library eLibrary = createJavaLibrary();
        final IQ iq = createGetIQ();
        writeLong(iq, Constants.Xml.Library.ID, eLibrary.getId());
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

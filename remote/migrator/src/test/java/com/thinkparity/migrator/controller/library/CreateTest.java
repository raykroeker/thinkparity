/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Constants;
import com.thinkparity.migrator.MockLibrary;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.controller.MockIQ;

/**
 * A test for the library create controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class CreateTest extends ControllerTestCase {

    /** The test data. */
    private Fixture data;

    /** Create CreateTest. */
    public CreateTest() {
        super("[RMIGRATOR] [CONTROLLER] [LIBRARY] [CREATE TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        try { data.create.handleIQ(data.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        final MockLibrary mockLibrary = MockLibrary.create(this);
        final MockIQ mockIQ = MockIQ.createGet();
        mockIQ.writeString(Constants.Xml.Library.ARTIFACT_ID, mockLibrary.getArtifactId());
        mockIQ.writeString(Constants.Xml.Library.GROUP_ID, mockLibrary.getGroupId());
        mockIQ.writeLibraryType(Constants.Xml.Library.TYPE, mockLibrary.getType());
        mockIQ.writeString(Constants.Xml.Library.VERSION, mockLibrary.getVersion());
        data = new Fixture(new Create(), mockIQ);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final Create create;
        private final IQ iq;
        private Fixture(final Create create, final IQ iq) {
            this.create = create;
            this.iq = iq;
        }
    }
}

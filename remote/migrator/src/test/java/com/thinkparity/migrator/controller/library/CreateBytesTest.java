/*
 * Created On: Thu May 11 2006 09:26 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.MockLibrary;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.controller.MockIQ;
import com.thinkparity.migrator.model.library.LibraryModel;

/**
 * A test for the library create bytes controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class CreateBytesTest extends ControllerTestCase {

    /** The test data. */
    private Fixture data;

    /** Create CreateBytesTest. */
    public CreateBytesTest() {
        super("[RMIGRATOR] [CONTROLLER] [LIBRARY] [CREATE BYTES TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        try { data.createBytes.handleIQ(data.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        final LibraryModel lModel = getLibraryModel(getClass());
        
        final MockLibrary mockLibrary = MockLibrary.create(this);
        lModel.create(mockLibrary.getArtifactId(), mockLibrary.getGroupId(),
                mockLibrary.getType(), mockLibrary.getVersion());

        final MockIQ mockIQ = MockIQ.createGet();
        mockIQ.writeLong("libraryId", mockLibrary.getId());
        mockIQ.writeByteArray("bytes", mockLibrary.getBytes());
        data = new Fixture(new CreateBytes(), mockIQ);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final CreateBytes createBytes;
        private final IQ iq;
        private Fixture(final CreateBytes createBytes, final IQ iq) {
            this.createBytes = createBytes;
            this.iq = iq;
        }
    }
}

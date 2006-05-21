/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Constants;
import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.controller.ControllerTestCase;

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
        final Library eLibrary = createJavaLibrary();
        final IQ iq = createGetIQ();
        writeString(iq, Constants.Xml.Library.ARTIFACT_ID, eLibrary.getArtifactId());
        writeString(iq, Constants.Xml.Library.GROUP_ID, eLibrary.getGroupId());
        writeLibraryType(iq, Constants.Xml.Library.TYPE, eLibrary.getType());
        writeString(iq, Constants.Xml.Library.VERSION, eLibrary.getVersion());
        data = new Fixture(new Create(), iq);
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

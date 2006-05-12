/*
 * Created On: Thu May 11 2006 10:09 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MockLibrary;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.controller.MockIQ;
import com.thinkparity.migrator.model.library.LibraryModel;
import com.thinkparity.migrator.util.IQReader;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

/**
 * A test for the library read controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadTest extends ControllerTestCase {

    /** The test data. */
    private Fixture data;

    /** Create ReadTest. */
    public ReadTest() {
        super("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        IQ response = null;
        try { response = data.read.handleIQ(data.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final Library library = new Library();
        library.setArtifactId(iqReader.readString("artifactId"));
        library.setGroupId(iqReader.readString("groupId"));
        library.setId(iqReader.readLong("id"));
        library.setType(iqReader.readLibraryType("type"));
        library.setVersion(iqReader.readString("version"));

        assertNotNull("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY IS NULL]", library);
        assertEquals("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY DOES NOT MATCH EXPECTATION]", library.getArtifactId(), data.eLibrary.getArtifactId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY GROUP ID DOES NOT MATCH EXPECTATION]", library.getGroupId(), data.eLibrary.getGroupId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY ID DOES NOT MATCH EXPECTATION]", library.getId(), data.eLibrary.getId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY TYPE DOES NOT MATCH EXPECTATION]", library.getType(), data.eLibrary.getType());
        assertEquals("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY VERSION DOES NOT MATCH EXPECTATION]", library.getVersion(), data.eLibrary.getVersion());
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        final LibraryModel lModel = getLibraryModel(getClass());

        final MockLibrary mockLibrary = MockLibrary.create(this);
        lModel.create(mockLibrary.getArtifactId(), mockLibrary.getGroupId(),
                mockLibrary.getType(), mockLibrary.getVersion());

        final MockIQ mockIQ = MockIQ.createGet();
        mockIQ.writeLong("libraryId", mockLibrary.getId());
        data = new Fixture(mockLibrary, new Read(), mockIQ);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final Library eLibrary;
        private final Read read;
        private final IQ iq;
        private Fixture(final Library eLibrary, final Read read, final IQ iq) {
            this.eLibrary = eLibrary;
            this.read = read;
            this.iq = iq;
        }
    }
}

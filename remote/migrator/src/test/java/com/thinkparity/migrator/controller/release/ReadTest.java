/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import java.util.List;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MockLibrary;
import com.thinkparity.migrator.MockRelease;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.controller.MockIQ;
import com.thinkparity.migrator.model.library.LibraryModel;
import com.thinkparity.migrator.model.release.ReleaseModel;
import com.thinkparity.migrator.util.IQReader;

/**
 * A test for the library create controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadTest extends ControllerTestCase {

    /** The test data. */
    private Fixture data;

    /** Create CreateTest. */
    public ReadTest() {
        super("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        IQ response = null;
        try { response = data.read.handleIQ(data.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ TEST] [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final Release release = new Release();
        release.setArtifactId(iqReader.readString("artifactId"));
        release.setGroupId(iqReader.readString("groupId"));
        release.setId(iqReader.readLong("id"));
        release.setName(iqReader.readString("name"));
        release.setVersion(iqReader.readString("version"));
        final List<Long> libraryIds = iqReader.readLongs("libraryIds", "libraryId");
        for(final Long libraryId : libraryIds) {
            release.addLibrary(data.lModel.read(libraryId));
        }

        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE DOES NOT EQUAL EXPECTATION]", data.eRelease, release);
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE ARTIFACT ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getArtifactId(), release.getArtifactId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE GROUP ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getGroupId(), release.getGroupId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getId(), release.getId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE VERSION DOES NOT EQUAL EXPECTATION]", data.eRelease.getVersion(), release.getVersion());
        for(final Library library : data.eRelease.getLibraries()) {
            assertTrue("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE LIBRARES DO NOT CONTAIN EXPECTATION]", release.getLibraries().contains(library));
        }
        for(final Library library : release.getLibraries()) {
            assertTrue("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE EXPECTATION LIBRARIES DO NOT CONTAIN LIBRARY]", data.eRelease.getLibraries().contains(library));
        }
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        final LibraryModel lModel = getLibraryModel(getClass());
        final ReleaseModel rModel = getReleaseModel(getClass());

        final MockRelease mockRelease = MockRelease.create(this);

        for(final MockLibrary mockLibrary : mockRelease.getMockLibraries()) {
            lModel.create(mockLibrary.getArtifactId(), mockLibrary.getGroupId(),
                    mockLibrary.getType(), mockLibrary.getVersion());
            lModel.createBytes(mockLibrary.getId(), mockLibrary.getBytes());
        }
        rModel.create(mockRelease.getArtifactId(), mockRelease.getGroupId(),
                mockRelease.getName(), mockRelease.getVersion(),
                mockRelease.getLibraries());


        final MockIQ mockIQ = MockIQ.createGet();
        mockIQ.writeString("releaseName", mockRelease.getName());
        data = new Fixture(new Read(), lModel, mockRelease, mockIQ);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final Read read;
        private final Release eRelease;
        private final LibraryModel lModel;
        private final IQ iq;
        private Fixture(final Read read, final LibraryModel lModel,
                final Release eRelease, final IQ iq) {
            this.read = read;
            this.eRelease = eRelease;
            this.lModel = lModel;
            this.iq = iq;
        }
    }
}

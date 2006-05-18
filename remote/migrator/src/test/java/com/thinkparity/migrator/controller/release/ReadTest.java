/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MockLibrary;
import com.thinkparity.migrator.MockRelease;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.Constants.Xml;
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
    private Map<String, Fixture> data;

    /** Create CreateTest. */
    public ReadTest() {
        super("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        final Fixture datum = data.get("testHandleIQ");

        IQ response = null;
        try { response = datum.read.handleIQ(datum.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ TEST] [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final Release release = new Release();
        release.setArtifactId(iqReader.readString(Xml.Release.ARTIFACT_ID));
        release.setGroupId(iqReader.readString(Xml.Release.GROUP_ID));
        release.setId(iqReader.readLong(Xml.Release.ID));
        release.addAllLibraries(iqReader.readLibraries(Xml.Release.LIBRARIES, Xml.Release.LIBRARY));
        release.setName(iqReader.readString(Xml.Release.NAME));
        release.setVersion(iqReader.readString(Xml.Release.VERSION));

        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE DOES NOT EQUAL EXPECTATION]", datum.eRelease, release);
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE ARTIFACT ID DOES NOT EQUAL EXPECTATION]", datum.eRelease.getArtifactId(), release.getArtifactId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE GROUP ID DOES NOT EQUAL EXPECTATION]", datum.eRelease.getGroupId(), release.getGroupId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE ID DOES NOT EQUAL EXPECTATION]", datum.eRelease.getId(), release.getId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE VERSION DOES NOT EQUAL EXPECTATION]", datum.eRelease.getVersion(), release.getVersion());
        for(final Library library : datum.eRelease.getLibraries()) {
            assertTrue("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE LIBRARES DO NOT CONTAIN EXPECTATION]", release.getLibraries().contains(library));
        }
        for(final Library library : release.getLibraries()) {
            assertTrue("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE EXPECTATION LIBRARIES DO NOT CONTAIN LIBRARY]", datum.eRelease.getLibraries().contains(library));
        }
    }
   
    /** Test the handle iq api when using a release name that does not exist. */
    public void testHandleIQNullResult() {
        final Fixture datum = data.get("testHandleIQNullResult");

        IQ response = null;
        try { response = datum.read.handleIQ(datum.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ TEST] [TEST NULL RESULT] [RESPONSE NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        assertNull(iqReader.readString(Xml.Release.ARTIFACT_ID));
        assertNull(iqReader.readString(Xml.Release.GROUP_ID));
        assertNull(iqReader.readLong(Xml.Release.ID));
        assertNull(iqReader.readLibraries(Xml.Release.LIBRARIES, Xml.Release.LIBRARY));
        assertNull(iqReader.readString(Xml.Release.NAME));
        assertNull(iqReader.readString(Xml.Release.VERSION));
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

        // 2 scenarios
        data = new HashMap<String, Fixture>(2, 1.0F);

        final MockIQ mockIQ = MockIQ.createGet();
        mockIQ.writeString(Xml.Release.NAME, mockRelease.getName());
        // 0:  the positive use-case
        data.put("testHandleIQ", new Fixture(new Read(), mockRelease, mockIQ));
        // 1:  no release is expected; no name is provided in the query
        data.put("testHandleIQNullResult", new Fixture(new Read(), null, MockIQ.createGet()));
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final Read read;
        private final Release eRelease;
        private final IQ iq;
        private Fixture(final Read read, final Release eRelease, final IQ iq) {
            this.read = read;
            this.eRelease = eRelease;
            this.iq = iq;
        }
    }
}

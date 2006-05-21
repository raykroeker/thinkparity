/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.ControllerTestCase;
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
        release.setVersion(iqReader.readString(Xml.Release.VERSION));

        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE DOES NOT EQUAL EXPECTATION]", datum.eRelease, release);
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE ARTIFACT ID DOES NOT EQUAL EXPECTATION]", datum.eRelease.getArtifactId(), release.getArtifactId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE GROUP ID DOES NOT EQUAL EXPECTATION]", datum.eRelease.getGroupId(), release.getGroupId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE ID DOES NOT EQUAL EXPECTATION]", datum.eRelease.getId(), release.getId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE VERSION DOES NOT EQUAL EXPECTATION]", datum.eRelease.getVersion(), release.getVersion());
    }
   
    /** Set up the test data. */
    protected void setUp() throws Exception {
        data = new HashMap<String, Fixture>(2, 1.0F);
        final Release eRelease = createRelease();

        final IQ iq = createGetIQ();
        writeLong(iq, Xml.Release.ID, eRelease.getId());
        data.put("testHandleIQ", new Fixture(new Read(), eRelease, iq));
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

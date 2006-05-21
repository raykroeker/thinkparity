/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.util.IQReader;

/**
 * A test for the library read latest controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadLatestTest extends ControllerTestCase {

    /** The test data. */
    private Fixture data;

    /** Create ReadLatestTest. */
    public ReadLatestTest() {
        super("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LATEST TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        IQ response = null;
        try { response = data.readLatest.handleIQ(data.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LATEST TEST] [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final Release release = new Release();
        release.setArtifactId(iqReader.readString(Xml.Release.ARTIFACT_ID));
        release.setGroupId(iqReader.readString(Xml.Release.GROUP_ID));
        release.setId(iqReader.readLong(Xml.Release.ID));
        release.setVersion(iqReader.readString(Xml.Release.VERSION));

        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LATEST TEST] [RELEASE DOES NOT EQUAL EXPECTATION]", data.eRelease, release);
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LATEST TEST] [RELEASE ARTIFACT ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getArtifactId(), release.getArtifactId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LATEST TEST] [RELEASE GROUP ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getGroupId(), release.getGroupId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LATEST TEST] [RELEASE ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getId(), release.getId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LATEST TEST] [RELEASE VERSION DOES NOT EQUAL EXPECTATION]", data.eRelease.getVersion(), release.getVersion());
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        createRelease();
        createRelease();
        final Release eRelease = createRelease();

        final IQ iq = createGetIQ();
        writeString(iq, Xml.Release.ARTIFACT_ID, eRelease.getArtifactId());
        writeString(iq, Xml.Release.GROUP_ID, eRelease.getGroupId());
        data = new Fixture(new ReadLatest(), eRelease, iq);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final Release eRelease;
        private final IQ iq;
        private final ReadLatest readLatest;
        private Fixture(final ReadLatest readLatest, final Release eRelease,
                final IQ iq) {
            this.readLatest = readLatest;
            this.eRelease = eRelease;
            this.iq = iq;
        }
    }
}

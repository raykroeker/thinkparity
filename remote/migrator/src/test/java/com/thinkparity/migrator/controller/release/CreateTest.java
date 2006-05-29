/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
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
public class CreateTest extends ControllerTestCase {

    /** The test data. */
    private Fixture data;

    /** Create CreateTest. */
    public CreateTest() {
        super("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        IQ response = null;
        try { response = data.create.handleIQ(data.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final Release release = new Release();
        release.setArtifactId(iqReader.readString(Xml.Release.ARTIFACT_ID));
        release.setCreatedOn(iqReader.readCalendar(Xml.Release.CREATED_ON));
        release.setGroupId(iqReader.readString(Xml.Release.GROUP_ID));
        release.setId(iqReader.readLong(Xml.Release.ID));
        release.setVersion(iqReader.readString(Xml.Release.VERSION));

        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST]", release);
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE ARTIFACT ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getArtifactId(), release.getArtifactId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE GROUP ID DOES NOT EQUAL EXPECTATION]", data.eRelease.getGroupId(), release.getGroupId());
        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE ID IS NULL]", release.getId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [CREATE TEST] [RELEASE VERSION DOES NOT EQUAL EXPECTATION]", data.eRelease.getVersion(), release.getVersion());
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        final Release eRelease = getRelease();
        final List<Library> libraries = new LinkedList<Library>();
        libraries.add(createJavaLibrary());
        libraries.add(createNativeLibrary());

        final IQ iq = createGetIQ();
        writeString(iq, "artifactId", eRelease.getArtifactId());
        writeString(iq, "groupId", eRelease.getGroupId());
        writeString(iq, "version", eRelease.getVersion());
        writeLongs(iq, "libraryIds", "libraryId", extractIds(libraries));
        data = new Fixture(new Create(), eRelease, iq);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final Create create;
        private final Release eRelease;
        private final IQ iq;
        private Fixture(final Create create, final Release eRelease, final IQ iq) {
            this.create = create;
            this.eRelease = eRelease;
            this.iq = iq;
        }
    }
}

/*
 * Created On: Thu May 11 2006 10:09 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.util.IQReader;

/**
 * A test for the library read controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadTest extends ControllerTestCase {

    /** The test data. */
    private Map<String, Fixture> data;

    /** Create ReadTest. */
    public ReadTest() {
        super("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQScenario1() { testHandleIQ(data.get("Scenario1")); }

    /** Test the handle IQ api. */
    public void testHandleIQScenario2() { testHandleIQ(data.get("Scenario2")); }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        data = new HashMap<String, Fixture>(2, 1.0F);
        final Library eLibrary = createJavaLibrary();

        IQ iq = createGetIQ();
        writeLong(iq, Xml.Library.ID, eLibrary.getId());
        data.put("Scenario1", new Fixture(eLibrary, new Read(), iq));

        iq = createGetIQ();
        writeString(iq, Xml.Library.ARTIFACT_ID, eLibrary.getArtifactId());
        writeString(iq, Xml.Library.GROUP_ID, eLibrary.getGroupId());
        writeLibraryType(iq, Xml.Library.TYPE, eLibrary.getType());
        writeString(iq, Xml.Library.VERSION, eLibrary.getVersion());
        data.put("Scenario2", new Fixture(eLibrary, new Read(), iq));
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception {
        data.clear();
        data = null;
    }

    private void testHandleIQ(final Fixture datum) {
        IQ response = null;
        try { response = datum.read.handleIQ(datum.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final Library library = new Library();
        library.setArtifactId(iqReader.readString(Xml.Library.ARTIFACT_ID));
        library.setGroupId(iqReader.readString(Xml.Library.GROUP_ID));
        library.setId(iqReader.readLong(Xml.Library.ID));
        library.setType(iqReader.readLibraryType(Xml.Library.TYPE));
        library.setVersion(iqReader.readString(Xml.Library.VERSION));

        assertNotNull("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY IS NULL]", library);
        assertEquals("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY DOES NOT MATCH EXPECTATION]", library.getArtifactId(), datum.eLibrary.getArtifactId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY GROUP ID DOES NOT MATCH EXPECTATION]", library.getGroupId(), datum.eLibrary.getGroupId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY ID DOES NOT MATCH EXPECTATION]", library.getId(), datum.eLibrary.getId());
        assertEquals("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY TYPE DOES NOT MATCH EXPECTATION]", library.getType(), datum.eLibrary.getType());
        assertEquals("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ TEST] [LIBRARY VERSION DOES NOT MATCH EXPECTATION]", library.getVersion(), datum.eLibrary.getVersion());
    }

    /** The test data fixture. */
    private class Fixture {
        private final Library eLibrary;
        private final IQ iq;
        private final Read read;
        private Fixture(final Library eLibrary, final Read read, final IQ iq) {
            this.eLibrary = eLibrary;
            this.read = read;
            this.iq = iq;
        }
    }
}

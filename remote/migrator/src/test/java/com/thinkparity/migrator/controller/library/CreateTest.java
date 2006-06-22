/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.xmpp.IQReader;

/**
 * A test for the library create controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class CreateTest extends ControllerTestCase {

    /** The test name. */
    private static final String NAME = "[RMIGRATOR] [CONTROLLER] [LIBRARY] [CREATE TEST]";

    /** The test data. */
    private Fixture data;

    /** Create CreateTest. */
    public CreateTest() { super(NAME); }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        IQ response = null;
        try { response = data.create.handleIQ(data.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull(NAME + " [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final Library library = new Library();
        library.setArtifactId(iqReader.readString(Xml.Library.ARTIFACT_ID));
        library.setCreatedOn(iqReader.readCalendar(Xml.Library.CREATED_ON));
        library.setGroupId(iqReader.readString(Xml.Library.GROUP_ID));
        library.setId(iqReader.readLong(Xml.Library.ID));
        library.setPath(iqReader.readString(Xml.Library.PATH));
        library.setType(iqReader.readLibraryType(Xml.Library.TYPE));
        library.setVersion(iqReader.readString(Xml.Library.VERSION));

        assertNotNull(NAME, library);
        assertEquals(NAME + " [LIBRARY ARTIFACT ID DOES NOT MATCH EXPECTATION]", data.eLibrary.getArtifactId(), library.getArtifactId());
        assertEquals(NAME + " [LIBRARY GROUP ID DOES NOT MATCH EXPECTATION]", data.eLibrary.getGroupId(), library.getGroupId());
        assertEquals(NAME + " [LIBRARY PATH DOES NOT MATCH EXPECTATION]", data.eLibrary.getPath(), library.getPath());
        assertEquals(NAME + " [LIBRARY TYPE DOES NOT MATCH EXPECTATION]", data.eLibrary.getType(), library.getType());
        assertEquals(NAME + " [LIBRARY VERSION DOES NOT MATCH EXPECTATION]", data.eLibrary.getVersion(), library.getVersion());
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        final Library eLibrary = getJavaLibrary();
        final IQ iq = createGetIQ();
        writeString(iq, Xml.Library.ARTIFACT_ID, eLibrary.getArtifactId());
        writeString(iq, Xml.Library.GROUP_ID, eLibrary.getGroupId());
        writeString(iq, Xml.Library.PATH, eLibrary.getPath());
        writeLibraryType(iq, Xml.Library.TYPE, eLibrary.getType());
        writeString(iq, Xml.Library.VERSION, eLibrary.getVersion());
        data = new Fixture(new Create(), eLibrary, iq);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final Create create;
        private final Library eLibrary;
        private final IQ iq;
        private Fixture(final Create create, final Library eLibrary, final IQ iq) {
            this.create = create;
            this.eLibrary = eLibrary;
            this.iq = iq;
        }
    }
}

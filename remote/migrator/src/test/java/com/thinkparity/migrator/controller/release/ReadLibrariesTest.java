/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.model.release.ReleaseModel;
import com.thinkparity.migrator.xmpp.IQReader;

/**
 * A test for the library read libraries controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadLibrariesTest extends ControllerTestCase {

    /** The test name. */
    private static final String NAME = "[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LIBRARIES TEST]";

    /** The test data. */
    private Fixture datum;

    /** Create ReadLibrariesTest. */
    public ReadLibrariesTest() { super(NAME); }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        IQ response = null;
        try { response = datum.readLibraries.handleIQ(datum.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LIBRARIES TEST] [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final List<Library> libraries = iqReader.readLibraries("libraries", "library");

        final String assertion = "[RMIGRATOR] [RELEASE] [READ LIBRARIES TEST] [EXPECTED LIBRARIES DOES NOT CONTAIN LIBRARY] [{0}]";
        for(final Library library : libraries) {
            assertNotNull(NAME, library);
            assertTrue(MessageFormat.format(assertion, new Object[] {library}), datum.eLibraries.contains(library));
        }
        for(final Library library : datum.eLibraries) {
            assertTrue(MessageFormat.format(assertion, new Object[] {library}), libraries.contains(library));
        }
    }
   
    /** Set up the test data. */
    protected void setUp() throws Exception {
        super.setUp();
        final ReleaseModel rModel = getReleaseModel(getClass());

        final List<Library> eLibraries = new LinkedList<Library>();
        eLibraries.add(createJavaLibrary());
        eLibraries.add(createNativeLibrary());

        final Release eRelease = getRelease();
        eRelease.setId(rModel.create(eRelease.getArtifactId(),
                eRelease.getGroupId(), eRelease.getVersion(), eLibraries)
                .getId());

        final IQ iq = createGetIQ();
        writeLong(iq, Xml.Release.ID, eRelease.getId());
        datum = new Fixture(new ReadLibraries(), eLibraries, iq);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { datum = null; }

    /** The test data fixture. */
    private class Fixture {
        private final ReadLibraries readLibraries;
        private final List<Library> eLibraries;
        private final IQ iq;
        private Fixture(final ReadLibraries readLibraries, final List<Library> eLibraries, final IQ iq) {
            this.readLibraries = readLibraries;
            this.eLibraries = eLibraries;
            this.iq = iq;
        }
    }
}

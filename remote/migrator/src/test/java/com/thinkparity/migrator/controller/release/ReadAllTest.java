/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.release;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.model.release.ReleaseModel;
import com.thinkparity.migrator.xmpp.IQReader;

/**
 * A test for the library read all controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadAllTest extends ControllerTestCase {

    /** The test name. */
    private static final String NAME = "[RMIGRATOR] [CONTROLLER] [RELEASE] [READ ALL TEST]";

    /** The test data. */
    private Map<String, Fixture> data;

    /** Create ReadAllTest. */
    public ReadAllTest() { super(NAME); }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        final Fixture datum = data.get("testHandleIQ");

        IQ response = null;
        try { response = datum.readAll.handleIQ(datum.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull(NAME + " [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final List<Release> releases = iqReader.readReleases("releases", "release");
        
        final String assertion = NAME + " [EXPECTED RELEASES DOES NOT CONTAIN RELEASE] [{0}]";
        for(final Release release : datum.eReleases) {
            assertNotNull(NAME, release);
            assertTrue(MessageFormat.format(assertion, new Object[] {release}), releases.contains(release));
        }
    }
   
    /** Set up the test data. */
    protected void setUp() throws Exception {
        data = new HashMap<String, Fixture>(2, 1.0F);
        final ReleaseModel rModel = getReleaseModel(getClass());

        final List<Library> eLibraries = new LinkedList<Library>();
        eLibraries.add(createJavaLibrary());
        eLibraries.add(createNativeLibrary());

        final Release eRelease = getRelease();
        eRelease.setId(rModel.create(eRelease.getArtifactId(),
                eRelease.getGroupId(), eRelease.getVersion(), eLibraries)
                .getId());

        final IQ iq = createGetIQ();
        data.put("testHandleIQ", new Fixture(new ReadAll(), rModel.readAll(), iq));
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final List<Release> eReleases;
        private final IQ iq;
        private final ReadAll readAll;
        private Fixture(final ReadAll readAll, final List<Release> eReleases,
                final IQ iq) {
            this.readAll = readAll;
            this.eReleases = eReleases;
            this.iq = iq;
        }
    }
}

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
import com.thinkparity.migrator.xmpp.IQReader;

/**
 * A test for the library read latest controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadLatestTest extends ControllerTestCase {

    /** The test name. */
    private static final String NAME = "[RMIGRATOR] [CONTROLLER] [RELEASE] [READ LATEST TEST]";

    /** The test datum. */
    private Fixture datum;

    /** Create ReadLatestTest. */
    public ReadLatestTest() { super(NAME); }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        IQ response = null;
        try { response = datum.readLatest.handleIQ(datum.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull(NAME + " [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final Release release = new Release();
        release.setArtifactId(iqReader.readString(Xml.Release.ARTIFACT_ID));
        release.setCreatedOn(iqReader.readCalendar(Xml.Release.CREATED_ON));
        release.setGroupId(iqReader.readString(Xml.Release.GROUP_ID));
        release.setId(iqReader.readLong(Xml.Release.ID));
        release.setVersion(iqReader.readString(Xml.Release.VERSION));

        assertNotNull(NAME, release);
        assertEquals(NAME, datum.eRelease, release);
    }

    /** Set up the test datum. */
    protected void setUp() throws Exception {
        createRelease();
        createRelease();
        final Release eRelease = createRelease();

        final IQ iq = createGetIQ();
        writeString(iq, Xml.Release.ARTIFACT_ID, eRelease.getArtifactId());
        writeString(iq, Xml.Release.GROUP_ID, eRelease.getGroupId());
        datum = new Fixture(new ReadLatest(), eRelease, iq);
    }

    /** Tear down the test datum. */
    protected void tearDown() throws Exception { datum = null; }

    /** The test datum fixture. */
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

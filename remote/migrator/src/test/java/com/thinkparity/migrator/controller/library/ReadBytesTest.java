/*
 * Created On: Thu May 11 2006 09:26 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.util.IQReader;

/**
 * A test for the library create bytes controller's handle iq api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadBytesTest extends ControllerTestCase {

    /** The test data. */
    private Fixture data;

    /** Create ReadBytesTest. */
    public ReadBytesTest() {
        super("[RMIGRATOR] [CONTROLLER] [LIBRARY] [READ BYTES TEST]");
    }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        IQ response = null;
        try { response = data.readBytes.handleIQ(data.iq); }
        catch(final UnauthorizedException ux) { fail(createFailMessage(ux)); }

        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ TEST] [RESPONSE IS NULL]", response);
        if(didFail(response)) { fail(createFailMessage(response)); }

        final IQReader iqReader = new IQReader(response);
        final Byte[] bytes = iqReader.readByteArray(Xml.Library.BYTES);
        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ TEST] [BYTES ARE NULL]", bytes);
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ TEST] [BYTE LENGTH IS NOT EQUAL TO EXPECTATION]", data.eBytes.length, bytes.length);
        for(int i = 0; i < bytes.length; i++) {
            assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ TEST] [BYTE NOT EQUAL TO EXPECTATION] [" + i + "]", data.eBytes[i], bytes[i]);
        }
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        super.setUp();
        final Library eLibrary = createJavaLibrary();
        final Byte[] eLibraryBytes = getJavaLibraryBytes();
        final IQ iq = createGetIQ();
        writeLong(iq, Xml.Library.ID, eLibrary.getId());
        data = new Fixture(eLibraryBytes, new ReadBytes(), iq);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final Byte[] eBytes;
        private final ReadBytes readBytes;
        private final IQ iq;
        private Fixture(final Byte[] eBytes, final ReadBytes readBytes, final IQ iq) {
            this.eBytes = eBytes;
            this.readBytes = readBytes;
            this.iq = iq;
        }
    }
}

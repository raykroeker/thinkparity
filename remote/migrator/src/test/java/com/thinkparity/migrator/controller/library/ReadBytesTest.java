/*
 * Created On: Thu May 11 2006 09:26 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller.library;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LibraryBytes;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.controller.ControllerTestCase;
import com.thinkparity.migrator.xmpp.IQReader;

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
        final LibraryBytes libraryBytes = new LibraryBytes();
        libraryBytes.setBytes(iqReader.readByteArray(Xml.Library.BYTES));
        libraryBytes.setChecksum(iqReader.readString(Xml.Library.CHECKSUM));
        libraryBytes.setLibraryId(iqReader.readLong(Xml.Library.ID));
        assertNotNull("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ TEST] [BYTES ARE NULL]", libraryBytes);
        assertEquals("[RMIGRATOR] [CONTROLLER] [RELEASE] [READ TEST]", data.eLibraryBytes, libraryBytes);
    }

    /** Set up the test data. */
    protected void setUp() throws Exception {
        super.setUp();
        final Library eLibrary = createJavaLibrary();
        final IQ iq = createGetIQ();
        writeLong(iq, Xml.Library.ID, eLibrary.getId());

        final LibraryBytes eLibraryBytes = new LibraryBytes();
        eLibraryBytes.setBytes(getJavaLibraryBytes(eLibrary));
        eLibraryBytes.setChecksum(getJavaLibraryChecksum(eLibraryBytes.getBytes()));
        eLibraryBytes.setLibraryId(eLibrary.getId());
        data = new Fixture(eLibraryBytes, new ReadBytes(), iq);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { data = null; }

    /** The test data fixture. */
    private class Fixture {
        private final LibraryBytes eLibraryBytes;
        private final IQ iq;
        private final ReadBytes readBytes;
        private Fixture(final LibraryBytes eLibraryBytes, final ReadBytes readBytes, final IQ iq) {
            this.eLibraryBytes = eLibraryBytes;
            this.readBytes = readBytes;
            this.iq = iq;
        }
    }
}

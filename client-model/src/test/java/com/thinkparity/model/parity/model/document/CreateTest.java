/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Vector;

import com.thinkparity.codebase.StreamUtil;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.DocumentEvent;
import com.thinkparity.model.parity.api.events.DocumentListener;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.util.MD5Util;

/**
 * Test the document model create api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class CreateTest extends DocumentTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [DOCUMENT] [CREATE TEST]";

    /** Test data. */
	private Vector<Fixture> data;

	/** Create a CreateTest. */
	public CreateTest() { super(NAME); }

	/**
	 * Test the document model create api.
	 * 
	 */
	public void testCreate() {
		Document document;
		DocumentVersion latestVersion;
		Collection<DocumentVersion> versions;
		for(final Fixture datum : data) {
            document = null;
            try {
                datum.documentModel.addListener(datum);
                document = datum.documentModel.create(datum.containerId, datum.name, datum.inputStream);
            }
            catch(final ParityException px) { fail(createFailMessage(px)); }
            finally {
                datum.documentModel.removeListener(datum);
                try { datum.inputStream.close(); }
                catch(final IOException iox) { fail(createFailMessage(iox)); }
            }
			assertNotNull(document);

            latestVersion = null;
            try { latestVersion = datum.documentModel.readLatestVersion(document.getId()); }
            catch(final ParityException px) { fail(createFailMessage(px)); }
			assertNotNull(NAME, latestVersion);
			assertEquals(latestVersion.getChecksum(), datum.documentContentChecksum);

			versions = null;
            try { versions = datum.documentModel.listVersions(document.getId()); }
            catch(final ParityException px) { fail(createFailMessage(px)); }
			assertNotNull(NAME, versions);
			assertEquals(
                    NAME + " [NUMBER OF VERSIONS DOES NOT MATCH EXPECTATION]",
                    datum.expectedVersionsSize, versions.size());
            assertTrue(NAME + " [DOCUMENT CREATION EVENT NOT FIRED]", datum.didNotify);
		}
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final DocumentModel documentModel = getDocumentModel();
		data = new Vector<Fixture>(4);
		String name;
		String documentContentChecksum;

        final Container container = getContainerModel().create(NAME);
        InputStream fis;
		for(File testFile : getInputFiles()) {
			name = testFile.getName();
            fis = new FileInputStream(testFile);
            try {
                documentContentChecksum = MD5Util.md5Hex(StreamUtil.read(fis));
            }
            finally { fis.close(); }

            fis = new FileInputStream(testFile);
            data.add(new Fixture(container.getId(), documentContentChecksum, documentModel, 1, fis, name));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	/**
	 * Test data fixture.
	 * 
	 * @see CreateTest#setUp()
	 * @see CreateTest#tearDown()
	 */
	private class Fixture implements DocumentListener {
        private final Long containerId;
        private Boolean didNotify;
		private final String documentContentChecksum;
		private final DocumentModel documentModel;
		private final int expectedVersionsSize;
		private final InputStream inputStream;
		private final String name;
		private Fixture(final Long containerId,
                final String documentContentChecksum,
                final DocumentModel documentModel,
                final int expectedVersionsSize, final InputStream inputStream,
                final String name) {
            this.containerId = containerId;
            this.didNotify = Boolean.FALSE;
			this.documentContentChecksum = documentContentChecksum;
			this.documentModel = documentModel;
			this.expectedVersionsSize = expectedVersionsSize;
			this.inputStream = inputStream;
			this.name = name;
		}
        public void confirmationReceived(final DocumentEvent e) {
            fail(NAME + " [CONFIRMATION RECEIVED EVENT FIRED]");
        }
        public void documentArchived(final DocumentEvent e) {
            fail(NAME + " [DOCUMENT ARCHIVED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentClosed(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentClosed(final DocumentEvent e) {
            fail(NAME + " [DOCUMENT CLOSED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentCreated(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentCreated(final DocumentEvent e) {
            didNotify = Boolean.TRUE;
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentDeleted(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentDeleted(final DocumentEvent e) {
            fail(NAME + " [DOCUMENT DELETED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentPublished(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentPublished(final DocumentEvent e) {
            fail(NAME + " [DOCUMENT PUBLISHED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentReactivated(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentReactivated(final DocumentEvent e) {
            fail(NAME + " [DOCUMENT REACTIVATED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentUpdated(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentUpdated(final DocumentEvent e) {
            fail(NAME + " [DOCUMENT UPDATED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#keyRequestAccepted(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void keyRequestAccepted(final DocumentEvent e) {
            fail(NAME + " [KEY REQUEST ACCEPTED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#keyRequestDeclined(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void keyRequestDeclined(final DocumentEvent e) {
            fail(NAME + " [KEY REQUEST DECLINED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#keyRequested(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void keyRequested(final DocumentEvent e) {
            fail(NAME + " [KEY REQUESTED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#teamMemberAdded(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void teamMemberAdded(final DocumentEvent e) {
            fail(NAME + " [TEAM MEMBER ADDED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#teamMemberRemoved(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void teamMemberRemoved(final DocumentEvent e) {
            fail(NAME + " [TEAM MEMBER REMOVED EVENT FIRED]");
        }

	}
}

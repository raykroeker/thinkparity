/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.model.document.Document;

/**
 * <b>Title:</b>thinkParity Container Add Document Test<br>
 * <b>Description:</b>thinkParity Container Add Document Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class AddDocumentTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [ADD DOCUMENT TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create AddDocumentTest. */
    public AddDocumentTest() { super(NAME); }

    /**
     * Test the container model's add document api.
     *
     */
    public void testAddDocument() {
        try { datum.cModel.addDocument(datum.containerId, datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        List<Document> documents = null;
        try { documents = datum.cModel.readDocuments(datum.containerId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        assertNotNull(NAME, documents);
        assertEquals(NAME + " [DOCUMENT'S SIZE DOES NOT MATCH EXPECTATION]", 1, documents.size());
        assertEquals(NAME, datum.eDocument, documents.get(0));
        assertTrue(NAME + " [ADD DOCUMENT EVENT NOT FIRED]", datum.didNotify);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();

        final ContainerModel cModel = getContainerModel();
        final Container eContainer = cModel.create(NAME);
        final Document eDocument = create(getInputFiles()[0]);
        datum = new Fixture(cModel, eContainer.getId(), eDocument.getId(), eContainer, eDocument);

        cModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        getContainerModel().removeListener(datum);
        logout();
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture implements ContainerListener {
        private final ContainerModel cModel;
        private final Long containerId;
        private Boolean didNotify;
        private final Long documentId;
        private final Container eContainer;
        private final Document eDocument;
        private Fixture(final ContainerModel cModel, final Long containerId,
                final Long documentId, final Container eContainer,
                final Document eDocument) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.didNotify = Boolean.FALSE;
            this.documentId = documentId;
            this.eContainer = eContainer;
            this.eDocument = eDocument;
        }
        public void containerClosed(final ContainerEvent e) {
            fail(NAME + " [CONTAINER CLOSED EVENT WAS FIRED]");
        }
        public void containerCreated(final ContainerEvent e) {
            fail(NAME + " [CONTAINER CREATED EVENT WAS FIRED]");
        }
        public void containerDeleted(final ContainerEvent e) {
            fail(NAME + " [CONTAINER DELETED EVENT WAS FIRED]");
        }
        public void containerReactivated(final ContainerEvent e) {
            fail(NAME + " [CONTAINER REACTIVATED EVENT WAS FIRED]");
        }
        public void documentAdded(final ContainerEvent e) {
            datum.didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT IS REMOTE]", !e.isRemote());
            assertEquals(NAME + " [EVENT CONTAINER DOES NOT MATCH EXPECTATION]", datum.eContainer, e.getContainer());
            assertEquals(NAME + " [EVENT DOCUMENT DOES NOT MATCH EXPECTATION]", datum.eDocument, e.getDocument());
            assertNull(NAME + " [EVENT USER IS NOT NULL]", e.getUser());
        }
        public void documentRemoved(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT REMOVED EVENT WAS FIRED]");
        }
    }
}

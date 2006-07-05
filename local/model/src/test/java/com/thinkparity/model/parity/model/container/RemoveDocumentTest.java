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
 * <b>Title:</b>thinkParity Container Remove Document Test<br>
 * <b>Description:</b>thinkParity Container Remove Document Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class RemoveDocumentTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [REMOVE DOCUMENT TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create RemoveDocumentTest. */
    public RemoveDocumentTest() { super(NAME); }

    /**
     * Test the container model's add document api.
     *
     */
    public void testRemoveDocument() {
        try { datum.cModel.removeDocument(datum.containerId, datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        List<Document> documents = null;
        try { documents = datum.cModel.readDocuments(datum.containerId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        assertNotNull(NAME, documents);
        assertEquals(NAME + " [DOCUMENT'S SIZE DOES NOT MATCH EXPECTATION]", 0, documents.size());
        assertTrue(NAME + " [DOCUMENT REMOVED EVENT DID NOT FIRE]", datum.didNotify);
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
        cModel.addDocument(eContainer.getId(), eDocument.getId());
        datum = new Fixture(cModel, eContainer.getId(), eDocument.getId());

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
        private Fixture(final ContainerModel cModel, final Long containerId,
                final Long documentId) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.didNotify = Boolean.FALSE;
            this.documentId = documentId;
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
            fail(NAME + " [DOCUMENT ADDED EVENT WAS FIRED]");
        }
        public void documentRemoved(final ContainerEvent e) {
            datum.didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT IS REMOTE]", !e.isRemote());
            assertNotNull(NAME + " [EVENT CONTAINER IS NULL]", e.getContainer());
            assertNotNull(NAME + " [EVENT DOCUMENT IS NULL]", e.getDocument());
            assertNull(NAME + " [EVENT USER IS NOT NULL]", e.getUser());
        }
    }
}

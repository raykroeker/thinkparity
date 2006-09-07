/*
 * Created On: Jun 27, 2006 4:00:45 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.List;

import com.thinkparity.model.container.Container;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.model.document.Document;

/**
 * <b>Title:</b>thinkParity Container Add Document Test<br>
 * <b>Description:</b>thinkParity Container Add Document Test
 * 
 * @author raymond@thinkparity.com
 * @version 
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
        datum.containerModel.addDocument(datum.container.getId(), datum.document.getId());

        assertTrue(NAME + " [DOCUMENT ADDED EVENT NOT FIRED]", datum.didNotify);
        final Container container = datum.containerModel.read(datum.container.getId());
        assertNotNull(NAME + " [CONTAINER IS NULL]", container);
        final ContainerDraft draft = datum.containerModel.readDraft(datum.container.getId());
        assertNotNull(NAME + " [CONTAINER DRAFT IS NULL]", draft);
        final List<Document> documents = draft.getDocuments();
        assertNotNull(NAME + " [CONTAINER DRAFT DOCUMENTS ARE NULL]", documents);
        assertTrue(NAME + " [DRAFT DOCUMENTS DOES NOT CONTAIN ADDED DOCUMENT]", documents.contains(datum.document));
        assertEquals(NAME + " [DRAFT DOCUMENT STATE DOES NOT MATCH EXPECTATION]",
                ContainerDraft.ArtifactState.ADDED,
                draft.getState(datum.document));
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final ContainerModel containerModel = getContainerModel();
        final Container container = createContainer(NAME);
        final Document document = createDocument(getInputFiles()[0]);
        datum = new Fixture(container, containerModel, document);
        containerModel.addListener(datum);
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
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final ContainerModel containerModel;
        private Boolean didNotify;
        private final Document document;
        private Fixture(final Container container,
                final ContainerModel containerModel, final Document document) {
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
            this.document = document;
        }
        @Override
        public void documentAdded(final ContainerEvent e) {
            datum.didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT IS REMOTE]", !e.isRemote());
            assertEquals(NAME + " [EVENT CONTAINER DOES NOT MATCH EXPECTATION]", datum.container, e.getContainer());
            assertEquals(NAME + " [EVENT DOCUMENT DOES NOT MATCH EXPECTATION]", datum.document, e.getDocument());
            assertNotNull(NAME + " [EVENT CONTAINER DRAFT IS NULL]", e.getDraft());
        }
    }
}

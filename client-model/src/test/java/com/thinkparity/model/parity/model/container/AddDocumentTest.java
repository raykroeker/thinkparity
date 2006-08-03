/*
 * Created On: Jun 27, 2006 4:00:45 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.List;

import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;
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
        final ContainerDraft draft = container.getDraft();
        assertNotNull(NAME + " [CONTAINER DRAFT IS NULL]", draft);
        final List<Document> documents = draft.getDocuments();
        assertNotNull(NAME + " [CONTAINER DRAFT DOCUMENTS ARE NULL]", documents);
        assertTrue(NAME + " [DRAFT DOCUMENTS DOES NOT CONTAIN ADDED DOCUMENT]", documents.contains(datum.document));
        assertEquals(NAME + " [DRAFT DOCUMENT STATE DOES NOT MATCH EXPECTATION]",
                ContainerDraftArtifactState.ADDED,
                draft.getArtifactState(datum.document.getId()));

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
        createContainerDraft(container.getId());
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
    private class Fixture implements ContainerListener {
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
            assertEquals(NAME + " [EVENT CONTAINER DOES NOT MATCH EXPECTATION]", datum.container, e.getContainer());
            assertEquals(NAME + " [EVENT DOCUMENT DOES NOT MATCH EXPECTATION]", datum.document, e.getDocument());
            assertNotNull(NAME + " [EVENT CONTAINER DRAFT IS NULL]", e.getDraft());
            assertNull(NAME + " [EVENT USER IS NOT NULL]", e.getUser());
        }
        public void documentRemoved(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT REMOVED EVENT WAS FIRED]");
        }
        public void draftCreated(ContainerEvent e) {
            fail(NAME + " [DRAFT CREATED EVENT FIRED]");
        }
        public void teamMemberAdded(ContainerEvent e) {
            fail(NAME + " [TEAM MEMBER ADDED EVENT FIRED]");
        }
        public void teamMemberRemoved(ContainerEvent e) {
            fail(NAME + " [TEAM MEMBER REMOVED EVENT FIRED]");
        }
    }
}

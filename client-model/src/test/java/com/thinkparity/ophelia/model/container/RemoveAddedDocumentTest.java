/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.document.Document;
import com.thinkparity.ophelia.model.events.ContainerEvent;

/**
 * <b>Title:</b>thinkParity Container Remove Document Test<br>
 * <b>Description:</b>thinkParity Container Remove Document Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class RemoveAddedDocumentTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "REMOVE ADDED DOCUMENT TEST";

    /** Test datum. */
    private Fixture datum;

    /** Create RemoveDocumentTest. */
    public RemoveAddedDocumentTest() { super(NAME); }

    /**
     * Test the container model's add document api.
     *
     */
    public void testRemoveDocument() {
        datum.containerModel.removeDocument(datum.container.getId(), datum.document.getId());
        assertTrue(NAME + " [REMOVE DOCUMENT EVENT NOT FIRED]", datum.didNotify);

        final ContainerDraft draft = datum.containerModel.readDraft(datum.container.getId());
        final List<Document> documents = draft.getDocuments();
        assertNotNull(NAME + " [CONTAINER DRAFT DOCUMENTS ARE NULL]", documents);
        assertTrue(NAME + " [DRAFT DOCUMENTS CONTAINS REMOVED DOCUMENT]", !documents.contains(datum.document));
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        final ContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = containerModel.create(NAME);
        final Document document = create(OpheliaTestUser.JUNIT, getInputFiles()[0]);
        containerModel.addDocument(container.getId(), document.getId());
        datum = new Fixture(container, containerModel, document);
        containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        getContainerModel(OpheliaTestUser.JUNIT).removeListener(datum);
        logout(OpheliaTestUser.JUNIT);
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
            this.container= container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
            this.document = document;
        }
        @Override
        public void documentRemoved(final ContainerEvent e) {
            datum.didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT IS REMOTE]", !e.isRemote());
            assertEquals(NAME + " [EVENT CONTAINER DOES NOT MATCH EXPECTATION]", datum.container, e.getContainer());
            assertEquals(NAME + " [EVENT DOCUMENT DOES NOT MATCH EXPECTATION]", datum.document, e.getDocument());
            assertNotNull(NAME + " [EVENT DOCUMENT IS NULL]", e.getDraft());
            assertNull(NAME + " [EVENT USER IS NOT NULL]", e.getTeamMember());
        }
    }
}

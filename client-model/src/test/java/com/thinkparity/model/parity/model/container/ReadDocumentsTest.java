/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.model.document.Document;

/**
 * <b>Title:</b>thinkParity Container Read Test<br>
 * <b>Description:</b>thinkParity Container Read Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadDocumentsTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [READ DOCUMENTS TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create ReadDocumentsTest. */
    public ReadDocumentsTest() { super(NAME); }

    /**
     * Test the container model's read api.
     *
     */
    public void testReadDocuments() {
        List<Document> documents = null;
        try { documents = datum.cModel.readDocuments(datum.containerId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        assertNotNull(NAME, documents);
        assertEquals(NAME + " [DOCUMENT'S SIZE DOES NOT MATCH EXPECTATION]", datum.eDocuments.size(), documents.size());
        for(final Document eDocument : datum.eDocuments) {
            assertTrue(NAME + " [EXPECTED DOCUMENT LIST DOES NOT CONTAIN ACTUAL DOCUMENT]", documents.contains(eDocument));
        }
        for(final Document document : documents) {
            assertTrue(NAME + " [ACTUAL DOCUMENT LIST DOES NOT CONTAIN EXPECTATION]", datum.eDocuments.contains(document));
        }
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
        final List<Document> eDocuments = new LinkedList<Document>();
        eDocuments.addAll(cModel.readDocuments(eContainer.getId()));
        final Document document = create(getInputFiles()[0]);
        eDocuments.add(document);
        cModel.addDocument(eContainer.getId(), document.getId());
        datum = new Fixture(cModel, eContainer.getId(), eDocuments);

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
        private final List<Document> eDocuments;
        private Fixture(final ContainerModel cModel, final Long containerId,
                final List<Document> eDocuments) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.eDocuments = eDocuments;
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
            fail(NAME + " [DOCUMENT REMOVED EVENT WAS FIRED]");
        }
    }
}

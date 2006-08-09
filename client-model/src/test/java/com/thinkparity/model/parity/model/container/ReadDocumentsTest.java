/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.LinkedList;
import java.util.List;

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
        final List<Document> documents = datum.cModel.readDocuments(datum.containerId, datum.versionId);

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
        final ContainerVersion version = cModel.readLatestVersion(eContainer.getId());
        final List<Document> eDocuments = new LinkedList<Document>();
        eDocuments.addAll(cModel.readDocuments(eContainer.getId(), version.getVersionId()));

        datum = new Fixture(cModel, eContainer.getId(), eDocuments, version.getVersionId());

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
    private class Fixture extends ContainerTestCase.Fixture {
        private final ContainerModel cModel;
        private final Long containerId;
        private final List<Document> eDocuments;
        private final Long versionId;
        private Fixture(final ContainerModel cModel, final Long containerId,
                final List<Document> eDocuments, final Long versionId) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.eDocuments = eDocuments;
            this.versionId = versionId;
        }
    }
}

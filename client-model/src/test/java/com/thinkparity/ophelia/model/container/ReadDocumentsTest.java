/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.Document;

/**
 * <b>Title:</b>thinkParity Container Read Test<br>
 * <b>Description:</b>thinkParity Container Read Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadDocumentsTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[READ DOCUMENTS TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create ReadDocumentsTest. */
    public ReadDocumentsTest() { super(NAME); }

    /** Test the container model's read documents api. */
    public void testReadDocuments() {
        final List<Document> documents =
            datum.containerModel.readDocuments(datum.container.getId(),
                    datum.version.getVersionId());

        assertNotNull(NAME, documents);
        assertEquals(NAME + " [DOCUMENT'S SIZE DOES NOT MATCH EXPECTATION]", datum.documents.size(), documents.size());
        for(final Document document : datum.documents) {
            assertTrue(NAME + " [EXPECTED DOCUMENT LIST DOES NOT CONTAIN ACTUAL DOCUMENT]", documents.contains(document));
        }
        for(final Document document : documents) {
            assertTrue(NAME + " [ACTUAL DOCUMENT LIST DOES NOT CONTAIN EXPECTATION]", datum.documents.contains(document));
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final ContainerModel containerModel = getContainerModel();
        final Container container = createContainer(NAME);
        final List<Document> documents = addDocuments(container);
        publish(container);
        final ContainerVersion version = containerModel.readLatestVersion(container.getId());
        datum = new Fixture(container, containerModel, documents, version);
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        logout();
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final ContainerModel containerModel;
        private final List<Document> documents;
        private final ContainerVersion version;
        private Fixture(final Container container, final ContainerModel containerModel, 
                final List<Document> documents, final ContainerVersion version) {
            this.container = container;
            this.containerModel = containerModel;
            this.documents = documents;
            this.version = version;
        }
    }
}

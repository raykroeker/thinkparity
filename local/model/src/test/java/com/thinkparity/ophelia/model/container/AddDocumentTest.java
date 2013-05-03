/*
 * Created On: Jun 27, 2006 4:00:45 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Add Document Test<br>
 * <b>Description:</b>thinkParity Container Add Document Test
 * 
 * @author raymond@thinkparity.com
 * @version 
 */
public class AddDocumentTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "Add document test.";

    /** Test datum. */
    private Fixture datum;

    /** Create AddDocumentTest. */
    public AddDocumentTest() { super(NAME); }

    /**
     * Test the container model's add document api.
     *
     */
    public void testAddDocument() {
        final Container c = createContainer(datum.junit, NAME);
        final Document d = createDocument(datum.junit, "JUnitTestFramework.doc");
        getContainerModel(datum.junit).addDocument(c.getId(), d.getId());

        final Container container = readContainer(datum.junit, c.getId());
        assertNotNull(NAME + " [CONTAINER IS NULL]", container);
        final ContainerDraft draft = readContainerDraft(datum.junit, c.getId());
        assertNotNull(NAME + " [CONTAINER DRAFT IS NULL]", draft);
        final List<Document> documents = draft.getDocuments();
        assertNotNull(NAME + " [CONTAINER DRAFT DOCUMENTS ARE NULL]", documents);
        assertTrue(NAME + " [DRAFT DOCUMENTS DOES NOT CONTAIN ADDED DOCUMENT]", documents.contains(d));
        assertEquals(NAME + " [DRAFT DOCUMENT STATE DOES NOT MATCH EXPECTATION]",
                ContainerDraft.ArtifactState.ADDED,
                draft.getState(d));
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X,
                OpheliaTestUser.JUNIT_Y);
        login(datum.junit);
        login(datum.junit_x);
        login(datum.junit_y);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    /** Test datumn fixture. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
    }
}

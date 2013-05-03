/*
 * Created On: Aug 23, 2006 11:36:42 AM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadDraftTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "Read draft test.";

    /** Test datum. */
    private Fixture datum;

    /** Create ReadDraftTest. */
    public ReadDraftTest() { super(NAME); }

    /** Test the read draft api. */
    public void testReadDraft() {
        final Container c = createContainer(datum.junit, NAME);
        final Document d = addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        final ContainerDraft c_d = getContainerModel(datum.junit).readDraft(c.getId());
        assertNotNull(NAME + " DRAFT IS NULL", c_d);
        assertEquals("Draft container id does not match expectation.", c.getId(), c_d.getContainerId());
        assertEquals("Draft owner does not match expectation.", datum.junit.getId(), c_d.getOwner().getId());
        assertTrue("Draft does not contain document.", 1 == c_d.getDocumentCount());
        final Document c_d_d = c_d.getDocument(d.getId());
        assertEquals("Draft document does not match expectation.", d, c_d_d);
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

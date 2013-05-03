/*
 * Created On: Jun 27, 2006 4:00:45 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Add Document Test<br>
 * <b>Description:</b>thinkParity Container Add Document Test
 * 
 * @author raymond@thinkparity.com
 * @version 
 */
public class AddDocumentsTest extends ContainerTestCase {

    /** A default count of the number of documents to add. */
    private static final Integer COUNT = 1024;

    /** Test test name. */
    private static final String NAME = "Add documents test.";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create AddDocumentsTest.
     *
     */
    public AddDocumentsTest() {
        super(NAME);
    }

    /**
     * Test the container model's add document api.
     *
     */
    public void testAddDocuments() {
        logger.logApiId();
        logger.logInfo("Adding {0} documents.", COUNT);
        testAddDocuments(COUNT);
    }

    /**
     * Add a number of documents.
     * 
     * @param count
     *            The number of documents to add.
     */
    private void testAddDocuments(final Integer count) {
        final Container c = createContainer(datum.junit, getName());
        for (int i = 0; i < count.intValue(); i++) {
            logger.logVariable("i", i);
            addDocument(datum.junit, c.getId(), getSequenceFile(128, i));
        }
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

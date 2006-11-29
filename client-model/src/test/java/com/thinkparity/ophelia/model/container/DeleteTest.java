/*
 * Created On: Jun 28, 2006 8:29:43 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.4.5
 */
public final class DeleteTest extends ContainerTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "Delete Test";

    /** Test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create DeleteTest.
     * 
     */
    public DeleteTest() { super(NAME); }

    /**
     * Test the delete api.
     * 
     */
    public void testDelete() {
        final Container c = createContainer(datum.junit, NAME);
        getContainerModel(datum.junit).addListener(datum);
        logger.logInfo("Deleting container \"{0}\" as \"{1}.\"", c.getName(),
                datum.junit_x.getSimpleUsername());
        getContainerModel(datum.junit).delete(c.getId());
        getContainerModel(datum.junit).removeListener(datum);

        final Container cRead = getContainerModel(datum.junit).read(c.getId());
        assertNull("Container \"" + c.getName() + "\" was not deleted.",
                cRead);
        assertTrue("Container deleted event was not fired for container \""
                + c.getName() + ".\"", datum.didNotify);
    }

    /**
     * Test the delete api after adding a document.
     *
     */
    public void testDeletePostAdd() {
        final Container c = createContainer(datum.junit, NAME);
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        getContainerModel(datum.junit).addListener(datum);
        logger.logInfo("Deleting container \"{0}\" as \"{1}.\"", c.getName(),
                datum.junit_x.getSimpleUsername());
        getContainerModel(datum.junit).delete(c.getId());
        getContainerModel(datum.junit).removeListener(datum);

        final Container cRead = getContainerModel(datum.junit).read(c.getId());
        assertNull("Container \"" + c.getName() + "\" was not deleted.",
                cRead);
        assertTrue("Container deleted event was not fired for container \""
                + c.getName() + ".\"", datum.didNotify);
    }

    /**
     * Test the delete api after adding a document.
     *
     */
    public void testDeletePostPublish() {
        final Container c = createContainer(datum.junit, NAME);
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        publishToContacts(datum.junit, c.getId(), "JUnit.X thinkParity",
                "JUnit.Y thinkParity");
        datum.waitForEvents();
        getContainerModel(datum.junit).addListener(datum);
        logger.logInfo("Deleting container \"{0}\" as \"{1}.\"", c.getName(),
                datum.junit.getSimpleUsername());
        getContainerModel(datum.junit).delete(c.getId());
        getContainerModel(datum.junit).removeListener(datum);
        datum.waitForEvents();

        final Container cRead = getContainerModel(datum.junit).read(c.getId());
        assertNull("Container \"" + c.getName() + "\" was not deleted.",
                cRead);
        assertTrue("Container deleted event was not fired for container \""
                + c.getName() + ".\"", datum.didNotify);
    }

    /**
     * Test the delete api after receiving a container.
     *
     */
    public void testDeletePostPublishAsRecipient() {
        final Container c = createContainer(datum.junit, NAME);
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        publishToContacts(datum.junit, c.getId(), "JUnit.X thinkParity",
                "JUnit.Y thinkParity");
        datum.waitForEvents();
        final Container c_x = readContainer(datum.junit_x, c.getUniqueId());
        getContainerModel(datum.junit_x).addListener(datum);
        logger.logInfo("Deleting container \"{0}\" as \"{1}.\"", c_x.getName(),
                datum.junit_x.getSimpleUsername());
        getContainerModel(datum.junit_x).delete(c_x.getId());
        getContainerModel(datum.junit_x).removeListener(datum);
        datum.waitForEvents();

        final Container cRead = getContainerModel(datum.junit_x).read(c_x.getId());
        assertNull("Container \"" + c_x.getName() + "\" was not deleted.",
                cRead);
        assertTrue("Container deleted event was not fired for container \""
                + c_x.getName() + ".\"", datum.didNotify);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
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
     * 
     */
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private Boolean didNotify;
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            this.didNotify = Boolean.FALSE;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
        @Override
        public void containerDeleted(ContainerEvent e) {
            didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT GENERATED IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT GENERATED IS REMOTE]", !e.isRemote());
            assertNotNull(NAME + " EVENT CONTAINER IS NULL", e.getContainer());
            assertNull(NAME + " [EVENT DOCUMENT IS NOT NULL]", e.getDocument());
            assertNull(NAME + " [EVENT DRAFT IS NOT NULL]", e.getDraft());
            assertNull(NAME + " [EVENT TEAM MEMBER IS NOT NULL]", e.getTeamMember());
            assertNull(NAME + " [EVENT VERSION NOT NULL]", e.getVersion());
        }
    }
}

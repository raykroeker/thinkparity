/*
 * Created On:  13-Dec-06 7:37:52 AM
 */
package com.thinkparity.ophelia.model.container;


import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class IsDistributedTest extends ContainerTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "Is Distributed Test";

    private Fixture datum;

    /**
     * Create IsDistributedTest.
     *
     * @param name
     */
    public IsDistributedTest() {
        super(NAME);
    }

    /**
     * Test the is distributed api.
     *
     */
    public void testIsDistributed() {
        final Container c = createContainer(datum.junit, NAME);
        assertFalse("Container is distributed post creation.", getContainerModel(datum.junit).isDistributed(c.getId()));
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        assertFalse("Container is distributed post document addition.", getContainerModel(datum.junit).isDistributed(c.getId()));
        publishToContacts(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        assertTrue("Container is not distributed post publish.", getContainerModel(datum.junit).isDistributed(c.getId()));
        assertTrue("Container is not distributed post publish.", getContainerModel(datum.junit_x).isDistributed(c.getId()));
        assertTrue("Container is not distributed post publish.", getContainerModel(datum.junit_y).isDistributed(c.getId()));

        final Container c2 = createContainer(datum.junit, NAME + " 2");
        assertFalse("Container " + c2.getName() + " is distributed " + datum.junit.getSimpleUsername() + ".", getContainerModel(datum.junit).isDistributed(c2.getId()));
        addDocument(datum.junit, c2.getId(), "JUnitTestFramework.doc");
        assertFalse("Container " + c2.getName() + " is distributed for user " + datum.junit.getSimpleUsername() + ".", getContainerModel(datum.junit).isDistributed(c2.getId()));
        publishToContacts(datum.junit, c2.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        assertTrue("Container " + c2.getName() + " is not distributed for user " + datum.junit.getSimpleUsername() + ".", getContainerModel(datum.junit).isDistributed(c2.getId()));
        assertTrue("Container " + c2.getName() + " is not distributed for user " + datum.junit_x.getSimpleUsername() + ".", getContainerModel(datum.junit_x).isDistributed(c2.getId()));
        createDraft(datum.junit, c2.getId());
        datum.waitForEvents();
        assertTrue("Container " + c2.getName() + " is not distributed for user " + datum.junit.getSimpleUsername() + ".", getContainerModel(datum.junit).isDistributed(c2.getId()));
        assertTrue("Container " + c2.getName() + " is not distributed for user " + datum.junit_x.getSimpleUsername() + ".", getContainerModel(datum.junit_x).isDistributed(c2.getId()));
        modifyDocuments(datum.junit, c2);
        assertTrue("Container " + c2.getName() + " is not distributed for user " + datum.junit.getSimpleUsername() + ".", getContainerModel(datum.junit).isDistributed(c2.getId()));
        assertTrue("Container " + c2.getName() + " is not distributed for user " + datum.junit_x.getSimpleUsername() + ".", getContainerModel(datum.junit_x).isDistributed(c2.getId()));
        publishToContacts(datum.junit, c2.getId(), Boolean.TRUE, "JUnit.Y thinkParity");
        assertTrue("Container " + c2.getName() + " is not distributed for user " + datum.junit.getSimpleUsername() + ".", getContainerModel(datum.junit).isDistributed(c2.getId()));
        assertTrue("Container " + c2.getName() + " is not distributed for user " + datum.junit_x.getSimpleUsername() + ".", getContainerModel(datum.junit_x).isDistributed(c2.getId()));
        assertTrue("Container " + c2.getName() + " is not distributed post publish for user " + datum.junit_y.getSimpleUsername() + ".", getContainerModel(datum.junit_y).isDistributed(c2.getId()));
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.datum = new Fixture(OpheliaTestUser.JUNIT,
                OpheliaTestUser.JUNIT_X, OpheliaTestUser.JUNIT_Y);
        login(datum.junit);
        login(datum.junit_x);
        login(datum.junit_y);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    private final class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            super();
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
    }
}

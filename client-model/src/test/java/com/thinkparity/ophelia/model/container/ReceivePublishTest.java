/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReceivePublishTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "Receive publish test.";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create ReceivePublishTest.
     *
     */
    public ReceivePublishTest() {
        super(NAME);
    }

    /** Test the publish api. */
    public void testReceivePublish() {
        final Container c = createContainer(datum.junit, NAME);
        addDocuments(datum.junit, c.getId());
        publish(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();

        final Container c_x = readContainer(datum.junit_x, c.getUniqueId());
        assertNotNull("Container not received by user " + datum.junit_x.getSimpleUsername() + ".", c_x);
        final Container c_y = readContainer(datum.junit_x, c.getUniqueId());
        assertNotNull("Container not received by user " + datum.junit_y.getSimpleUsername() + ".", c_y);
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
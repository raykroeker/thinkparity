/*
 * Created On:  13-Dec-06 8:25:28 AM
 */
package com.thinkparity.ophelia.model.test.ticket;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Ticket330Test extends TicketTestCase {

    /** Test name <code>String</code>. */
    private static final String NAME = "Test ticket 330";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket329Test.
     *
     * @param name
     */
    public Ticket330Test() {
        super(NAME);
    }

    /**
     * Test publishing a version to an existing team member.
     *
     */
    public void testPublishVersion() {
        final Container c = createContainer(datum.junit, NAME);
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        publish(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        publish(datum.junit, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        publishVersion(datum.junit, c.getId(), cv_latest.getVersionId(), "JUnit.Y thinkParity");
        datum.waitForEvents();
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        login(OpheliaTestUser.JUNIT_X);
        login(OpheliaTestUser.JUNIT_Y);
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X,
                OpheliaTestUser.JUNIT_Y);
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(OpheliaTestUser.JUNIT);
        logout(OpheliaTestUser.JUNIT_X);
        logout(OpheliaTestUser.JUNIT_Y);
        datum = null;
        super.tearDown();
    }

    private final class Fixture extends TicketTestCase.Fixture {
        private final OpheliaTestUser junit;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            super();
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
            this.junit = junit;
        }
    }
}

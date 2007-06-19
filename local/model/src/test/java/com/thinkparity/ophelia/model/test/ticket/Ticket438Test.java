/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ticket 438 Test<br>
 * <b>Description:</b>A test case causing the workflow described in ticket 438
 * to fail.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket438Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 438";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket438Test.
     *
     */
    public Ticket438Test() {
        super(NAME);
    }

    /**
     * {@link http://thinkparity.dyndns.org/trac/parity/ticket/438 Ticket 438}
     * 
     */
    public void testTicket() {
        // create a container; add a document; publish to first user
        final Container c_initial = createContainer(datum.junit, NAME);
        addDocuments(datum.junit, c_initial.getId());
        addContainerListener(datum.junit_x, datum.listener_x);
        publishToUsers(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        removeContainerListener(datum.junit_x, datum.listener_x);
        assertTrue("Container flagged event did not fire.", datum.flagged_x);
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X);
        login(datum.junit);
        login(datum.junit_x);
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        datum = null;
        super.tearDown();
    }

    /** Test datum fixture. */
    private class Fixture extends TicketTestCase.Fixture {
        private boolean flagged_x;
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final ContainerListener listener_x;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x) {
            this.flagged_x = false;
            this.junit = junit;
            this.junit_x = junit_x;
            this.listener_x = new ContainerAdapter() {
                @Override
                public void containerFlagLatestAdded(final ContainerEvent e) {
                    flagged_x = true;
                    assertNotNull("Container flagged event is null.", e);
                    assertNotNull("Container flagged event container is null.", e.getContainer());
                    final Container c_e = e.getContainer();
                    assertTrue("Container flagged event container latest flag is not set.", c_e.isLatest().booleanValue());
                }
            };
            addQueueHelper(junit);
            addQueueHelper(junit_x);
        }
    }
}

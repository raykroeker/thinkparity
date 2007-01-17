/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ticket 437 Test<br>
 * <b>Description:</b>A test case causing the workflow described in ticket 437
 * to fail.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket437Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 437";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket437Test.
     *
     */
    public Ticket437Test() {
        super(NAME);
    }

    /**
     * NOTE In order to see the manifestation of the error; the logs will have
     * to be checked.
     * {@link http://thinkparity.dyndns.org/trac/parity/ticket/437 Ticket 437}
     */
    public void testTicket() {
        // create a container; add a document; publish to first user
        final Container c_initial = createContainer(datum.junit, NAME);
        addDocuments(datum.junit, c_initial.getId());
        publish(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        // login
        login(datum.junit_x);
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
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        datum = null;
        super.tearDown();
    }

    /** Test datum fixture. */
    private class Fixture extends TicketTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x) {
            this.junit = junit;
            this.junit_x = junit_x;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
        }
    }
}

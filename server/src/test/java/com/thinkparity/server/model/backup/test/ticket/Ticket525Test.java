/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import java.io.IOException;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ticket 525 Test<br>
 * <b>Description:</b>A test case causing the workflow described in ticket 525
 * to fail.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket525Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 525";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket525Test.
     *
     */
    public Ticket525Test() {
        super(NAME);
    }

    /**
     * {@link http://thinkparity.dyndns.org/trac/parity/ticket/525 Ticket 525}
     * 
     */
    public void testTicket() {
        try {
            testTicket(8192);
        } catch (final IOException iox) {
            fail(iox, "Could not complete ticket test.");
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT);
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

    private void testTicket(final Integer count) throws IOException {
        final Container c = createContainer(datum.junit, getName());
        for (int i = 0; i < count.intValue(); i++) {
            addDocument(datum.junit, c.getId(), getSequenceFile(128, i));
        }
    }

    /** Test datum fixture. */
    private class Fixture extends TicketTestCase.Fixture {
        private final OpheliaTestUser junit;
        private Fixture(final OpheliaTestUser junit) {
            this.junit = junit;
            addQueueHelper(junit);
        }
    }
}

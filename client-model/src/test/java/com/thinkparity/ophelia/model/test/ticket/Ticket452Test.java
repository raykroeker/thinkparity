/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ticket 452 Test<br>
 * <b>Description:</b>A test case causing the workflow described in ticket 452
 * to fail.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket452Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 452";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket452Test.
     *
     */
    public Ticket452Test() {
        super(NAME);
    }

    /**
     * {@link http://thinkparity.dyndns.org/trac/parity/ticket/452 Ticket 452}
     * 
     */
    public void testTicket() {
        // create a container; add a document; publish
        final Container c_initial = createContainer(datum.junit, NAME);
        final Document d_txt = addDocument(datum.junit, c_initial.getId(), "JUnitTestFramework.txt");
        publish(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        // create a draft; modify a document
        createDraft(datum.junit, c_initial.getId());
        datum.waitForEvents();
        modifyDocument(datum.junit, d_txt.getId());
        // remove a document
        removeDocument(datum.junit, c_initial.getId(), d_txt.getId());
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

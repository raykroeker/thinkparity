/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ticket 628 Test<br>
 * <b>Description:</b>A test case causing the workflow described in ticket 628
 * to fail.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket628Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 628";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket628Test.
     *
     */
    public Ticket628Test() {
        super(NAME);
    }

    /**
     * {@link http://thinkparity.dyndns.org/trac/parity/ticket/628 Ticket 628}
     * 
     */
    public void testTicket() {
        // create package
        final Container c = createContainer(datum.junit, getName());

        // add documents
        final List<Document> d_list =  addDocuments(datum.junit, c.getId());
        d_list.add(addDocument(datum.junit, c.getId(), "JUnitTestFramework1MB.txt"));

        // search
        final List<Long> c_id_list = getContainerModel(datum.junit).search("JUnitTestFramework1MB.txt");
        assertEquals("Search result does not match expectation.", c_id_list.size(), 1);
        assertEquals("Search result does not match expectation.", c_id_list.get(0), c.getId());

        // delete draft
        deleteDraft(datum.junit, c.getId());

        // search
        c_id_list.clear();
        c_id_list.addAll(getContainerModel(datum.junit).search("JUnitTestFramework1MB.txt"));
        assertEquals("Search result does not match expectation.", c_id_list.size(), 0);
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

    /**
     * <b>Title:</b>Ticket 628 Test Fixture<br>
     * <b>Description:</b><br>
     * 
     */
    private class Fixture extends TicketTestCase.Fixture {
        private final OpheliaTestUser junit;
        private Fixture(final OpheliaTestUser junit) {
            this.junit = junit;
            addQueueHelper(junit);
        }
    }
}

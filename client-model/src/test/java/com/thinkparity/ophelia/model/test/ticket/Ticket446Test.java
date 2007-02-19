/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ticket 446 Test<br>
 * <b>Description:</b>A test case causing the workflow described in ticket 446
 * to fail.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket446Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 446";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket446Test.
     *
     */
    public Ticket446Test() {
        super(NAME);
    }

    /**
     * {@link http://thinkparity.dyndns.org/trac/parity/ticket/446 Ticket 446}
     * 
     */
    public void testTicket() {
        // create a container; add a document; publish
        final Container c_initial = createContainer(datum.junit, NAME);
        final Document d_txt = addDocument(datum.junit, c_initial.getId(), "JUnitTestFramework.txt");
        saveDraft(datum.junit, c_initial.getId());
        publish(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        // create a draft; remove a document
        createDraft(datum.junit, c_initial.getId());
        datum.waitForEvents();
        removeDocument(datum.junit, c_initial.getId(), d_txt.getId());
        // publish
        saveDraft(datum.junit, c_initial.getId());
        publish(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        // verify container; latest container version
        final Container c = readContainer(datum.junit, c_initial.getUniqueId());
        assertNotNull("Container for user " + datum.junit + " is null.", c);
        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        assertNotNull("Latest container version for user " + datum.junit + " is null.", cv_latest);

        // verify container; latest container version
        final Container c_x = readContainer(datum.junit_x, c_initial.getUniqueId());
        assertNotNull("Container for user " + datum.junit_x.getSimpleUsername() + " is null.", c_x);
        final ContainerVersion cv_latest_x = readContainerLatestVersion(datum.junit_x, c_x.getId());
        assertNotNull("Latest container version for user " + datum.junit_x.getSimpleUsername() + " is null.", cv_latest_x);

        assertEquals("Latest container version does not match expectation.", cv_latest.getVersionId(), cv_latest_x.getVersionId());
        assertEquals("Latest container version does not match expectation.", cv_latest.getCreatedOn(), cv_latest_x.getCreatedOn());
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

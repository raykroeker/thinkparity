/*
 * Created On:  24-Nov-06 6:21:40 PM
 */
package com.thinkparity.ophelia.model.test.ticket;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Ticket270Test extends TicketTestCase {

    /** Test name <code>String</code>. */
    private static final String NAME = "Trac Ticket 270";

    /** Test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create Ticket270Test.
     *
     */
    public Ticket270Test() {
        super(NAME);
    }

    /**
     * Test scenario one.
     *
     */
    public void testScenarioOne() {
        final Container c = createContainer(datum.junit, NAME);
        final Document d_one = addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");

        publish(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        final Container c_x = readContainer(datum.junit_x, c.getUniqueId());
        final Container c_y = readContainer(datum.junit_y, c.getUniqueId());
        ContainerDraft draft = readContainerDraft(datum.junit, c.getId());
        assertNull("Draft for " + datum.junit.getSimpleUsername() + " is not null.", draft);
        ContainerDraft draft_x = readContainerDraft(datum.junit_x, c_x.getId());
        assertNull("Draft for " + datum.junit_x.getSimpleUsername() + " is not null.", draft_x);
        ContainerDraft draft_y = readContainerDraft(datum.junit_y, c_y.getId());
        assertNull("Draft for " + datum.junit_y.getSimpleUsername() + " is not null.", draft_y);

        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        draft = readContainerDraft(datum.junit, c.getId());
        assertNotNull("Draft for " + datum.junit.getSimpleUsername() + " is null.", draft);
        draft_x = readContainerDraft(datum.junit_x, c_x.getId());
        assertNotNull("Draft for " + datum.junit_x.getSimpleUsername() + " is null.", draft_x);
        draft_y = readContainerDraft(datum.junit_y, c_y.getId());
        assertNotNull("Draft for " + datum.junit_y.getSimpleUsername() + " is null.", draft_y);

        modifyDocument(datum.junit, d_one.getId());
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");

        publish(datum.junit, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        draft = readContainerDraft(datum.junit, c.getId());
        assertNull("Draft for " + datum.junit.getSimpleUsername() + " is not null.", draft);
        draft_x = readContainerDraft(datum.junit_x, c_x.getId());
        assertNull("Draft for " + datum.junit_x.getSimpleUsername() + " is not null.", draft_x);
        draft_y = readContainerDraft(datum.junit_y, c_y.getId());
        assertNull("Draft for " + datum.junit_y.getSimpleUsername() + " is not null.", draft_y);
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
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    /**
     * <b>Title:</b><br>
     * <b>Description:</b><br>
     * @author raymond@thinkparity.com
     * @version 1.1.2.1
     */
    private final class Fixture extends TicketTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Fixture(final OpheliaTestUser junit,
            final OpheliaTestUser junit_x,
            final OpheliaTestUser junit_y) {
            super();
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
        }
    }
}

/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ticket 452 Test<br>
 * <b>Description:</b>A test case causing the workflow described in ticket 534
 * to fail.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket534Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 534";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket534Test.
     *
     */
    public Ticket534Test() {
        super(NAME);
    }

    /**
     * {@link http://thinkparity.dyndns.org/trac/parity/ticket/534 Ticket 534}
     * 
     */
    public void testTicket() {
        // the starting point requires no contact relationship between junit and junit_x
        // as well as no invitations
        final InternalContactModel cm = getContactModel(datum.junit); 
        final Contact c = cm.read(datum.junit_x.getId());
        if (null != c) {
            logger.logInfo("Deleting contact {0} as {1}.", c.getId().getUsername(), datum.junit.getSimpleUsername());
            cm.delete(c.getId());
            datum.waitForEvents();
        }
        List<? extends OutgoingInvitation> oi_list = cm.readOutgoingEMailInvitations();
        for (final OutgoingInvitation oi : oi_list) {
            logger.logInfo("Deleting e-mail invitation {0} as {1}.", oi.getId(), datum.junit.getSimpleUsername());
            cm.deleteOutgoingEMailInvitation(oi.getId());
            datum.waitForEvents();
        }
        oi_list = cm.readOutgoingUserInvitations();
        for (final OutgoingInvitation oi : oi_list) {
            logger.logInfo("Deleting user invitation {0} as {1}.", oi.getId(), datum.junit.getSimpleUsername());
            cm.deleteOutgoingUserInvitation(oi.getId());
            datum.waitForEvents();
        }

        final InternalContactModel cm_x = getContactModel(datum.junit_x); 
        final Contact c_x = cm_x.read(datum.junit.getId());
        if (null != c_x) {
            logger.logInfo("Deleting contact {0} as {1}.", c_x.getId().getUsername(), datum.junit_x.getSimpleUsername());
            cm_x.delete(c_x.getId());
            datum.waitForEvents();
        }
        List<? extends OutgoingInvitation> oi_list_x = cm_x.readOutgoingEMailInvitations();
        for (final OutgoingInvitation oi_x : oi_list_x) {
            logger.logInfo("Deleting invitation {0} as {1}.", oi_x.getId(), datum.junit_x.getSimpleUsername());
            cm_x.deleteOutgoingEMailInvitation(oi_x.getId());
            datum.waitForEvents();
        }
        oi_list_x = cm_x.readOutgoingUserInvitations();
        for (final OutgoingInvitation oi_x : oi_list_x) {
            logger.logInfo("Deleting invitation {0} as {1}.", oi_x.getId(), datum.junit_x.getSimpleUsername());
            cm_x.deleteOutgoingEMailInvitation(oi_x.getId());
            datum.waitForEvents();
        }

        // invite junit_x from junit
        getContactModel(datum.junit).createOutgoingEMailInvitation(datum.junit_x.getEmail());
        datum.waitForEvents();
        // invite junit from junit_x
        getContactModel(datum.junit_x).createOutgoingEMailInvitation(datum.junit.getEmail());
        datum.waitForEvents();
        // accept the invitation from junit to junit_x
        final List<IncomingInvitation> invitations = getContactModel(datum.junit).readIncomingInvitations();
        assertEquals("Number of incoming invitations does not match expectation.", invitations.size(), 1);
        getContactModel(datum.junit).acceptIncomingInvitation(invitations.get(0).getId());
        datum.waitForEvents();
        final Contact c2 = getContactModel(datum.junit).read(datum.junit_x.getId());
        assertNotNull("Contact is null.", c2);
        final Contact c2_x = getContactModel(datum.junit_x).read(datum.junit.getId());
        assertNotNull("Contact is null.", c2_x);
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

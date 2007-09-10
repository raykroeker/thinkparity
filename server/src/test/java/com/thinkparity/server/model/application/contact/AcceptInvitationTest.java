/*
 * Created On:  8-Sep-07 10:23:21 AM
 */
package com.thinkparity.desdemona.model.contact;

import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Accept Contact Invitation Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AcceptInvitationTest extends ContactTestCase {

    /** Test name. */
    private static final String NAME = "Test accept invitation";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create AcceptInvitationTest.
     *
     */
    public AcceptInvitationTest() {
        super(NAME);
    }

    /**
     * Test of the accept incoming e-mail test.
     * 
     */
    public void testAcceptIncomingEMail() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test accept incoming e-mail.");
        setUpAcceptIncomingEMail();
        try {
            final OutgoingEMailInvitation outgoingEMail = new OutgoingEMailInvitation();
            outgoingEMail.setCreatedBy(datum.lookupUser(datum.inviteAs));
            outgoingEMail.setCreatedOn(datum.now());
            outgoingEMail.setInvitationEMail(datum.lookupEMail(datum.acceptAs));
            datum.getContactModel(datum.inviteAs).createInvitation(outgoingEMail);

            final IncomingEMailInvitation incomingEMail =
                datum.findIncomingEMail(datum.acceptAs,
                        outgoingEMail.getCreatedBy(),
                        outgoingEMail.getCreatedOn(),
                        outgoingEMail.getInvitationEMail());
            assertNotNull("Cannot find incoming e-mail invitation.", incomingEMail);
            datum.getContactModel(datum.acceptAs).acceptInvitation(incomingEMail, datum.now());
        } finally {
            tearDownAcceptIncomingEMail();
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture();
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.ContactTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }

    /**
     * Set up accept incoming e-mail.
     * 
     */
    private void setUpAcceptIncomingEMail() {
        final String acceptAsUsername = datum.newUniqueUsername();
        datum.createProfile(acceptAsUsername);
        datum.acceptAs = datum.login(acceptAsUsername);
        datum.verifyEMail(datum.acceptAs);

        final String inviteAsUsername = datum.newUniqueUsername();
        datum.createProfile(inviteAsUsername);
        datum.inviteAs = datum.login(inviteAsUsername);
        datum.verifyEMail(datum.inviteAs);
    }

    /**
     * Tear down accept incoming e-mail.
     * 
     */
    private void tearDownAcceptIncomingEMail() {
        if (null == datum.acceptAs) {
            TEST_LOGGER.logWarning("Accept as user is not logged in.");
        } else {
            datum.logout(datum.acceptAs);
        }

        if (null == datum.inviteAs) {
            TEST_LOGGER.logWarning("Invite as user is not logged in.");
        } else {
            datum.logout(datum.inviteAs);
        }
    }

    /** <b>Title:</b>Accept Invitation Test Fixture<br> */
    private class Fixture extends ContactTestCase.Fixture {
        private AuthToken inviteAs, acceptAs;
    }
}

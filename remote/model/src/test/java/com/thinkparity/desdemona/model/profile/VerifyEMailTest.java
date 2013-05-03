/*
 * Created On:  6-Oct-07 3:14:38 PM
 */
package com.thinkparity.desdemona.model.profile;

import java.util.List;

import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Verify EMail Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class VerifyEMailTest extends ProfileTestCase {

    /** Test name. */
    private static final String NAME = "Test verify e-mail.";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create VerifyEMailTest.
     *
     */
    public VerifyEMailTest() {
        super(NAME);
    }

    /**
     * Test verify with pending invitations.
     * 
     */
    public void testVerifyWithInvitations() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test verify e-mail with pending invitations.");
        setUpForVerifyWithInvitations();
        try {
            datum.verifyEMail(datum.authToken);

            /* validate the incoming e-mail invitation list */
            final List<IncomingEMailInvitation> invitationList =
                datum.getContactModel(datum.authToken).readIncomingEMailInvitations();
            assertEquals("Number of invitations does not match expectation.",
                    datum.invitationArray.length, invitationList.size());
            boolean found;
            for (final IncomingEMailInvitation in : invitationList) {
                found = false;
                for (final OutgoingEMailInvitation out : datum.invitationArray) {
                    if (out.getCreatedBy().equals(in.getCreatedBy())
                            && out.getCreatedOn().equals(in.getCreatedOn())
                            && out.getInvitationEMail().equals(in.getInvitationEMail())) {
                        found = true;
                        break;
                    }
                }
                assertTrue("Could not find expected invitation \"" + in.getId()
                        + ".\"", found);
            }
        } finally {
            tearDownForVerify();
        }
    }

    /**
     * Test verify with pending invitations.
     * 
     */
    public void testVerifyWithoutInvitations() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test verify e-mail without pending invitations.");
        setUpForVerifyWithoutInvitations();
        try {
            datum.verifyEMail(datum.authToken);

            /* validate the incoming e-mail invitation list */
            final List<IncomingEMailInvitation> invitationList =
                datum.getContactModel(datum.authToken).readIncomingEMailInvitations();
            assertEquals("Number of invitations does not match expectation.", 0,
                    invitationList.size());
            assertEquals("Number of invitations does not match expectation.", 0,
                    datum.invitationArray.length);
        } finally {
            tearDownForVerify();
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture();
    }

    /**
     * @see com.thinkparity.desdemona.model.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }

    /**
     * Set up the test.
     * 
     */
    private void setUpForVerifyWithInvitations() {
        setUpForVerifyWithInvitations(5);
    }

    /**
     * Set up the test.
     * 
     * @param invitationCount
     *            An <code>int</code>.
     */
    private void setUpForVerifyWithInvitations(final int invitationCount) {
        final String username = datum.newUniqueUsername();
        datum.createProfile(username);
        datum.authToken = datum.login(username);
        final ProfileEMail email = datum.getProfileModel(datum.authToken).readEMail();

        final String[] invitedByUsernameArray = new String[invitationCount];
        final Profile[] invitedByProfileArray = new Profile[invitationCount];
        final AuthToken[] invitedByAuthTokenArray = new AuthToken[invitationCount];
        datum.invitationArray = new OutgoingEMailInvitation[invitationCount];
        for (int i = 0; i < invitationCount; i++) {
            invitedByUsernameArray[i] = datum.newUniqueUsername();
            invitedByProfileArray[i] = datum.createProfile(invitedByUsernameArray[i]);
            invitedByAuthTokenArray[i] = datum.login(invitedByUsernameArray[i]);
            datum.verifyEMail(invitedByAuthTokenArray[i]);

            datum.invitationArray[i] = new OutgoingEMailInvitation();
            datum.invitationArray[i].setCreatedBy(invitedByProfileArray[i]);
            datum.invitationArray[i].setCreatedOn(datum.now());
            datum.invitationArray[i].setInvitationEMail(email.getEmail());
            datum.getContactModel(invitedByAuthTokenArray[i]).createInvitation(datum.invitationArray[i]);
        }
    }

    /**
     * Set up the test.
     * 
     */
    private void setUpForVerifyWithoutInvitations() {
        setUpForVerifyWithInvitations(0);
    }

    /**
     * Tear down the test.
     * 
     */
    private void tearDownForVerify() {
        datum.authToken = null;
        for (int i = 0; i < datum.invitationArray.length; i++) {
            datum.invitationArray[i] = null;
        }
    }

    /** <b>Title:</b>Ticket Test Fixture<br> */
    private class Fixture extends ProfileTestCase.Fixture {
        private AuthToken authToken;
        private OutgoingEMailInvitation[] invitationArray;
    }
}

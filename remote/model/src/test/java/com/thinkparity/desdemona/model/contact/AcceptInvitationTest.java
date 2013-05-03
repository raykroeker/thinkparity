/*
 * Created On:  8-Sep-07 10:23:21 AM
 */
package com.thinkparity.desdemona.model.contact;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.profile.Profile;

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
            datum.validateInvitations(datum.acceptAuthToken, "accept", 1, 0, 0, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 0, 1, 0);
            datum.validateContacts(datum.acceptAuthToken, datum.inviteAuthToken, Boolean.FALSE);
            datum.getContactModel(datum.acceptAuthToken).acceptInvitation(
                    datum.incomingEMail, datum.now());
            datum.validateInvitations(datum.acceptAuthToken, "accept", 0, 0, 0, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 0, 0, 0);
            datum.validateContacts(datum.acceptAuthToken, datum.inviteAuthToken, Boolean.TRUE);
        } finally {
            tearDownAccept();
        }
    }

    /**
     * Test of accept incoming user.
     * 
     */
    public void testAcceptIncomingUser() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test accept incoming user.");
        setUpAcceptIncomingUser();
        try {
            datum.validateInvitations(datum.acceptAuthToken, "accept", 0, 1, 0, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 0, 0, 1);
            datum.validateContacts(datum.acceptAuthToken, datum.inviteAuthToken, Boolean.FALSE);
            datum.getContactModel(datum.acceptAuthToken).acceptInvitation(
                    datum.incomingUser, datum.now());
            datum.validateInvitations(datum.acceptAuthToken, "accept", 0, 0, 0, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 0, 0, 0);
            datum.validateContacts(datum.acceptAuthToken, datum.inviteAuthToken, Boolean.TRUE);
        } finally {
            tearDownAccept();
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
        datum.acceptAuthToken = datum.login(acceptAsUsername);
        datum.verifyEMail(datum.acceptAuthToken);
        final EMail acceptAsEMail = datum.getProfileModel(datum.acceptAuthToken).readEMail().getEmail();

        final String inviteAsUsername = datum.newUniqueUsername();
        final Profile inviteAsProfile = datum.createProfile(inviteAsUsername);
        datum.inviteAuthToken = datum.login(inviteAsUsername);
        datum.verifyEMail(datum.inviteAuthToken);

        final OutgoingEMailInvitation invitation = new OutgoingEMailInvitation();
        invitation.setCreatedBy(inviteAsProfile);
        invitation.setCreatedOn(datum.now());
        invitation.setInvitationEMail(acceptAsEMail);
        datum.getContactModel(datum.inviteAuthToken).createInvitation(invitation);

        datum.incomingEMail = datum.getContactModel(datum.acceptAuthToken).readIncomingEMailInvitations().get(0);
    }

    /**
     * Set up accept incoming e-mail.
     * 
     */
    private void setUpAcceptIncomingUser() {
        final String acceptAsUsername = datum.newUniqueUsername();
        final Profile acceptAsProfile = datum.createProfile(acceptAsUsername);
        datum.acceptAuthToken = datum.login(acceptAsUsername);
        datum.verifyEMail(datum.acceptAuthToken);

        final String inviteAsUsername = datum.newUniqueUsername();
        final Profile inviteAsProfile = datum.createProfile(inviteAsUsername);
        datum.inviteAuthToken = datum.login(inviteAsUsername);
        datum.verifyEMail(datum.inviteAuthToken);

        final OutgoingUserInvitation invitation = new OutgoingUserInvitation();
        invitation.setCreatedBy(inviteAsProfile);
        invitation.setCreatedOn(datum.now());
        invitation.setInvitationUser(acceptAsProfile);
        datum.getContactModel(datum.inviteAuthToken).createInvitation(invitation);

        datum.incomingUser = datum.getContactModel(datum.acceptAuthToken).readIncomingUserInvitations().get(0);
    }

    /**
     * Tear down accept.
     * 
     */
    private void tearDownAccept() {
        datum.acceptAuthToken = datum.inviteAuthToken = null;
        datum.incomingEMail = null;
        datum.incomingUser = null;
    }

    /** <b>Title:</b>Accept Invitation Test Fixture<br> */
    private class Fixture extends ContactTestCase.Fixture {
        private AuthToken acceptAuthToken;
        private IncomingEMailInvitation incomingEMail;
        private IncomingUserInvitation incomingUser;
        private AuthToken inviteAuthToken;
    }
}

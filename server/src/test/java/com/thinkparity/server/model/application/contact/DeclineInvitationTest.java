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
 * <b>Title:</b>thinkParity Desdemona Model Decline Contact Invitation Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeclineInvitationTest extends ContactTestCase {

    /** Test name. */
    private static final String NAME = "Test decline invitation";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create DeclineInvitationTest.
     *
     */
    public DeclineInvitationTest() {
        super(NAME);
    }

    /**
     * Test of the decline incoming e-mail test.
     * 
     */
    public void testDeclineIncomingEMail() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test decline incoming e-mail.");
        setUpDeclineIncomingEMail();
        try {
            datum.validateInvitations(datum.declineAuthToken, "decline", 1, 0, 0, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 0, 2, 0);
            datum.validateContacts(datum.declineAuthToken, datum.inviteAuthToken, Boolean.FALSE);
            datum.getContactModel(datum.declineAuthToken).declineInvitation(
                    datum.incomingEMail, datum.now());
            datum.validateInvitations(datum.declineAuthToken, "decline", 0, 0, 0, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 0, 1, 0);
            datum.validateContacts(datum.declineAuthToken, datum.inviteAuthToken, Boolean.FALSE);
        } finally {
            tearDownDecline();
        }
    }

    /**
     * Test of decline incoming user.
     * 
     */
    public void testDeclineIncomingUser() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test decline incoming user.");
        setUpDeclineIncomingUser();
        try {
            datum.validateInvitations(datum.declineAuthToken, "decline", 0, 1, 0, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 0, 0, 2);
            datum.validateContacts(datum.declineAuthToken, datum.inviteAuthToken, Boolean.FALSE);
            datum.getContactModel(datum.declineAuthToken).declineInvitation(
                    datum.incomingUser, datum.now());
            datum.validateInvitations(datum.declineAuthToken, "decline", 0, 0, 0, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 0, 0, 1);
            datum.validateContacts(datum.declineAuthToken, datum.inviteAuthToken, Boolean.FALSE);
        } finally {
            tearDownDecline();
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
     * Set up decline incoming e-mail.
     * 
     */
    private void setUpDeclineIncomingEMail() {
        final String inviteUsername = datum.newUniqueUsername();
        final Profile inviteProfile = datum.createProfile(inviteUsername);
        datum.inviteAuthToken = datum.login(inviteUsername);
        datum.verifyEMail(datum.inviteAuthToken);

        final String declineUsername = datum.newUniqueUsername();
        datum.createProfile(declineUsername);
        datum.declineAuthToken = datum.login(declineUsername);
        datum.verifyEMail(datum.declineAuthToken);
        final EMail declineEMail = datum.getProfileModel(datum.declineAuthToken).readEMail().getEmail();

        OutgoingEMailInvitation invitation = new OutgoingEMailInvitation();
        invitation.setCreatedBy(inviteProfile);
        invitation.setCreatedOn(datum.now());
        invitation.setInvitationEMail(declineEMail);
        datum.getContactModel(datum.inviteAuthToken).createInvitation(invitation);
        /* setup an extra */
        invitation = new OutgoingEMailInvitation();
        invitation.setCreatedBy(inviteProfile);
        invitation.setCreatedOn(datum.now());
        invitation.setInvitationEMail(datum.newEMail(datum.newUniqueUsername()));
        datum.getContactModel(datum.inviteAuthToken).createInvitation(invitation);

        datum.incomingEMail = datum.getContactModel(datum.declineAuthToken).readIncomingEMailInvitations().get(0);
    }

    /**
     * Set up decline incoming user.
     * 
     */
    private void setUpDeclineIncomingUser() {
        final String inviteUsername = datum.newUniqueUsername();
        final Profile inviteProfile = datum.createProfile(inviteUsername);
        datum.inviteAuthToken = datum.login(inviteUsername);
        datum.verifyEMail(datum.inviteAuthToken);

        final String declineUsername = datum.newUniqueUsername();
        final Profile declineProfile = datum.createProfile(declineUsername);
        datum.declineAuthToken = datum.login(declineUsername);
        datum.verifyEMail(datum.declineAuthToken);

        final String extraUsername = datum.newUniqueUsername();
        final Profile extraProfile = datum.createProfile(extraUsername);
        datum.declineAuthToken = datum.login(extraUsername);
        datum.verifyEMail(datum.declineAuthToken);

        OutgoingUserInvitation invitation = new OutgoingUserInvitation();
        invitation.setCreatedBy(inviteProfile);
        invitation.setCreatedOn(datum.now());
        invitation.setInvitationUser(declineProfile);
        datum.getContactModel(datum.inviteAuthToken).createInvitation(invitation);
        /* setup an extra */
        invitation = new OutgoingUserInvitation();
        invitation.setCreatedBy(inviteProfile);
        invitation.setCreatedOn(datum.now());
        invitation.setInvitationUser(extraProfile);
        datum.getContactModel(datum.inviteAuthToken).createInvitation(invitation);

        datum.incomingUser = datum.getContactModel(datum.declineAuthToken).readIncomingUserInvitations().get(0);
    }

    /**
     * Tear down decline.
     * 
     */
    private void tearDownDecline() {
        datum.declineAuthToken = datum.inviteAuthToken = null;
        datum.incomingEMail = null;
        datum.incomingUser = null;
    }

    /** <b>Title:</b>Decline Invitation Test Fixture<br> */
    private class Fixture extends ContactTestCase.Fixture {
        private AuthToken declineAuthToken;
        private IncomingEMailInvitation incomingEMail;
        private IncomingUserInvitation incomingUser;
        private AuthToken inviteAuthToken;
    }
}

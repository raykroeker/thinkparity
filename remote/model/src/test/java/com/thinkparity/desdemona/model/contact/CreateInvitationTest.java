/*
 * Created On:  8-Sep-07 10:23:21 AM
 */
package com.thinkparity.desdemona.model.contact;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Create Contact Invitation Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateInvitationTest extends ContactTestCase {

    /** Test name. */
    private static final String NAME = "Test create invitation";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create CreateInvitationTest.
     *
     */
    public CreateInvitationTest() {
        super(NAME);
    }

    /**
     * Test of create outgoing e-mail.
     * 
     */
    public void testCreateOutgoingEMail() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test create outgoing e-mail.");
        setUpCreateOutgoingEMail();
        try {
            datum.validateInvitations(datum.createAuthToken, "create", 0, 0, 0, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 0, 0, 0);
            datum.validateContacts(datum.createAuthToken, datum.inviteAuthToken, Boolean.FALSE);
            datum.getContactModel(datum.createAuthToken).createInvitation(datum.outgoingEMailOne);
            datum.validateInvitations(datum.createAuthToken, "create", 0, 0, 1, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 1, 0, 0, 0);
            datum.validateContacts(datum.createAuthToken, datum.inviteAuthToken, Boolean.FALSE);
            datum.getContactModel(datum.createAuthToken).createInvitation(datum.outgoingEMailTwo);
            datum.validateInvitations(datum.createAuthToken, "create", 0, 0, 2, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 1, 0, 0, 0);
            datum.validateContacts(datum.createAuthToken, datum.inviteAuthToken, Boolean.FALSE);
        } finally {
            tearDownCreate();
        }
    }

    /**
     * Test of create outgoing user.
     * 
     */
    public void testCreateOutgoingUser() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test create outgoing user.");
        setUpCreateOutgoingUser();
        try {
            datum.validateInvitations(datum.createAuthToken, "create", 0, 0, 0, 0);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 0, 0, 0);
            datum.validateContacts(datum.createAuthToken, datum.inviteAuthToken, Boolean.FALSE);
            datum.getContactModel(datum.createAuthToken).createInvitation(datum.outgoingUser);
            datum.validateInvitations(datum.createAuthToken, "create", 0, 0, 0, 1);
            datum.validateInvitations(datum.inviteAuthToken, "invite", 0, 1, 0, 0);
            datum.validateContacts(datum.createAuthToken, datum.inviteAuthToken, Boolean.FALSE);
        } finally {
            tearDownCreate();
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
     * Set up create outgoing e-mail.
     * 
     */
    private void setUpCreateOutgoingEMail() {
        final String createUsername = datum.newUniqueUsername();
        final Profile createProfile = datum.createProfile(createUsername);
        datum.createAuthToken = datum.login(createUsername);
        datum.verifyEMail(datum.createAuthToken);

        final String inviteAsUsername = datum.newUniqueUsername();
        datum.createProfile(inviteAsUsername);
        datum.inviteAuthToken = datum.login(inviteAsUsername);
        datum.verifyEMail(datum.inviteAuthToken);
        final EMail inviteEMail = datum.getProfileModel(datum.inviteAuthToken).readEMail().getEmail();

        datum.outgoingEMailOne = new OutgoingEMailInvitation();
        datum.outgoingEMailOne.setCreatedBy(createProfile);
        datum.outgoingEMailOne.setCreatedOn(datum.now());
        datum.outgoingEMailOne.setInvitationEMail(inviteEMail);

        datum.outgoingEMailTwo = new OutgoingEMailInvitation();
        datum.outgoingEMailTwo.setCreatedBy(createProfile);
        datum.outgoingEMailTwo.setCreatedOn(datum.now());
        datum.outgoingEMailTwo.setInvitationEMail(datum.newEMail(datum.newUniqueUsername()));
    }

    /**
     * Set up create ougtoing user.
     * 
     */
    private void setUpCreateOutgoingUser() {
        final String createUsername = datum.newUniqueUsername();
        final Profile createProfile = datum.createProfile(createUsername);
        datum.createAuthToken = datum.login(createUsername);
        datum.verifyEMail(datum.createAuthToken);

        final String inviteUsername = datum.newUniqueUsername();
        final Profile inviteProfile = datum.createProfile(inviteUsername);
        datum.inviteAuthToken = datum.login(inviteUsername);
        datum.verifyEMail(datum.inviteAuthToken);

        datum.outgoingUser = new OutgoingUserInvitation();
        datum.outgoingUser.setCreatedBy(createProfile);
        datum.outgoingUser.setCreatedOn(datum.now());
        datum.outgoingUser.setInvitationUser(inviteProfile);
    }

    /**
     * Tear down create.
     * 
     */
    private void tearDownCreate() {
        datum.createAuthToken = datum.inviteAuthToken = null;
        datum.outgoingEMailOne = datum.outgoingEMailTwo = null;
        datum.outgoingUser = null;
    }

    /** <b>Title:</b>Create Invitation Test Fixture<br> */
    private class Fixture extends ContactTestCase.Fixture {
        private AuthToken createAuthToken;
        private AuthToken inviteAuthToken;
        private OutgoingEMailInvitation outgoingEMailOne;
        private OutgoingEMailInvitation outgoingEMailTwo;
        private OutgoingUserInvitation outgoingUser;
    }
}

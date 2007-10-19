/*
 * Created On:  8-Sep-07 10:20:33 AM
 */
package com.thinkparity.desdemona.model.contact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.ContactInvitation;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.ModelTestCase;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Contact Test Case<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class ContactTestCase extends ModelTestCase {

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#ModelTestCase(String)
     * 
     */
    protected ContactTestCase(final String name) {
        super(name);
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** <b>Title:</b>Contact Test Fixture<br> */
    protected abstract class Fixture extends ModelTestCase.Fixture {

        /**
         * @see ModelTestCase.Fixture#findIncomingEMailInvitation(AuthToken,
         *      User, Calendar, EMail)
         * 
         */
        public IncomingEMailInvitation findIncomingEMail(
                final AuthToken authToken, final User createdBy,
                final Calendar createdOn, final EMail email) {
            return super.findIncomingEMailInvitation(authToken, createdBy, createdOn, email);
        }

        /**
         * Validate that token one and two are or are not contacts.
         * 
         * @param authTokenOne
         *            An <code>AuthToken</code>.
         * @param authTokenTwo
         *            An <code>AuthToken</code>.
         * @param contacts
         *            A <Code>Boolean</code>.
         */
        public final void validateContacts(final AuthToken authTokenOne,
                final AuthToken authTokenTwo, final Boolean contacts) {
            final List<Contact> contactListOne = getContactModel(authTokenOne).read();
            final List<Contact> contactListTwo = getContactModel(authTokenTwo).read();
            boolean hit = false;
            for (final Contact contactOne : contactListOne) {
                for (final Contact contactTwo : contactListTwo) {
                    if (contactOne.getId().equals(contactTwo.getId())) {
                        hit = true;
                        break;
                    }
                }
            }
            assertEquals("Contact hit does not match expectation.", contacts.booleanValue(), hit);
        }

        /**
         * Validate the correct number of invitations exist for the user
         * represented by the auth token.
         * 
         * @param authToken
         *            An <code>AuthToken</code>.
         * @param role
         *            A <code>String</code>.
         * @param incomingEMailCount
         *            An <code>int</code>.
         * @param incomingUserCount
         *            An <code>int</code>.
         * @param outgoingEMailCount
         *            An <code>int</code>.
         * @param outgoingUserCount
         *            An <code>int</code>.
         */
        public final void validateInvitations(final AuthToken authToken,
                final String role, final int incomingEMailCount,
                final int incomingUserCount, final int outgoingEMailCount,
                final int outgoingUserCount) {
            final List<ContactInvitation> invitationList = new ArrayList<ContactInvitation>();

            invitationList.clear();
            invitationList.addAll(getContactModel(authToken).readIncomingEMailInvitations());
            assertEquals("Incoming e-mail invitation list size does not match expectation for "
                    + lookupUser(authToken).getSimpleUsername() + " (" + role + ").", incomingEMailCount,
                    invitationList.size());

            invitationList.clear();
            invitationList.addAll(getContactModel(authToken).readIncomingUserInvitations());
            assertEquals("Incoming user invitation list size does not match expectation for "
                    + lookupUser(authToken).getSimpleUsername() + " (" + role + ").", incomingUserCount,
                    invitationList.size());

            invitationList.clear();
            invitationList.addAll(getContactModel(authToken).readOutgoingEMailInvitations());
            assertEquals("Outgoing e-mail invitation list size does not match expectation for "
                    + lookupUser(authToken).getSimpleUsername() + " (" + role + ").", outgoingEMailCount,
                    invitationList.size());

            invitationList.clear();
            invitationList.addAll(getContactModel(authToken).readOutgoingUserInvitations());
            assertEquals("Outgoing user invitation list size does not match expectation for "
                    + lookupUser(authToken).getSimpleUsername() + " (" + role + ").", outgoingUserCount,
                    invitationList.size());
        }
    }
}

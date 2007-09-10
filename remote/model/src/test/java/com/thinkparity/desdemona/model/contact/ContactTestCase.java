/*
 * Created On:  8-Sep-07 10:20:33 AM
 */
package com.thinkparity.desdemona.model.contact;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;

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
    }
}

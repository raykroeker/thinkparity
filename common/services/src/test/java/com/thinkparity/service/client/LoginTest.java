/*
 * Created On:  7-Jun-07 11:06:12 AM
 */
package com.thinkparity.service.client;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.service.SessionService;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class LoginTest extends ServiceTestCase {

    private static final String NAME = "Login service test";

    private Fixture datum;

    /**
     * Create LoginTest.
     *
     * @param name
     */
    public LoginTest() {
        super(NAME);
    }

    /**
     * Test the login service.
     *
     */
    public void testLogin() {
        try {
            datum.service.login(datum.credentials);
        } catch (final InvalidCredentialsException icx) {
            fail("Could not login to service.");
        }
    }

    /**
     * @see com.thinkparity.service.client.ServiceTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture(getServiceFactory().getSessionService());
    }

    /**
     * @see com.thinkparity.service.client.ServiceTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        datum = null;
    }

    private class Fixture {

        private final Credentials credentials;

        private final SessionService service;

        private Fixture(final SessionService service) {
            super();
            this.credentials = new Credentials();
            this.credentials.setPassword("dl-rkroeker");
            this.credentials.setUsername("rkroeker");
            this.service = service;
        }
    }
    
}

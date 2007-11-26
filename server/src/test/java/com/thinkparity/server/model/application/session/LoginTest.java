/*
 * Created On:  21-Nov-07 12:25:05 PM
 */
package com.thinkparity.desdemona.model.session;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Login Test<br />
 * <b>Description:</b><br />
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class LoginTest extends SessionTestCase {

    /** Test name. */
    private static final String NAME = "Login";

    /** Test datum. */
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
     * Test login with an invalid password.
     * 
     */
    public void testLoginInvalidPassword() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test login invalid password.");

        boolean didThrow = false;
        AuthToken authToken = null;
        try {
            authToken = datum.getSessionModel().login(datum.invalidPasswordCredentials);
        } catch (final InvalidCredentialsException icx) {
            didThrow = true;
        }
        assertTrue("Login did not throw error.", didThrow);
        assertNull("Authentication token is not null.", authToken);
    }

    /**
     * Test login with an invalid username.
     * 
     */
    public void testLoginInvalidUsername() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test login invalid username.");

        boolean didThrow = false;
        AuthToken authToken = null;
        try {
            authToken = datum.getSessionModel().login(datum.invalidUsernameCredentials);
        } catch (final InvalidCredentialsException icx) {
            didThrow = true;
        }
        assertTrue("Login did not throw error.", didThrow);
        assertNull("Authentication token is not null.", authToken);
    }

    /**
     * Test login with null credentials.
     * 
     */
    public void testLoginNullCredentials() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test login null credentials.");

        boolean didThrow = false;
        AuthToken authToken = null;
        try {
            authToken = datum.getSessionModel().login(datum.nullCredentials);
        } catch (final InvalidCredentialsException icx) {
            didThrow = true;
        }
        assertTrue("Login did not throw error.", didThrow);
        assertNull("Authentication token is not null.", authToken);
    }

    /**
     * Test login with a null password.
     * 
     */
    public void testLoginNullPassword() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test login null password.");

        boolean didThrow = false;
        AuthToken authToken = null;
        try {
            authToken = datum.getSessionModel().login(datum.nullPasswordCredentials);
        } catch (final InvalidCredentialsException icx) {
            didThrow = true;
        }
        assertTrue("Login did not throw error.", didThrow);
        assertNull("Authentication token is not null.", authToken);
    }

    /**
     * Test login with a null username.
     * 
     */
    public void testLoginNullUsername() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test login null username.");

        boolean didThrow = false;
        AuthToken authToken = null;
        try {
            authToken = datum.getSessionModel().login(datum.nullUsernameCredentials);
        } catch (final InvalidCredentialsException icx) {
            didThrow = true;
        }
        assertTrue("Login did not throw error.", didThrow);
        assertNull("Authentication token is not null.", authToken);
    }

    /**
     * Test login with valid credentials.
     * 
     */
    public void testLoginValidCredentials() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test login valid credentials.");

        AuthToken authToken = null;
        try {
            authToken = datum.getSessionModel().login(datum.validCredentials);
        } catch (final InvalidCredentialsException icx) {
            fail(icx, "Could not login:  {0}", datum.validCredentials);
        }
        assertNotNull("Authentication token is null.", authToken);
        assertNotNull("Authentication token expiry date is null.", authToken.getExpiresOn());
        assertTrue("Authentication token expiry date is is in the past.",
                System.currentTimeMillis() < authToken.getExpiresOn().getTime());
        assertNotNull("Authentication token session id is null.", authToken.getSessionId());
        final Session session = datum.getSessionModel().readSession(authToken.getSessionId());
        assertNotNull("Authentication token session id is invalid.", session);
        assertNotNull("Authentication token session id is invalid.", session.getId());
        assertEquals("Authentication token session id is invalid.",
                authToken.getSessionId(), session.getId());
    }

    /**
     * @see com.thinkparity.desdemona.model.session.SessionTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture();
        final String username = datum.newUniqueUsername();
        datum.createProfile(username);
        final String password = datum.lookupPassword(username);
        final String invalid = String.valueOf(System.currentTimeMillis());

        datum.invalidPasswordCredentials = new Credentials();
        datum.invalidPasswordCredentials.setUsername(username);
        datum.invalidPasswordCredentials.setPassword(invalid);
        
        datum.invalidUsernameCredentials = new Credentials();
        datum.invalidUsernameCredentials.setUsername(invalid);
        datum.invalidUsernameCredentials.setPassword(password);
        
        datum.nullCredentials = null;

        datum.nullPasswordCredentials = new Credentials();
        datum.nullPasswordCredentials.setUsername(username);
        datum.nullPasswordCredentials.setPassword(null);

        datum.nullUsernameCredentials = new Credentials();
        datum.nullUsernameCredentials.setUsername(null);
        datum.nullUsernameCredentials.setPassword(password);

        datum.validCredentials = new Credentials();
        datum.validCredentials.setUsername(username);
        datum.validCredentials.setPassword(password);
    }

    /**
     * @see com.thinkparity.desdemona.model.session.SessionTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }

    /** <b>Title:</b>Login Test Fixture */
    private class Fixture extends SessionTestCase.Fixture {
        private Credentials invalidPasswordCredentials;
        private Credentials invalidUsernameCredentials;
        private Credentials nullCredentials;
        private Credentials nullPasswordCredentials;
        private Credentials nullUsernameCredentials;
        private Credentials validCredentials;
    }
}

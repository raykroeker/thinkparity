/*
 * Created On:  20-Sep-07 9:25:57 AM
 */
package com.thinkparity.desdemona.model.profile;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Create Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class UpdatePasswordTest extends ProfileTestCase {

    /** Test name. */
    private static final String NAME = "Update password";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create UpdatePasswordTest.
     *
     */
    public UpdatePasswordTest() {
        super(NAME);
    }

    /**
     * Test updating a password with an invalid password.
     * 
     */
    public void testUpdatePasswordInvalidPassword() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test update password with an invalid password.");
        AuthToken authToken = datum.login(datum.validCredentials.getUsername());
        try {
            boolean didThrow = false;
            try {
                datum.getProfileModel(authToken).updatePassword(
                        datum.invalidPasswordCredentials, datum.newPassword());
            } catch (final InvalidCredentialsException icx) {
                didThrow = true;
            }
            assertTrue("Did not throw invalid credentials error.", didThrow);
        } finally {
            datum.logout(authToken);
        }
        authToken = datum.login(datum.validCredentials.getUsername());
        try {
            assertNotNull("Password has been changed.", authToken);
        } finally {
            datum.logout(authToken);
        }
    }

    /**
     * Test updating a password with an invalid username.
     * 
     */
    public void testUpdatePasswordInvalidUsername() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test update password with an invalid username.");
        AuthToken authToken = datum.login(datum.validCredentials.getUsername());
        try {
            boolean didThrow = false;
            try {
                datum.getProfileModel(authToken).updatePassword(
                        datum.invalidUsernameCredentials, datum.newPassword());
            } catch (final InvalidCredentialsException icx) {
                didThrow = true;
            }
            assertTrue("Did not throw invalid credentials error.", didThrow);
        } finally {
            datum.logout(authToken);
        }
        authToken = datum.login(datum.validCredentials.getUsername());
        try {
            assertNotNull("Password has been changed.", authToken);
        } finally {
            datum.logout(authToken);
        }
    }

    /**
     * Test updating a password with null credentials.
     * 
     */
    public void testUpdatePasswordNullCredentials() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test update password with null credentials.");
        AuthToken authToken = datum.login(datum.validCredentials.getUsername());
        try {
            boolean didThrow = false;
            try {
                datum.getProfileModel(authToken).updatePassword(
                        datum.nullCredentials, datum.newPassword());
            } catch (final InvalidCredentialsException icx) {
                didThrow = true;
            }
            assertTrue("Did not throw invalid credentials error.", didThrow);
        } finally {
            datum.logout(authToken);
        }
        authToken = datum.login(datum.validCredentials.getUsername());
        try {
            assertNotNull("Password has been changed.", authToken);
        } finally {
            datum.logout(authToken);
        }
    }

    /**
     * Test updating a password with a null password.
     * 
     */
    public void testUpdatePasswordNullPassword() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test update password with a null username.");
        AuthToken authToken = datum.login(datum.validCredentials.getUsername());
        try {
            boolean didThrow = false;
            try {
                datum.getProfileModel(authToken).updatePassword(
                        datum.nullPasswordCredentials, datum.newPassword());
            } catch (final InvalidCredentialsException icx) {
                didThrow = true;
            }
            assertTrue("Did not throw invalid credentials error.", didThrow);
        } finally {
            datum.logout(authToken);
        }
        authToken = datum.login(datum.validCredentials.getUsername());
        try {
            assertNotNull("Password has been changed.", authToken);
        } finally {
            datum.logout(authToken);
        }
    }

    /**
     * Test updating a password with a null username.
     * 
     */
    public void testUpdatePasswordNullUsername() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test update password with a null username.");
        AuthToken authToken = datum.login(datum.validCredentials.getUsername());
        try {
            boolean didThrow = false;
            try {
                datum.getProfileModel(authToken).updatePassword(
                        datum.nullUsernameCredentials, datum.newPassword());
            } catch (final InvalidCredentialsException icx) {
                didThrow = true;
            }
            assertTrue("Did not throw invalid credentials error.", didThrow);
        } finally {
            datum.logout(authToken);
        }
        authToken = datum.login(datum.validCredentials.getUsername());
        try {
            assertNotNull("Password has been changed.", authToken);
        } finally {
            datum.logout(authToken);
        }
    }

    /**
     * Test updating a password.
     * 
     */
    public void testUpdatePasswordValidCredentials() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test update password with valid credentials.");
        final AuthToken authToken = datum.login(datum.validCredentials.getUsername());
        try {
            try {
                datum.getProfileModel(authToken).updatePassword(
                        datum.validCredentials, datum.newPassword());
            } catch (final InvalidCredentialsException icx) {
                fail(icx, "Could not update password:  {0}", datum.validCredentials);
            }
        } finally {
            datum.logout(authToken);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#setUp()
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
     * @see com.thinkparity.desdemona.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }

    /** <b>Title:</b>Update Password Fixture<br> */
    private class Fixture extends ProfileTestCase.Fixture {
        private Credentials invalidPasswordCredentials;
        private Credentials invalidUsernameCredentials;
        private Credentials nullCredentials;
        private Credentials nullPasswordCredentials;
        private Credentials nullUsernameCredentials;
        private Credentials validCredentials;
    }
}

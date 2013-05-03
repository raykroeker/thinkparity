/*
 * Created On:  13-Nov-07 11:48:45 AM
 */
package com.thinkparity.desdemona.model.session;

import com.thinkparity.codebase.model.session.Configuration;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.service.AuthToken;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadCustomConfigurationTest extends SessionTestCase {

    /** Test name. */
    private static final String NAME = "Read custom configuration";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create ReadConfigurationTest.
     *
     */
    public ReadCustomConfigurationTest() {
        super(NAME);
    }

    /**
     * Test reading the configuration.
     * 
     */
    public void testReadCustomConfiguration() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test reading the user's custom session configuration.");
        final Configuration configuration =
            datum.getSessionModel(datum.authToken).readConfiguration(
                    datum.authToken);
        if (configuration.containsKey("com.thinkparity.session.reaper.enabled")) {
            if (Boolean.valueOf(configuration.getProperty("com.thinkparity.session.reaper.enabled"))) {
                assertContainsKey(configuration, "com.thinkparity.session.reaper.timeout");
                assertContainsKey(configuration, "com.thinkparity.session.reaper.firstexecutiondelay");
                assertContainsKey(configuration, "com.thinkparity.session.reaper.recurringexecutionperiod");
                assertValueIsNotNull(configuration, "com.thinkparity.session.reaper.timeout");
                assertValueIsNotNull(configuration, "com.thinkparity.session.reaper.firstexecutiondelay");
                assertValueIsNotNull(configuration, "com.thinkparity.session.reaper.recurringexecutionperiod");
                configuration.setProperty("com.thinkparity.session.reaper.timeout", "-1");
                configuration.setProperty("com.thinkparity.session.reaper.firstexecutiondelay", "-1");
                configuration.setProperty("com.thinkparity.session.reaper.recurringexecutionperiod", "-1");
                /* update the user's configuration */
                datum.newAdminUserModel(datum.adminAuthToken).updateProductConfiguration(
                        datum.user, datum.readOpheliaProduct(), configuration);
                final Configuration customConfiguration =
                    datum.getSessionModel(datum.authToken).readConfiguration(
                            datum.authToken);
                assertContainsKey(configuration, "com.thinkparity.session.reaper.timeout");
                assertContainsKey(configuration, "com.thinkparity.session.reaper.firstexecutiondelay");
                assertContainsKey(configuration, "com.thinkparity.session.reaper.recurringexecutionperiod");
                assertValueIsNotNull(configuration, "com.thinkparity.session.reaper.timeout");
                assertValueIsNotNull(configuration, "com.thinkparity.session.reaper.firstexecutiondelay");
                assertValueIsNotNull(configuration, "com.thinkparity.session.reaper.recurringexecutionperiod");
                assertEquals(configuration, customConfiguration, "com.thinkparity.session.reaper.timeout");
                assertEquals(configuration, customConfiguration, "com.thinkparity.session.reaper.firstexecutiondelay");
                assertEquals(configuration, customConfiguration, "com.thinkparity.session.reaper.recurringexecutionperiod");
            }
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
        datum.user = datum.createProfile(username);
        datum.authToken = datum.login(username);
        datum.verifyEMail(datum.authToken);
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        try {
            datum.logout(datum.authToken);
        } finally {
            datum = null;
        }
        super.tearDown();
    }

    /**
     * Assert that the key is set.
     * 
     * @param configuration
     *            A <code>Configuration</code>.
     * @param key
     *            A <code>String</code>.
     */
    private void assertContainsKey(final Configuration configuration,
            final String key) {
        assertTrue("Configuration is missing key " + key + ".",
                configuration.containsKey(key));
    }

    /**
     * Assert that the values equal.
     * 
     * @param configuration
     *            A <code>Configuration</code>.
     * @param other
     *            A <code>Configuration</code>.
     * @param key
     *            A <code>String</code>.
     */
    private void assertEquals(final Configuration configuration,
            final Configuration other, final String key) {
        assertEquals("Configuration is value for " + key + " does not match expectation.",
                configuration.getProperty(key), other.getProperty(key));
    }

    /**
     * Assert that the value is not null.
     * 
     * @param configuration
     *            A <code>Configuration</code>.
     * @param key
     *            A <code>String</code>.
     */
    private void assertValueIsNotNull(final Configuration configuration,
            final String key) {
        assertNotNull("Configuration value for " + key + " is null.",
                configuration.getProperty(key));
    }

    /** <b>Title:</b>Read Configuration Test Fixture<br> */
    private class Fixture extends SessionTestCase.Fixture {
        private AuthToken adminAuthToken;
        private AuthToken authToken;
        private User user;
    }
}

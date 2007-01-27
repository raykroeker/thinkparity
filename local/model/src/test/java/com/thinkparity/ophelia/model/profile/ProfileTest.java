/*
 * Created On: 2007-01-27 10:05
 */
package com.thinkparity.ophelia.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.events.ProfileAdapter;
import com.thinkparity.ophelia.model.events.ProfileEvent;
import com.thinkparity.ophelia.model.events.ProfileListener;

/**
 * <b>Title:</b>thinkParity Profile Test<br>
 * <b>Description:</b><br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProfileTest extends ProfileTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "Profile test";

    /** The test <code>Fixture</code> datum. */
    private Fixture datum;

    /**
     * Create ProfileTest.
     *
     */
    public ProfileTest() {
        super(NAME);
    }

    /**
     * Test adding an email address.
     *
     */
    public void testAddEmail() {
        final String emailInjection = "+" + String.valueOf(System.currentTimeMillis());
        final EMail email = EMailBuilder.parse("junit" + emailInjection + "@thinkparity.com");
        getModel(datum.junit).addListener(datum.add_email_listener);
        getModel(datum.junit).addEmail(email);
        datum.waitForEvents();
        getModel(datum.junit).removeListener(datum.add_email_listener);
        assertTrue("Email added event not fired.", datum.add_email_notify);
    }

    /**
     * Test removing an email address.
     *
     */
    public void testRemoveEmail() {
        final String emailInjection = "+" + String.valueOf(System.currentTimeMillis());
        final EMail email = EMailBuilder.parse("junit" + emailInjection + "@thinkparity.com");
        getModel(datum.junit).addEmail(email);
        final List<ProfileEMail> emails = getModel(datum.junit).readEmails();
        getModel(datum.junit).addListener(datum.remove_email_listener);
        getModel(datum.junit).removeEmail(emails.get(emails.size() - 1).getEmailId());
        datum.waitForEvents();
        getModel(datum.junit).removeListener(datum.remove_email_listener);
        assertTrue("Email removed event not fired.", datum.remove_email_notify);
    }

    /**
     * Test resetting the password.
     *
     */
    public void testResetPassword() {
        final String password = datum.junit.getCredentials().getPassword();
        getModel(datum.junit).addListener(datum.reset_password_listener);
        getModel(datum.junit).resetPassword(datum.junit.getSimpleUsername());
        datum.waitForEvents();
        getModel(datum.junit).removeListener(datum.reset_password_listener);
        // change it back
        final String newPassword = getModel(datum.junit).readCredentials().getPassword();
        getModel(datum.junit).updatePassword(newPassword, password);
        assertTrue("Reset password event not fired.", datum.reset_password_notify);
    }

    /**
     * Test updating the password.
     *
     */
    public void testUpdatePassword() {
        getModel(datum.junit).addListener(datum.update_password_listener);
        final String password = datum.junit.getCredentials().getPassword();
        final String newPassword = "ASKJSL!)!(#^)!:@#{}";
        getModel(datum.junit).updatePassword(password, newPassword);
        datum.waitForEvents();
        getModel(datum.junit).removeListener(datum.update_password_listener);
        // change it back
        getModel(datum.junit).updatePassword(newPassword, password);
        assertTrue("Update password event not fired.", datum.update_password_notify);
    }

    /**
     * Test verifying an email address.
     *
     */
    public void testVerifyEmail() {
        final String emailInjection = "+" + String.valueOf(System.currentTimeMillis());
        final EMail email = EMailBuilder.parse("junit" + emailInjection + "@thinkparity.com");
        getModel(datum.junit).addEmail(email);
        final List<ProfileEMail> emails = getModel(datum.junit).readEmails();
        getModel(datum.junit).addListener(datum.verify_email_listener);
        getModel(datum.junit).verifyEmail(emails.get(emails.size() - 1).getEmailId(), email.toString());
        datum.waitForEvents();
        getModel(datum.junit).removeListener(datum.verify_email_listener);
        assertTrue("Email verified event not fired.", datum.verify_email_notify);
    }

    /**
     * Test reading the profile.
     *
     */
    public void testRead() {
        final Profile profile = getModel(datum.junit).read();
        assertNotNull(profile);
    }

    /**
     * Test updating the profile.
     *
     */
    public void testUpdate() {
        final Profile profile = getModel(datum.junit).read();
        final String updateSuffix = String.valueOf(System.currentTimeMillis());
        profile.setCity("Vancouver" + updateSuffix);
        profile.setCountry("Canada" + updateSuffix);
        profile.setMobilePhone("888-555-1111" + updateSuffix);
        profile.setPhone("888-555-1111" + updateSuffix);
        getModel(datum.junit).addListener(datum.update_listener);
        getModel(datum.junit).update(profile);
        datum.waitForEvents();
        getModel(datum.junit).removeListener(datum.update_listener);
        assertTrue("Update event not fired.", datum.update_notify);
    }

    /**
     * Set up the profile test.
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture();
        login(datum.junit);
    }

    /**
     * Tear down the profile test.
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        datum = null;
        super.tearDown();
    }

    /**
     * <b>Title:</b>Profile Test Fixture<br>
     * <b>Description:</b><br>
     */
    private class Fixture extends ProfileTestCase.Fixture {
        private final ProfileListener add_email_listener;
        private boolean add_email_notify;
        private final OpheliaTestUser junit;
        private final ProfileListener remove_email_listener;
        private boolean remove_email_notify;
        private final ProfileListener reset_password_listener;
        private boolean reset_password_notify;
        private final ProfileListener update_listener;
        private boolean update_notify;
        private final ProfileListener update_password_listener;
        private boolean update_password_notify;
        private final ProfileListener verify_email_listener;
        private boolean verify_email_notify;
        /**
         * Create Fixture.
         *
         * @param junit
         *      The junit test user.
         */
        private Fixture() {
            super();
            this.junit = OpheliaTestUser.JUNIT;
            this.add_email_listener = new ProfileAdapter() {
                @Override
                public void emailAdded(final ProfileEvent e) {
                    assertNotNull("Profile event is null.", e);
                    assertNotNull("Profile event profile is null.", e.getProfile());
                    assertNotNull("Profile event email null.", e.getEmail());
                    add_email_notify = true;
                }
            };
            this.add_email_notify = false;
            this.remove_email_listener = new ProfileAdapter() {
                @Override
                public void emailRemoved(final ProfileEvent e) {
                    assertNotNull("Profile event is null.", e);
                    assertNotNull("Profile event profile is null.", e.getProfile());
                    assertNotNull("Profile event email null.", e.getEmail());
                    remove_email_notify = true;
                }
            };
            this.remove_email_notify = false;
            this.reset_password_listener = new ProfileAdapter() {
                @Override
                public void passwordReset(final ProfileEvent e) {
                    assertNotNull("Profile event is null.", e);
                    assertNotNull("Profile event profile is null.", e.getProfile());
                    assertNull("Profile event email is not null.", e.getEmail());
                    reset_password_notify = true;
                }
            };
            this.reset_password_notify = false;
            this.update_listener = new ProfileAdapter() {
                @Override
                public void profileUpdated(final ProfileEvent e) {
                    assertNotNull("Profile event is null.", e);
                    assertNotNull("Profile event profile is null.", e.getProfile());
                    assertNull("Profile event email is not null.", e.getEmail());
                    update_notify = true;
                }
            };
            this.update_notify = false;
            this.update_password_listener = new ProfileAdapter() {
                @Override
                public void passwordUpdated(final ProfileEvent e) {
                    assertNotNull("Profile event is null.", e);
                    assertNotNull("Profile event profile is null.", e.getProfile());
                    assertNull("Profile event email is not null.", e.getEmail());
                    update_password_notify = true;
                }
            };
            this.update_password_notify = false;
            this.verify_email_listener = new ProfileAdapter() {
                @Override
                public void emailVerified(final ProfileEvent e) {
                    assertNotNull("Profile event is null.", e);
                    assertNotNull("Profile event profile is null.", e.getProfile());
                    assertNotNull("Profile event email is not null.", e.getEmail());
                    verify_email_notify = true;
                }
            };
            this.verify_email_notify = false;
            addQueueHelper(junit);
        }
    }
}

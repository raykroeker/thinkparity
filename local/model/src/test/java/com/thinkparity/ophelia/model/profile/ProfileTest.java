/*
 * Created On: 2007-01-27 10:05
 */
package com.thinkparity.ophelia.model.profile;

import java.util.Locale;
import java.util.TimeZone;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;

import com.thinkparity.codebase.model.profile.EMailIntegrityException;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.events.ProfileAdapter;
import com.thinkparity.ophelia.model.events.ProfileEvent;
import com.thinkparity.ophelia.model.events.ProfileListener;

import com.thinkparity.ophelia.OpheliaTestUser;

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
     * Test creating a profile.
     *
     */
    public void testCreate() {
        logout(datum.junit);

        final Credentials credentials = new Credentials();
        credentials.setPassword("password");
        credentials.setUsername("junit." + System.currentTimeMillis());

        final UsernameReservation usernameReservation =
            getModel(datum.junit).createUsernameReservation(
                    credentials.getUsername());

        final String emailInjection = "+" + String.valueOf(System.currentTimeMillis());
        final EMail email = EMailBuilder.parse("junit" + emailInjection + "@thinkparity.com");
        final EMailReservation emailReservation =
            getModel(datum.junit).createEMailReservation(email);

        final Profile profile = new Profile();
        profile.setVCard(new ProfileVCard());
        profile.setAddress("1234 5th Street Apt 6");
        profile.setCity("City");
        profile.setLocale(Locale.CANADA);
        profile.setMobilePhone("555-555-1111");
        profile.setName("JUnit Test User");
        profile.setOrganization("thinkParity Solutions Inc.");
        profile.setOrganizationLocale(Locale.CANADA);
        profile.setPhone("555-555-2222");
        profile.setPostalCode("A7AC0C");
        profile.setProvince("Province");
        profile.setTimeZone(TimeZone.getDefault());
        profile.setTitle("Title");

        final SecurityCredentials securityCredentials = new SecurityCredentials();
        securityCredentials.setQuestion("What is my username?");
        securityCredentials.setAnswer(profile.getSimpleUsername());
        
        final PaymentPlanCredentials paymentPlanCredentials = new PaymentPlanCredentials();
        paymentPlanCredentials.setName("Test Payment Plan");
        paymentPlanCredentials.setPassword("Test Payment Plan");
        try {
            getModel(datum.junit).create(usernameReservation, emailReservation,
                    credentials, profile, email, securityCredentials,
                    paymentPlanCredentials);
        } catch (final InvalidCredentialsException icx) {
            fail(icx, "Profile payment plan credentials are invalid.");
        } catch (final ReservationExpiredException rex) {
            fail(rex, "Profile reservation has expired.");
        }

        login(datum.junit);
        datum.waitForEvents(datum.junit);
    }

    /**
     * Test determination of email availability.
     *
     */
    public void testIsEmailAvailable() {
        // add an e-mail to a contact
        String emailInjection = "+" + String.valueOf(System.currentTimeMillis());
        EMail email = EMailBuilder.parse("junit" + emailInjection + "@thinkparity.com");
        try {
            getModel(datum.junit).updateEMail(email);
        } catch (final EMailIntegrityException emix) {
            fail(emix, "Cannot update e-mail.");
        }
        assertFalse("Email availability incorrect.", getModel(datum.junit_x).isAvailable(email));

        // add an e-mail to a non-contact
        emailInjection = "+" + String.valueOf(System.currentTimeMillis());
        email = EMailBuilder.parse("junit" + emailInjection + "@thinkparity.com");
        try {
            getModel(datum.junit_w).updateEMail(email);
        } catch (final EMailIntegrityException emix) {
            fail(emix, "Cannot update e-mail.");
        }
        assertFalse("Email availability incorrect.", getModel(datum.junit_x).isAvailable(email));

        emailInjection = "+" + String.valueOf(System.currentTimeMillis());
        email = EMailBuilder.parse("junit" + emailInjection + "@thinkparity.com");
        assertTrue("Email availability incorrect.", getModel(datum.junit_x).isAvailable(email));
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
        profile.setLocale(Locale.CANADA);
        profile.setMobilePhone("888-555-1111" + updateSuffix);
        profile.setPhone("888-555-1111" + updateSuffix);
        getModel(datum.junit).addListener(datum.update_listener);
        getModel(datum.junit).update(profile);
        datum.waitForEvents();
        getModel(datum.junit).removeListener(datum.update_listener);
        assertTrue("Update event not fired.", datum.update_notify);
    }

    /**
     * Test adding an email address.
     *
     */
    public void testUpdateEMail() {
        final String emailInjection = "+" + String.valueOf(System.currentTimeMillis());
        final EMail email = EMailBuilder.parse("junit" + emailInjection + "@thinkparity.com");
        getModel(datum.junit).addListener(datum.add_email_listener);
        try {
            getModel(datum.junit).updateEMail(email);
        } catch (final EMailIntegrityException emix) {
            fail(emix, "Cannot update e-mail.");
        }
        datum.waitForEvents();
        getModel(datum.junit).removeListener(datum.add_email_listener);
        assertTrue("Email added event not fired.", datum.update_email_notify);
    }

    /**
     * Test updating the password.
     *
     */
    public void testUpdatePassword() {
        final Credentials credentials = datum.junit.getCredentials();
        final String originalPassword = credentials.getPassword();
        final String newPassword = "ASKJSL!)!(#^)!:@#{}";

        // test update password
        getModel(datum.junit).addListener(datum.update_password_listener);
        try {
            getModel(datum.junit).updatePassword(credentials, newPassword);
        } catch (final InvalidCredentialsException icx) {
            fail(icx, "Cannot update password.");
        }
        datum.waitForEvents();
        getModel(datum.junit).removeListener(datum.update_password_listener);
        // change it back
        credentials.setPassword(newPassword);
        try {
            getModel(datum.junit).updatePassword(credentials, originalPassword);
        } catch (final InvalidCredentialsException icx) {
            fail(icx, "Cannot update password.");
        }
        assertTrue("Update password event not fired.", datum.update_password_notify);
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
        login(datum.junit_w);
        login(datum.junit_x);
    }

    /**
     * Tear down the profile test.
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_w);
        logout(datum.junit_x);
        datum = null;
        super.tearDown();
    }

    /**
     * <b>Title:</b>Profile Test Fixture<br>
     * <b>Description:</b><br>
     */
    private class Fixture extends ProfileTestCase.Fixture {
        private final ProfileListener add_email_listener;
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_w;
        private final OpheliaTestUser junit_x;
        private boolean update_email_notify;
        private final ProfileListener update_listener;
        private boolean update_notify;
        private final ProfileListener update_password_listener;
        private boolean update_password_notify;
        /**
         * Create Fixture.
         *
         * @param junit
         *      The junit test user.
         */
        private Fixture() {
            super();
            this.junit = OpheliaTestUser.JUNIT;
            this.junit_w = OpheliaTestUser.JUNIT_W;
            this.junit_x = OpheliaTestUser.JUNIT_X;
            this.add_email_listener = new ProfileAdapter() {
                @Override
                public void emailUpdated(final ProfileEvent e) {
                    assertNotNull("Profile event is null.", e);
                    assertNotNull("Profile event profile is null.", e.getProfile());
                    assertNotNull("Profile event email null.", e.getEmail());
                    update_email_notify = true;
                }
            };
            this.update_email_notify = false;
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
            addQueueHelper(junit);
        }
    }
}

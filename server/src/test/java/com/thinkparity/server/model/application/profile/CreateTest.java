/*
 * Created On:  20-Sep-07 9:25:57 AM
 */
package com.thinkparity.desdemona.model.profile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.thinkparity.codebase.assertion.TrueAssertion;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.desdemona.model.Constants;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Create Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateTest extends ProfileTestCase {

    /** Test name. */
    private static final String NAME = "Test create";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create CreateTest.
     *
     */
    public CreateTest() {
        super(NAME);
    }

    /**
     * Test creating a profile.
     * 
     */
    public void testCreateAllFeatures() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test create profile.");
        setUpCreate();
        try {
            final String username = datum.newUniqueUsername();
            final EMail email = datum.newEMail(username);
            final UsernameReservation usernameReservation =
                datum.getProfileModel(datum.create).createUsernameReservation(username);
            final EMailReservation emailReservation =
                datum.getProfileModel(datum.create).createEMailReservation(email);
            final Credentials credentials = new Credentials();
            credentials.setPassword(datum.lookupPassword(username));
            credentials.setUsername(username);
            final Profile profile = new Profile();
            profile.setVCard(new ProfileVCard());
            profile.setFeatures(datum.allFeatures);
            profile.setLocale(Locale.getDefault());
            profile.setName("Test User:" + getName());
            profile.setOrganization("thinkParity Solutions Inc.");
            profile.setOrganizationCountry(profile.getCountry());
            profile.setTimeZone(TimeZone.getDefault());
            profile.setTitle("Test User");
            final SecurityCredentials securityCredentials = new SecurityCredentials();
            securityCredentials.setAnswer(username);
            securityCredentials.setQuestion(username);
            final PaymentInfo paymentInfo = datum.newPaymentInfo();

            datum.getProfileModel(datum.create).create(datum.product,
                    datum.release, usernameReservation, emailReservation,
                    credentials, profile, email, securityCredentials,
                    paymentInfo);
        } finally {
            tearDownCreate();
        }
    }

    /**
     * Test creating a profile without specifying payment info.
     * 
     */
    public void testCreateAllFeaturesNoPayment() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test create profile without payment info.");
        setUpCreate();
        try {
            final String username = datum.newUniqueUsername();
            final EMail email = datum.newEMail(username);
            final UsernameReservation usernameReservation =
                datum.getProfileModel(datum.create).createUsernameReservation(username);
            final EMailReservation emailReservation =
                datum.getProfileModel(datum.create).createEMailReservation(email);
            final Credentials credentials = new Credentials();
            credentials.setPassword(datum.lookupPassword(username));
            credentials.setUsername(username);
            final Profile profile = new Profile();
            profile.setVCard(new ProfileVCard());
            profile.setFeatures(datum.allFeatures);
            profile.setLocale(Locale.getDefault());
            profile.setName("Test User:" + getName());
            profile.setOrganization("thinkParity Solutions Inc.");
            profile.setOrganizationCountry(profile.getCountry());
            profile.setTimeZone(TimeZone.getDefault());
            profile.setTitle("Test User");
            final SecurityCredentials securityCredentials = new SecurityCredentials();
            securityCredentials.setAnswer(username);
            securityCredentials.setQuestion(username);

            boolean didFail = false;
            try {
                datum.getProfileModel(datum.create).create(datum.product,
                        datum.release, usernameReservation, emailReservation,
                        credentials, profile, email, securityCredentials);
            } catch (final TrueAssertion ta) {
                if (ta.getMessage().startsWith("Payment required for ")) {
                    didFail = true;
                }
            }

            assertTrue("Create user did not fail without payment info.", didFail);
        } finally {
            tearDownCreate();
        }
    }

    /**
     * Test creating a profile without any features; without any payment.
     * 
     */
    public void testCreateNoFeaturesNoPayment() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test create no features no payment.");
        setUpCreate();
        try {
            final String username = datum.newUniqueUsername();
            final EMail email = datum.newEMail(username);
            final UsernameReservation usernameReservation =
                datum.getProfileModel(datum.create).createUsernameReservation(username);
            final EMailReservation emailReservation =
                datum.getProfileModel(datum.create).createEMailReservation(email);
            final Credentials credentials = new Credentials();
            credentials.setPassword(datum.lookupPassword(username));
            credentials.setUsername(username);
            final Profile profile = new Profile();
            profile.setVCard(new ProfileVCard());
            profile.setFeatures(datum.guestFeatures);
            profile.setLocale(Locale.getDefault());
            profile.setName("Test User:" + getName());
            profile.setOrganization("thinkParity Solutions Inc.");
            profile.setOrganizationCountry(profile.getCountry());
            profile.setTimeZone(TimeZone.getDefault());
            profile.setTitle("Test User");
            final SecurityCredentials securityCredentials = new SecurityCredentials();
            securityCredentials.setAnswer(username);
            securityCredentials.setQuestion(username);

            datum.getProfileModel(datum.create).create(datum.product,
                    datum.release, usernameReservation, emailReservation,
                    credentials, profile, email, securityCredentials);
        } finally {
            tearDownCreate();
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
        datum.product = datum.readOpheliaProduct();
        datum.release = datum.readOpheliaLatestRelease();
        datum.allFeatures = datum.readOpheliaFeatures();
        datum.guestFeatures = new ArrayList<Feature>(datum.allFeatures.size());
        datum.guestFeatures.addAll(datum.allFeatures);
        final Iterator<Feature> iFeatures = datum.guestFeatures.iterator();
        while (iFeatures.hasNext()) {
            if (iFeatures.next().getName().equals(
                    Constants.Product.Ophelia.Feature.CORE)) {
                iFeatures.remove();
            }
        }
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

    /**
     * Set up create.
     * 
     */
    private void setUpCreate() {
        datum.create = new AuthToken();
    }

    /**
     * Tear down create.
     * 
     */
    private void tearDownCreate() {
        datum.create = null;
    }

    /** <b>Title:</b>Create Test Fixture<br> */
    private class Fixture extends ProfileTestCase.Fixture {
        private List<Feature> allFeatures;
        private AuthToken create;
        private List<Feature> guestFeatures;
        private Product product;
        private Release release;
    }
}

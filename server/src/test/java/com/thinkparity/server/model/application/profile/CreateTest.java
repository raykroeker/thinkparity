/*
 * Created On:  20-Sep-07 9:25:57 AM
 */
package com.thinkparity.desdemona.model.profile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials;
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
     * Test creating a profile with all features and payment info.
     * 
     */
    public void testCreateAllFeaturesInfo() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test create all features info.");
        setUpCreate();
        try {
            final String username = datum.newUniqueUsername();
            final EMail email = datum.newEMail(username);
            final EMailReservation emailReservation =
                datum.getProfileModel(datum.create).createEMailReservation(email);
            final Profile profile = datum.newProfile(datum.allFeatures);
            final UsernameReservation usernameReservation =
                datum.getProfileModel(datum.create).createUsernameReservation(profile);
            final Credentials credentials = new Credentials();
            credentials.setPassword(datum.lookupPassword(username));
            credentials.setUsername(usernameReservation.getUsername());
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
    public void testCreateAllFeaturesNoInfo() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test create all features no info.");
        setUpCreate();
        try {
            final String username = datum.newUniqueUsername();
            final EMail email = datum.newEMail(username);
            final EMailReservation emailReservation =
                datum.getProfileModel(datum.create).createEMailReservation(email);
            final Profile profile = datum.newProfile(datum.allFeatures);
            final UsernameReservation usernameReservation =
                datum.getProfileModel(datum.create).createUsernameReservation(profile);
            final Credentials credentials = new Credentials();
            credentials.setPassword(datum.lookupPassword(username));
            credentials.setUsername(usernameReservation.getUsername());
            final SecurityCredentials securityCredentials = new SecurityCredentials();
            securityCredentials.setAnswer(username);
            securityCredentials.setQuestion(username);

            boolean didFail = false;
            try {
                datum.getProfileModel(datum.create).create(datum.product,
                        datum.release, usernameReservation, emailReservation,
                        credentials, profile, email, securityCredentials);
            } catch (final ThinkParityException tpx) {
                if (tpx.getMessage().startsWith("Payment required for ")) {
                    didFail = true;
                }
            }

            assertTrue("Create user did not fail without payment info.", didFail);
        } finally {
            tearDownCreate();
        }
    }


    /**
     * Test creating a profile with all features and a payment plan.
     * 
     */
    public void testCreateAllFeaturesPlan() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test create all features plan.");
        setUpCreate();
        try {
            final String username = datum.newUniqueUsername();
            final EMail email = datum.newEMail(username);
            final EMailReservation emailReservation =
                datum.getProfileModel(datum.create).createEMailReservation(email);
            final Profile profile = datum.newProfile(datum.allFeatures);
            final UsernameReservation usernameReservation =
                datum.getProfileModel(datum.create).createUsernameReservation(profile);
            final Credentials credentials = new Credentials();
            credentials.setPassword(datum.lookupPassword(username));
            credentials.setUsername(usernameReservation.getUsername());
            final SecurityCredentials securityCredentials = new SecurityCredentials();
            securityCredentials.setAnswer(username);
            securityCredentials.setQuestion(username);
            final PaymentPlanCredentials paymentPlanCredentials = datum.newPaymentPlanCredentials();

            try {
                datum.getProfileModel(datum.create).create(datum.product,
                        datum.release, usernameReservation, emailReservation,
                        credentials, profile, email, securityCredentials,
                        paymentPlanCredentials);
            } catch (final InvalidCredentialsException icx) {
                fail(icx, "Could not create profile with payment plan.");
            }
        } finally {
            tearDownCreate();
        }
    }

    /**
     * Test creating a profile with all features and a payment plan.
     * 
     */
    public void testCreateAllFeaturesPlanInvalidCredentials() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test create all features plan; invalid credentials.");
        setUpCreate();
        try {
            final String username = datum.newUniqueUsername();
            final EMail email = datum.newEMail(username);
            final EMailReservation emailReservation =
                datum.getProfileModel(datum.create).createEMailReservation(email);
            final Profile profile = datum.newProfile(datum.allFeatures);
            final UsernameReservation usernameReservation =
                datum.getProfileModel(datum.create).createUsernameReservation(profile);
            final Credentials credentials = new Credentials();
            credentials.setPassword(datum.lookupPassword(username));
            credentials.setUsername(usernameReservation.getUsername());
            final SecurityCredentials securityCredentials = new SecurityCredentials();
            securityCredentials.setAnswer(username);
            securityCredentials.setQuestion(username);
            final PaymentPlanCredentials paymentPlanCredentials = new PaymentPlanCredentials();
            paymentPlanCredentials.setName(String.valueOf(System.currentTimeMillis()));
            paymentPlanCredentials.setPassword(paymentPlanCredentials.getName());

            boolean didThrow = false;
            try {
                datum.getProfileModel(datum.create).create(datum.product,
                        datum.release, usernameReservation, emailReservation,
                        credentials, profile, email, securityCredentials,
                        paymentPlanCredentials);
            } catch (final InvalidCredentialsException icx) {
                didThrow = true;
            }
            assertTrue("Did not throw expected invalid credentials exception.", didThrow);
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
            final EMailReservation emailReservation =
                datum.getProfileModel(datum.create).createEMailReservation(email);
            final Profile profile = datum.newProfile(datum.guestFeatures);
            final UsernameReservation usernameReservation =
                datum.getProfileModel(datum.create).createUsernameReservation(profile);
            final Credentials credentials = new Credentials();
            credentials.setPassword(datum.lookupPassword(username));
            credentials.setUsername(usernameReservation.getUsername());
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
            if (Feature.Name.CORE == iFeatures.next().getName()) {
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

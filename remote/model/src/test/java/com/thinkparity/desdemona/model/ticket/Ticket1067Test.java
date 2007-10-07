/*
 * Created On:  6-Oct-07 3:14:38 PM
 */
package com.thinkparity.desdemona.model.ticket;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.thinkparity.codebase.constraint.StringConstraint;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileConstraints;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Ticket 1067 Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Ticket1067Test extends TicketTestCase {

    private static final String NAME = "Test ticket ";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket1067Test.
     *
     */
    public Ticket1067Test() {
        super(NAME);
    }

    /**
     * Test creating a profile with the maximum length allowed for profile
     * string fields.
     * 
     */
    public void testTicket1067() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test create profile maximum length fields.");
        setUpTicket1067();
        try {
            final StringBuilder buffer = new StringBuilder();
            final String username = datum.newUniqueUsername();
            final EMail email = datum.newEMail(username);
            final UsernameReservation usernameReservation =
                datum.getProfileModel(datum.createAs).createUsernameReservation(username);
            final EMailReservation emailReservation =
                datum.getProfileModel(datum.createAs).createEMailReservation(email);
            final Credentials credentials = new Credentials();
            credentials.setPassword(datum.lookupPassword(username));
            credentials.setUsername(username);
            final ProfileConstraints constraints = ProfileConstraints.getInstance();
            final Profile profile = new Profile();
            profile.setVCard(new ProfileVCard());
            profile.setFeatures(datum.allFeatures);
            buffer.setLength(0);
            buffer.append("Address_");
            padToMaximumLength(buffer, constraints.getAddress());
            profile.setAddress(buffer.toString());
            buffer.setLength(0);
            buffer.append("City_");
            padToMaximumLength(buffer, constraints.getCity());
            profile.setCity(buffer.toString());
            profile.setLocale(Locale.getDefault());
            buffer.setLength(0);
            buffer.append("MobilePhone_");
            padToMaximumLength(buffer, constraints.getMobilePhone());
            buffer.setLength(0);
            buffer.append("Name_").append(getName());
            padToMaximumLength(buffer, constraints.getName());
            profile.setName(buffer.toString());
            buffer.setLength(0);
            buffer.append("Organization_");
            padToMaximumLength(buffer, constraints.getOrganization());
            profile.setOrganization(buffer.toString());
            profile.setOrganizationCountry(profile.getCountry());
            buffer.setLength(0);
            buffer.append("Phone_");
            padToMaximumLength(buffer, constraints.getPhone());
            profile.setPhone(buffer.toString());
            buffer.setLength(0);
            buffer.append("PostalCode_");
            padToMaximumLength(buffer, constraints.getPostalCode());
            profile.setPostalCode(buffer.toString());
            buffer.setLength(0);
            buffer.append("Province_");
            padToMaximumLength(buffer, constraints.getProvince());
            profile.setProvince(buffer.toString());
            profile.setTimeZone(TimeZone.getDefault());
            buffer.setLength(0);
            buffer.append("Title_");
            padToMaximumLength(buffer, constraints.getTitle());
            profile.setTitle(buffer.toString());
            final SecurityCredentials securityCredentials = new SecurityCredentials();
            securityCredentials.setAnswer(username);
            securityCredentials.setQuestion(username);
            final PaymentInfo paymentInfo = datum.newPaymentInfo();

            datum.getProfileModel(datum.createAs).create(datum.product,
                    datum.release, usernameReservation, emailReservation,
                    credentials, profile, email, securityCredentials,
                    paymentInfo);

            final AuthToken readAs = datum.login(username);
            final Profile exepectation = datum.getProfileModel(readAs).read();
            assertEquals("Profile address does not match expectation.", profile.getAddress(), exepectation.getAddress());
            assertEquals("Profile city does not match expectation.", profile.getCity(), exepectation.getCity());
            assertEquals("Profile locale does not match expectation.", profile.getLocale(), exepectation.getLocale());
            assertEquals("Profile name does not match expectation.", profile.getName(), exepectation.getName());
            assertEquals("Profile organization does not match expectation.", profile.getOrganization(), exepectation.getOrganization());
            assertEquals("Profile organization country does not match expectation.", profile.getOrganizationCountry(), exepectation.getOrganizationCountry());
            assertEquals("Profile phone does not match expectation.", profile.getPhone(), exepectation.getPhone());
            assertEquals("Profile postal code does not match expectation.", profile.getPostalCode(), exepectation.getPostalCode());
            assertEquals("Profile province does not match expectation.", profile.getProvince(), exepectation.getProvince());
            assertEquals("Profile time zone does not match expectation.", profile.getTimeZone(), exepectation.getTimeZone());
            assertEquals("Profile title does not match expectation.", profile.getTitle(), exepectation.getTitle());
        } finally {
            tearDownTicket1067();
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture();
        datum.product = datum.readOpheliaProduct();
        datum.release = datum.readOpheliaLatestRelease();
        datum.allFeatures = datum.readOpheliaFeatures();
    }

    /**
     * @see com.thinkparity.desdemona.model.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }

    /**
     * Pad the buffer to the constraint's maximum length.
     * 
     * @param buffer
     *            A <code>StringBuilder</code>.
     * @param constraint
     *            A <code>StringConstraint</code>.
     */
    private void padToMaximumLength(final StringBuilder buffer,
            final StringConstraint constraint) {
        while (buffer.length() < constraint.getMaxLength()) {
            buffer.append('X');
        }
    }

    /**
     * Set up the test.
     * 
     */
    private void setUpTicket1067() {
        datum.createAs = new AuthToken();
    }

    /**
     * Tear down the test.
     * 
     */
    private void tearDownTicket1067() {
        datum.createAs = new AuthToken();
    }

    /** <b>Title:</b>Ticket Test Fixture<br> */
    private class Fixture extends TicketTestCase.Fixture {
        private List<Feature> allFeatures;
        private AuthToken createAs;
        private Product product;
        private Release release;
    }
}

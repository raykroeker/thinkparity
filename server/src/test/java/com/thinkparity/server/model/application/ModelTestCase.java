/*
 * Created On:  8-Sep-07 10:16:46 AM
 */
package com.thinkparity.desdemona.model;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.Constants.ChecksumAlgorithm;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.profile.VerificationKey;
import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.codec.MD5Util;

import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.backup.BackupModel;
import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.model.container.ContainerModel;
import com.thinkparity.desdemona.model.migrator.MigratorModel;
import com.thinkparity.desdemona.model.profile.ProfileModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.session.SessionModel;
import com.thinkparity.desdemona.util.DateTimeProvider;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Abstract Test Case<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ModelTestCase extends TestCase {

    /** A log4j wrapper. */
    protected static final Log4JWrapper TEST_LOGGER;

    /** A shared buffer. */
    private static final Buffer BUFFER;

    /** The ophelia product name. */
    private static final String OPHELIA_PRODUCT_NAME = "OpheliaProduct";

    static {
        BUFFER = new Buffer();
        TEST_LOGGER = new Log4JWrapper("TEST_DEBUGGER");
    }

    /**
     * Create a formatted failure message.
     * 
     * @param cause
     *            The <code>Throwable</code> cause of the failure.
     * @param The
     *            message <code>String</code>. The message
     *            <code>Object[]</code> arguments.
     * @return The failure message.
     */
    protected static String createFailMessage(final Throwable cause,
            final String message, final Object... arguments) {
        final StringBuilder failMessageBuilder = new StringBuilder(
                MessageFormat.format(message, arguments))
            .append(Separator.SystemNewLine);
        failMessageBuilder.append(StringUtil.printStackTrace(cause));
        return failMessageBuilder.toString();
    }

    /**
     * Fail a test.
     * 
     * @param message
     *            A fail message <code>String</code>.
     * @param arguments
     *            The fail message arguments <code>Object[]</code>.
     */
    protected static final void fail(final Throwable cause,
            final String message, final Object... arguments) {
        fail(createFailMessage(cause, message, arguments));
    }

    /** An e-mail cache. */
    private final Cache<EMail> emailCache;

    /** A class loader. */
    private final ClassLoader loader;

    /** A session model. */
    private final SessionModel sessionModel;

    /** A user cache. */
    private final Cache<User> userCache;

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#TestCase(String)
     * 
     */
    protected ModelTestCase(final String name) {
        super(name);
        this.loader = Thread.currentThread().getContextClassLoader();
        this.sessionModel = ModelFactory.getInstance(loader).getSessionModel();

        this.emailCache = new Cache<EMail>();
        this.userCache = new Cache<User>();
    }

    /**
     * @see junit.framework.TestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        emailCache.clear();
        userCache.clear();

        super.tearDown();
    }

    /**
     * Checksum a readable byte channel.
     * 
     * @param channel
     *            A <code>ReadableByteChannel</code>.
     * @return A <code>String</code>.
     */
    private String checksum(final ReadableByteChannel channel) {
        try {
            synchronized (BUFFER.getLock()) {
                return MD5Util.md5Base64(channel, BUFFER.getArray());
            }
        } catch (final IOException iox) {
            throw new AssertionFailedError(createFailMessage(iox,
                    "Could not checksum channel."));
        }
    }

    /**
     * Create a profile.
     * 
     * @param username
     *            A <code>String</code>.
     * @return A <code>Profile</code>.
     */
    private Profile createProfile(final String username) {
        /* product */
        final Product product = readOpheliaProduct();
        /* release */
        final Release release = readOpheliaLatestRelease();
        /* features */
        final List<Feature> features = readOpheliaFeatures();

        final ProfileModel profileModel = getProfileModel();
        final EMail email = newEMail(username);
        final UsernameReservation usernameReservation = profileModel.createUsernameReservation(username);
        final EMailReservation emailReservation = profileModel.createEMailReservation(email);
        final Credentials credentials = new Credentials();
        credentials.setPassword(lookupPassword(username));
        credentials.setUsername(username);
        final Profile profile = newProfile(features);
        final SecurityCredentials securityCredentials = new SecurityCredentials();
        securityCredentials.setAnswer(username);
        securityCredentials.setQuestion(username);
        final PaymentInfo paymentInfo = newPaymentInfo();

        profileModel.create(product, release, usernameReservation,
                emailReservation, credentials, profile, email,
                securityCredentials, paymentInfo);
        return profile;
    }

    /**
     * Find an incoming e-mail invitation for the corresponing outgoing e-mail
     * invitation. If a corresponding invitation cannot be found; null is
     * returned.
     * 
     * @param contactModel
     *            A <code>ContactModel</code>.
     * @param createdBy
     *            A <code>User</code>.
     * @param createdOn
     *            A <code>Calendar</code>.
     * @param invitationEMail
     *            An <code>EMail</code>.
     * @return An <code>IncomingEMailInvitation</code>.
     */
    private IncomingEMailInvitation findIncomingEMailInvitation(
            final ContactModel contactModel, final User createdBy,
            final Calendar createdOn, final EMail invitationEMail) {
        final List<IncomingEMailInvitation> incomingEMails = contactModel.readIncomingEMailInvitations();
        for (final IncomingEMailInvitation iei : incomingEMails) {
            if (iei.getCreatedBy().equals(createdBy)) {
                if (iei.getCreatedOn().equals(createdOn)) {
                    if (iei.getInvitationEMail().equals(invitationEMail)) {
                        return iei;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find an incoming e-mail invitation for the corresponing outgoing e-mail
     * invitation. If a corresponding invitation cannot be found; null is
     * returned.
     * 
     * @param contactModel
     *            A <code>ContactModel</code>.
     * @param createdBy
     *            A <code>User</code>.
     * @param invitationEMail
     *            An <code>EMail</code>.
     * @return An <code>IncomingEMailInvitation</code>.
     */
    private IncomingEMailInvitation findIncomingEMailInvitation(
            final ContactModel contactModel, final User createdBy,
            final EMail invitationEMail) {
        final List<IncomingEMailInvitation> incomingEMails = contactModel.readIncomingEMailInvitations();
        for (final IncomingEMailInvitation iei : incomingEMails) {
            if (iei.getCreatedBy().equals(createdBy)) {
                if (iei.getInvitationEMail().equals(invitationEMail)) {
                    return iei;
                }
            }
        }
        return null;
    }

    /**
     * Obtain an artifact model.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return An instance of <code>ArtifactModel</code>.
     */
    private ArtifactModel getArtifactModel(final AuthToken authToken) {
        return getModelFactory(authToken).getArtifactModel();
    }

    /**
     * Obtain a backup model.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return An instance of <code>BackupModel</code>.
     */
    private BackupModel getBackupModel(final AuthToken authToken) {
        return getModelFactory(authToken).getBackupModel();
    }

    /**
     * Obtain the checksum algorithm name.
     * 
     * @return A <code>String</code>.
     */
    private String getChecksumAlgorithm() {
        return ChecksumAlgorithm.MD5.name();
    }

    /**
     * Obtain a contact model.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return An instance of <code>ContactModel</code>.
     */
    private ContactModel getContactModel(final AuthToken authToken) {
        return getModelFactory(authToken).getContactModel();
    }

    /**
     * Obtain a container model.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return An instance of <code>ContainerModel</code>.
     */
    private ContainerModel getContainerModel(final AuthToken authToken) {
        return getModelFactory(authToken).getContainerModel();
    }

    /**
     * Obtain a migrator model.
     * 
     * @return An instance of <code>MigratorModel</code>.
     */
    private MigratorModel getMigratorModel() {
        return getModelFactory().getMigratorModel();
    }

    /**
     * Obtain a model factory.
     * 
     * @return A <code>ModelFactory</code>.
     */
    private ModelFactory getModelFactory() {
        return ModelFactory.getInstance(loader);
    }

    /**
     * Obtain a model factory.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return A <code>ModelFactory</code>.
     */
    private ModelFactory getModelFactory(final AuthToken authToken) {
        return ModelFactory.getInstance(readUser(readSession(authToken)), loader);
    }

    /**
     * Obtain a profile model.
     * 
     * @return An instance of <code>ProfileModel</code>.
     */
    private ProfileModel getProfileModel() {
        return getModelFactory().getProfileModel();
    }

    /**
     * Obtain a profile model.
     * 
     * @return An instance of <code>ProfileModel</code>.
     */
    private ProfileModel getProfileModel(final AuthToken authToken) {
        return getModelFactory(authToken).getProfileModel();
    }

    /**
     * Login.
     * 
     * @param credentials
     *            A set of <code>Credentials</code>.
     * @return An <code>AuthToken</code>.
     */
    private AuthToken login(final Credentials credentials)
            throws InvalidCredentialsException {
        return sessionModel.login(credentials);
    }

    /**
     * Login.
     * 
     * @param username
     *            A <code>String</code>.
     * @param password
     *            A <code>String</code>.
     * @return An <code>AuthToken</code>.
     */
    private AuthToken login(final String username, final String password) {
        final Credentials credentials = new Credentials();
        credentials.setPassword(password);
        credentials.setUsername(username);
        try {
            return login(credentials);
        } catch (final InvalidCredentialsException icx) {
            throw new AssertionFailedError(createFailMessage(icx,
                    "Could not login using given credentials.", credentials));
        }
    }

    /**
     * Logout.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     */
    private void logout(final AuthToken authToken) {
        logout(authToken.getSessionId());
    }


    /**
     * Logout.
     * 
     * @param sessionId
     *            A <code>String</code>.
     */
    private void logout(final String sessionId) {
        sessionModel.logout(sessionId);
    }

    /**
     * Lookup an e-mail from the cache; and if the e-mail is not in the cache;
     * read the e-mail and cache the information.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return A <code>EMail</code>.
     */
    private EMail lookupEMail(final AuthToken authToken) {
        if (emailCache.contains(authToken)) {
            return emailCache.lookup(authToken);
        } else {
            return emailCache.cache(authToken, readEMail(authToken));
        }
    }

    /**
     * Lookup a password for a username.
     * 
     * @param username
     *            A <code>String</code>.
     * @return A <code>String</code>.
     */
    private String lookupPassword(final String username) {
        return "parity";
    }

    /**
     * Lookup a user from the cache; and if the user is not in the cache; read
     * the user and cache the information.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return A <code>User</code>.
     */
    private User lookupUser(final AuthToken authToken) {
        if (userCache.contains(authToken)) {
            return userCache.lookup(authToken);
        } else {
            return userCache.cache(authToken, readUser(authToken));
        }
    }

    /**
     * Instantiate an e-mail address for a username.
     * 
     * @param username
     *            A <code>String</code>.
     * @return An <code>EMail</code>.
     */
    private EMail newEMail(final String username) {
        return EMailBuilder.parse("application+" + username + "@thinkparity.com");
    }

    /**
     * Instantiate valid payment info.
     * 
     * @return A <code>PaymentInfo</code>.
     */
    private PaymentInfo newPaymentInfo() {
        final PaymentInfo paymentInfo = new PaymentInfo();
        final Calendar now = now();
        paymentInfo.setCardExpiryMonth(Short.valueOf(Integer.valueOf(now.get(Calendar.MONTH) + 1).shortValue()));
        paymentInfo.setCardExpiryYear(Short.valueOf(Integer.valueOf(now.get(Calendar.YEAR)).shortValue()));
        paymentInfo.setCardName(PaymentInfo.CardName.MASTER_CARD);
        paymentInfo.setCardNumber("5454545454545454");
        return paymentInfo;
    }

    /**
     * Instantiate valid payment plan credentials.
     * 
     * @return A set of <code>PaymentPlanCredentials</code>.
     */
    private PaymentPlanCredentials newPaymentPlanCredentials() {
        final PaymentPlanCredentials paymentPlanCredentials = new PaymentPlanCredentials();
        paymentPlanCredentials.setName("thinkParity Solutions Inc.");
        paymentPlanCredentials.setPassword("password");
        return paymentPlanCredentials;
    }

    /**
     * Instantiate a profile with the bare minimum field values set in order for
     * creation.
     * 
     * @param features
     *            A <code>List<Feature></code>.
     * @return A <code>Profile</code>.
     */
    private Profile newProfile(final List<Feature> featureList) {
        final Profile profile = new Profile();
        profile.setFeatures(featureList);
        profile.setVCard(new ProfileVCard());

        profile.setAddress("Address");
        profile.setCity("City");
        profile.setLocale(Locale.getDefault());
        profile.setName(MessageFormat.format("Name_{0}", getName()));
        profile.setOrganization("Organization");
        profile.setOrganizationCountry(profile.getCountry());
        profile.setPostalCode("PostalCode");
        profile.setProvince("Province");
        profile.setTimeZone(TimeZone.getDefault());
        profile.setTitle("Title");
        return profile;
    }

    /**
     * Create a new unique username.
     * 
     * @return A <code>String</code>.
     */
    private String newUniqueUsername() {
        return "test_" + System.currentTimeMillis();
    }

    /**
     * Obtain the current date/time.
     * 
     * @return A <code>Calendar</code>.
     */
    private Calendar now() {
        return DateTimeProvider.getCurrentDateTime();
    }

    /**
     * Read an e-mail address.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return A <code>EMail</code>.
     */
    private EMail readEMail(final AuthToken authToken) {
        return getModelFactory(authToken).getProfileModel().readEMail().getEmail();
    }

    /**
     * Read the features for the product.
     * 
     * @return A <code>List<Feature></code>.
     */
    private List<Feature> readOpheliaFeatures() {
        return getMigratorModel().readProductFeatures(OPHELIA_PRODUCT_NAME);
    }

    /**
     * Read the latest release for the ophelia product on the os.
     * 
     * @return A <code>Release</code>.
     */
    private Release readOpheliaLatestRelease() {
        return readOpheliaLatestRelease(OSUtil.getOS());
    }

    /**
     * Read the latest release for the ophelia product for the os.
     * 
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    private Release readOpheliaLatestRelease(final OS os) {
        return getMigratorModel().readLatestRelease(OPHELIA_PRODUCT_NAME, os);
    }

    /**
     * Read the ophelia product.
     * 
     * @return A <code>Product</code>.
     */
    private Product readOpheliaProduct() {
        return getMigratorModel().readProduct(OPHELIA_PRODUCT_NAME);
    }

    /**
     * Read a session for the authentiation token. If the token is null; or the
     * session has expired null is returned.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return A <code>Session</code>.
     */
    private Session readSession(final AuthToken authToken) {
        if (null == authToken) {
            return null;
        } else {
            return sessionModel.readSession(authToken.getSessionId());
        }
    }

    /**
     * Read a user.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return A <code>User</code>.
     */
    private User readUser(final AuthToken authToken) {
        return getModelFactory(authToken).getProfileModel().read();
    }

    /**
     * Read a user for a session. If the session is null; or the session has
     * expired null is returned.
     * 
     * @param session
     *            A <code>Session</code>.
     * @return A <code>User</code>.
     */
    private User readUser(final Session session) {
        if (null == session) {
            return null;
        } else {
            return sessionModel.readUser(session.getId());
        }
    }

    /**
     * Verify an e-mail address.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     */
    private void verifyEMail(final AuthToken authToken) {
        final EMail email = lookupEMail(authToken);
        final VerificationKey verificationKey = VerificationKey.generate(email);
        getProfileModel(authToken).verifyEMail(email, verificationKey.getKey());
    }

    /** <b>Title:</b>Model Test Fixture<br> */
    protected abstract class Fixture {

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#checksum(ReadableByteChannel)
         * 
         */
        public String checksum(final ReadableByteChannel channel) {
            return ModelTestCase.this.checksum(channel);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#createProfile(String)
         * 
         */
        public Profile createProfile(final String username) {
            return ModelTestCase.this.createProfile(username);
        }

        /**
         * @see com.thinkparity.desdemona.model.contact.ContactTestCase#findIncomingEMail(ContactModel,
         *      User, Calendar, EMail)
         * 
         */
        public IncomingEMailInvitation findIncomingEMailInvitation(
                final AuthToken authToken, final User createdBy,
                final Calendar createdOn, final EMail email) {
            return ModelTestCase.this.findIncomingEMailInvitation(
                    getContactModel(authToken), createdBy, createdOn, email);
        }

        /**
         * @see com.thinkparity.desdemona.model.contact.ContactTestCase#findIncomingEMail(ContactModel,
         *      User, EMail)
         * 
         */
        public IncomingEMailInvitation findIncomingEMailInvitation(
                final AuthToken authToken, final User createdBy,
                final EMail email) {
            return ModelTestCase.this.findIncomingEMailInvitation(
                    getContactModel(authToken), createdBy, email);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#getArtifactModel(AuthToken)
         * 
         */
        public ArtifactModel getArtifactModel(final AuthToken authToken) {
            return ModelTestCase.this.getArtifactModel(authToken);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#getBackupModel(AuthToken)
         * 
         */
        public BackupModel getBackupModel(final AuthToken authToken) {
            return ModelTestCase.this.getBackupModel(authToken);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#getChecksumAlgorithm()
         * 
         */
        public String getChecksumAlgorithm() {
            return ModelTestCase.this.getChecksumAlgorithm();
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#getContactModel(AuthToken)
         * 
         */
        public ContactModel getContactModel(final AuthToken authToken) {
            return ModelTestCase.this.getContactModel(authToken);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#getContainerModel(AuthToken)
         * 
         */
        public ContainerModel getContainerModel(final AuthToken authToken) {
            return ModelTestCase.this.getContainerModel(authToken);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#getProfileModel(AuthToken)
         * 
         */
        public ProfileModel getProfileModel(final AuthToken authToken) {
            return ModelTestCase.this.getProfileModel(authToken);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#getContactModel(AuthToken)
         * 
         */
        public AuthToken login(final String username) {
            return ModelTestCase.this.login(username, lookupPassword(username));
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#logout(AuthToken)
         * 
         */
        public void logout(final AuthToken authToken) {
            ModelTestCase.this.logout(authToken);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#lookupEMail(AuthToken)
         * 
         */
        public EMail lookupEMail(final AuthToken authToken) {
            return ModelTestCase.this.lookupEMail(authToken);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#lookupPassword(String)
         * 
         */
        public String lookupPassword(final String username) {
            return ModelTestCase.this.lookupPassword(username);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#lookupUser(AuthToken)
         * 
         */
        public User lookupUser(final AuthToken authToken) {
            return ModelTestCase.this.lookupUser(authToken);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#newEMail(String)
         * 
         */
        public EMail newEMail(final String username) {
            return ModelTestCase.this.newEMail(username);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#newPaymentInfo()
         * 
         */
        public PaymentInfo newPaymentInfo() {
            return ModelTestCase.this.newPaymentInfo();
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#newPaymentPlanCredentials()
         * 
         */
        public PaymentPlanCredentials newPaymentPlanCredentials() {
            return ModelTestCase.this.newPaymentPlanCredentials();
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#newProfile(List)
         * 
         */
        public Profile newProfile(final List<Feature> featureList) {
            return ModelTestCase.this.newProfile(featureList);
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#newUniqueUsername()
         * 
         */
        public String newUniqueUsername() {
            return ModelTestCase.this.newUniqueUsername();
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#now()
         * 
         */
        public Calendar now() {
            return ModelTestCase.this.now();
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#readOpheliaFeatures()
         * 
         */
        public List<Feature> readOpheliaFeatures() {
            return ModelTestCase.this.readOpheliaFeatures();
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#readOpheliaLatestRelease()
         * 
         */
        public Release readOpheliaLatestRelease() {
            return ModelTestCase.this.readOpheliaLatestRelease();
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#readOpheliaProduct()
         * 
         */
        public Product readOpheliaProduct() {
            return ModelTestCase.this.readOpheliaProduct();
        }

        /**
         * @see com.thinkparity.desdemona.model.ModelTestCase#verifyEMail(AuthToken)
         * 
         */
        public void verifyEMail(final AuthToken authToken) {
            ModelTestCase.this.verifyEMail(authToken);
        }
    }
}

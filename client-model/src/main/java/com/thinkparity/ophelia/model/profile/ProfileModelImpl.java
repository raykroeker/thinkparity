/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.thinkparity.codebase.LocaleUtil;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.profile.*;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.Constants;
import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.Constants.Product.Features;
import com.thinkparity.ophelia.model.events.ProfileEvent;
import com.thinkparity.ophelia.model.events.ProfileListener;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.handler.ProfileIOHandler;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.ProfileService;
import com.thinkparity.service.ServiceFactory;

/**
 * <b>Title:</b>thinkParity Profile Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.15
 */
public final class ProfileModelImpl extends Model<ProfileListener> implements
        ProfileModel, InternalProfileModel {

    /** A temporary user's backup disk-usage allotment. */
    private static final Long DU_BACKUP_ALLOTMENT;

    static {
        DU_BACKUP_ALLOTMENT = Long.valueOf(1024L * 1024L * 1024L * 10L);
    }

    /**
     * Determine if the hypersonic (database layer) error was caused by an
     * integrity constraint violation.
     * 
     * @param hypersonicException
     *            A <code>HypersonicException</code>.
     * @return True if the error is caused by an integrity constraint.
     */
    private static boolean isSQLIntegrityConstraintViolation(
            final HypersonicException hypersonicException) {
        if (SQLIntegrityConstraintViolationException.class.isAssignableFrom(
                hypersonicException.getCause().getClass())) {
            return "23503".equals(((SQLException) hypersonicException.getCause()).getSQLState());
        } else {
            return false;
        }
    }

    /** A <code>ProfileEventGenerator</code> for local events. */
    private final ProfileEventGenerator localEventGenerator;

    /** The profile db io. */
    private ProfileIOHandler profileIO;

    /** The profile web-service. */
    private ProfileService profileService;

    /**
     * Create ProfileModelImpl.
     *
     */
    public ProfileModelImpl() {
        super();
        this.localEventGenerator = new ProfileEventGenerator(ProfileEvent.Source.LOCAL);
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#addListener(com.thinkparity.ophelia.model.events.ProfileListener)
     *
     */
    @Override
    public void addListener(final ProfileListener listener) {
        super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#create(com.thinkparity.codebase.model.profile.Reservation,
     *      com.thinkparity.codebase.model.session.Credentials,
     *      com.thinkparity.codebase.model.profile.Profile,
     *      com.thinkparity.codebase.email.EMail)
     * 
     */
    public void create(final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials)
            throws ReservationExpiredException {
        try {
            assertIsValid(profile);
            assertIsValid(securityCredentials);

            final long now = currentDateTime().getTimeInMillis();
            if (now > usernameReservation.getExpiresOn().getTimeInMillis())
                throw new ReservationExpiredException(usernameReservation.getExpiresOn());
            if (now > emailReservation.getExpiresOn().getTimeInMillis())
                throw new ReservationExpiredException(emailReservation.getExpiresOn());

            Assert.assertTrue(usernameReservation.getUsername().equals(credentials.getUsername()),
                    "Reservation username {0} does not match credentials username {1}.",
                    usernameReservation.getUsername(), credentials.getUsername());
            Assert.assertTrue(emailReservation.getEMail().equals(email),
                    "Reservation e-mail address {0} does not match e-mail address {1}.",
                    emailReservation.getEMail(), email);

            final Product product = new Product();
            product.setName(Constants.Product.NAME);

            final Release release = new Release();
            release.setName(Constants.Release.NAME);
            release.setOs(OSUtil.getOs());

            final ServiceFactory serviceFactory = workspace.getServiceFactory(newFiniteRetryHandler());
            final ProfileService profileService = serviceFactory.getProfileService();
            profileService.create(newEmptyAuthToken(), product, release,
                    usernameReservation, emailReservation, credentials, profile,
                    email, securityCredentials);
        } catch (final ReservationExpiredException rex) {
            throw rex;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#createEMailReservation(com.thinkparity.codebase.email.EMail)
     * 
     */
    public EMailReservation createEMailReservation(final EMail email) {
        try {
            final ServiceFactory serviceFactory = workspace.getServiceFactory(newFiniteRetryHandler());
            final ProfileService profileService = serviceFactory.getProfileService();
            return profileService.createEMailReservation(newEmptyAuthToken(), email);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    
    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#createUsernameReservation(java.lang.String)
     *
     */
    public UsernameReservation createUsernameReservation(final String username) {
        try {
            final ServiceFactory serviceFactory = workspace.getServiceFactory(newFiniteRetryHandler());
            final ProfileService profileService = serviceFactory.getProfileService();
            return profileService.createUsernameReservation(newEmptyAuthToken(), username);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.InternalProfileModel#initialize()
     * 
     */
    public void initialize() {
        try {
            /* download */
            final AuthToken authToken = getAuthToken();
            final Profile profile = profileService.read(authToken);
            final ProfileEMail remoteEMail = profileService.readEMail(authToken);

            /* create */
            final User profileUser = getUserModel().readLazyCreate(
                    profile.getId());
            profile.setLocalId(profileUser.getLocalId());
            profileIO.create(profile);

            /* create e-mail */
            final ProfileEMail localEMail = new ProfileEMail();
            localEMail.setEmail(remoteEMail.getEmail());
            localEMail.setProfileId(profile.getLocalId());
            localEMail.setVerified(remoteEMail.isVerified());
            profileIO.createEmail(profile.getLocalId(), localEMail);

            /* index */
            getIndexModel().indexProfile();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#isAvailable(com.thinkparity.codebase.email.EMail)
     *
     */
    public Boolean isAvailable(final EMail email) {
        try {
            // check contacts first
            final List<Contact> contacts = getContactModel().read();
            final List<EMail> emails = new ArrayList<EMail>();
            for (final Contact contact : contacts)
                emails.addAll(contact.getEmails());
            for (final EMail contactEmail : emails)
                if (email.toString().equals(contactEmail.toString()))
                    return Boolean.FALSE;
            // check all users
            return profileService.isEMailAvailable(getAuthToken(), email);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.InternalProfileModel#isBackupAvailable()
     *
     */
    public Boolean isBackupEnabled() {
        try {
            final Profile profile = read();
            final List<Feature> features = profileIO.readFeatures(profile.getLocalId());
            for (final Feature feature : features) {
                if (Features.BACKUP.equals(feature.getName())) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.InternalProfileModel#isBackupAvailable()
     *
     */
    public Boolean isCoreEnabled() {
        try {
            final Profile profile = read();
            final List<Feature> features = profileIO.readFeatures(profile.getLocalId());
            for (final Feature feature : features) {
                if (Features.CORE.equals(feature.getName())) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#isInviteAvailable(User)
     * 
     */
    public Boolean isInviteAvailable(final User user) {
        try {
            return !getSessionModel().isInviteRestricted(user).booleanValue();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#isSignUpAvailable()
     * 
     */
    public Boolean isSignUpAvailable() {
        try {
            return Boolean.valueOf(!isCoreEnabled().booleanValue());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read the logged in user's profile. Note that the user's profile will
     * always exist remotely. If it does not exist locally (in the form of a row
     * in the user table) create it.
     * 
     * @return A profile.
     */
    public Profile read() {
        logger.logApiId();
        try {
            return profileIO.read(localUserId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#readBackupStatistics()
     *
     */
    public BackupStatistics readBackupStatistics() {
        try {
            Assert.assertTrue(isBackupEnabled(), "Backup is not enabled.");

            final BackupStatistics backupStatistics = new BackupStatistics();
            // NOCOMMIT - ProfileModelImpl#readBackupStatistics - Move to server.
            backupStatistics.setDiskUsageAllotment(DU_BACKUP_ALLOTMENT);
            backupStatistics.setDiskUsage(profileIO.readDiskUsage());
            return backupStatistics;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#readCredentials()
     * 
     */
    public Credentials readCredentials() {
        return super.readCredentials();
    }


    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#readEMail()
     *
     */
    @Override
    public ProfileEMail readEMail() {
        try {
            final List<ProfileEMail> profileEMails = readEMails();
            return profileEMails.get(0);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.InternalProfileModel#readEMails()
     *
     */
    @Override
    public  List<ProfileEMail> readEMails() {
        try {
            final Profile profile = read();
            final List<ProfileEMail> profileEMails = profileIO.readEmails(
                    profile.getLocalId());
            if (1 < profileEMails.size()) {
                logger.logWarning("Profile contains {0} e-mails.",
                        profileEMails.size());
            }
            return profileEMails;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#readFeatures()
     *
     */
    public List<Feature> readFeatures() {
        try {
            return getSessionModel().readMigratorProductFeatures(Constants.Product.NAME);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#readStatistics()
     *
     */
    public Statistics readStatistics() {
        try {
            final Statistics statistics = new Statistics();
            statistics.setDiskUsage(profileIO.readDiskUsage());
            return statistics;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#readUsername()
     *
     */
    @Override
    public String readUsername() {
        try {
            return readCredentials().getUsername();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#removeListener(com.thinkparity.ophelia.model.events.ProfileListener)
     *
     */
    @Override
    public void removeListener(final ProfileListener listener) {
        super.removeListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.InternalProfileModel#restoreBackup(com.thinkparity.ophelia.model.util.ProcessMonitor)
     *
     */
    @Override
    public void restoreBackup(final ProcessMonitor monitor) {
        try {
            final Profile profile = read();
            /* delete e-mail */
            final List<ProfileEMail> emails = readEMails();
            for (final ProfileEMail email : emails) {
                profileIO.deleteEmail(profile.getLocalId(), email.getEmailId());
            }

            /* delete */
            profileIO.delete(profile);

            /* create */
            initialize();

            /* fire event */
            //notifyProfileUpdated(profile, localEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#search(java.lang.String)
     *
     */
    public List<JabberId> search(final String expression) {
        try {
            return getIndexModel().searchProfile(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Update the logged in user's profile.
     * 
     * @param profile
     *            A profile.
     */
    public void update(final Profile profile) {
        logger.logApiId();
        logger.logVariable("profile", profile);
        try {
            assertIsValid(profile);
            
            // update local data
            profileIO.update(profile);
            getIndexModel().indexProfile();
            getSessionModel().updateProfile(localUserId(), profile);
            notifyProfileUpdated(read(), localEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#updateEMail(com.thinkparity.codebase.email.EMail)
     *
     */
    @Override
    public void updateEMail(final EMail email) throws EMailIntegrityException {
        try {
            final Profile profile = read();
            final List<ProfileEMail> profileEMails = readEMails();
            if (1 < profileEMails.size()) {
                logger.logWarning("Profile contains {0} e-mail addresses.",
                        profileEMails.size());
            }
            for (final ProfileEMail profileEMail : profileEMails) {
                try {
                    profileIO.deleteEmail(profileEMail.getProfileId(),
                            profileEMail.getEmailId());
                } catch (final HypersonicException hx) {
                    if (isSQLIntegrityConstraintViolation(hx)) {
                        logger.logWarning("Could not delete e-mail {0}.  {1}",
                                profileEMail.getEmail(), hx.getMessage());
                        throw new EMailIntegrityException(profileEMail.getEmail());
                    } else {
                        throw hx;
                    }
                }
            }
            // add email data
            final ProfileEMail profileEMail = new ProfileEMail();
            profileEMail.setEmail(email);
            profileEMail.setProfileId(profile.getLocalId());
            profileEMail.setVerified(Boolean.FALSE);
            profileIO.createEmail(profile.getLocalId(), profileEMail);
            // update remote e-mail
            profileService.updateEMail(getAuthToken(), email);
            // fire event
            notifyEmailUpdated(profile, profileEMail, localEventGenerator);
        } catch (final EMailIntegrityException emix) {
            throw emix;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#updatePassword(com.thinkparity.codebase.model.session.Credentials,
     *      java.lang.String)
     * 
     */
    public void updatePassword(final Credentials credentials,
            final String newPassword) throws InvalidCredentialsException {
        try {
            validateCredentials(credentials);

            // duplicate credentials
            final Credentials orginalCredentials = readCredentials();

            // update local data
            final Credentials localCredentials = readCredentials();
            localCredentials.setPassword(newPassword);
            updateCredentials(localCredentials);

            // update remote data.
            getSessionModel().updateProfilePassword(orginalCredentials, newPassword);
            notifyPasswordUpdated(read(), localEventGenerator);
        } catch (final InvalidCredentialsException icx) {
            throw icx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.InternalProfileModel#updateProductRelease()
     *
     */
    public void updateProductRelease() {
        try {
            final Product product = new Product();
            product.setName(Constants.Product.NAME);

            final Release release = new Release();
            release.setName(Constants.Release.NAME);
            release.setOs(OSUtil.getOs());

            profileService.updateProductRelease(getAuthToken(), product, release);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#validateCredentials(com.thinkparity.codebase.model.session.Credentials)
     *
     */
    public void validateCredentials(final Credentials credentials) throws InvalidCredentialsException {
        try {
            final Credentials localCredentials = readCredentials();
            if (localCredentials.getUsername().equals(credentials.getUsername()) &&
                    localCredentials.getPassword().equals(credentials.getPassword())) {
            } else {
                throw new InvalidCredentialsException();
            }
        } catch (final InvalidCredentialsException icx) {
            delay();
            throw icx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#verifyEMail(java.lang.Long,
     *      java.lang.String)
     * 
     */
    @Override
    public void verifyEMail(final Long emailId, final String key) {
        try {
            final Profile profile = read();
            final ProfileEMail email = profileIO.readEmail(profile.getLocalId(), emailId);
            getSessionModel().verifyProfileEmail(email, key);
            profileIO.verifyEmail(email.getProfileId(), email.getEmailId(), Boolean.TRUE);
            notifyEmailVerified(read(), readEMail(), localEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.profileIO = IOFactory.getDefault(workspace).createProfileHandler();

        final ServiceFactory serviceFactory = workspace.getServiceFactory(newDefaultRetryHandler());
        this.profileService = serviceFactory.getProfileService();
    }

    /**
     * Assert that a named value is set.
     * 
     * @param name
     *            A value name <code>String</code>..
     * @param value
     *            A value <code>String</code>.
     */
    private void assertIsSet(final String name, final String value) {
       Assert.assertNotNull(value, "Profile field {0} is not set.", name);
    }

    private void assertIsValid(final Profile profile) {
        assertIsValid(profile.getVCard());
    }

    private void assertIsValid(final ProfileVCard vcard) {
        assertIsSet("country", vcard.getCountry());
        assertIsValidCountry("country", vcard.getCountry());
        assertIsSet("language", vcard.getLanguage());
        assertIsValidLanguage("language", vcard.getLanguage());
        assertIsSet("name", vcard.getName());
        assertIsSet("organization", vcard.getOrganization());
        assertIsSet("organization country", vcard.getOrganizationCountry());
        assertIsValidCountry("organization country", vcard.getOrganizationCountry());
        assertIsSet("timeZone", vcard.getTimeZone());
        assertIsValidTimeZone("timeZone", vcard.getTimeZone());
        assertIsSet("title", vcard.getTitle());
    }

    private void assertIsValid(final SecurityCredentials securityCredentials) {
        Assert.assertNotNull(securityCredentials, "Security question cannot be null.");
        Assert.assertNotNull(securityCredentials.getQuestion(), "Security question cannot be null.");
        Assert.assertNotNull(securityCredentials.getAnswer(), "Security question answer cannot be null.");
    }

    private void assertIsValidCountry(final String name, final String value) {
        final Locale[] locales = LocaleUtil.getInstance().getAvailableLocales();
        boolean found = false;
        for (final Locale locale : locales) {
            if (locale.getISO3Country().equals(value)) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, "Profile field {0} is invalid.", name, value);
    }

    private void assertIsValidLanguage(final String name, final String value) {
        final Locale[] locales = LocaleUtil.getInstance().getAvailableLocales();
        boolean found = false;
        for (final Locale locale : locales) {
            if (locale.getISO3Language().equals(value)) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, "Profile field {0} contains invalid value {1}.", name, value);
    }

    private void assertIsValidTimeZone(final String name, final String value) {
        final TimeZone timeZone = TimeZone.getTimeZone(value);
        Assert.assertTrue(timeZone.getID().equals(value), "Profile field {0} contains invalid value {1}.", name, value);
    }

    /**
     * Delay the current thread for a number of seconds.
     *
     */
    private void delay() {
        try {
            Thread.sleep(3 * 1000);
        } catch (final InterruptedException ix) {}
    }

    /**
     * Obtain the web-service authentication token.
     * 
     * @return An <code>AuthToken</code>.
     */
    private AuthToken getAuthToken() {
        return getSessionModel().getAuthToken();
    }

    /**
     * Create an empty authentication token.
     * 
     * @return An <code>AuthToken</code>.
     */
    private AuthToken newEmptyAuthToken() {
        /* HACK - ProfileModelImpl#newEmptyAuthToken() - here we need to ensure
         * correct client id; client version */
        return new AuthToken();
    }

    /**
     * Notify that an email address has been removed.
     * 
     * @param profile
     *      A <code>Profile</code>.
     * @param email
     *      An <code>EMail</code> address.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyEmailUpdated(final Profile profile,
            final ProfileEMail email, final ProfileEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ProfileListener>() {
            public void notifyListener(final ProfileListener listener) {
                listener.emailUpdated(eventGenerator.generate(profile, email));
            }
        });
    }

    /**
     * Notify that an email address has been verified.
     * 
     * @param profile
     *      A <code>Profile</code>.
     * @param email
     *      An <code>EMail</code> address.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyEmailVerified(final Profile profile,
            final ProfileEMail email,
            final ProfileEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ProfileListener>() {
            public void notifyListener(final ProfileListener listener) {
                listener.emailVerified(eventGenerator.generate(profile, email));
            }
        });
    }

    /**
     * Notify that the password has been updated.
     * 
     * @param profile
     *      A <code>Profile</code>.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyPasswordUpdated(final Profile profile,
            final ProfileEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ProfileListener>() {
            public void notifyListener(final ProfileListener listener) {
                listener.passwordUpdated(eventGenerator.generate(profile));
            }
        });
    }

    /**
     * Notify that the profile has been updated.
     * 
     * @param profile
     *      A <code>Profile</code>.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyProfileUpdated(final Profile profile,
            final ProfileEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ProfileListener>() {
            public void notifyListener(final ProfileListener listener) {
                listener.profileUpdated(eventGenerator.generate(profile));
            }
        });
    }
}

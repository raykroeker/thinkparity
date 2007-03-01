/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.thinkparity.codebase.LocaleUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.Constants.Product.Features;
import com.thinkparity.ophelia.model.events.ProfileEvent;
import com.thinkparity.ophelia.model.events.ProfileListener;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ProfileIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Profile Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.15
 */
public final class ProfileModelImpl extends Model<ProfileListener> implements
        ProfileModel, InternalProfileModel {

    /** A <code>ProfileEventGenerator</code> for local events. */
    private final ProfileEventGenerator localEventGenerator;

    /** The profile db io. */
    private ProfileIOHandler profileIO;

    /**
     * Create ProfileModelImpl.
     *
     */
    public ProfileModelImpl() {
        super();
        this.localEventGenerator = new ProfileEventGenerator(ProfileEvent.Source.LOCAL);
    }

    /**
     * Add an email to the profile.
     * 
     * @param email
     *            An <code>EMail</code>.
     */
    public void addEmail(final EMail email) {
        logger.logApiId();
        logger.logVariable("email", email);
        try {
            assertOnline("USER NOT ONLINE");
            final Profile profile = read();
            // add email data
            final ProfileEMail profileEMail = new ProfileEMail();
            profileEMail.setEmail(email);
            profileEMail.setProfileId(profile.getLocalId());
            profileEMail.setVerified(Boolean.FALSE);
            profileIO.createEmail(profile.getLocalId(), profileEMail);
            // add email remotely
            getSessionModel().addProfileEmail(localUserId(), profileEMail);
            // fire event
            notifyEmailAdded(read(), readEmail(profileEMail.getEmailId()),
                    localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
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
     * Create the user's profile.
     *
     */
    public Profile create() {
        logger.logApiId();
        try {
            final Profile remoteProfile = getSessionModel().readProfile();
            /*
             * NOTE Only verified emails are downloaded and created in the local
             * profile.
             */
            final List<EMail> remoteEmails =
                    getSessionModel().readProfileEMails();
            profileIO.create(remoteProfile);
            ProfileEMail profileEmail;
            for (final EMail remoteEmail : remoteEmails) {
                profileEmail = new ProfileEMail();
                profileEmail.setEmail(remoteEmail);
                profileEmail.setProfileId(remoteProfile.getLocalId());
                profileEmail.setVerified(Boolean.TRUE);
                profileIO.createEmail(remoteProfile.getLocalId(), profileEmail);
            }
            return profileIO.read(localUserId());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileModel#isAvailable(com.thinkparity.codebase.email.EMail)
     *
     */
    public Boolean isAvailable(final EMail email) {
        try {
            assertOnline();
            // check contacts first
            final List<Contact> contacts = getContactModel().read();
            final List<EMail> emails = new ArrayList<EMail>();
            for (final Contact contact : contacts)
                emails.addAll(contact.getEmails());
            for (final EMail contactEmail : emails)
                if (email.toString().equals(contactEmail.toString()))
                    return Boolean.FALSE;
            // check all users
            return getSessionModel().isEmailAvailable(localUserId(), email);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Determine if sign up is available.
     * 
     * @return True if sign up is available.
     */
    public Boolean isSignUpAvailable() {
        try {
            final Profile profile = read();
            final List<Feature> features = profileIO.readFeatures(profile.getLocalId());
            for (final Feature feature : features) {
                if (Features.CORE.equals(feature.getName())) {
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
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
            throw translateError(t);
        }
    }

    /**
     * Read the user's credentials.
     * 
     * @return The user's credentials.
     */
    @Override
    public Credentials readCredentials() {
        logger.logApiId();
        return super.readCredentials();
    }

    /**
     * Read a profile email.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     * @return A <code>ProfileEmail</code>.
     */
    public ProfileEMail readEmail(final Long emailId) {
        logger.logApiId();
        logger.logVariable("emailId", emailId);
        try {
            final Profile profile = read();
            return profileIO.readEmail(profile.getLocalId(), emailId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }


    /**
     * Read a list of profile email addresses.
     * 
     * @return A list of email addresses.
     */
    public List<ProfileEMail> readEmails() {
        logger.logApiId();
        try {
            final Profile profile = read();
            return profileIO.readEmails(profile.getLocalId());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the security question.
     * 
     * @return A security question.
     */
    public String readSecurityQuestion() {
        logger.logApiId();
        try {
            final Profile profile = read();
            return getSessionModel().readProfileSecurityQuestion(profile.getId());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Remove an email from a the profile.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     */
    public void removeEmail(final Long emailId) {
        logger.logApiId();
        logger.logVariable("emailId", emailId);
        try {
            assertOnline("USER NOT ONLINE");
            final Profile profile = read();
            final ProfileEMail email = profileIO.readEmail(profile.getLocalId(), emailId);
            // remove email data
            profileIO.deleteEmail(email.getProfileId(), email.getEmailId());
            // remove email remotely
            getSessionModel().removeProfileEmail(localUserId(), email);
            notifyEmailRemoved(read(), email, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
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
     * Reset the user's password.
     *
     */
    public void resetPassword(final String securityAnswer) {
        logger.logApiId();
        try {
            // update remote data.
            final String resetPassword =
                getSessionModel().resetProfilePassword(localUserId(),
                    securityAnswer);
            // update local data
            final Credentials credentials = readCredentials();
            credentials.setPassword(resetPassword);
            updateCredentials(credentials);
            notifyPasswordReset(read(), localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
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
            getSessionModel().updateProfile(localUserId(), profile);
            notifyProfileUpdated(read(), localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

        /**
         * Update the profile password.
         * 
         * @param password
         *            The current password <code>String</code>.
         * @param newPassword
         *            The new password <code>String</code>.
         */
        public void updatePassword(final String password,
                final String newPassword) {
            logger.logApiId();
            logger.logVariable("password", "XXXXX");
            logger.logVariable("newPassword", "XXXXX");
            try {
                final Credentials credentials = readCredentials();
                Assert.assertTrue("PASSWORD INCORRECT",
                        password.equals(credentials.getPassword()));
                // update local data
                credentials.setPassword(newPassword);
                updateCredentials(credentials);
                // update remote data.
                getSessionModel().updateProfileCredentials(localUserId(),
                        credentials);
                notifyPasswordUpdated(read(), localEventGenerator);
            } catch (final Throwable t) {
                throw translateError(t);
            }
        }

    /**
     * Verify an email.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            A verification key <code>String</code>.
     */
    public void verifyEmail(final Long emailId, final String key) {
        logger.logApiId();
        logger.logVariable("emailId", emailId);
        logger.logVariable("key", key);
        try {
            assertOnline("USER NOT ONLINE");
            final Profile profile = read();
            final ProfileEMail email = profileIO.readEmail(profile.getLocalId(), emailId);
            getSessionModel().verifyProfileEmail(localUserId(), email, key);
            profileIO.verifyEmail(email.getProfileId(), email.getEmailId(), Boolean.TRUE);
            notifyEmailVerified(read(), readEmail(email.getEmailId()),
                    localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
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
       Assert.assertNotTrue("".equals(value.trim()), "Profile field {0} is not set.", name);
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
     * Notify that an email address has been added.
     * 
     * @param profile
     *      A <code>Profile</code>.
     * @param email
     *      An <code>EMail</code> address.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyEmailAdded(final Profile profile,
            final ProfileEMail email,
            final ProfileEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ProfileListener>() {
            public void notifyListener(final ProfileListener listener) {
                listener.emailAdded(eventGenerator.generate(profile, email));
            }
        });
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
    private void notifyEmailRemoved(final Profile profile,
            final ProfileEMail email,
            final ProfileEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ProfileListener>() {
            public void notifyListener(final ProfileListener listener) {
                listener.emailRemoved(eventGenerator.generate(profile, email));
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
     * Notify that the password has been reset.
     * 
     * @param profile
     *      A <code>Profile</code>.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyPasswordReset(final Profile profile,
            final ProfileEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ProfileListener>() {
            public void notifyListener(final ProfileListener listener) {
                listener.passwordReset(eventGenerator.generate(profile));
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

/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.events.ProfileEvent;
import com.thinkparity.ophelia.model.events.ProfileListener;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ProfileIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Profile Model Implementation<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.10
 */
public final class ProfileModelImpl extends Model<ProfileListener> implements
        ProfileModel, InternalProfileModel {

    /** The profile db io. */
    private ProfileIOHandler profileIO;

    /** A <code>ProfileEventGenerator</code> for local events. */
    private final ProfileEventGenerator localEventGenerator;

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
     * Determine if sign up is available.
     * 
     * @return True if sign up is available.
     */
    public Boolean isSignUpAvailable() {
        logger.logApiId();
        return Boolean.FALSE;
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

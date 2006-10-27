/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ProfileIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Profile Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1.1.1
 */
final class ProfileModelImpl extends AbstractModelImpl {

    /** The profile db io. */
    private final ProfileIOHandler profileIO;

    /**
     * Create ProfileModelImpl.
     *
     * @param workspace
     *      The thinkParity workspace.
     */
    ProfileModelImpl(final Environment environment, final Workspace workspace) {
        super(environment, workspace);
        this.profileIO = IOFactory.getDefault(workspace).createProfileHandler();
    }

    /**
     * Create the user's profile.
     *
     */
    Profile create() {
        logger.logApiId();
        try {
            final Profile remoteProfile = getSessionModel().readProfile();
            /*
             * NOTE Only verified emails are downloaded and created in the local
             * profile.
             */
            final List<EMail> remoteEmails =
                    getSessionModel().readProfileEmails();
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
     * Read the user's credentials.
     * 
     * @return The user's credentials.
     */
    @Override
    protected Credentials readCredentials() {
        logger.logApiId();
        return super.readCredentials();
    }

    /**
     * Add an email to the profile.
     * 
     * @param email
     *            An <code>EMail</code>.
     */
    void addEmail(final EMail email) {
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
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the logged in user's profile. Note that the user's profile will
     * always exist remotely. If it does not exist locally (in the form of a row
     * in the user table) create it.
     * 
     * @return A profile.
     */
    Profile read() {
        logger.logApiId();
        try {
            return profileIO.read(localUserId());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }


    /**
     * Read a profile email.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     * @return A <code>ProfileEmail</code>.
     */
    ProfileEMail readEmail(final Long emailId) {
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
    List<ProfileEMail> readEmails() {
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
    String readSecurityQuestion() {
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
    void removeEmail(final Long emailId) {
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
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Reset the user's password.
     *
     */
    void resetPassword(final String securityAnswer) {
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
    void update(final Profile profile) {
        logger.logApiId();
        logger.logVariable("profile", profile);
        try {
            // update local data
            profileIO.update(profile);
            getSessionModel().updateProfile(localUserId(), profile);
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
    void updatePassword(final String password,
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
    void verifyEmail(final Long emailId, final String key) {
        logger.logApiId();
        logger.logVariable("emailId", emailId);
        logger.logVariable("key", key);
        try {
            assertOnline("USER NOT ONLINE");
            final Profile profile = read();
            final ProfileEMail email = profileIO.readEmail(profile.getLocalId(), emailId);
            getSessionModel().verifyProfileEmail(localUserId(), email, key);
            profileIO.verifyEmail(email.getProfileId(), email.getEmailId(), Boolean.TRUE);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
}

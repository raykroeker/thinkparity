/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ProfileIOHandler;
import com.thinkparity.model.parity.model.session.Credentials;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.profile.ProfileEMail;

/**
 * <b>Title:</b>thinkParity Profile Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1.1.1
 */
class ProfileModelImpl extends AbstractModelImpl {

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
        logApiId();
        logVariable("password", "XXXXX");
        logVariable("newPassword", "XXXXX");
        try {
            final Credentials credentials = readCredentials();
            Assert.assertTrue("PASSWORD INCORRECT",
                    password.equals(credentials.getPassword()));
            // update local data
            credentials.setPassword(newPassword);
            updateCredentials(credentials);
            // update remote data.
            getInternalSessionModel().updateProfileCredentials(localUserId(),
                    credentials);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /** The profile db io. */
    private final ProfileIOHandler profileIO;

    /**
     * Create ProfileModelImpl.
     *
     * @param workspace
     *      The thinkParity workspace.
     */
    ProfileModelImpl(final Workspace workspace) {
        super(workspace);
        this.profileIO = IOFactory.getDefault().createProfileHandler();
    }

    /**
     * Add an email to the profile.
     * 
     * @param email
     *            An <code>EMail</code>.
     */
    void addEmail(final EMail email) {
        logApiId();
        logVariable("email", email);
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
            getInternalSessionModel().addProfileEmail(localUserId(), profileEMail);
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
        logApiId();
        try {
            final Profile profile = profileIO.read(localUserId());
            if (null == profile) {
                return lazyCreateProfile();
            } else {
                return profile;
            }
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
        logApiId();
        logVariable("emailId", emailId);
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
        logApiId();
        try {
            final Profile profile = read();
            return profileIO.readEmails(profile.getLocalId());
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
        logApiId();
        logVariable("emailId", emailId);
        try {
            assertOnline("USER NOT ONLINE");
            final Profile profile = read();
            final ProfileEMail email = profileIO.readEmail(profile.getLocalId(), emailId);
            // remove email data
            profileIO.deleteEmail(email.getProfileId(), email.getEmailId());
            // remove email remotely
            getInternalSessionModel().removeProfileEmail(localUserId(), email);
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
        logApiId();
        logVariable("profile", profile);
        try {
            // update local data
            profileIO.update(profile);
            getInternalSessionModel().updateProfile(profile);
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
        logApiId();
        logVariable("emailId", emailId);
        logVariable("key", key);
        try {
            assertOnline("USER NOT ONLINE");
            final Profile profile = read();
            final ProfileEMail email = profileIO.readEmail(profile.getLocalId(), emailId);
            getInternalSessionModel().verifyProfileEmail(localUserId(), email, key);
            profileIO.verifyEmail(email.getProfileId(), email.getEmailId(), Boolean.TRUE);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }


    /**
     * Read the profile from the server; along with any email addresses and
     * store them locally.
     * 
     * @return A profile.
     */
    private Profile lazyCreateProfile() {
        final Profile remoteProfile = getInternalSessionModel().readProfile();
        // NOTE Only verified emails are downloaded and created in the local
        // profile.
        final List<EMail> remoteEmails =
                getInternalSessionModel().readProfileEmails();
        profileIO.create(remoteProfile);
        ProfileEMail profileEmail;
        for (final EMail remoteEmail : remoteEmails) {
            profileEmail = new ProfileEMail();
            profileEmail.setEmail(remoteEmail);
            profileEmail.setProfileId(remoteProfile.getLocalId());
            profileEmail.setVerified(Boolean.TRUE);
            profileIO.createEmail(remoteProfile.getLocalId(), profileEmail);
        }
        return read();
    }
}

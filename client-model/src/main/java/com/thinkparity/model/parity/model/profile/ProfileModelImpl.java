/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ProfileIOHandler;
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
        assertOnline("USER NOT ONLINE");
        final Profile profile = read();
        // add email data
        final ProfileEMail profileEMail = new ProfileEMail();
        profileEMail.setEmail(email);
        profileEMail.setProfileId(profile.getLocalId());
        profileEMail.setVerified(Boolean.FALSE);
        profileIO.createEmail(profileEMail);
        // add email remotely
        getInternalSessionModel().addProfileEmail(localUserId(), profileEMail);
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
        final Profile profile = profileIO.read(localUserId());
        if (null == profile) {
            return lazyCreateProfile();
        } else {
            return profile;
        }
    }

    /**
     * Read a list of profile email addresses.
     * 
     * @return A list of email addresses.
     */
    List<ProfileEMail> readEmails() {
        logApiId();
        final Profile profile = read();
        return profileIO.readEmails(profile.getLocalId());
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
        assertOnline("USER NOT ONLINE");
        final Profile profile = read();
        final ProfileEMail email = profileIO.readEmail(profile.getLocalId(), emailId);
        // remove email data
        profileIO.deleteEmail(email.getProfileId(), email.getEmailId());
        // remove email remotely
        getInternalSessionModel().removeProfileEmail(localUserId(), email);

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
        // update local data
        profileIO.update(profile);
        getInternalSessionModel().updateProfile(profile);
    }

    /**
     * Read the profile from the server; along with any email addresses and
     * store them locally.
     * 
     * @return A profile.
     */
    private Profile lazyCreateProfile() {
        final Profile remoteProfile = getInternalSessionModel().readProfile();
//        final List<ProfileEMail> remoteProfileEMails =
//                getInternalSessionModel().readProfileEMails();
        profileIO.create(remoteProfile, new ArrayList<ProfileEMail>());//remoteProfileEMails);
        return read();
    }
}

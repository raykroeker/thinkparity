/**
 * Created On: Feb 1, 2007 4:36:25 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.profile.BackupStatistics;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.profile.Statistics;
import com.thinkparity.ophelia.model.session.SessionModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UpdateProfileProvider extends ContentProvider {

    /** An instance of <code>SessionModel</code>. */
    private final SessionModel sessionModel;

    /**
     * Create UpdateProfileProvider.
     * 
     * @param profileModel
     *            A profile model interface.
     * @param sessionModel
     *            An instance of <code>SessionModel</code>.
     */
    public UpdateProfileProvider(final ProfileModel profileModel,
            final SessionModel sessionModel) {
        super(profileModel);
        this.sessionModel = sessionModel;
    }

    /**
     * Determine if the backup feature is available.
     * 
     * @return True if backup is available for the profile.
     */
    public Boolean isBackupEnabled() {
        return profileModel.isBackupEnabled();
    }

    /**
     * Determine if the user's backup is online.
     * 
     * @return True if the user's backup is online.
     */
    public Boolean isOnline() {
        return sessionModel.isOnline();
    }

    /**
     * Determine if signup is available (ie. this is a guest account).
     * 
     * @return True if signup is available (ie. this is a guest account)
     */
    public Boolean isSignUpAvailable() {
        return profileModel.isSignUpAvailable();
    }

    /**
     * Read the backup statistics.
     * 
     * @return The <code>BackupStatistics</code>.
     */
    public BackupStatistics readBackupStatistics() {
        return profileModel.readBackupStatistics();
    }

    /**
     * Read the profile e-mail address.
     * 
     * @return A <code>ProfileEMail</code>.
     */
    public ProfileEMail readEMail() {
        return profileModel.readEMail();
    }

    /**
     * Determine whether or not an e-mail address is available.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if the address is not in use.
     */
    public Boolean readIsEmailAvailable(final EMail email) {
        return profileModel.isAvailable(email);
    }

    /**
     * Read the profile.
     * 
     * @return A <code>Profile</code>.  
     */
    public Profile readProfile() {
        return profileModel.read();
    }

    /**
     * Read the statistics.
     * 
     * @return The <code>Statistics</code>.
     */
    public Statistics readStatistics() {
        return profileModel.readStatistics();
    }
}

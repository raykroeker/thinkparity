/**
 * Created On: Feb 1, 2007 4:36:25 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

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
     * @param backupModel
     *            An instance of <code>BackupModel</code>.
     * @param sessionModel
     *            An instance of <code>SessionModel</code>.
     */
    public UpdateProfileProvider(final ProfileModel profileModel,
            final SessionModel sessionModel) {
        super(profileModel);
        this.sessionModel = sessionModel;
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
     * Read the statistics.
     * 
     * @return The backup <code>Statistics</code>.
     */
    public Statistics readStatistics() {
        return profileModel.readStatistics();
    }

    /**
     * Read emails.
     * 
     * @return A list of email addresses.
     */
    public List<ProfileEMail> readEMails() {
        return profileModel.readEmails();
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
}

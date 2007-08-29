/**
 * Created On: 28-Aug-07 3:00:44 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.backup;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class RestoreBackupProvider extends ContentProvider {

    /**
     * Create RestoreBackupProvider.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     */
    public RestoreBackupProvider(final ProfileModel profileModel) {
        super(profileModel);
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
     * Get the username.
     * 
     * @return The username <code>String</code>.             
     */
    public String readUsername() {
        return profileModel.readUsername();
    }
}

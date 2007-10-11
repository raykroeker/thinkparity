/*
 * Created On: Aug 24, 2006 8:53:41 AM
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.profile.BackupStatistics;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.profile.Statistics;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Update extends AbstractBrowserAction {

    /** The thinkParity browser application. */
    private final Browser browser;

    /**
     * Create Update.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public Update(final Browser browser) {
        super(ActionId.PROFILE_UPDATE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Profile profile = (Profile) data.get(DataKey.PROFILE);
        final ProfileModel profileModel = getProfileModel();
        if (null == profile) {
            /* display profile dialog */
            final Boolean backupEnabled = profileModel.isBackupEnabled();
            final Boolean signUpAvailable = profileModel.isSignUpAvailable();
            final Boolean paymentInfoAccessible = profileModel.isAccessiblePaymentInfo();
            final BackupStatistics backupStatistics = profileModel.readBackupStatistics();
            final ProfileEMail email = profileModel.readEMail();
            final Profile existingProfile = profileModel.read();
            final Statistics statistics = profileModel.readStatistics();
            SwingUtil.ensureDispatchThread(new Runnable() {
                public void run() {
                    browser.displayUpdateProfileDialog(backupEnabled,
                            signUpAvailable, paymentInfoAccessible,
                            backupStatistics, email, existingProfile,
                            statistics);
                }
            });
        } else {
            /* update profile */
            profileModel.update(profile);
        }
    }

    /** <b>Title:</b>Update Data Key<br> */
    public enum DataKey { PROFILE }
}

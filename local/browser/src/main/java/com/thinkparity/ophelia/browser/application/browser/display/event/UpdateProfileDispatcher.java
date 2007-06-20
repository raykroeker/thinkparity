/**
 * Created On: 20-Jun-07 12:04:26 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.event;

import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.events.BackupAdapter;
import com.thinkparity.ophelia.model.events.BackupEvent;
import com.thinkparity.ophelia.model.events.BackupListener;
import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpdateProfileAvatar;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UpdateProfileDispatcher implements
        EventDispatcher<UpdateProfileAvatar> {

    /** A <code>BackupListener</code>. */
    private BackupListener backupListener;

    /** An instance of <code>BackupModel</code>. */
    private final BackupModel backupModel;

    /** An instance of <code>ProfileModel</code>. */
    private final ProfileModel profileModel;

    /**
     * Create UpdateProfileDispatcher.
     * 
     * @param backupModel
     *            An instance of <code>BackupModel</code>.
     */
    public UpdateProfileDispatcher(final BackupModel backupModel,
            final ProfileModel profileModel) {
        super();
        this.backupModel = backupModel;
        this.profileModel = profileModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#addListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     *
     */
    public void addListeners(final UpdateProfileAvatar avatar) {
        if (profileModel.isBackupEnabled()) {
            backupListener = new BackupAdapter() {
                @Override
                public void statisticsUpdated(final BackupEvent e) {
                    avatar.fireBackupEvent(e);
                }
            };
            backupModel.addListener(backupListener);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#removeListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     * 
     */
    public void removeListeners(final UpdateProfileAvatar avatar) {
        if (profileModel.isBackupEnabled()) {
            backupModel.removeListener(backupListener);
            backupListener = null;
        }
    }
}

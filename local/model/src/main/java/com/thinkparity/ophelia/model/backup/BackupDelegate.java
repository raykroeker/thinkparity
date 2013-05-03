/*
 * Created On:  24-Aug-07 10:38:56 AM
 */
package com.thinkparity.ophelia.model.backup;

import com.thinkparity.ophelia.model.DefaultDelegate;

import com.thinkparity.service.BackupService;
import com.thinkparity.service.SessionService;

/**
 * <b>Title:</b>thinkParity Ophelia Model Backup Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class BackupDelegate extends DefaultDelegate<BackupModelImpl> {

    /** An instance of a backup web-service interface. */
    protected BackupService backupService;

    /** An instance of a session web-service interface. */
    protected SessionService sessionService;

    /**
     * Create BackupDelegate.
     *
     */
    protected BackupDelegate() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.DefaultDelegate#initialize(com.thinkparity.ophelia.model.Model)
     *
     */
    @Override
    public void initialize(final BackupModelImpl modelImplementation) {
        super.initialize(modelImplementation);
        this.backupService = modelImplementation.getBackupService();
        this.sessionService = modelImplementation.getSessionService();
    }
}

/*
 * Created On:  24-Aug-07 10:44:24 AM
 */
package com.thinkparity.ophelia.model.backup.delegate;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.InvalidLocationException;

import com.thinkparity.ophelia.model.backup.BackupDelegate;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

/**
 * <b>Title:</b>thinkParity Ophelia Model Backup Restore Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Restore extends BackupDelegate {

    /** A set of credentials. */
    private Credentials credentials;

    /** A process monitor. */
    private ProcessMonitor monitor;

    /**
     * Create Restore.
     *
     */
    public Restore() {
        super();
    }

    /**
     * Restore the profile from backup.
     * 
     */
    public void restore() throws InvalidCredentialsException,
            InvalidLocationException {
        /* resolve saved credentials */
        deleteCredentials();
        deleteToken();

        /* login */
        final InternalSessionModel sessionModel = getSessionModel();
        sessionModel.login(credentials);    /* will create credentials */
        try {
            sessionModel.initializeToken(); /* will invalidate other profiles */
    
            /* restore */
            getProfileModel().restoreBackup(monitor);
            getContactModel().restoreBackup(monitor);
            getContainerModel().restoreBackup(monitor);
        } finally {
            sessionModel.logout();
        }

        /* login again to take care of auto-update issues */
        sessionModel.login(monitor);
    }

    /**
     * @param credentials the credentials to set
     */
    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Set the monitor.
     *
     * @param monitor
     *		A <code>ProcessMonitor</code>.
     */
    public void setMonitor(final ProcessMonitor monitor) {
        this.monitor = monitor;
    }
}

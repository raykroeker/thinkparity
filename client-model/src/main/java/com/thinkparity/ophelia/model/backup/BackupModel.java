/*
 * Generated On: Oct 04 06 09:40:46 AM
 */
package com.thinkparity.ophelia.model.backup;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.InvalidLocationException;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.events.BackupListener;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

/**
 * <b>Title:</b>thinkParity Backup Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface BackupModel {

    /**
     * Add a backup listener.
     * 
     * @param listener
     *            A <code>BackupListener</code>.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    void addListener(BackupListener listener);

    
    /**
     * Remove a backup listener.
     * 
     * @param listener
     *            A <code>BackupListener</code>.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    void removeListener(BackupListener listener);

    /**
     * Restore the profile from backup.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param credentials
     *            A set of <code>Credentials</code>.
     * 
     * @throws InvalidCredentialsException
     *             if the stored credentials are no longer valid
     * @throws InvalidLocationException
     */
    void restore(ProcessMonitor monitor, Credentials credentials)
            throws InvalidCredentialsException, InvalidLocationException;
}

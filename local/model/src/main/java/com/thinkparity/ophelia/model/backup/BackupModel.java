/*
 * Generated On: Oct 04 06 09:40:46 AM
 */
package com.thinkparity.ophelia.model.backup;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.events.BackupListener;

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
     * @param l
     *            A <code>BackupListener</code>.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void addListener(final BackupListener l);

    /**
     * Determine if the backup is online.
     * 
     * @return True if the backup is online.
     */
    public Boolean isOnline();

    /**
     * Read the backup statistics.
     * 
     * @return An instance of <code>Statistics</code>.
     */
    public Statistics readStatistics();

    /**
     * Remove a backup listener.
     * 
     * @param l
     *            A <code>BackupListener</code>.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void removeListener(final BackupListener l);
}

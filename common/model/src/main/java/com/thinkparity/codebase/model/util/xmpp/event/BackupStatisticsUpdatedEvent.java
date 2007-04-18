/*
 * Created On:  13-Mar-07 7:56:08 PM
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import com.thinkparity.codebase.model.backup.Statistics;

/**
 * <b>Title:</b>thinkParity CommonModel XMPP Backup Statistics Updated Event<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public final class BackupStatisticsUpdatedEvent extends XMPPEvent {

    /** The backup <code>Statistics</code>. */
    private Statistics statistics;

    /**
     * Create BackupStatisticsUpdatedEvent.
     *
     */
    public BackupStatisticsUpdatedEvent() {
        super();
    }

    /**
     * Obtain statistics.
     * 
     * @return The backup <code>Statistics</code>.
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Set statistics.
     * 
     * @param statistics
     *            The backup <code>Statistics</code>.
     */
    public void setStatistics(final Statistics statistics) {
        this.statistics = statistics;
    }
}

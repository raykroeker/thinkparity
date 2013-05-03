/*
 * Created On:  16-Jul-07 6:08:21 PM
 */
package com.thinkparity.ophelia.model.profile;

/**
 * <b>Title:</b>thinkParity Ophelia Model Profile Backup Statistics<br>
 * <b>Description:</b>Statistics for the profile when a user's backup feature
 * is enabled.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BackupStatistics extends Statistics {

    /** The total alloted disk usage. */
    private Long diskUsageAllotment;

    /**
     * Create BackupStatistics.
     *
     */
    public BackupStatistics() {
        super();
    }

    /**
     * Obtain diskUsageAllotment.
     *
     * @return A Long.
     */
    public Long getDiskUsageAllotment() {
        return diskUsageAllotment;
    }

    /**
     * Set diskUsageAllotment.
     *
     * @param diskUsageAllotment
     *		A Long.
     */
    public void setDiskUsageAllotment(final Long diskUsageAllotment) {
        this.diskUsageAllotment = diskUsageAllotment;
    }
}

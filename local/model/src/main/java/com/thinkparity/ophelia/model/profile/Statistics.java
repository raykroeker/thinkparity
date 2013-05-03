/*
 * Created On:  13-Mar-07 6:51:25 PM
 */
package com.thinkparity.ophelia.model.profile;

/**
 * <b>Title:</b>thinkParity Profile Statistics<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Statistics {

    /** The backup disk usage in bytes. */
    private Long diskUsage;

    /**
     * Create Statistics.
     *
     */
    public Statistics() {
        super();
    }

    /**
     * Obtain disk usage.
     *
     * @return A <code>Long</code>.
     */
    public Long getDiskUsage() {
        return diskUsage;
    }

    /**
     * Set disk usage.
     * 
     * @param diskUsage
     *            The disk usage <code>Long</code>.
     */
    public void setDiskUsage(final Long diskUsage) {
        this.diskUsage = diskUsage;
    }
}

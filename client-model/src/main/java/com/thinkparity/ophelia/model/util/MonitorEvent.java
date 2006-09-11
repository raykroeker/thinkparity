/*
 * Created On: Aug 22, 2006 12:07:18 PM
 */
package com.thinkparity.ophelia.model.util;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MonitorEvent {

    /** The timestamp of the change. */
    private final long timestamp;

    /** Create MonitorEvent. */
    public MonitorEvent() {
        super();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Obtain the timestamp
     *
     * @return The long.
     */
    public long getTimestamp() {
        return timestamp;
    }
}

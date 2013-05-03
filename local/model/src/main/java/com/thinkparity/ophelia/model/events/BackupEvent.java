/*
 * Created On:  14-Mar-07 3:57:48 PM
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.model.backup.Statistics;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class BackupEvent {

    /** The event <code>Source</code>. */
    private final Source source;

    /** The backup <code>Statistics</code>. */
    private final Statistics statistics;

    /**
     * Create BackupEvent.
     * 
     * @param source
     *            The event <code>Source</code>.
     * @param statistics
     *            The backup <code>Statistics</code>.
     */
    public BackupEvent(final Source source, final Statistics statistics) {
        super();
        this.source = source;
        this.statistics = statistics;
    }

    /**
     * Obtain statistics.
     * 
     * @return A <code>Statistics</code>.
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Determine if the event is a local event.
     * 
     * @return True if the event is a local event.
     */
    public Boolean isLocal() {
        return Source.LOCAL == source;
    }

    /**
     * Determine if the event is a remote event.
     * 
     * @return True if the event is a remote event.
     */
    public Boolean isRemote() {
        return Source.REMOTE == source;
    }

    /**
     * <b>Title:</b>Backup Event Source<br>
     * <b>Description:</b><br>
     */
    public enum Source { LOCAL, REMOTE }
}

/*
 * Created On:  15-Mar-07 12:48:29 AM
 */
package com.thinkparity.ophelia.model.backup;

import com.thinkparity.codebase.model.backup.Statistics;

import com.thinkparity.ophelia.model.events.BackupEvent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class BackupEventGenerator {

    /** The backup event <code>Source</code>. */
    private final BackupEvent.Source source;

    /**
     * Create BackupEventGenerator.
     *
     */
    BackupEventGenerator(final BackupEvent.Source source) {
        super();
        this.source = source;
    }

    /**
     * Generate a backup event.
     * 
     * @param statistics
     *            A <code>Statistics</code>.
     * @return A <code>BackupEvent</code>.
     */
    BackupEvent generate(final Statistics statistics) {
        return new BackupEvent(source, statistics);
    }
}

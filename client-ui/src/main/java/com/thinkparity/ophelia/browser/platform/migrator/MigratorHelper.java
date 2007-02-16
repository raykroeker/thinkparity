/*
 * Created On:  1-Feb-07 2:05:02 PM
 */
package com.thinkparity.ophelia.browser.platform.migrator;

import com.thinkparity.ophelia.model.migrator.MigratorModel;
import com.thinkparity.ophelia.model.migrator.monitor.MigrateMonitor;

import com.thinkparity.ophelia.browser.platform.Platform;

/**
 * <b>Title:</b>thinkParity OpheliaUI Migrator Helper<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MigratorHelper {

    /** An instance of <code>MigratorModel</code>. */
    private final MigratorModel model;

    /**
     * Create MigratorHelper.
     *
     */
    public MigratorHelper(final Platform platform) {
        super();
        this.model = platform.getModelFactory().getMigratorModel(getClass());
    }

    /**
     * Determine if migration is possible.
     * 
     * @return True if migration is possible.
     */
    public Boolean isMigrationPossible() {
        return model.isMigrationPossible();
    }

    /**
     * Migrate.
     *
     */
    public void migrate() {
        model.migrate(new MigrateMonitor() {});
    }
}

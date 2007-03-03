/*
 * Created On:  1-Feb-07 2:05:02 PM
 */
package com.thinkparity.ophelia.browser.platform.migrator;

import com.thinkparity.ophelia.model.migrator.MigratorModel;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.Step;

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
        model.migrate(new ProcessAdapter() {
            @Override
            public void beginProcess() {}
            @Override
            public void beginStep(final Step step, final Object data) {}
            @Override
            public void determineSteps(final Integer steps) {}
            @Override
            public void endProcess() {}
            @Override
            public void endStep(final Step step) {}
        });
    }
}

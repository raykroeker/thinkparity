/*
 * Created On:  31-Jan-07 3:15:07 PM
 */
package com.thinkparity.ophelia.model.migrator;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;

import com.thinkparity.ophelia.model.events.MigratorEvent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class MigratorEventGenerator {

    /** The <code>Source</code> to be used with this generator. */
    private final MigratorEvent.Source source;

    /**
     * Create MigratorEventGenerator.
     *
     */
    MigratorEventGenerator(final MigratorEvent.Source source) {
        super();
        this.source = source;
    }

    /**
     * Generate a migrator event.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return A <code>MigratorEvent</code>.
     */
    MigratorEvent generate(final Product product, final Release release) {
        return new MigratorEvent(source, product, release);
    }

}

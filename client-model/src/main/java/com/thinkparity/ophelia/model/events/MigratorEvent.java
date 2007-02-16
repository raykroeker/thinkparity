/*
 * Created On: Jun 29, 2006 8:58:06 AM
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;


/**
 * <b>Title:</b>thinkParity Migrator Event<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MigratorEvent {

    /** A <code>Product</code>. */
    private final Product product;

    /** A <code>Release</code>. */
    private final Release release;

    /** The migrator event <code>Source</code>. */
    private final Source source;

    /**
     * Create MigratorEvent.
     * 
     * @param source
     *            The migrator event <code>Source</code>.
     */
    public MigratorEvent(final Source source, final Product product,
            final Release release) {
        super();
        this.source = source;
        this.product = product;
        this.release = release;
    }

    /**
     * Obtain product.
     *
     * @return A Product.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Obtain release.
     *
     * @return A Release.
     */
    public Release getRelease() {
        return release;
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

    /** A migrator event source. */
    public enum Source { LOCAL, REMOTE }
}

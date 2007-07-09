/*
 * Created On: Jun 29, 2006 8:58:06 AM
 */
package com.thinkparity.ophelia.model.events;



/**
 * <b>Title:</b>thinkParity Migrator Event<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MigratorEvent {

    /** A <code>Product</code>. */
    private final String productName;

    /** A <code>Release</code>. */
    private final String releaseName;

    /** The migrator event <code>Source</code>. */
    private final Source source;

    /**
     * Create MigratorEvent.
     * 
     * @param source
     *            The migrator event <code>Source</code>.
     */
    public MigratorEvent(final Source source, final String productName,
            final String releaseName) {
        super();
        this.source = source;
        this.productName = productName;
        this.releaseName = releaseName;
    }

    /**
     * Obtain product.
     *
     * @return A Product.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Obtain release.
     *
     * @return A Release.
     */
    public String getReleaseName() {
        return releaseName;
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

/*
 * Created On:  31-Jan-07 1:01:02 PM
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;

/**
 * <b>Title:</b>thinkParity Product Release Deployed Event<br>
 * <b>Description:</b>The event that is fired when a product release has been
 * deployed.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProductReleaseDeployedEvent extends XMPPEvent {

    /** A <code>Product</code>. */
    private Product product;

    /** A <code>Release</code>. */
    private Release release;

    /** A <code>List</code> of <code>Resource</code>s. */
    private final List<Resource> resources;

    /**
     * Create ProductReleaseDeployedEvent.
     *
     */
    public ProductReleaseDeployedEvent() {
        super();
        this.resources = new ArrayList<Resource>();
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
     * Obtain resources.
     *
     * @return A List<Resource>.
     */
    public List<Resource> getResources() {
        return Collections.unmodifiableList(resources);
    }

    /**
     * Set product.
     *
     * @param product
     *		A Product.
     */
    public void setProduct(final Product product) {
        this.product = product;
    }

    /**
     * Set release.
     *
     * @param release
     *		A Release.
     */
    public void setRelease(final Release release) {
        this.release = release;
    }

    /**
     * Set resources.
     *
     * @param resources
     *		A List<Resource>.
     */
    public void setResources(final List<Resource> resources) {
        this.resources.clear();
        this.resources.addAll(resources);
    }
}

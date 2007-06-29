/*
 * Created On:  28-Feb-07 6:05:31 PM
 */
package com.thinkparity.codebase.model.migrator;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Feature {

    /** The feature id <code>Long</code>. */
    private transient Long featureId;

    /** The feature name <code>String</code>. */
    private String name;

    /** The product id <code>Long</code>. */
    private transient Long productId;

    /**
     * Create Feature.
     *
     */
    public Feature() {
        super();
    }

    /**
     * Obtain id.
     *
     * @return A Long.
     */
    public Long getFeatureId() {
        return featureId;
    }

    /**
     * Obtain name.
     *
     * @return A String.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtain productId.
     *
     * @return A Long.
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Set id.
     *
     * @param id
     *		A Long.
     */
    public void setFeatureId(final Long id) {
        this.featureId = id;
    }

    /**
     * Set name.
     *
     * @param name
     *		A String.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set productId.
     *
     * @param productId
     *		A Long.
     */
    public void setProductId(final Long productId) {
        this.productId = productId;
    }
}

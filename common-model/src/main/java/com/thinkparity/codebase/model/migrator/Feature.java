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

    /** The feature name <code>Name</code>. */
    private Name name;

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
     * @return A Name.
     */
    public Name getName() {
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
    public void setName(final Name name) {
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

    /** <b>Title:</b>Feature Name<br> */
    public enum Name { BACKUP, CORE }
}

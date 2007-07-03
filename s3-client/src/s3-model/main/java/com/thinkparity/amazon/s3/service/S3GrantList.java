/*
 * Created On:  22-Jun-07 8:42:44 AM
 */
package com.thinkparity.amazon.s3.service;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3GrantList {

    /** The grants. */
    private final List<S3Grant> grants;

    /** The owner. */
    private S3Owner owner;

    /**
     * Create S3GrantList.
     *
     */
    public S3GrantList() {
        super();
        this.grants = new ArrayList<S3Grant>();
    }

    /**
     * Add a grant.
     * 
     * @param grant
     *            A <code>S3Grant</code>.
     */
    public void addGrant(final S3Grant grant) {
        this.grants.add(grant);
    }

    /**
     * Clear the grants.
     *
     */
    public void clearGrants() {
        this.grants.clear();
    }

    /**
     * Obtain grants.
     *
     * @return A List<S3Grant>.
     */
    public List<S3Grant> getGrants() {
        return grants;
    }

    /**
     * Obtain owner.
     *
     * @return A S3Owner.
     */
    public S3Owner getOwner() {
        return owner;
    }

    /**
     * Remove a grant.
     * 
     * @param grant
     *            A <code>S3Grant</code>.
     */
    public void removeGrant(final S3Grant grant) {
        this.grants.remove(grant);
    }

    /**
     * Set grants.
     *
     * @param grants
     *		A List<S3Grant>.
     */
    public void setGrants(final List<S3Grant> grants) {
        this.grants.clear();
        this.grants.addAll(grants);
    }

    /**
     * Set owner.
     *
     * @param owner
     *		A S3Owner.
     */
    public void setOwner(final S3Owner owner) {
        this.owner = owner;
    }
}

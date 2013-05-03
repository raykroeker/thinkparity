/*
 * Created On:  19-Jun-07 8:34:07 PM
 */
package com.thinkparity.amazon.s3.service;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3Owner {

    /** The owner display name. */
    private String displayName;

    /** The owner id. */
    private String id;

    /**
     * Create S3Owner.
     *
     */
    public S3Owner() {
        super();
    }

    /**
     * Obtain displayName.
     *
     * @return A String.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Obtain id.
     *
     * @return A String.
     */
    public String getId() {
        return id;
    }

    /**
     * Set displayName.
     *
     * @param displayName
     *		A String.
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Set id.
     *
     * @param id
     *		A String.
     */
    public void setId(final String id) {
        this.id = id;
    }
}

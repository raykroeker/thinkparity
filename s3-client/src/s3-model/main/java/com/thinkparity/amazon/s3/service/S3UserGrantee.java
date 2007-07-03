/*
 * Created On:  21-Jun-07 7:30:40 PM
 */
package com.thinkparity.amazon.s3.service;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3UserGrantee extends S3Grantee {

    /** A grantee display name. */
    private String displayName;

    /** A grantee id. */
    private String id;

    /**
     * Create S3UserGrantee.
     *
     */
    public S3UserGrantee() {
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
     * @see com.thinkparity.amazon.s3.service.S3Grantee#getType()
     *
     */
    @Override
    public Type getType() {
        return Type.USER;
    }

    /**
     * Set displayName.
     *
     * @param displayName
     *      A String.
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Set id.
     *
     * @param id
     *      A String.
     */
    public void setId(final String id) {
        this.id = id;
    }
}

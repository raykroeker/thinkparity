/*
 * Created On:  21-Jun-07 7:30:17 PM
 */
package com.thinkparity.amazon.s3.service;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3Grant {

    /** The grantee. */
    private S3Grantee grantee;

    /** The permission. */
    private Permission permission;

    /**
     * Create S3Grant.
     *
     */
    public S3Grant() {
        super();
    }

    /**
     * Obtain grantee.
     *
     * @return A S3Grantee.
     */
    public S3Grantee getGrantee() {
        return grantee;
    }

    /**
     * Obtain permission.
     *
     * @return A Permission.
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Set grantee.
     *
     * @param grantee
     *		A S3Grantee.
     */
    public void setGrantee(final S3Grantee grantee) {
        this.grantee = grantee;
    }

    /**
     * Set permission.
     *
     * @param permission
     *		A Permission.
     */
    public void setPermission(final Permission permission) {
        this.permission = permission;
    }

    /** <b>Title:</b>Amazon S3 Grant Permission<br> */
    public enum Permission { FULL_CONTROL, READ, READ_ACP, WRITE, WRITE_ACP }
}

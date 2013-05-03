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
public abstract class S3Grantee {

    /**
     * Create S3Grantee.
     *
     */
    public S3Grantee() {
        super();
    }

    /**
     * Obtain the grantee type.
     * 
     * @return A <code>Type</code>.
     */
    public abstract Type getType();

    /** <b>Title:</b>S3 Grantee Type<br> */
    public enum Type { EMAIL, USER }
}

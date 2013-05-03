/*
 * Created On:  21-Jun-07 4:06:41 PM
 */
package com.thinkparity.amazon.s3.service.object;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3Key {

    private String resource;

    /**
     * Create S3Key.
     *
     */
    public S3Key() {
        super();
    }

    /**
     * Obtain resource.
     *
     * @return A String.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Set resource.
     *
     * @param resource
     *		A String.
     */
    public void setResource(final String resource) {
        this.resource = resource;
    }
}

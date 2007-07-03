/*
 * Created On:  20-Jun-07 10:21:25 AM
 */
package com.thinkparity.amazon.s3.service;

/**
 * <b>Title:</b>thinkParity Amazon S3 Service Authentication<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3Authentication {

    /** The access key id. */
    private String accessKeyId;

    /** The secret access key bytes. */
    private byte[] secretAccessKey;

    /**
     * Create S3Authentication.
     *
     */
    public S3Authentication() {
        super();
    }

    /**
     * Obtain accessKeyId.
     *
     * @return A String.
     */
    public String getAccessKeyId() {
        return accessKeyId;
    }

    /**
     * Obtain secretAccessKey.
     *
     * @return A byte[].
     */
    public byte[] getSecretAccessKey() {
        return secretAccessKey;
    }

    /**
     * Set accessKeyId.
     *
     * @param accessKeyId
     *		A String.
     */
    public void setAccessKeyId(final String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    /**
     * Set secretAccessKey.
     *
     * @param secretAccessKey
     *		A byte[].
     */
    public void setSecretAccessKey(final byte[] secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }
}

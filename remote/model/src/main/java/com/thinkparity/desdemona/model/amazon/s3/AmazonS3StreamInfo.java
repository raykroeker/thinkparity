/*
 * Created On:  25-Jul-07 10:51:03 AM
 */
package com.thinkparity.desdemona.model.amazon.s3;

/**
 * <b>Title:</b>thinkParity Desdemona Model Amazon S3 Stream Info<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AmazonS3StreamInfo {

    /** The stream size. */
    private String length;

    /** The stream md5 checksum. */
    private String md5;

    /** The stream content type. */
    private String type;

    /**
     * Create AmazonStreamInfo.
     *
     */
    public AmazonS3StreamInfo() {
        super();
    }

    public String getLength() {
        return length;
    }

    public String getMD5() {
        return md5;
    }

    /**
     * Obtain type.
     *
     * @return A String.
     */
    public String getType() {
        return type;
    }

    /**
     * Set streamSize.
     *
     * @param streamSize
     *		A Long.
     */
    public void setLength(final String length) {
        this.length = length;
    }

    /**
     * Set streamChecksum.
     *
     * @param streamChecksum
     *		A String.
     */
    public void setMD5(final String md5) {
        this.md5 = md5;
    }

    /**
     * Set type.
     *
     * @param type
     *		A String.
     */
    public void setType(final String type) {
        this.type = type;
    }
}

/*
 * Created On:  19-Jun-07 4:07:55 PM
 */
package com.thinkparity.amazon.s3.service.object;

import java.util.Date;

import com.thinkparity.amazon.s3.service.S3Owner;

/**
 * <b>Title:</b>thinkParity Amazon S3 Object<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3Object {

    /** The content checksum. */
    private String checksum;

    /** An etag. */
    private String etag;

    /** A key. */
    private S3Key key;

    /** A last modified date. */
    private Date lastModified;

    /** The object owner. */
    private S3Owner owner;

    /** The object size. */
    private Long size;

    /** The object storage class. */
    private StorageClass storageClass;

    /** The object content type. */
    private S3ObjectContentType type;

    /**
     * Create S3Object.
     *
     */
    public S3Object() {
        super();
    }

    /**
     * Obtain checksum.
     *
     * @return A String.
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * Obtain etag.
     *
     * @return A String.
     */
    public String getETag() {
        return etag;
    }

    /**
     * Obtain key.
     *
     * @return A S3Key.
     */
    public S3Key getKey() {
        return key;
    }

    /**
     * Obtain lastModified.
     *
     * @return A Date.
     */
    public Date getLastModified() {
        return lastModified;
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
     * Obtain size.
     *
     * @return A Long.
     */
    public Long getSize() {
        return size;
    }

    /**
     * Obtain storageClass.
     *
     * @return A StorageClass.
     */
    public StorageClass getStorageClass() {
        return storageClass;
    }

    /**
     * Obtain contentType.
     *
     * @return A S3ObjectContentType.
     */
    public S3ObjectContentType getType() {
        return type;
    }

    /**
     * Set checksum.
     *
     * @param checksum
     *		A String.
     */
    public void setChecksum(final String checksum) {
        this.checksum = checksum;
    }

    /**
     * Set etag.
     *
     * @param etag
     *		A String.
     */
    public void setETag(final String etag) {
        this.etag = etag;
    }

    /**
     * Set key.
     *
     * @param key
     *		A S3Key.
     */
    public void setKey(final S3Key key) {
        this.key = key;
    }

    /**
     * Set lastModified.
     *
     * @param lastModified
     *		A Date.
     */
    public void setLastModified(final Date lastModified) {
        this.lastModified = lastModified;
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

    /**
     * Set size.
     *
     * @param size
     *		A Long.
     */
    public void setSize(final Long size) {
        this.size = size;
    }

    /**
     * Set storageClass.
     *
     * @param storageClass
     *		A StorageClass.
     */
    public void setStorageClass(final StorageClass storageClass) {
        this.storageClass = storageClass;
    }

    /**
     * Set contentType.
     *
     * @param type
     *		A S3ObjectContentType.
     */
    public void setType(final S3ObjectContentType type) {
        this.type = type;
    }

    /** <b>Title:</b>Object Storage Class<br> */
    public enum StorageClass { STANDARD }
}

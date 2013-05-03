/*
 * Created On:  19-Jun-07 4:07:01 PM
 */
package com.thinkparity.amazon.s3.service.bucket;

import java.util.Date;

/**
 * <b>Title:</b>thinkParity Amazon S3 Bucket<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3Bucket {

    /** The creation date. */
    private Date creationDate;

    /** The name. */
    private String name;

    /**
     * Create S3Bucket.
     *
     */
    public S3Bucket() {
        super();
    }

    /**
     * Obtain creationDate.
     *
     * @return A Calendar.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Obtain name.
     *
     * @return A String.
     */
    public String getName() {
        return name;
    }

    /**
     * Set creationDate.
     *
     * @param creationDate
     *		A Calendar.
     */
    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Set name.
     *
     * @param name
     *		A String.
     */
    public void setName(final String name) {
        this.name = name;
    }
}

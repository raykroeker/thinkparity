/*
 * Created On:  22-Jun-07 8:29:44 AM
 */
package com.thinkparity.amazon.s3.service.bucket;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.amazon.s3.service.S3Owner;

/**
 * <b>Title:</b>thinkParity Amazon S3 Bucket List<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3BucketList {

    /** The bucket list. */
    private final List<S3Bucket> buckets;

    /** The bucket list owner. */
    private S3Owner owner;

    /**
     * Create S3BucketList.
     *
     */
    public S3BucketList() {
        super();
        this.buckets = new ArrayList<S3Bucket>();
    }

    /**
     * Add a bucket to the list.
     * 
     * @param bucket
     *            A <code>S3Bucket</code>.
     */
    public void addBucket(final S3Bucket bucket) {
        this.buckets.add(bucket);
    }

    /**
     * Clear the bucket list.
     *
     */
    public void clearBuckets() {
        this.buckets.clear();
    }

    /**
     * Obtain buckets.
     *
     * @return A List<S3Bucket>.
     */
    public List<S3Bucket> getBuckets() {
        return buckets;
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
     * Remove a bucket from the list.
     * 
     * @param bucket
     *            A <code>S3Bucket</code>.
     */
    public void removeBucket(final S3Bucket bucket) {
        this.buckets.remove(bucket);
    }

    /**
     * Set buckets.
     *
     * @param buckets
     *		A List<S3Bucket>.
     */
    public void setBuckets(final List<S3Bucket> buckets) {
        this.buckets.clear();
        this.buckets.addAll(buckets);
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

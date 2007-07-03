/*
 * Created On:  19-Jun-07 3:39:21 PM
 */
package com.thinkparity.amazon.s3.service.bucket;

import java.util.List;

import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.S3Grant;
import com.thinkparity.amazon.s3.service.S3GrantList;
import com.thinkparity.amazon.s3.service.S3Owner;
import com.thinkparity.amazon.s3.service.object.S3ObjectList;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface BucketService {

    /**
     * Create a bucket.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @param bucket
     *            A <code>S3Bucket</code>.
     */
    void create(S3Authentication auth, S3Bucket bucket);

    /**
     * Delete a bucket.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @param bucket
     *            A <code>S3Bucket</code>.
     */
    void delete(S3Authentication auth, S3Bucket bucket);

    /**
     * Read the list of buckets.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @return A <code>S3BucketList</code>.
     */
    S3BucketList read(S3Authentication auth);

    /**
     * Read the bucket acl.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @param bucket
     *            A <code>S3Bucket</code>.
     * @return A <code>List<S3Grant></code>.
     */
    S3GrantList readACL(S3Authentication auth, S3Bucket bucket);

    /**
     * Read the list of objects.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @param bucket
     *            A <code>S3Bucket</code>.
     * @return A <code>S3ObjectList</code>.
     */
    S3ObjectList readObjects(S3Authentication auth, S3Bucket bucket);

    /**
     * Update the bucket acl.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @param bucket
     *            A <code>S3Bucket</code>.
     * @param owner
     *            A <code>S3Owner</code>.
     * @param grants
     *            A <code>List<S3Grant></code>.
     */
    void updateACL(S3Authentication auth, S3Bucket bucket, S3Owner owner,
            List<S3Grant> grants);
}

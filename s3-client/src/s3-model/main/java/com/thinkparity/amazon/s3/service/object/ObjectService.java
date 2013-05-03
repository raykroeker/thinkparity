/*
 * Created On:  19-Jun-07 3:39:34 PM
 */
package com.thinkparity.amazon.s3.service.object;

import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.S3GrantList;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;

/**
 * <b>Title:</b>thinkParity Amazon S3 Object Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ObjectService {

    /**
     * Create an object.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @param bucket
     *            A <code>S3Bucket</code>.
     * @param object
     *            A <code>S3Object</code>.
     * @param readableContent
     *            A <code>S3ReadableObjectContent</code>.
     */
    void create(S3Authentication auth, S3Bucket bucket, S3Object object,
            S3ReadableObjectContent readableContent);

    /**
     * Delete an object.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @param bucket
     *            A <code>S3Bucket</code>.
     * @param key
     *            A <code>S3Key</code>.
     */
    void delete(S3Authentication auth, S3Bucket bucket, S3Key key);

    /**
     * Read the object.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @param bucket
     *            A <code>S3Bucket</code>.
     * @param key
     *            A <code>S3Key</code>.
     * @return A <code>S3Object</code>.
     */
    S3Object read(S3Authentication auth, S3Bucket bucket, S3Key key);

    /**
     * Read the object content.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @param bucket
     *            A <code>S3Bucket</code>.
     * @param key
     *            A <code>S3Key</code>.
     * @param writableContent
     *            A <code>S3WritableObjectContent</code>.
     * @return A <code>S3Object</code>.
     */
    void readContent(S3Authentication auth, S3Bucket bucket, S3Key key,
            S3WritableObjectContent writableContent);

    /**
     * Read the object acl.
     * 
     * @param auth
     *            An amazon <code>S3Authentication</code>.
     * @param bucket
     *            A <code>S3Bucket</code>.
     * @param key
     *            A <code>S3Key</code>.
     * @return A <code>List<S3Grant></code>.
     */
    S3GrantList readACL(S3Authentication auth, S3Bucket bucket, S3Key key);
}

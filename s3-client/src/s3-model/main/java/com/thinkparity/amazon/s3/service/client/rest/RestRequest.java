/*
 * Created On:  20-Jun-07 8:47:11 AM
 */
package com.thinkparity.amazon.s3.service.client.rest;

import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.S3Context;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.bucket.S3Filter;
import com.thinkparity.amazon.s3.service.object.S3Key;
import com.thinkparity.amazon.s3.service.object.S3Object;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RestRequest {

    /** An indicator of whether or not this is an acl request. */
    private Boolean aclRequest;

    /** An amazon s3 service authentication. */
    private S3Authentication authentication;

    /** The request bucket. */
    private S3Bucket bucket;

    /** An amazon s3 service context. */
    private S3Context context;

    /** A filter. */
    private S3Filter filter;

    /** The key. */
    private S3Key key;

    /** The request object. */
    private S3Object object;

    /** A request content serializer. */
    private RestRequestContentSerializer serializer;

    /** A rest request type. */
    private Type type;

    /**
     * Create RestMethod.
     *
     */
    public RestRequest() {
        super();
    }

    /**
     * Obtain the request authentication.
     * 
     * @return A <code>S3Authentication</code>.
     */
    public S3Authentication getAuthentication() {
        return authentication;
    }

    /**
     * Obtain bucket.
     *
     * @return A S3Bucket.
     */
    public S3Bucket getBucket() {
        return bucket;
    }


    /**
     * Obtain context.
     *
     * @return A S3Context.
     */
    public S3Context getContext() {
        return context;
    }

    /**
     * Obtain the filter.
     *
     * @return A <code>S3Filter</code>.
     */
    public S3Filter getFilter() {
        return filter;
    }

    /**
     * Obtain the key.
     * 
     * @return A <code>S3Key</code>.
     */
    public S3Key getKey() {
        return key;
    }

    /**
     * Obtain key.
     *
     * @return A S3Key.
     */
    public S3Object getObject() {
        return object;
    }

    /**
     * Obtain the request content serializer.
     * 
     * @return A <code>RestRequestContentSerializer</code>.
     */
    public RestRequestContentSerializer getSerializer() {
        return serializer;
    }

    /**
     * Obtain type.
     *
     * @return A Type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Determine whether or not this is an acl request.
     * 
     * @return True if this is an acl request.
     */
    public Boolean isACLRequest() {
        if (null == aclRequest) {
            return Boolean.FALSE;
        } else {
            return aclRequest;
        }
    }

    /**
     * Determine if the bucket has been set.
     * 
     * @return True if the bucket is set.
     */
    public Boolean isSetBucket() {
        return Boolean.valueOf(null != bucket);
    }

    /**
     * Determine if the filter is set.
     * 
     * @return True if the filter is set.
     */
    public Boolean isSetFilter() {
        return null != filter;
    }

    /**
     * Determine if the key is set.
     * 
     * @return True if the key is set.
     */
    public Boolean isSetKey() {
        return Boolean.valueOf(null != key);
    }

    /**
     * Determine if the key has been set.
     * 
     * @return True if the key is set.
     */
    public Boolean isSetObject() {
        return Boolean.valueOf(null != object);
    }

    /**
     * Determine if the content writer is set.
     * 
     * @return True if the content writer is set.
     */
    public Boolean isSetSerializer() {
        return Boolean.valueOf(null != serializer);
    }

    /**
     * Set the acl request to true.
     * 
     */
    public void setACLRequest() {
        setACLRequest(Boolean.TRUE);
    }

    /**
     * Set the acl request.
     * 
     * @param aclRequest
     *            A <code>Boolean</code>.
     */
    public void setACLRequest(final Boolean aclRequest) {
        this.aclRequest = aclRequest;
    }

    /**
     * Set the request authentication.
     * 
     * @param authentication
     *            A <code>S3Authentication</code>.
     */
    public void setAuthentication(final S3Authentication authentication) {
        this.authentication = authentication;
    }

    /**
     * Set bucket.
     *
     * @param bucket
     *		A S3Bucket.
     */
    public void setBucket(final S3Bucket bucket) {
        this.bucket = bucket;
    }

    /**
     * Set context.
     *
     * @param context
     *		A S3Context.
     */
    public void setContext(final S3Context context) {
        this.context = context;
    }

    /**
     * Set the filter.
     *
     * @param filter
     *		A <code>S3Filter</code>.
     */
    public void setFilter(final S3Filter filter) {
        this.filter = filter;
    }

    /**
     * Set the key.
     * 
     * @param key
     *            A <code>S3Key</code>.
     */
    public void setKey(final S3Key key) {
        this.key = key;
    }

    /**
     * Set key.
     *
     * @param key
     *		A S3Key.
     */
    public void setObject(final S3Object object) {
        this.object = object;
        setKey(object.getKey());
    }

    /**
     * Set type.
     *
     * @param type
     *      A Type.
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * Set type.
     *
     * @param type
     *      A Type.
     */
    public void setType(final Type type,
            final RestRequestContentSerializer serializer) {
        switch (type) {
        case PUT:
            break;
        default:
            throw new IllegalArgumentException("Only put methods support a serializer.");
        }
        setType(type);
        this.serializer = serializer;
    }

    /** <b>Title:</b>Rest Request Type<br> */
    public enum Type { DELETE, GET, HEAD, PUT }
}

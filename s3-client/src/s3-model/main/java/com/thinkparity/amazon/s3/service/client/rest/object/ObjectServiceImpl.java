/*
 * Created On:  19-Jun-07 10:02:49 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.object;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.thinkparity.codebase.StreamUtil;

import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.S3AuthenticationConstraints;
import com.thinkparity.amazon.s3.service.S3Context;
import com.thinkparity.amazon.s3.service.S3GrantList;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.bucket.S3BucketConstraints;
import com.thinkparity.amazon.s3.service.client.rest.BinarySerializer;
import com.thinkparity.amazon.s3.service.client.rest.ResponseBodySerializer;
import com.thinkparity.amazon.s3.service.client.rest.RestRequest;
import com.thinkparity.amazon.s3.service.client.rest.RestResponse;
import com.thinkparity.amazon.s3.service.client.rest.RestServiceImpl;
import com.thinkparity.amazon.s3.service.client.rest.RestUtils;
import com.thinkparity.amazon.s3.service.client.rest.http.HeaderReader;
import com.thinkparity.amazon.s3.service.client.rest.http.S3ObjectHeaderReader;
import com.thinkparity.amazon.s3.service.client.rest.xml.Parser;
import com.thinkparity.amazon.s3.service.client.rest.xml.S3GrantListParser;
import com.thinkparity.amazon.s3.service.object.ObjectService;
import com.thinkparity.amazon.s3.service.object.S3Key;
import com.thinkparity.amazon.s3.service.object.S3Object;
import com.thinkparity.amazon.s3.service.object.S3ObjectConstraints;
import com.thinkparity.amazon.s3.service.object.S3ReadableObjectContent;
import com.thinkparity.amazon.s3.service.object.S3WritableObjectContent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ObjectServiceImpl extends RestServiceImpl implements
        ObjectService {

    /**
     * Create a grant list.
     * 
     * @return A <code>S3GrantList</code>.
     */
    private static S3GrantList newS3GrantList() {
        return new S3GrantList();
    }

    /**
     * Create a grant list parser.
     * 
     * @return A <code>S3GrantListParser</code>.
     */
    private static Parser<S3GrantList> newS3GrantListParser() {
        return new S3GrantListParser();
    }

    /**
     * Create an s3 object.
     * 
     * @return An instance of <code>S3Object</code>.
     */
    private static S3Object newS3Object() {
        return new S3Object();
    }

    /**
     * Obtain a new object header reader.
     * 
     * @return A <code>HeaderReader<S3Object></code>.
     */
    private static HeaderReader<S3Object> newS3ObjectHeaderReader() {
        return new S3ObjectHeaderReader();
    }

    /** The <code>S3AuthenticationConstraints</code>. */
    private final S3AuthenticationConstraints authConstraints;

    /** The <code>S3BucketConstraints</code>. */
    private final S3BucketConstraints bucketConstraints;

    /** The <code>S3ObjectConstraints</code>. */
    private final S3ObjectConstraints objectConstraints;

    /** A set of rest utilities. */
    private final RestUtils utils;

    /**
     * Create ObjectServiceImpl.
     * 
     */
    public ObjectServiceImpl() {
        super();
        this.authConstraints = S3AuthenticationConstraints.getInstance();
        this.bucketConstraints = S3BucketConstraints.getInstance();
        this.objectConstraints = S3ObjectConstraints.getInstance();
        this.utils = new RestUtils();
    }

    /**
     * @see com.thinkparity.amazon.s3.service.object.ObjectService#create(com.thinkparity.amazon.s3.service.S3Authentication,
     *      com.thinkparity.amazon.s3.service.bucket.S3Bucket,
     *      com.thinkparity.amazon.s3.service.object.S3Object,
     *      com.thinkparity.amazon.s3.service.object.S3ReadableObjectContent,
     *      com.thinkparity.amazon.s3.service.object.S3ObjectHeaders)
     * 
     */
    public void create(final S3Authentication auth, final S3Bucket bucket,
            final S3Object object,
            final S3ReadableObjectContent readableContent) {
        try {
            validate(auth);
            validate(bucket);
            validate(object);

            final RestRequest request = new RestRequest();
            request.setContext(context);
            request.setAuthentication(auth);
            request.setType(RestRequest.Type.PUT, new BinarySerializer() {
                @Override
                public long getContentLength() {
                    return object.getSize().longValue();
                }
                @Override
                public String getContentType() {
                    return object.getType().getValue();
                }
                @Override
                public void write(final OutputStream stream) throws IOException {
                    final Object lock = context.lockBuffer();
                    try {
                        synchronized (lock) {
                            final ReadableByteChannel channel = readableContent.openReadChannel();
                            try {
                                StreamUtil.copy(channel, stream, context.getBuffer(lock));
                            } finally {
                                channel.close();
                            }
                        }
                    } finally {
                        context.unlockBuffer(lock);
                    }
                }
            });
            request.setBucket(bucket);
            request.setObject(object);

            utils.service(request);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.amazon.s3.service.object.ObjectService#delete(com.thinkparity.amazon.s3.service.S3Authentication, com.thinkparity.amazon.s3.service.bucket.S3Bucket, com.thinkparity.amazon.s3.service.object.S3Key)
     *
     */
    public void delete(final S3Authentication auth, final S3Bucket bucket,
            final S3Key key) {
        try {
            validate(auth);
            validate(bucket);
            validate(key);

            final RestRequest request = new RestRequest();
            request.setContext(context);
            request.setAuthentication(auth);
            request.setType(RestRequest.Type.DELETE);
            request.setBucket(bucket);
            request.setKey(key);

            utils.service(request);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.amazon.s3.service.client.rest.RestServiceImpl#initialize(com.thinkparity.amazon.s3.service.S3Context)
     *
     */
    @Override
    public void initialize(S3Context context) {
        super.initialize(context);
    }

    /**
     * @see com.thinkparity.amazon.s3.service.object.ObjectService#read(com.thinkparity.amazon.s3.service.S3Authentication, com.thinkparity.amazon.s3.service.bucket.S3Bucket, com.thinkparity.amazon.s3.service.object.S3Key)
     *
     */
    public S3Object read(final S3Authentication auth, final S3Bucket bucket,
            final S3Key key) {
        try {
            validate(auth);
            validate(bucket);
            validate(key);

            final RestRequest request = new RestRequest();
            request.setContext(context);
            request.setAuthentication(auth);
            request.setType(RestRequest.Type.HEAD);
            request.setBucket(bucket);
            request.setKey(key);

            final RestResponse<S3Object> response = utils.service(request,
                    newS3ObjectHeaderReader(), newS3Object());
            final S3Object result = response.getResult();
            result.setKey(key);
            return result;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.amazon.s3.service.object.ObjectService#readACL(com.thinkparity.amazon.s3.service.S3Authentication, com.thinkparity.amazon.s3.service.bucket.S3Bucket, com.thinkparity.amazon.s3.service.object.S3Key)
     *
     */
    public S3GrantList readACL(final S3Authentication auth,
            final S3Bucket bucket, final S3Key key) {
        try {
            validate(auth);
            validate(bucket);
            validate(key);

            final RestRequest request = new RestRequest();
            request.setACLRequest();
            request.setAuthentication(auth);
            request.setBucket(bucket);
            request.setContext(context);
            request.setKey(key);
            request.setType(RestRequest.Type.GET);

            final RestResponse<S3GrantList> response = utils.service(request,
                    newS3GrantListParser(), newS3GrantList());

            return response.getResult();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.amazon.s3.service.object.ObjectService#readContent(com.thinkparity.amazon.s3.service.S3Authentication, com.thinkparity.amazon.s3.service.bucket.S3Bucket, com.thinkparity.amazon.s3.service.object.S3Key, com.thinkparity.amazon.s3.service.object.S3WritableObjectContent)
     *
     */
    public void readContent(final S3Authentication auth, final S3Bucket bucket,
            final S3Key key, final S3WritableObjectContent writableContent) {
        try {
            validate(auth);
            validate(bucket);
            validate(key);

            final RestRequest request = new RestRequest();
            request.setAuthentication(auth);
            request.setBucket(bucket);
            request.setContext(context);
            request.setKey(key);
            request.setType(RestRequest.Type.GET);

            final WritableByteChannel channel = writableContent.openWriteChannel();
            try {
                utils.service(request, new ResponseBodySerializer() {
                    public void read(final InputStream stream) throws IOException {
                        final Object lock = context.lockBuffer();
                        try {
                            synchronized (lock) {
                                StreamUtil.copy(stream, channel,
                                        context.getBuffer(lock));
                            }
                        } finally {
                            context.unlockBuffer(lock);
                        }
                    }
                });
            } finally {
                channel.close();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }
    
    /**
     * Validate the authentication.
     * 
     * @param auth
     *            A <code>S3Authentication</code>.
     */
    private void validate(final S3Authentication auth) {
        authConstraints.getAccessKeyId().validate(auth.getAccessKeyId());
        authConstraints.getSecretAccessKey().validate(auth.getSecretAccessKey());
    }

    /**
     * Validate the bucket.
     * 
     * @param bucket
     *            A <code>S3Bucket</code>.
     */
    private void validate(final S3Bucket bucket) {
        bucketConstraints.getName().validate(bucket.getName());
    }

    /**
     * Validate the key.
     * 
     * @param key
     *            A <code>S3Key</code>.
     */
    private void validate(final S3Key key) {
        objectConstraints.getKey().validate(key);
    }

    /**
     * Validate the object.
     * 
     * @param object
     *            A <code>S3Object</code>.
     */
    private void validate(final S3Object object) {
        validate(object.getKey());
        objectConstraints.getType().validate(object.getType());
        objectConstraints.getChecksum().validate(object.getChecksum());
        objectConstraints.getSize().validate(object.getSize());
    }
}

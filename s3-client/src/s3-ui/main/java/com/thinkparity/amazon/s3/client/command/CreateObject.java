/*
 * Created On:  21-Jun-07 1:09:14 PM
 */
package com.thinkparity.amazon.s3.client.command;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.text.MessageFormat;

import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.nio.ChannelUtil;

import com.thinkparity.codebase.model.util.codec.MD5Util;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.client.S3Command;
import com.thinkparity.amazon.s3.client.S3CommandContext;
import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.object.ObjectService;
import com.thinkparity.amazon.s3.service.object.S3Key;
import com.thinkparity.amazon.s3.service.object.S3Object;
import com.thinkparity.amazon.s3.service.object.S3ObjectContentType;
import com.thinkparity.amazon.s3.service.object.S3ReadableObjectContent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateObject implements S3Command {

    /** An amazon s3 authentication. */
    private S3Authentication authentication;

    /** The console. */
    private S3ClientConsole console;

    /** The command context. */
    private S3CommandContext context;

    /** A log4j wrapper. */
    private Log4JWrapper logger;

    /** An amazon s3 object web-service. */
    private ObjectService objectService;

    /**
     * Create CreateBucket.
     *
     */
    public CreateObject() {
        super();
    }

    /**
     * @see com.thinkparity.amazon.s3.client.S3Command#getName()
     *
     */
    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * @see com.thinkparity.amazon.s3.client.S3Command#initialize(com.thinkparity.amazon.s3.client.S3CommandContext)
     *
     */
    public void initialize(final S3CommandContext context) {
        this.authentication = context.getAuthentication();
        this.logger = context.getLogger();
        this.console = context.getConsole();
        this.context = context;
        this.objectService = context.getServiceFactory().getObjectService();
    }

    /**
     * @see com.thinkparity.amazon.s3.client.S3Command#invoke()
     *
     */
    public void invoke() {
        logger.logTraceId();
        final String bucketName = console.readLine("Bucket name:");
        logger.logVariable("bucketName", bucketName);
        final String objectLocation = console.readLine("Object location:");
        logger.logVariable("objectLocation", objectLocation);
        final String objectKey = console.readLine("Object key:");
        logger.logVariable("objectKey", objectKey);
        String message;
        if (console.confirm("Are you sure you want to create ''{0}'' in ''{1}''?",
                objectKey, bucketName)) {
            final File file = locate(objectLocation);
            if (null == file) {
                throw new RuntimeException("Cannot locate " + objectLocation + ".");
            }

            final S3Bucket bucket = new S3Bucket();
            bucket.setName(bucketName);

            final S3Object object = new S3Object();
            try {
                object.setChecksum(checksum(file));
            } catch (final IOException iox) {
                throw new RuntimeException("Cannot calculate checksum.", iox);
            }
            final S3Key key = new S3Key();
            key.setResource(objectKey);
            object.setKey(key);
            object.setSize(size(file));
            object.setType(S3ObjectContentType.BINARY_OCTET_STREAM);

            final S3ReadableObjectContent content = new S3ReadableObjectContent() {
                public ReadableByteChannel openReadChannel() throws IOException {
                    return ChannelUtil.openReadChannel(file);
                }
            };
            create(bucket, object, content);
            message = MessageFormat.format(
                    "Object ''{0}'' has been created in bucket ''{1}''.",
                    object.getKey().getResource(), bucket.getName());
            logger.logInfo(message);
            console.println(message);
        }
        logger.logTraceId();
    }

    /**
     * Calculate the object checksum.
     * 
     * @return A md5 checksum <code>String</code>.
     * @throws IOException
     */
    private String checksum(final File file) throws IOException {
        final ReadableByteChannel channel = ChannelUtil.openReadChannel(file);
        try {
            final Object lock = context.getBufferLock();
            synchronized (lock) {
                return MD5Util.md5Base64(channel, context.getBufferArray(lock));
            }
        } finally {
            channel.close();
        }
    }

    /**
     * Create an object.
     * 
     * @param bucket
     *            A <code>S3Bucket</code>.
     * @param object
     *            A <code>S3Object</code>.
     * @param headers
     *            A <code>S3ObjectHeaders</code>.
     */
    private void create(final S3Bucket bucket, final S3Object object,
            final S3ReadableObjectContent content) {
        objectService.create(authentication, bucket, object, content);
    }

    private File locate(final String location) {
        final File file = new File(location);
        if (file.exists()) {
            return file;
        } else {
            return null; 
        }
    }

    /**
     * Calculate the object size.
     * 
     * @return The size <code>Long</code>.
     */
    private Long size(final File file) {
        return Long.valueOf(file.length());
    }
}

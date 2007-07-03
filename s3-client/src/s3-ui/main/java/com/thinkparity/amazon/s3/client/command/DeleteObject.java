/*
 * Created On:  21-Jun-07 1:09:14 PM
 */
package com.thinkparity.amazon.s3.client.command;

import java.text.MessageFormat;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.client.S3Command;
import com.thinkparity.amazon.s3.client.S3CommandContext;
import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.object.ObjectService;
import com.thinkparity.amazon.s3.service.object.S3Key;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeleteObject implements S3Command {

    /** An amazon s3 authentication. */
    private S3Authentication authentication;

    /** An amazon s3 object web-service. */
    private ObjectService objectService;

    /** The console. */
    private S3ClientConsole console;

    /** A log4j wrapper. */
    private Log4JWrapper logger;

    /**
     * Create CreateBucket.
     *
     */
    public DeleteObject() {
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
        this.logger = context.getLogger();
        this.authentication = context.getAuthentication();
        this.console = context.getConsole();
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
        final String objectKey = console.readLine("Object key to delete:");
        logger.logVariable("objectKey", objectKey);
        if (console.confirm("Are you sure you want to delete ''{0}'' from bucket ''{1}''?", objectKey, bucketName)) {
            final S3Bucket bucket = new S3Bucket();
            bucket.setName(bucketName);
            final S3Key key = new S3Key();
            key.setResource(objectKey);
            delete(bucket, key);
            final String message = MessageFormat.format(
                    "Object ''{0}'' has been deleted from bucket ''{1}''.",
                    key.getResource(), bucket.getName());
            logger.logInfo(message);
            console.println(message);
        }
        logger.logTraceId();
    }

    /**
     * Delete a bucket.
     * 
     * @param bucket
     *            A <code>S3Bucket</code>.
     */
    private void delete(final S3Bucket bucket, final S3Key key) {
        objectService.delete(authentication, bucket, key);
    }
}

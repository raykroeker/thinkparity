/*
 * Created On:  20-Jun-07 9:52:39 AM
 */
package com.thinkparity.amazon.s3.client.command;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.client.S3Command;
import com.thinkparity.amazon.s3.client.S3CommandContext;
import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.object.ObjectService;
import com.thinkparity.amazon.s3.service.object.S3Key;
import com.thinkparity.amazon.s3.service.object.S3Object;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadObject implements S3Command {

    /** An amazon s3 authentication. */
    private S3Authentication authentication;

    /** An amazon s3 object web-service. */
    private ObjectService objectService;

    /** A <code>S3ClientConsole</code>. */
    private S3ClientConsole console;

    /** A log4j wrapper. */
    private Log4JWrapper logger;

    /**
     * Create ReadBuckets.
     *
     */
    public ReadObject() {
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
     * @see com.thinkparity.amazon.s3.client.Command#invoke()
     *
     */
    public void invoke() {
        logger.logTraceId();
        final String bucketName = console.readLine("Bucket name:");
        logger.logVariable("bucketName", bucketName);
        final S3Bucket bucket = new S3Bucket();
        bucket.setName(bucketName);
        final String objectKey = console.readLine("Object key:");
        logger.logVariable("objectKey", objectKey);
        final S3Key key = new S3Key();
        key.setResource(objectKey);
        final S3Object object = objectService.read(authentication, bucket, key);
        S3ClientConsoleUtil.print(console, object);
        logger.logTraceId();
    }
}

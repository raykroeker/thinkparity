/*
 * Created On:  21-Jun-07 1:09:14 PM
 */
package com.thinkparity.amazon.s3.client.command;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.client.S3Command;
import com.thinkparity.amazon.s3.client.S3CommandContext;
import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.bucket.BucketService;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeleteBucket implements S3Command {

    /** An amazon s3 authentication. */
    private S3Authentication authentication;

    /** An amazon s3 bucket web-service. */
    private BucketService bucketService;

    /** The console. */
    private S3ClientConsole console;

    /** A log4j wrapper. */
    private Log4JWrapper logger;

    /**
     * Create CreateBucket.
     *
     */
    public DeleteBucket() {
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
        this.bucketService = context.getServiceFactory().getBucketService();
        this.console = context.getConsole();
    }

    /**
     * @see com.thinkparity.amazon.s3.client.S3Command#invoke()
     *
     */
    public void invoke() {
        logger.logTraceId();
        final String name = console.readLine("Bucket name to delete:");
        logger.logVariable("name", name);
        if (console.confirm("Are you sure you want to delete ''{0}''?", name)) {
            final S3Bucket bucket = new S3Bucket();
            bucket.setName(name);
            delete(bucket);
            logger.logInfo("Bucket ''{0}'' has been deleted.", bucket.getName());
            console.println("Bucket ''{0}'' has been deleted.", bucket.getName());
        }
        logger.logTraceId();
    }

    /**
     * Delete a bucket.
     * 
     * @param bucket
     *            A <code>S3Bucket</code>.
     */
    private void delete(final S3Bucket bucket) {
        bucketService.delete(authentication, bucket);
    }
}

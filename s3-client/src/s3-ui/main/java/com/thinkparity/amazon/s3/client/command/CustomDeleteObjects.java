/*
 * Created On:  21-Jun-07 1:09:14 PM
 */
package com.thinkparity.amazon.s3.client.command;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.TimeFormat;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.client.S3Command;
import com.thinkparity.amazon.s3.client.S3CommandContext;
import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.bucket.BucketService;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.bucket.S3Filter;
import com.thinkparity.amazon.s3.service.object.ObjectService;
import com.thinkparity.amazon.s3.service.object.S3Key;
import com.thinkparity.amazon.s3.service.object.S3Object;
import com.thinkparity.amazon.s3.service.object.S3ObjectList;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CustomDeleteObjects implements S3Command {

    /** An amazon s3 authentication. */
    private S3Authentication authentication;

    /** An amazon s3 bucket web-service. */
    private BucketService bucketService;

    /** The console. */
    private S3ClientConsole console;

    /** A log4j wrapper. */
    private Log4JWrapper logger;

    /** An amazon s3 object web-service. */
    private ObjectService objectService;

    /**
     * Create CreateBucket.
     *
     */
    public CustomDeleteObjects() {
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
        this.bucketService = context.getServiceFactory().getBucketService();
        this.objectService = context.getServiceFactory().getObjectService();
    }

    /**
     * @see com.thinkparity.amazon.s3.client.S3Command#invoke()
     *
     */
    public void invoke() {
        logger.logTraceId();
        final String name = console.readLine("Bucket name:");
        logger.logVariable("name", name);
        final S3Bucket bucket = new S3Bucket();
        bucket.setName(name);

        final S3Filter filter = new S3Filter();
        filter.setPrefix("archives");
        if (console.confirm("Are you sure you want to delete all \"{0}\" in \"{1}\"?",
                filter.getPrefix(), bucket.getName())) {
            delete(bucket, filter);
        }
        filter.setPrefix("artifacts");
        if (console.confirm("Are you sure you want to delete all \"{0}\" in \"{1}\"?",
                filter.getPrefix(), bucket.getName())) {
            delete(bucket, filter);
        }
        filter.setPrefix("products");
        if (console.confirm("Are you sure you want to delete all \"{0}\" in \"{1}\"?",
                filter.getPrefix(), bucket.getName())) {
            delete(bucket, filter);
        }
    }

    /**
     * Delete all objects in the bucket that match the filter.
     * 
     * @param bucket
     *            A <code>S3Bucket</code>.
     * @param filter
     *            A <code>S3Filter</code>.
     */
    private void delete(final S3Bucket bucket, final S3Filter filter) {
        final List<S3Object> s3ObjectArrayList = new ArrayList<S3Object>(1000);
        S3ObjectList s3ObjectList;
        long totalDeleted = 0;
        long totalSizeDeleted = 0;
        final BytesFormat bytesFormat = new BytesFormat();
        final TimeFormat timeFormat = new TimeFormat();
        final long begin = System.currentTimeMillis();
        logger.logVariable("begin", begin);
        long iterationBegin, iterationDuration;
        while (true) {
            iterationBegin = System.currentTimeMillis();
            s3ObjectList = bucketService.readObjects(authentication, bucket, filter);
            if (null == s3ObjectList) {
                console.println("Delete \"{0}\" is complete.", filter.getPrefix());
                logger.logInfo("No objects remain.");
                break;
            }
            s3ObjectArrayList.clear();
            s3ObjectArrayList.addAll(s3ObjectList.getObjects());
            if (0 == s3ObjectArrayList.size()) {
                console.println("Delete \"{0}\" is complete.", filter.getPrefix());
                logger.logInfo("No objects remain.");
                break;
            }
            for (final S3Object s3Object : s3ObjectArrayList) {
                delete(bucket, s3Object.getKey());
                totalDeleted++;
                totalSizeDeleted += s3Object.getSize();
                logger.logVariable("s3Object.getKey().getResource()", s3Object.getKey().getResource());
                logger.logVariable("s3Object.getLastModified()", s3Object.getLastModified());
                logger.logVariable("s3Object.getSize()", s3Object.getSize());
                logger.logVariable("totalDeleted", totalDeleted);
                logger.logVariable("totalSizeDeleted", bytesFormat.format(totalSizeDeleted));
            }
            iterationDuration = System.currentTimeMillis() - iterationBegin;
            console.println("{0} ({1}) objects deleted in {2}.", totalDeleted,
                    bytesFormat.format(totalSizeDeleted),
                    timeFormat.format(iterationDuration));
            logger.logVariable("iterationDuration", timeFormat.format(iterationDuration));
        }
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

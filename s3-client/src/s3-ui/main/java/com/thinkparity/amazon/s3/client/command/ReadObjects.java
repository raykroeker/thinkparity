/*
 * Created On:  20-Jun-07 9:52:39 AM
 */
package com.thinkparity.amazon.s3.client.command;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.client.S3Command;
import com.thinkparity.amazon.s3.client.S3CommandContext;
import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.bucket.BucketService;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.object.S3Object;
import com.thinkparity.amazon.s3.service.object.S3ObjectList;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadObjects implements S3Command {

    /** An amazon s3 authentication. */
    private S3Authentication authentication;

    /** An amazon s3 bucket web-service. */
    private BucketService bucketService;

    /** A <code>S3ClientConsole</code>. */
    private S3ClientConsole console;

    /** A log4j wrapper. */
    private Log4JWrapper logger;

    /**
     * Create ReadBuckets.
     *
     */
    public ReadObjects() {
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
     * @see com.thinkparity.amazon.s3.client.Command#invoke()
     *
     */
    public void invoke() {
        logger.logTraceId();
        final String name = console.readLine("Bucket name:");
        logger.logVariable("name", name);
        final S3Bucket bucket = new S3Bucket();
        bucket.setName(name);
        final S3ObjectList list = bucketService.readObjects(authentication, bucket);
        final List<S3Object> objects = list.getObjects();
        Collections.sort(objects, new Comparator<S3Object>() {
            public int compare(final S3Object o1, final S3Object o2) {
                return o1.getKey().getResource().compareTo(o2.getKey().getResource());
            }
        });
        // display object count
        String message = MessageFormat.format(
                "{0,choice,0#No amazon objects.|1#One amazon object.|1<{0} amazon objects.}",
                objects.size());
        logger.logInfo(message);
        console.println(message);
        // display objects
        S3Object object;
        final BytesFormat bytesFormat = new BytesFormat();
        for (int i = 0; i < objects.size(); i++) {
            object = objects.get(i);
            message = MessageFormat.format(
                    "{0,number,0000}. {1}, {2,date,yyyy-MM-dd HH:mm:ss.SSS Z}, {3}, {4}",
                    i + 1, object.getKey().getResource(), object.getLastModified(),
                    bytesFormat.format(object.getSize()), object.getStorageClass());
            console.println("\t" + message);
            logger.logInfo(message);
            // display object owner
            message = MessageFormat.format("Owner:  {0}, {1}",
                    object.getOwner().getDisplayName(),
                    object.getOwner().getId());
            logger.logInfo(message);
            console.println("\t\t" + message);
        }
        logger.logTraceId();
    }
}

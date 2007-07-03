/*
 * Created On:  20-Jun-07 9:52:39 AM
 */
package com.thinkparity.amazon.s3.client.command;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.client.S3Command;
import com.thinkparity.amazon.s3.client.S3CommandContext;
import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.bucket.BucketService;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.bucket.S3BucketList;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadBuckets implements S3Command {

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
    public ReadBuckets() {
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
        final S3BucketList list = bucketService.read(authentication);
        final List<S3Bucket> buckets = list.getBuckets();
        Collections.sort(buckets, new Comparator<S3Bucket>() {
            public int compare(final S3Bucket o1, final S3Bucket o2) {
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            }
        });
        // display owner
        String message = MessageFormat.format("Owner:  {0}, {1}",
                list.getOwner().getDisplayName(), list.getOwner().getId());
        logger.logInfo(message);
        console.println(message);
        // display bucket count
        message = MessageFormat.format(
                "{0,choice,0#No amazon buckets.|1#One amazon bucket.|1<{0} amazon buckets.}",
                buckets.size());
        logger.logInfo(message);
        console.println(message);
        // display buckets
        S3Bucket bucket;
        for (int i = 0; i < buckets.size(); i++) {
            bucket = buckets.get(i);
            message = MessageFormat.format(
                    "{0,number,000}. {1,date,yyyy-MM-dd HH:mm:ss.SSS Z}, {2}",
                    i, bucket.getCreationDate(), bucket.getName());
            console.println("\t" + message);
            logger.logInfo(message);
        }
        logger.logTraceId();
    }
}

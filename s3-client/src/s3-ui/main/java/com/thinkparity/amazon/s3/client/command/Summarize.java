/*
 * Created On:  20-Jun-07 9:52:39 AM
 */
package com.thinkparity.amazon.s3.client.command;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.client.S3Command;
import com.thinkparity.amazon.s3.client.S3CommandContext;
import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.bucket.BucketService;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.bucket.S3BucketList;
import com.thinkparity.amazon.s3.service.object.S3Object;
import com.thinkparity.amazon.s3.service.object.S3ObjectList;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Summarize implements S3Command {

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
    public Summarize() {
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
        if (console.confirm("Are you sure you want to summarize all s3 data?")) {
            final S3Data data = new S3Data();
            console.print("Downloading data");
            data.bucketList = bucketService.read(authentication);
            final List<S3Bucket> buckets = data.bucketList.getBuckets();
            S3ObjectList objectList;
            for (final S3Bucket bucket : buckets) {
                do {
                    console.print(".");
                    objectList = bucketService.readObjects(authentication, bucket);
                    data.addObjects(bucket, objectList.getObjects());
                } while (objectList.isTruncated());
                console.println("");
            }
            console.println("Download complete.");
            // display objects
            String message;
            int i_b;
            i_b = 0;
            for (final S3Bucket bucket : data.bucketList.getBuckets()) {
                i_b++;
                message = MessageFormat.format(
                    "{0,number,000}. {1,date,yyyy-MM-dd HH:mm:ss.SSS Z}, {2} {3,number,0000}",
                    i_b, bucket.getCreationDate(), bucket.getName(),
                    data.bucketObjectList.get(bucket).size());
                console.println(message);
                logger.logInfo(message);
                S3ClientConsoleUtil.print(console, data.bucketObjectList.get(bucket));
                S3ClientConsoleUtil.logInfo(logger, data.bucketObjectList.get(bucket));
            }
        }
        logger.logTraceId();
    }

    /** <b>Title:</b>S3 Data<br> */
    private class S3Data {

        /** The buckets. */
        private S3BucketList bucketList;

        /** The bucket objects. */
        private final Map<S3Bucket, List<S3Object>> bucketObjectList;

        /**
         * Create S3Data.
         *
         */
        private S3Data() {
            super();
            this.bucketObjectList = new HashMap<S3Bucket, List<S3Object>>();
        }

        /**
         * Add objects for the bucket.
         * 
         * @param bucket
         *            An <code>S3Bucket</code>.
         * @param objects
         *            A <code>List<S3Object></code>.
         */
        private void addObjects(final S3Bucket bucket, final List<S3Object> objects) {
            if (!bucketObjectList.containsKey(bucket)) {
                bucketObjectList.put(bucket, new ArrayList<S3Object>(objects.size()));
            }
            bucketObjectList.get(bucket).addAll(objects);
        }
    }
}

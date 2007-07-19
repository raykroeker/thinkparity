/*
 * Created On:  21-Jun-07 1:09:14 PM
 */
package com.thinkparity.amazon.s3.client.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.client.S3Command;
import com.thinkparity.amazon.s3.client.S3CommandContext;
import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.bucket.BucketService;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
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
public final class DeleteObjects implements S3Command {

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
    public DeleteObjects() {
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
        final List<S3Object> allObjects = new ArrayList<S3Object>();
        S3ObjectList objectList;
        do {
            objectList = bucketService.readObjects(authentication, bucket);
            allObjects.addAll(objectList.getObjects());
        } while (objectList.isTruncated());
        final String pattern = "yyyy-MM-dd HH:mm";
        String input = null;
        Date inputDate = null;
        int attempt = 0;
        while (null == inputDate && (attempt++) < 3) {
            try {
                input = console.readLine("Last modified on ({0}):", pattern);
                inputDate = new SimpleDateFormat(pattern).parse(input);
            } catch (final ParseException px) {
                console.println("Invalid date {0}.", input, pattern);
            }
        }
        if (null == inputDate) {
            console.println("Could not parse date.");
            return;
        }
        final Date lastModifiedDate = inputDate;
        console.println("{0,date,yyyy-MM-dd HH:mm:ss.SSS Z}", lastModifiedDate);

        final List<S3Object> filteredObjects = new ArrayList<S3Object>(allObjects.size());
        filteredObjects.addAll(allObjects);
        Collections.sort(filteredObjects, new Comparator<S3Object>() {
            public int compare(final S3Object o1, final S3Object o2) {
                return o1.getLastModified().compareTo(o2.getLastModified());
            }
        });
        // filter out objects where the last modified date is after the input date
        FilterManager.filter(filteredObjects, new Filter<S3Object>() {
            public Boolean doFilter(final S3Object o) {
                return o.getLastModified().after(lastModifiedDate);
            }
        });

        S3ClientConsoleUtil.print(console, filteredObjects);
        if (console.confirm("Delete all objects?")) {
            for (final S3Object filteredObject : filteredObjects) {
                console.print("Deleting ");
                S3ClientConsoleUtil.print(console, filteredObject);
                delete(bucket, filteredObject.getKey());
            }
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

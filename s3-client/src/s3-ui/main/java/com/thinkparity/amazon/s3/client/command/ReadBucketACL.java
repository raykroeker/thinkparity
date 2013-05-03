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
import com.thinkparity.amazon.s3.service.S3Grant;
import com.thinkparity.amazon.s3.service.S3GrantList;
import com.thinkparity.amazon.s3.service.S3UserGrantee;
import com.thinkparity.amazon.s3.service.bucket.BucketService;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadBucketACL implements S3Command {

    /** An amazon s3 authentication. */
    private S3Authentication authentication;

    /** An amazon s3 bucket web-service. */
    private BucketService bucketService;

    /** A <code>S3ClientConsole</code>. */
    private S3ClientConsole console;

    /** A log4j wrapper. */
    private Log4JWrapper logger;

    /**
     * Create ReadBucketACL.
     *
     */
    public ReadBucketACL() {
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
        final S3GrantList list = bucketService.readACL(authentication, bucket);
        final List<S3Grant> grants = list.getGrants();
        Collections.sort(grants, new Comparator<S3Grant>() {
            public int compare(final S3Grant o1, final S3Grant o2) {
                return o1.getPermission().compareTo(o2.getPermission());
            }
        });
        // display owner
        String message = MessageFormat.format("Owner:  {0}, {1}",
                list.getOwner().getDisplayName(), list.getOwner().getId());
        logger.logInfo(message);
        console.println(message);
        // display grant count
        message = MessageFormat.format(
                "{0,choice,0#No amazon grants.|1#One amazon grant.|1<{0} amazon grants.}",
                grants.size());
        logger.logInfo(message);
        console.println(message);
        // display grants
        S3Grant grant;
        for (int i = 0; i < grants.size(); i++) {
            grant = grants.get(i);
            message = MessageFormat.format(
                    "{0,number,000}. {1}, {2}",
                    i, grant.getPermission(),
                    ((S3UserGrantee) grant.getGrantee()).getDisplayName());
            console.println("\t" + message);
            logger.logInfo(message);
        }
        logger.logTraceId();
    }
}

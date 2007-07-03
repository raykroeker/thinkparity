/*
 * Created On:  20-Jun-07 9:52:39 AM
 */
package com.thinkparity.amazon.s3.client.command;

import java.io.File;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.text.MessageFormat;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.nio.ChannelUtil;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.client.S3Command;
import com.thinkparity.amazon.s3.client.S3CommandContext;
import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.object.ObjectService;
import com.thinkparity.amazon.s3.service.object.S3Key;
import com.thinkparity.amazon.s3.service.object.S3WritableObjectContent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadObjectContent implements S3Command {

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
    public ReadObjectContent() {
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
        final String objectLocation = console.readLine("Object location:");
        logger.logVariable("objectLocation", objectLocation);
        final File file = new File(objectLocation);
        if (file.exists()) {
            if (console.confirm("Object location ''{0}'' exists.  Delete?", file.getAbsolutePath())) {
                if (!file.delete()) {
                    throw new RuntimeException("Could not delete object location " + file.getAbsolutePath() + ".");
                }
            }
        }
        final File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            if (console.confirm("Object location parent ''{0}'' does not exist.  Create?", parentFile.getAbsolutePath())) {
                if (!parentFile.mkdirs()) {
                    throw new RuntimeException("Could not create object location parent " + parentFile.getAbsolutePath() + ".");
                }
            }
        }

        final S3Key key = new S3Key();
        key.setResource(objectKey);
        final S3WritableObjectContent content = new S3WritableObjectContent() {
            public WritableByteChannel openWriteChannel() throws IOException {
                return ChannelUtil.openWriteChannel(file);
            }
        };
        final long begin = System.currentTimeMillis();
        objectService.readContent(authentication, bucket, key, content);
        final long end = System.currentTimeMillis();
        // display object
        final BytesFormat bytesFormat = new BytesFormat();
        String message = MessageFormat.format("Content size {0} downloaded in {1}ms.",
                bytesFormat.format(file.length()),
                end - begin);
        console.println(message);
        logger.logInfo(message);
        logger.logTraceId();
    }
}

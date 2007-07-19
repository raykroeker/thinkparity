/*
 * Created On:  19-Jul-07 1:57:40 PM
 */
package com.thinkparity.amazon.s3.client.command;

import java.text.MessageFormat;
import java.util.List;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.amazon.s3.client.S3ClientConsole;
import com.thinkparity.amazon.s3.service.object.S3Object;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class S3ClientConsoleUtil {

    private static final BytesFormat BYTES_FORMAT;

    static {
        BYTES_FORMAT = new BytesFormat();
    }

    static void logInfo(final Log4JWrapper logger, final List<S3Object> objects) {
        int i = 0;
        String message;
        for (final S3Object object : objects) {
            i++;
            message = MessageFormat.format(
                    "{0,number,0000}. {1}, {2,date,yyyy-MM-dd HH:mm:ss.SSS Z}, {3}, {4}",
                    i, object.getKey().getResource(), object.getLastModified(),
                    BYTES_FORMAT.format(object.getSize()), object.getStorageClass());
            logger.logInfo(message);
            // display object owner
            message = MessageFormat.format("Owner:  {0}, {1}",
                    object.getOwner().getDisplayName(),
                    object.getOwner().getId());
            logger.logInfo(message);
        }
    }

    static void print(final S3ClientConsole console,
            final List<S3Object> objects) {
        int i = 0;
        String message;
        for (final S3Object object : objects) {
            i++;
            message = MessageFormat.format(
                    "{0,number,0000}. {1}, {2,date,yyyy-MM-dd HH:mm:ss.SSS Z}, {3}, {4}",
                    i, object.getKey().getResource(), object.getLastModified(),
                    BYTES_FORMAT.format(object.getSize()), object.getStorageClass());
            console.println("\t" + message);
            // display object owner
            message = MessageFormat.format("Owner:  {0}, {1}",
                    object.getOwner().getDisplayName(),
                    object.getOwner().getId());
            console.println("\t\t" + message);
        }
    }

    static void print(final S3ClientConsole console, final S3Object object) {
        final String message = MessageFormat.format(
                "{0}, {1,date,yyyy-MM-dd HH:mm:ss.SSS Z}, {2}, {3}",
                object.getKey().getResource(), object.getLastModified(),
                BYTES_FORMAT.format(object.getSize()), object.getETag());
        console.println(message);
    }

    /**
     * Create ConsoleUtil.
     *
     */
    private S3ClientConsoleUtil() {
        super();
    }
}

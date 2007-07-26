/*
 * Created On:  25-Jul-07 11:39:21 AM
 */
package com.thinkparity.codebase.model.stream.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class UploadFile {

    /** A bytes format. */
    private static final BytesFormat BYTES_FORMAT;

    private static final StreamMonitor DEFAULT_MONITOR;

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    static {
        BYTES_FORMAT = new BytesFormat();
        DEFAULT_MONITOR = new StreamMonitor () {
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#chunkReceived(int)
             *
             */
            public void chunkReceived(final int chunkSize) {
                // not possbile in upload
                Assert.assertUnreachable("");
            }
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#chunkSent(int)
             *
             */
            public void chunkSent(final int chunkSize) {
                LOGGER.logDebug("Uploaded {0}.", BYTES_FORMAT.format(
                        new Long(chunkSize)));
            }
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#getName()
             *
             */
            public String getName() {
                return "UploadFile#defaultMonitor";
            }
        };
        LOGGER = new Log4JWrapper(UploadFile.class);
    }

    /** The stream monitor. */
    private final StreamMonitor monitor;

    /** The interpreted number of retry attempts. */
    private final int retryAttempts;

    /** The stream session. */
    private final StreamSession session;

    /** The source file. */
    private File source;

    /**
     * Create UploadFile.
     * 
     * @param monitor
     *            A <code>StreamMonitor</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    public UploadFile(final StreamMonitor monitor, final StreamSession session) {
        super();
        this.monitor = monitor;
        this.session = session;
        if (null == session.getRetryAttempts() || 1 > session.getRetryAttempts()) {
            this.retryAttempts = 1;
        } else {
            this.retryAttempts = session.getRetryAttempts();
        }
    }

    /**
     * Create UploadFile.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    public UploadFile(final StreamSession session) {
        this(DEFAULT_MONITOR, session);
    }

    /**
     * Upload the stream represented by the source to the session.
     * 
     * @param source
     *            A <code>File</code> to upload. The source must not be null;
     *            the file must exist and must be a file.
     */
    public void upload(final File source) throws IOException {
        this.source = source;
        for (int i = 0; i < retryAttempts; i++) {
            ensureUpload();
            try {
                LOGGER.logInfo("Upload attempt {0}/{1}.", i, retryAttempts);
                attemptUpload();
                break;
            } catch (final StreamException sx) {
                if (sx.isRecoverable()) {
                    LOGGER.logWarning("Could not upload target.");
                } else {
                    throw sx;
                }
            }
        }
    }
        
    /**
     * Attempt an upload of the file. Create a new stream writer using the
     * session; and upload the file.
     * 
     * @throws IOException
     */
    private void attemptUpload() throws IOException {
        final StreamWriter writer = new StreamWriter(monitor, session);
        final InputStream input = new FileInputStream(source);
        try {
            writer.write(input, Long.valueOf(source.length()));
        } finally {
            input.close();
        }
    }

    /**
     * Ensure an upload is possible by checking the source file for validity as
     * well as the session.
     * 
     */
    private void ensureUpload() {
        final String error;
        if (null == source) {
            error = "Source must not be null.";
        } else if (!source.exists()) {
            error = "Source {0} must exist.";
        } else if (!source.isFile()) {
            error = "Source {0} must be a file.";
        } else if (session == null) {
            error = "Stream session must exist.";
        } else {
            error = null;
        }
        if (null != error) {
            throw new IllegalArgumentException(MessageFormat.format(error,
                    source, session));
        }
    }
}

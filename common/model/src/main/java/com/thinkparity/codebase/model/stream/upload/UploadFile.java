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
import com.thinkparity.codebase.model.stream.StreamRetryHandler;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;

import com.thinkparity.network.NetworkException;

/**
 * <b>Title:</b>thinkParity Common Model Stream Upload File<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UploadFile {

    /** A bytes format. */
    private static final BytesFormat BYTES_FORMAT;

    /** A default stream monitor. */
    private static final StreamMonitor DEFAULT_MONITOR;

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    /** The duration to wait between retry attempts. */
    private static final long RETRY_PERIOD;

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
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#reset()
             *
             */
            @Override
            public void reset() {
                LOGGER.logDebug("Upload reset.");
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
        RETRY_PERIOD = 1 * 1000;
    }

    /** The retry count; */
    private int invocation;

    /** The stream monitor. */
    private final StreamMonitor monitor;

    /** The network error retry handler. */
    private final StreamRetryHandler networkRetryHandler;

    /** The stream session. */
    private final StreamSession session;

    /** The source file. */
    private File source;

    /** The stream protocol error retry handler. */
    private final StreamRetryHandler streamRetryHandler;

    /** The stream writer. */
    private StreamWriter streamWriter;

    /**
     * Create UploadFile.
     * 
     * @param monitor
     *            A <code>StreamMonitor</code>.
     * @param retryHandler
     *            A <code>StreamRetryHandler</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    public UploadFile(final StreamMonitor monitor,
            final StreamRetryHandler retryHandler, final StreamSession session) {
        super();
        this.monitor = monitor;
        this.networkRetryHandler = retryHandler;
        this.session = session;

        this.streamRetryHandler = new StreamRetryHandler() {

            /** The current number of retry attempts. */
            private int retryAttempt = 0;

            /** The number of retry attempts. */
            private int retryAttempts = UploadFile.this.session.getRetryAttempts();

            /**
             * @see com.thinkparity.codebase.model.stream.StreamRetryHandler#retry()
             *
             */
            @Override
            public Boolean retry() {
                if (retryAttempt++ < retryAttempts) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }

            /**
             * @see com.thinkparity.codebase.model.stream.StreamRetryHandler#period()
             *
             */
            @Override
            public Long waitPeriod() {
                return RETRY_PERIOD;
            }
        };
    }

    /**
     * Create UploadFile.
     * 
     * @param retryHandler
     *            A <code>StreamRetryHandler</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    public UploadFile(final StreamRetryHandler retryHandler,
            final StreamSession session) {
        this(DEFAULT_MONITOR, retryHandler, session);
    }

    /**
     * Abort the file upload.
     * 
     */
    public void abort() {
        if (null == streamWriter) {
            LOGGER.logWarning("Cannot abort.  Writer no longer exists.");
        } else {
            streamWriter.abort();
            streamWriter = null;
        }
    }

    /**
     * Upload the stream represented by the source to the session.
     * 
     * @param source
     *            A <code>File</code> to upload. The source must not be null;
     *            the file must exist and must be a file.
     * @throws NetworkException
     *             if a network write error occurs
     * @throws IOException
     *             if a file read error occurs
     * @throws StreamException
     *             if a stream protocol error occurs
     */
    public void upload(final File source) throws NetworkException, IOException,
            StreamException {
        this.source = source;
        invocation = 0;
        while (true) {
            LOGGER.logInfo("Upload file attempt {0}.", ++invocation);
            ensureUpload();
            try {
                attemptUpload();
                break;
            } catch (final StreamException sx) {
                LOGGER.logWarning("Could not upload target.  {0}", sx.getMessage());
                if (retry(sx)) {
                    resetProgress();
                    waitBeforeRetry(sx);
                } else {
                    throw sx;
                }
            } catch (final NetworkException nx) {
                LOGGER.logWarning("Could not upload target.  {0}", nx.getMessage());
                if (retry(nx)) {
                    waitBeforeRetry(nx);
                } else {
                    throw nx;
                }
            }
        }
    }

    /**
     * Attempt an upload of the file. Create a new stream writer using the
     * session; and upload the file.
     * 
     * @throws NetworkException
     *             if a network write error occurs
     * @throws IOException
     *             if a file read error occurs
     * @throws StreamException
     *             if a stream protocol error occurs
     */
    private void attemptUpload() throws NetworkException, IOException,
            StreamException {
        streamWriter = new StreamWriter(monitor, session);
        final InputStream input = new FileInputStream(source);
        try {
            streamWriter.write(input, Long.valueOf(source.length()));
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

    /**
     * Determine whether or not to retry an upload file for a network error.
     * 
     * @param nx
     *            A <code>NetworkException</code>.
     * @return True if a retry should be attempted.
     */
    private boolean retry(final NetworkException nx) {
        return networkRetryHandler.retry().booleanValue();
    }

    /**
     * Determine whether or not to retry an upload file for a stream protocol
     * error.
     * 
     * @param sx
     *            A <code>StreamException</code>.
     * @return True if a retry should be attempted.
     */
    private boolean retry(final StreamException sx) {
        if (sx.isRecoverable()) {
            return streamRetryHandler.retry().booleanValue();
        } else {
            return false;
        }
    }

    /**
     * Wait a pre-determined duration before attempting a retry after a network
     * error.
     * 
     * @param nx
     *            A <code>NetworkException</code>.
     */
    private void waitBeforeRetry(final NetworkException nx) {
        try {
            Thread.sleep(networkRetryHandler.waitPeriod());
        } catch (final InterruptedException ix) {
            LOGGER.logWarning("Upload file retry interruped.  {0}",
                    ix.getMessage());
        }
    }

    /**
     * Reset the stream monitor.
     * 
     */
    private void resetProgress() {
        monitor.reset();
    }

    /**
     * Wait a pre-determined duration before attempting a retry after a stream
     * error.
     * 
     * @param sx
     *            A <code>StreamException</code>.
     */
    private void waitBeforeRetry(final StreamException sx) {
        try {
            Thread.sleep(streamRetryHandler.waitPeriod());
        } catch (final InterruptedException ix) {
            LOGGER.logWarning("Upload file retry interruped.  {0}",
                    ix.getMessage());
        }
    }
}

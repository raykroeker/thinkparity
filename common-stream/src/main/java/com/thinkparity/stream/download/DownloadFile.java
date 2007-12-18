/*
 * Created On:  21-Jul-07 12:52:10 PM
 */
package com.thinkparity.stream.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.delegate.CancelException;
import com.thinkparity.codebase.delegate.Cancelable;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamSession;


import com.thinkparity.network.NetworkException;

import com.thinkparity.stream.StreamException;
import com.thinkparity.stream.StreamMonitor;
import com.thinkparity.stream.StreamReader;
import com.thinkparity.stream.StreamRetryHandler;

/**
 * <b>Title:</b>thinkParity Model Codebase Stream Download File<br>
 * <b>Description:</b>A stream download utility that will download a stream to
 * a local file.  The utility enables retry attempts based upon the session's
 * retry attempt setting as well as the<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DownloadFile implements Cancelable {

    /** A bytes format. */
    private static final BytesFormat BYTES_FORMAT;

    /** A default stream monitor. */
    private static final StreamMonitor DEFAULT_MONITOR;

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    /** The duration to wait between retry attempts. */
    private static final Long RETRY_PERIOD;

    static {
        BYTES_FORMAT = new BytesFormat();
        DEFAULT_MONITOR = new StreamMonitor () {
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#chunkReceived(int)
             *
             */
            public void chunkReceived(final int chunkSize) {
                LOGGER.logDebug("Downloaded {0}.", BYTES_FORMAT.format(
                        new Long(chunkSize)));
            }
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#chunkSent(int)
             *
             */
            public void chunkSent(final int chunkSize) {
                // not possbile in download
                Assert.assertUnreachable("");
            }
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#getName()
             *
             */
            public String getName() {
                return "DownloadFile#defaultMonitor";
            }
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#reset()
             *
             */
            @Override
            public void reset() {
                LOGGER.logDebug("Download reset.");
            }
        };
        LOGGER = new Log4JWrapper(DownloadFile.class);
        RETRY_PERIOD = Long.valueOf(1L * 1000L);
    }

    /** The invocation count. */
    private int invocation;

    /** The stream monitor. */
    private final StreamMonitor monitor;

    /** A network error retry handler. */
    private final StreamRetryHandler networkRetryHandler;

    /** The stream session. */
    private final StreamSession session;

    /** A delegate stream reader. */
    private StreamReader streamReader;

    /** A stream protocol retry handler. */
    private final StreamRetryHandler streamRetryHandler;

    /** The target file. */
    private File target;

    /** A target output stream. */
    private OutputStream targetOutputStream;

    /**
     * Create DownloadFile.
     * 
     * @param monitor
     *            A <code>StreamMonitor</code>.
     * @param retryHandler
     *            A <code>StreamRetryHandler</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    public DownloadFile(final StreamMonitor monitor,
            final StreamRetryHandler retryHandler, final StreamSession session) {
        super();
        this.monitor = monitor;
        this.networkRetryHandler = retryHandler;
        this.session = session;

        this.streamRetryHandler = new StreamRetryHandler() {

            /** The current number of retry attempts. */
            private int retryAttempt = 0;

            /** The number of retry attempts. */
            private int retryAttempts = DownloadFile.this.session.getRetryAttempts();

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
     * Create DownloadFile.
     * 
     * @param retryHandler
     *            A <code>StreamRetryHandler</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    public DownloadFile(final StreamRetryHandler retryHandler,
            final StreamSession session) {
        this(DEFAULT_MONITOR, retryHandler, session);
    }

    /**
     * Cancel the download. Call the stream reader's cancel; and ensure cleanup
     * of the target stream.
     * 
     * @see com.thinkparity.codebase.delegate.Cancelable#cancel()
     */
    public void cancel() throws CancelException {
        if (null != streamReader) {
            streamReader.cancel();
        }
        if (null != targetOutputStream) {
            /* this can happen if cancel is called after creating the stream
             * and before the try/finally is "entered" */
            try {
                targetOutputStream.close();
            } catch (final IOException iox) {
                throw new CancelException(iox);
            } finally {
                targetOutputStream = null;
            }
        }
    }

    /**
     * Download the stream represented by the session to the target file.
     * 
     * @param target
     *            A <code>File</code> to download to. The target must not be
     *            null; the file must exist and must be a file.
     * @throws NetworkException
     *             if a network read error occurs
     * @throws IOException
     *             if a target write error occurrs
     * @throws StreamException
     *             if a stream protocol error occurs
     */
    public void download(final File target) throws NetworkException,
            IOException, StreamException {
        try {
            this.target = target;
            invocation = 0;
            while (true) {
                LOGGER.logInfo("Download file attempt {0}.", ++invocation);
                ensureDownload();
                try {
                    attemptDownload();
                    break;
                } catch (final StreamException sx) {
                    LOGGER.logWarning("Could not download target.  {0}", sx.getMessage());
                    Assert.assertTrue(target.delete(),
                            "Could not delete download target {0}.", target);
                    Assert.assertTrue(target.createNewFile(),
                            "Could not delete download target {0}.", target);
                    if (retry(sx)) {
                        resetProgress();
                        waitBeforeRetry(sx);
                    } else {
                        throw sx;
                    }
                } catch (final NetworkException nx) {
                    LOGGER.logWarning("Could not download target.");
                    Assert.assertTrue(target.delete(),
                            "Could not delete download target {0}.", target);
                    Assert.assertTrue(target.createNewFile(),
                            "Could not delete download target {0}.", target);
                    if (retry(nx)) {
                        resetProgress();
                        waitBeforeRetry(nx);
                    } else {
                        throw nx;
                    }
                }
            }
        } finally {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Attempt a download of the version. Create a new stream reader using the
     * session; and download the version to the temp file, then return the temp
     * file.
     * 
     * @throws NetworkException
     *             if a network read error occurs
     * @throws IOException
     *             if a target write error occurs
     * @throws StreamException
     *             if a stream protocol error occurs
     */
    private void attemptDownload() throws NetworkException, IOException,
            StreamException {
        streamReader = new StreamReader(monitor, session);
        try {
            targetOutputStream = new FileOutputStream(target);
            try {
                streamReader.read(targetOutputStream);
            } finally {
                try {
                    targetOutputStream.close();
                } finally {
                    targetOutputStream = null;
                }
            }
        } finally {
            streamReader = null;
        }
    }

    /**
     * Ensure a download is possible by checking the target file for validity as
     * well as the session.
     * 
     */
    private void ensureDownload() {
        final String error;
        if (null == target) {
            error = "Target must not be null.";
        } else if (!target.exists()) {
            error = "Target {0} must exist.";
        } else if (!target.isFile()) {
            error = "Target {0} must be a file.";
        } else if (session == null) {
            error = "Stream session must exist.";
        } else {
            error = null;
        }
        if (null != error) {
            throw new IllegalArgumentException(MessageFormat.format(error,
                    target, session));
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
     * Determine whether or not to retry a download.
     * 
     * @return True if a retry should be attempted.
     */
    private boolean retry(final NetworkException nx) {
        return networkRetryHandler.retry().booleanValue();
    }

    /**
     * Determine whether or not to retry a download after a stream protocol
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

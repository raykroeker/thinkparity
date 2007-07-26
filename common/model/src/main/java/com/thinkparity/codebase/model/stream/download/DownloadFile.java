/*
 * Created On:  21-Jul-07 12:52:10 PM
 */
package com.thinkparity.codebase.model.stream.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamReader;
import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * <b>Title:</b>thinkParity Model Codebase Stream Download File<br>
 * <b>Description:</b>A stream download utility that will download a stream to
 * a local file.  The utility enables retry attempts based upon the session's
 * retry attempt setting as well as the<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DownloadFile {

    /** A bytes format. */
    private static final BytesFormat BYTES_FORMAT;

    /** A default stream monitor. */
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
        };
        LOGGER = new Log4JWrapper(DownloadFile.class);
    }

    /** The stream monitor. */
    private final StreamMonitor monitor;

    /** The interpreted number of retry attempts. */
    private final int retryAttempts;

    /** The stream session. */
    private final StreamSession session;

    /** The target file. */
    private File target;

    /**
     * Create DownloadFile.
     * 
     * @param monitor
     *            A <code>StreamMonitor</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    public DownloadFile(final StreamMonitor monitor, final StreamSession session) {
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
     * Create DownloadFile.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    public DownloadFile(final StreamSession session) {
        this(DEFAULT_MONITOR, session);
    }

    /**
     * Download the stream represented by the session to the target file.
     * 
     * @param target
     *            A <code>File</code> to download to. The target must not be
     *            null; the file must exist and must be a file.
     */
    public void download(final File target) throws IOException {
        this.target = target;
        for (int i = 0; i < retryAttempts; i++) {
            ensureDownload();
            try {
                LOGGER.logInfo("Download attempt {0}/{1}.", i, retryAttempts);
                attemptDownload();
                break;
            } catch (final StreamException sx) {
                if (sx.isRecoverable()) {
                    LOGGER.logWarning("Could not download target.");
                    Assert.assertTrue(target.delete(),
                            "Could not delete download target {0}.", target);
                    Assert.assertTrue(target.createNewFile(),
                            "Could not delete download target {0}.", target);
                } else {
                    throw sx;
                }
            }
        }
    }
        
    /**
     * Attempt a download of the version. Create a new stream reader using the
     * session; and download the version to the temp file, then return the temp
     * file.
     * 
     * @throws IOException
     */
    private void attemptDownload() throws IOException {
        final StreamReader reader = new StreamReader(monitor, session);
        final OutputStream output = new FileOutputStream(target);
        try {
            reader.read(output);
        } finally {
            output.close();
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
}

/*
 * Created On:  21-Jul-07 2:14:47 PM
 */
package com.thinkparity.ophelia.model.document.delegate;

import java.io.IOException;
import java.io.InputStream;

import com.thinkparity.codebase.io.StreamOpener;

import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;

import com.thinkparity.ophelia.model.document.DocumentDelegate;

/**
 * <b>Title:</b>thinkParity Ophelia Model Document Upload Stream Delegate<br>
 * <b>Description:</b>An implementation of the upload stream api<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class UploadStream extends DocumentDelegate {

    /** A boolean indicating whether or not the upload is complete. */
    private boolean complete;

    /** The stream monitor. */
    private StreamMonitor monitor;

    /** The number of times to retry the upload. */
    private int retryAttempts;

    /** The document version. */
    private DocumentVersion version;

    /**
     * Create UploadStream.
     *
     */
    public UploadStream() {
        super();
    }

    /**
     * Set the stream monitor.
     * 
     * @param monitor
     *            A <code>StreamMonitor</code>.
     */
    public void setMonitor(final StreamMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Set the version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     */
    public void setVersion(final DocumentVersion version) {
        this.version = version;
    }

    /**
     * Upload the stream.
     *
     */
    public void uploadStream() {
        final StreamSession session = newUpstreamSession();
        final StreamWriter writer = newStreamWriter(session);
        complete = false;
        if (null == session.getRetryAttempts() || 1 > session.getRetryAttempts()) {
            retryAttempts = 1;
        } else {
            retryAttempts = session.getRetryAttempts();
        }
        for (int i = 0; i < retryAttempts; i++) {
            if (complete) {
                break;
            } else {
                logger.logInfo("Upload attempt {0}/{1}.", i, retryAttempts);
                openVersion(newOpener(writer));
            }
        }
    }

    /**
     * Create a new stream opener that will upload the version's content.
     * 
     * @param writer
     *            A <code>StreamWriter</code>.
     * @return A <code>StreamOpener</code>.
     */
    private StreamOpener newOpener(final StreamWriter writer) {
        return new StreamOpener() {
            public void open(final InputStream stream) throws IOException {
                try {
                    writer.write(stream, version.getSize());
                    setComplete();
                } catch (final StreamException sx) {
                    if (sx.isRecoverable()) {
                        logger.logWarning("Could not upload stream for {0}.",
                                version.getArtifactName());
                        
                    } else {
                        throw sx;
                    }
                }
            }
        };
    }

    /**
     * Create a new stream writer.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @return A <code>StreamWriter</code>.
     */
    private StreamWriter newStreamWriter(final StreamSession session) {
        return new StreamWriter(monitor, session);
    }

    /**
     * Create a new stream session for the version.
     * 
     * @return A <code>StreamSession</code>.
     */
    private StreamSession newUpstreamSession() {
        return getStreamModel().newUpstreamSession(version);
    }

    /**
     * Open a document version.
     * 
     * @param opener
     *            A <code>StreamOpener</code>.
     */
    private void openVersion(final StreamOpener opener) {
        openVersion(version, opener);
    }

    /**
     * Set the upload completion.
     *
     */
    private void setComplete() {
        complete = true;
    }
}

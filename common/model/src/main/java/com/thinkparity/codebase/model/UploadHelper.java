/*
 * Created On:  22-Feb-07 11:30:43 PM
 */
package com.thinkparity.codebase.model;

import java.io.IOException;
import java.io.InputStream;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;

/**
 * <b>Title:</b>thinkParity CommonModel Upload Helper<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class UploadHelper {

    private static final int MAX_ATTEMPT_OPEN;

    static {
    	MAX_ATTEMPT_OPEN = 7;
    }

    /** An apache <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** A thinkParity <code>AbstractModelImpl</code>. */
    private final AbstractModelImpl model;

    /**
     * Create UploadHelper.
     * 
     * @param model
     *            A thinkParity <code>AbstractModelImpl</code>.
     * @param logger
     *            An apache <code>Log4JWrapper</code>.
     */
    UploadHelper(final AbstractModelImpl model, final Log4JWrapper logger) {
        super();
        this.model = model;
        this.logger = logger;
    }

    /**
     * Upload a stream to the stream server using an existing session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param iStream
     *            A <code>Iterable</code> series of <code>InputStream</code>.
     * @throws IOException
     */
    private final void upload(final UploadMonitor uploadMonitor,
            final StreamMonitor streamMonitor, final String streamId,
            final StreamSession session, final InputStream stream,
            final Long streamSize, final Long streamOffset) throws IOException {
        stream.reset();
        long skipped = stream.skip(streamOffset);
        while (skipped < streamOffset && 0 < skipped) {
            skipped += stream.skip(streamOffset.longValue() - skipped);
        }
        final Long actualStreamOffset;
        if (skipped == streamOffset.longValue()) {
            logger.logInfo("Resuming upload for {0} at {1}.",
                    streamId, streamOffset);
            actualStreamOffset = streamOffset;
        } else {
            logger.logWarning("Could not resume upload for {0} at {1}.  Starting over.",
                    streamId, streamOffset);
            actualStreamOffset = 0L;
        }
        final StreamWriter writer = new StreamWriter(streamMonitor, session);
        /* attempt to open a stream writer - if the underlying network topology
         * is bad, many attempts can be made */
        int attempt = 1;
        while (attempt < MAX_ATTEMPT_OPEN && !writer.isOpen().booleanValue()) {
	        try {
	        	attempt++;
		        writer.open();
	        } catch (final StreamException sx) {
	        	try {
	        		writer.close();
	        	} catch (final IOException iox) {}
	        	logger.logWarning("{0} of {1} Could not open stream writer.{2}  {3}",
						attempt - 1, MAX_ATTEMPT_OPEN, Separator.SystemNewLine,
						sx.getMessage());
            } catch (final IOException iox) {
	        	try {
	        		writer.close();
	        	} catch (final IOException iox2) {}
                logger.logWarning("{0} of {1} Could not open stream writer.{2}  {3}",
					attempt - 1, MAX_ATTEMPT_OPEN, Separator.SystemNewLine,
					iox.getMessage());
            }
        }
        try {
            writer.write(streamId, stream, streamSize, actualStreamOffset);
        } finally {
            writer.close();
        }
    }

    /**
     * Upload a stream to the stream server. This invocation will take care of
     * common network errors.
     * 
     * @param uploadMonitor
     *            An <code>UploadMoniotor</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @param session
     *            A <code>StreamSession</code>.
     * @param stream
     *            An <code>InputStream</code>.
     * @param streamSize
     *            The stream size <code>Long</code>.
     * @throws IOException
     */
    final void upload(final UploadMonitor uploadMonitor,
            final String streamId, final StreamSession session,
            final InputStream stream, final Long streamSize) throws IOException {
        final StreamMonitor streamMonitor = new StreamMonitor() {
            long recoverChunkOffset = 0;
            long totalChunks = 0;
            public void chunkReceived(final int chunkSize) {}
            public void chunkSent(final int chunkSize) {
                totalChunks += chunkSize;
                uploadMonitor.chunkUploaded(chunkSize);
            }
            public void headerReceived(final String header) {
                logger.logApiId();
                logger.logInfo("header:{0}", header);
            }
            public void headerSent(final String header) {
                logger.logApiId();
                logger.logInfo("header:{0}", header);
            }
            public void streamError(final StreamException error) {
                logger.logWarning(error, "stream-error");
                if (error.isRecoverable()) {
                    if (recoverChunkOffset <= totalChunks) {
                        // attempt to resume the upload
                        recoverChunkOffset = totalChunks;
                        try {
                            upload(uploadMonitor, streamId, session, stream,
									streamSize);
                        } catch (final IOException iox) {
                            throw model.panic(iox);
                        }
                    } else {
                        throw error;
                    }
                } else {
                    throw error;
                }
            }
        };
        stream.mark(stream.available());
        upload(uploadMonitor, streamMonitor, streamId, session, stream,
                streamSize, 0L);
    }
}

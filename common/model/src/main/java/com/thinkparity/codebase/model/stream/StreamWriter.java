/*
 * Created On: Oct 22, 2006 3:46:10 PM
 */
package com.thinkparity.codebase.model.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.thinkparity.codebase.model.util.http.HttpUtils;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import com.thinkparity.network.NetworkException;

/**
 * <b>Title:</b>thinkParity CommonModel Stream Writer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamWriter implements RequestEntity {

    /** A stream monitor. */
    private final StreamMonitor monitor;

    /** The http put method. */
    private PutMethod putMethod;

    /** A stream session. */
    private final StreamSession session;

    /** A stream. */
    private InputStream stream;

    /** A stream size. */
    private long streamSize;

    /** A set of stream utilities. */
    private StreamUtils utils;

    /**
     * Create StreamWriter.
     * 
     * @param session
     *            A stream <code>Session</code>.
     */
    public StreamWriter(final StreamMonitor monitor, final StreamSession session) {
        super();
        this.monitor = monitor;
        this.session = session;
        this.utils = new StreamUtils();
    }

    /**
     * Abort the stream write.
     * 
     */
    public void abort() {
        if (null == putMethod) {
            return;
        } else {
            putMethod.abort();
            putMethod = null;
        }
    }

    /**
     * @see org.apache.commons.httpclient.methods.RequestEntity#getContentLength()
     *
     */
    public long getContentLength() {
        return streamSize;
    }

    /**
     * @see org.apache.commons.httpclient.methods.RequestEntity#getContentType()
     *
     */
    public String getContentType() {
        return HttpUtils.ContentTypeNames.BINARY_OCTET_STREAM;
    }

    /**
     * @see org.apache.commons.httpclient.methods.RequestEntity#isRepeatable()
     *
     */
    public boolean isRepeatable() {
        return false;   // cannot re-open the input stream from here
    }

    /**
     * Write a stream.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @param streamSize
     *            The stream size <code>Long</code>.
     * @throws NetworkException
     *             if a network write error occurs
     * @throws IOException
     *             if a source read error occurs
     * @throws StreamException
     *             if a stream protocol error occurs
     */
    public void write(final InputStream stream, final Long streamSize)
            throws NetworkException, IOException, StreamException {
        this.stream = stream;
        this.streamSize = streamSize;
        executePut();
    }

    /**
     * @see org.apache.commons.httpclient.methods.RequestEntity#writeRequest(java.io.OutputStream)
     *
     */
    public void writeRequest(final OutputStream out) throws IOException {
        int len = 0;
        final byte[] b = new byte[session.getBufferSize()];
        while ((len = stream.read(b)) > 0) {
            try {
                out.write(b, 0, len);
                out.flush();
            } catch (final IOException iox) {
                throw new IOException(new NetworkException(iox));
            }
            fireChunkSent(len);
        }
    }

    /**
     * Execute a http put; writing the stream.
     * 
     * @throws NetworkException
     *             if the network write operation fails
     * @throws IOException
     *             if the source read fails
     * @throws StreamException
     *             if a stream protocol error occurs
     */
    private void executePut() throws NetworkException, IOException,
            StreamException {
        StreamClientMetrics.begin(session);
        try {
            putMethod = new PutMethod(session.getURI());
            try {
                utils.setHeaders(putMethod, session.getHeaders());
                putMethod.setRequestEntity(this);
                switch (utils.execute(putMethod)) {
                case 200:
                    break;
                case 500:
                    utils.writeError(putMethod);
                    throw new StreamException(Boolean.TRUE,
                            "Could not upload stream.  {0}:{1}{2}{3}",
                            putMethod.getStatusCode(),
                            putMethod.getStatusLine(), "\n\t",
                            putMethod.getStatusText());
                default:
                    utils.writeError(putMethod);
                    throw new StreamException(
                            "Could not upload stream.  {0}:{1}{2}{3}",
                            putMethod.getStatusCode(),
                            putMethod.getStatusLine(), "\n\t",
                            putMethod.getStatusText());
                }
            } catch (final UnknownHostException uhx) {
                utils.writeError(putMethod);
                throw new NetworkException(uhx);
            } catch (final SocketException sx) {
                utils.writeError(putMethod);
                throw new NetworkException(sx);
            } catch (final SocketTimeoutException stx) {
                utils.writeError(putMethod);
                throw new NetworkException(stx);
            } catch (final HttpException hx) {
                utils.writeError(putMethod);
                throw new NetworkException(hx);
            } finally {
                putMethod.releaseConnection();
            }
        } finally {
            StreamClientMetrics.end("PUT", session);
        }
    }

    /**
     * Notify the client monitor that a chunk has been sent.
     * 
     * @param chunkSize
     *            The <code>int</code> size of the chunk.
     */
    private void fireChunkSent(final int chunkSize) {
        utils.logMonitorChunkSent(monitor, chunkSize);
        try {
            monitor.chunkSent(chunkSize);
        } catch (final Throwable cause) {
            utils.logMonitorError(monitor, cause);
        }
    }
}

/*
 * Created On: Wed Oct 25 2006 09:16  
 */
package com.thinkparity.codebase.model.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLException;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Environment;

/**
 * <b>Title:</b>thinkParity Stream Client<br>
 * <b>Description:</b>The stream client has the ability to create the remote
 * socket connection; negotiate the stream headers then read from and write to
 * the remote socket. When writing to the stream server the wait for completion
 * method will block until the appropriate stream id is written to the socket's
 * input stream. This allows the stream writer client to block when closing
 * until the stream upload is complete.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.12
 * @see StreamReader
 * @see StreamWriter
 */
abstract class StreamClient {

    /** A default <code>StreamMonitor</code>. */
    private static final StreamMonitor DEFAULT_MONITOR;

    /** An apache logger wrapper. */
    private static final Log4JWrapper LOGGER;

    /** A list of socket error messages know to be recoverable from. */
    private static final List<String> RECOVERABLE_MESSAGES;

    static {
        LOGGER = new Log4JWrapper();
        DEFAULT_MONITOR = new StreamMonitor() {
            public void chunkReceived(int chunkSize) {
                LOGGER.logApiId();
                LOGGER.logVariable("chunkSize", chunkSize);
            }
            public void chunkSent(final int chunkSize) {
                LOGGER.logApiId();
                LOGGER.logVariable("chunkSize", chunkSize);
            }
            public void headerReceived(final String header) {
                LOGGER.logApiId();
                LOGGER.logVariable("header", header);
            }
            public void headerSent(final String header) {
                LOGGER.logApiId();
                LOGGER.logVariable("header", header);
            }
            public void streamError(final StreamException error) {
                LOGGER.logApiId();
                if (error.isRecoverable())
                    LOGGER.logError(error, "A recoverable stream error has occured.");
                else
                    LOGGER.logError(error, "An unrecoverable stream error has occured.");
            }
        };
        RECOVERABLE_MESSAGES = new ArrayList<String>(2);
        RECOVERABLE_MESSAGES.add("bad record MAC");
        RECOVERABLE_MESSAGES.add("Connection reset");
        RECOVERABLE_MESSAGES.add("Received fatal alert: handshake_failure");
        RECOVERABLE_MESSAGES.add("Socket closed");
        RECOVERABLE_MESSAGES.add("Software caused connection abort: socket write error");
    }

    /** The stream's <code>InputStream</code>. */
    private InputStream input;

    /** A <code>StreamMonitor</code>. */
    private final StreamMonitor monitor;

    /** The stream's <code>OutputStream</code>. */
    private OutputStream output;

    /** The <code>StreamSession</code>. */
    private final StreamSession session;

    /** The backing <code>Socket</code>. */
    private Socket socket;

    /** The backing socket's <code>InetSocketAddress</code>. */
    private final InetSocketAddress socketAddress;

    /** The <code>SocketFactory</code> used to establish the stream. */
    private final SocketFactory socketFactory;

    /** The stream client <code>Type</code>. */
    private final Type type;

    /** An indicator of whether or not the write process is still happening. */
    private boolean writeInProgress;

    /**
     * Create StreamClient.
     * 
     * @param monitor
     *            A <code>StreamMonitor</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    protected StreamClient(final Type type, final StreamMonitor monitor,
            final StreamSession session) {
        super();
        this.monitor = monitor;
        this.session = session;
        final Environment environment = session.getEnvironment();
        this.socketAddress = new InetSocketAddress(
                environment.getStreamHost(), environment.getStreamPort());
        if (environment.isStreamTLSEnabled()) {
            new Log4JWrapper().logInfo("Stream Client - {0} - {1}:{2} - Secure",
                    type, environment.getStreamHost(),
                    environment.getStreamPort());
            final String keyStorePath = "security/stream_client_keystore";
            final char[] keyStorePassword = "password".toCharArray();
            try {
                socketFactory =
                    com.thinkparity.codebase.net.SocketFactory.getSecureInstance(keyStorePath, keyStorePassword, keyStorePath, keyStorePassword);
            } catch (final Exception x) {
                throw panic(x);
            }
        } else {
            new Log4JWrapper().logInfo("Stream Client - {0} - {1}:{2}",
                    type, environment.getStreamHost(),
                    environment.getStreamPort());
            socketFactory =
                com.thinkparity.codebase.net.SocketFactory.getInstance();
        }
        this.type = type;
    }

    /**
     * Create StreamClient.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     */
    protected StreamClient(final Type type, final StreamSession streamSession) {
        this(type, DEFAULT_MONITOR, streamSession);
    }

    /**
     * Connect the stream client.
     * 
     * @param type
     *            A stream <code>Type</code>.
     */
    protected final void connect() throws IOException {
        doConnect();
        write(new StreamHeader(StreamHeader.Type.SESSION_ID, session.getId()));
        write(new StreamHeader(StreamHeader.Type.SESSION_TYPE, type.name()));
    }

    /**
     * Disconnect the stream client.
     * 
     * @throws IOException
     */
    protected final void disconnect() throws IOException {
        try {
            output.flush();
        } finally {
            try {
                output.close();
            } finally {
                output = null;
                try {
                    input.close();
                } finally {
                    input = null;
                    try {
                        socket.close();
                    } finally {
                        socket = null;
                    }
                }
            }
        }
    }

    /**
     * Initialize the read operation. This will write the stream headers.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @param streamOffset
     *            The relative offset <code>Long</code> within the stream to
     *            begin writing.
     */
    protected void initializeRead(final String streamId, final Long streamOffset) {
        write(new StreamHeader(StreamHeader.Type.STREAM_ID, streamId));
        write(new StreamHeader(StreamHeader.Type.STREAM_OFFSET, String.valueOf(streamOffset)));
    }

    /**
     * Initialize the write operation. This will write the stream headers then
     * monitor the socket input stream for the stream id. The server will write
     * back the stream id when the write is complete.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @param streamSize
     *            The stream size <code>Long</code>.
     * @param streamOffset
     *            The relative offset <code>Long</code> within the stream to
     *            begin writing.
     */
    protected final void initializeWrite(final String streamId,
            final Long streamSize, final Long streamOffset) {
        writeInProgress = true;
        // NOTE order here is important; see StreamSocketDelegate#run()
        write(new StreamHeader(StreamHeader.Type.STREAM_ID, streamId));
        write(new StreamHeader(StreamHeader.Type.STREAM_OFFSET, String.valueOf(streamOffset)));
        write(new StreamHeader(StreamHeader.Type.STREAM_SIZE, String.valueOf(streamSize)));
        // THREAD stream client write listener
        new Thread("[TPS-Common-SCWriteListener]") {
            public void run() {
                try {
                    int nextByte;
                    final StringBuffer buffer = new StringBuffer(streamId.length());
                    while (-1 != (nextByte = input.read())) {
                        final char c = new String(new byte[] { (byte) nextByte },
                                session.getCharset().name()).charAt(0);
                        buffer.append(c);
                        if (streamId.equals(buffer.toString())) {
                            /* here we have just been notified that the write operation
                             * has been completed */
                            synchronized (StreamClient.this) {
                                writeInProgress = false;
                                StreamClient.this.notifyAll();
                                break;
                            }
                        }
                    }
                } catch (final IOException iox) {
                    throw panic(iox);
                }                
            }
        }.start();
    }

    /**
     * Read the underlying stream write it to stream.
     * 
     * @param stream
     *            The <code>OutputStream</code> to write to.
     */
    protected final void read(final OutputStream stream) {
        LOGGER.logDebug("Begin downstream session.");
        try {
            int len = 0, total = 0;
            final byte[] b = new byte[session.getBufferSize()];
            try {
                while ((len = input.read(b)) > 0) {
                    stream.write(b, 0, len);
                    stream.flush();
                    LOGGER.logDebug("Downstream bytes received:  {0}/?", total += len);
                    fireChunkReceived(len);
                }
            } finally {
                stream.flush();
            }
            LOGGER.logDebug("Downstream session complete.");
        } catch (final IOException iox) {
            if (SocketException.class.isAssignableFrom(iox.getClass()) ||
                    SSLException.class.isAssignableFrom(iox.getClass())) {
                if (RECOVERABLE_MESSAGES.contains(iox.getMessage())) {
                    if (isResumable()) {
                        fireStreamError(new StreamException(Boolean.TRUE, iox));
                    } else{ 
                        fireStreamError(new StreamException(Boolean.FALSE, iox));
                    }
                } else {
                    fireStreamError(new StreamException(Boolean.FALSE, iox));
                }
            } else {
                fireStreamError(new StreamException(Boolean.FALSE, iox));
            }
        }
    }

    /**
     * Wait for the write operation to be confirmed by the server.  Within the
     * write initialization we are monitoring for the stream id to be written to
     * the input stream then notify.
     *
     */
    protected final void waitForWriteCompletion() throws IOException {
        if (writeInProgress) {
            synchronized (StreamClient.this) {
                try {
                    StreamClient.this.wait();
                } catch (final InterruptedException ix) {}
            }
        }
    }

    /**
     * Write a stream.  Start a reader on the socket's input stream that will look
     * for the current session id.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @param streamSize
     *            The stream size <code>Long</code>.
     */
    protected final void write(final InputStream stream, final Long streamSize) {
        LOGGER.logDebug("Begin upstream session.");
        try {
            int len = 0, total = 0;
            final byte[] b = new byte[session.getBufferSize()];
            while ((len = stream.read(b)) > 0) {
                output.write(b, 0, len);
                output.flush();
                LOGGER.logDebug("Upstream bytes sent:  {0}/{1}", total += len, streamSize);
                fireChunkSent(len);
            }
            LOGGER.logDebug("Upstream session complete.");
        } catch (final IOException iox) {
            if(SocketException.class.isAssignableFrom(iox.getClass())) {
                if (RECOVERABLE_MESSAGES.contains(iox.getMessage())) {
                    if (isResumable(stream)) {
                        fireStreamError(new StreamException(Boolean.TRUE, iox));
                    } else {
                        fireStreamError(new StreamException(Boolean.FALSE, iox));
                    }
                } else {
                    fireStreamError(new StreamException(Boolean.FALSE, iox));
                }
            } else {
                fireStreamError(new StreamException(Boolean.FALSE, iox));
            }
        }
    }

    /**
     * Connect the stream client.
     * 
     * @throws IOException
     */
    private void doConnect() throws IOException {
        socket = socketFactory.createSocket(
                socketAddress.getAddress(), socketAddress.getPort());
        input = socket.getInputStream();
        output = socket.getOutputStream();
    }

    /**
     * Notiry the client monitor that a chunk has been received.
     * 
     * @param chunkSize
     *            The <code>int</code> chunk size.
     */
    private void fireChunkReceived(final int chunkSize) {
        try {
            monitor.chunkReceived(chunkSize);
        } catch (final Throwable t) {
            // do nothing; this is a client monitor issue
            LOGGER.logError(t, "Stream monitor error:  {0}", session);
        }
    }

    /**
     * Notify the client monitor that a chunk has been sent.
     * 
     * @param chunkSize
     *            The <code>int</code> size of the chunk.
     */
    private void fireChunkSent(final int chunkSize) {
        try {
            monitor.chunkSent(chunkSize);
        } catch (final Throwable t) {
            // do nothing; this is a client monitor issue
            LOGGER.logError(t, "Stream monitor error:  {0}", session);
        }
    }

    /**
     * Notify the client monitor that a header has been sent.
     * 
     * @param header
     *            The <code>StreamHeader</code>.
     */
    private void fireHeaderSent(final StreamHeader header) {
        try {
            monitor.headerSent(header.toHeader());
        } catch (final Throwable t) {
            // do nothing; this is a client monitor issue
            LOGGER.logError(t, "Stream monitor error:  {0}", session);
        }
    }

    /**
     * Notify the client monitor that a stream error has occured.
     * 
     * @param error
     *            The <code>StreamException</code>.
     */
    private void fireStreamError(final StreamException error) {
        try {
            monitor.streamError(error);
        } catch (final Throwable t) {
            // do nothing; this is a client monitor issue
            LOGGER.logError(t, "Stream monitor error:  {0}", session);
        }
    }

    /**
     * Determine if the stream client is connected.
     * 
     * @return True if the stream client is connected.
     */
    private boolean isConnected() {
        return null != socket && socket.isConnected();
    }

    /**
     * Determine whether or not a stream error is recoverable. The criteria for
     * being able to recover are:
     * <ul>
     * <li>Being able to create a socket connection to the socket address.
     * </ul>
     * 
     * @return True if the stream host is reachable.
     */
    private boolean isResumable() {
        Socket socket = null;
        try {
            socket = socketFactory.createSocket(
                socketAddress.getAddress(), socketAddress.getPort());
            return true;
        } catch (final Throwable t) {
            return false;
        } finally {
            if (null != socket) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (final IOException iox) {
                        throw panic(iox);
                    }
                }
            }
        }
    }

    /**
     * Determine whether or not a stream error is recoverable. The criteria for
     * being able to recover are:
     * <ul>
     * <li>Being able to create a socket connection to the socket address.
     * <li>Subsequent bytes being avaialbe on stream.
     * </ul>
     * 
     * @param stream
     *            A stream <code>InputStream</code>.
     * @return True if the stream host is reachable.
     */
    private boolean isResumable(final InputStream stream) {
        Socket socket = null;
        try {
            socket = socketFactory.createSocket(
                socketAddress.getAddress(), socketAddress.getPort());
            return 0 < stream.available();
        } catch (final Throwable t) {
            return false;
        } finally {
            if (null != socket) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (final IOException iox) {
                        throw panic(iox);
                    }
                }
            }
        }
    }

    /**
     * Panic. Check if the error is one that can be recovered from; and create
     * the appropriate stream error response.
     * 
     * @return A <code>StreamException</code>.
     */
    private StreamException panic(final Throwable t) {
        return new StreamException(Boolean.FALSE, t);
    }

    /**
     * Write a stream header.
     * 
     * @param streamHeader
     *            A <code>StreamHeader</code>.
     */
    private void write(final StreamHeader header) {
        try {
            write(header.toHeader());
            fireHeaderSent(header);
        } catch (final IOException iox) {
            throw panic(iox);
        }
    }

    /**
     * Write a message to the output stream.
     * 
     * @param message
     *            A message <code>String</code>.
     * @throws IOException
     */
    private void write(final String message) throws IOException {
        Assert.assertTrue(isConnected(),
                "{0} - No longer connected.", socketAddress);
        output.write(message.getBytes(session.getCharset().name()));
        output.flush();
    }

    /** An enumeration of the client type. */
    protected enum Type { DOWNSTREAM, UPSTREAM }
}

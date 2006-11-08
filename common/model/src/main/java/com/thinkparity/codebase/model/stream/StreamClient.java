/*
 * Created On: Wed Oct 25 2006 09:16  
 */
package com.thinkparity.codebase.model.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.SocketFactory;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Environment;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * 
 * HACK The stream client masks all io errors with a stream error. This is not
 * desirable and should be adjusted.
 */
public abstract class StreamClient {

    /** A default <code>StreamMonitor</code>. */
    private static final StreamMonitor DEFAULT_MONITOR = new StreamMonitor() {
        private final Log4JWrapper LOGGER = new Log4JWrapper();
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
    };

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

    /**
     * Create StreamClient.
     * 
     * @param monitor
     *            A <code>StreamMonitor</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    protected StreamClient(final StreamMonitor monitor,
            final StreamSession session) {
        super();
        this.monitor = monitor;
        this.session = session;
        final Environment environment = session.getEnvironment();
        this.socketAddress = new InetSocketAddress(
                environment.getStreamHost(), environment.getStreamPort());
        if (environment.isStreamTLSEnabled()) {
            new Log4JWrapper().logInfo("Stream Client - {0}:{1} - Secure",
                    environment.getStreamHost(), environment.getStreamPort());
            final String keyStorePath = "security/stream_client_keystore";
            final char[] keyStorePassword = "password".toCharArray();
            try {
                socketFactory =
                    com.thinkparity.codebase.net.SocketFactory.getSecureInstance(keyStorePath, keyStorePassword, keyStorePath, keyStorePassword);
            } catch (final Exception x) {
                throw new StreamException(x);
            }
        } else {
            new Log4JWrapper().logInfo("Stream Client - {0}:{1}",
                    environment.getStreamHost(), environment.getStreamPort());
            socketFactory =
                com.thinkparity.codebase.net.SocketFactory.getInstance();
        }
    }

    /**
     * Create StreamClient.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     */
    protected StreamClient(final StreamSession streamSession) {
        this(DEFAULT_MONITOR, streamSession);
    }

    /**
     * Establish a stream connection.
     * 
     * @param type
     *            A stream <code>Type</code>.
     */
    protected final void connect(final Type type) throws IOException {
        doConnect();
        write(new StreamHeader(StreamHeader.Type.SESSION_ID, session.getId()));
        write(new StreamHeader(StreamHeader.Type.SESSION_TYPE, type.name()));
    }

    protected final void disconnect() throws IOException {
        doDisconnect();
    }

    protected final void read(final OutputStream stream) {
        try {
            int len;
            final byte[] b = new byte[session.getBufferSize()];
            try {
                while((len = input.read(b)) > 0) {
                    stream.write(b, 0, len);
                    stream.flush();
                    fireChunkReceived(len);
                }
            } finally {
                stream.flush();
                stream.close();
            }
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }

    /**
     * Write a stream.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @param streamSize
     *            The stream size <code>Long</code>.
     */
    protected final void write(final InputStream stream, final Long streamSize) {
        try {
            int len;
            final byte[] b = new byte[session.getBufferSize()];
            try {
                while((len = stream.read(b)) > 0) {
                    output.write(b, 0, len);
                    output.flush();
                    fireChunkSent(len);
                }
            } finally {
                stream.close();
            }
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }

    /**
     * Write a stream header.
     * 
     * @param streamHeader
     *            A <code>StreamHeader</code>.
     */
    protected final void write(final StreamHeader header) {
        try {
            write(header.toHeader());
            fireHeaderSent(header);
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }

    private void doConnect() throws IOException {
        socket = socketFactory.createSocket(
                socketAddress.getAddress(), socketAddress.getPort());
        input = socket.getInputStream();
        output = socket.getOutputStream();
    }

    private void doDisconnect() throws IOException {
        output.flush();
        output.close();
        output = null;
        input.close();
        input = null;
        socket.close();
        socket = null;
    }

    private void fireChunkReceived(final int chunkSize) {
        monitor.chunkReceived(chunkSize);
    }

    private void fireChunkSent(final int chunkSize) {
        monitor.chunkSent(chunkSize);
    }

    private void fireHeaderSent(final StreamHeader header) {
        monitor.headerSent(header.toHeader());
    }

    private boolean isConnected() {
        return null != socket && socket.isConnected();
    }

    private void write(final String message) throws IOException {
        Assert.assertTrue(isConnected(),
                "{0} - No longer connected.", socketAddress);
        output.write(message.getBytes(session.getCharset().name()));
        output.flush();
    }

    protected enum Type {
        DOWNSTREAM, UPSTREAM
    }
}

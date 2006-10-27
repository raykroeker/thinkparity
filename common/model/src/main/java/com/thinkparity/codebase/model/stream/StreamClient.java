/*
 * Created On: Wed Oct 25 2006 09:16  
 */
package com.thinkparity.codebase.model.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Environment;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class StreamClient {

    private InputStream input;

    private final Log4JWrapper logger;

    private OutputStream output;

    private final StreamSession session;

    private Socket socket;

    private final SocketAddress socketAddress;

    protected StreamClient(final StreamSession session) {
        super();
        this.logger = new Log4JWrapper();
        this.session = session;
        final Environment environment = session.getEnvironment();
        this.socketAddress = new InetSocketAddress(
                environment.getStreamHost(), environment.getStreamPort());
    }

    protected final void connect(final Type type) {
        try {
            doConnect();
            write(new StreamHeader(StreamHeader.Type.SESSION_BEGIN, session.getId()));
            write(new StreamHeader(StreamHeader.Type.SESSION_TYPE, type.name()));
        } catch (final IOException iox) {
            if (isConnected())
                try {
                    doDisconnect();
                } catch (final IOException iox2) {
                    /* NOTE We do not want to mask the connection error. */
                    logger.logError(iox2, "Could not disconnect from bad connection.");
                }
            throw new StreamException(iox);
        }
    }

    protected final void disconnect() {
        try {
            doDisconnect();
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }

    protected final void read(final OutputStream stream) {
        logger.logApiId();
        try {
            StreamUtil.copy(input, stream, session.getBufferSize());
            output.flush();
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }

    protected final void write(final InputStream stream) {
        logger.logApiId();
        try {
            StreamUtil.copy(stream, output, session.getBufferSize());
            output.flush();
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }

    protected final void write(final StreamHeader streamHeader) {
        logger.logApiId();
        try {
            write(streamHeader.toHeader());
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }

    private void doConnect() throws IOException {
        logger.logTraceId();
        socket = new Socket();
        socket.connect(socketAddress);
        input = socket.getInputStream();
        output = socket.getOutputStream();
    }

    private void doDisconnect() throws IOException {
        logger.logTraceId();
        output.flush();
        output.close();
        output = null;
        input.close();
        input = null;
        socket.close();
        socket = null;
    }

    private boolean isConnected() {
        return null != socket && socket.isConnected();
    }

    private void write(final String message) throws IOException {
        Assert.assertTrue(isConnected(),
                "{0} - No longer connected.", socketAddress);
        logger.logTraceId();
        logger.logVariable("message.length()", message.length());
        output.write(message.getBytes(session.getCharset().name()));
        output.flush();
    }

    protected enum Type {
        DOWNSTREAM, UPSTREAM
    }
}

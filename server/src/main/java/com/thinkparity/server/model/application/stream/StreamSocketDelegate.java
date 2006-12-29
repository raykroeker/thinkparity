/*
 * Created On:  26-Oct-06 6:30:30 PM
 */
package com.thinkparity.desdemona.model.stream;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamHeader;
import com.thinkparity.codebase.model.stream.StreamSession;


/**
 * <b>Title:</b>thinkParity Stream Socket Delegate<br>
 * <b>Description:</b>Crack open the socket's input stream and read the first
 * header which will describe whether we want to perform up or downstream
 * transfer.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamSocketDelegate implements Runnable {

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    /** The client <code>Socket</code>. */
    private final Socket socket;

    /** A <code>StreamServer</code>. */
    private final StreamServer streamServer;

    /**
     * Create StreamSocketDelegate.
     *
     */
    StreamSocketDelegate(final StreamServer streamServer,
            final Socket socket) throws IOException {
        super();
        this.logger = new Log4JWrapper();
        this.socket = socket;
        this.streamServer = streamServer;
    }

    /**
     * Obtain the delegate name.
     * 
     * @return The delegate name.
     */
    String getName() {
        return new StringBuffer("StreamSocketDelegate - ")
            .append(socket.getRemoteSocketAddress())
            .toString();
    }

    /**
     * Run the stream socket delegate. The stream headers are extracted; and the
     * appropriate handler (upstream or downstream) is invoked.
     * 
     */
    public void run() {
        try {
            final HeaderReader headerReader = new HeaderReader();
            final StreamHeader sessionId = headerReader.readNext();
            if (null != sessionId) {
                final StreamSession streamSession =
                    streamServer.authenticate(sessionId.getValue(),
                            ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress());
                final StreamHeader sessionType = headerReader.readNext();
                final StreamHeader streamId = headerReader.readNext();
                final StreamHeader streamOffset = headerReader.readNext();
                if ("UPSTREAM".equals(sessionType.getValue())) {
                    final StreamHeader streamSize = headerReader.readNext();
                    new UpstreamHandler(streamServer, streamSession,
                            streamId.getValue(),
                            Long.valueOf(streamOffset.getValue()),
                            Long.valueOf(streamSize.getValue()),
                            socket.getInputStream()).run();
                } else if ("DOWNSTREAM".equals(sessionType.getValue())) {
                    new DownstreamHandler(streamServer, streamSession,
                            streamId.getValue(),
                            Long.valueOf(streamOffset.getValue()),
                            streamServer.getSize(streamSession, streamId.getValue()),
                            socket.getOutputStream()).run();
                } else {
                    Assert.assertUnreachable("Unkown stream transfer.");
                }
            }
        } catch (final IOException iox) {
            throw new StreamException(iox);
        } finally {
            try {
                socket.close();
            } catch (final IOException iox2) {
                logger.logError(iox2, "Could not close client socket.");
            }
        }
    }

    /**
     * <b>Title:</b>thinkParity StreamSocketDelegate Header Reader<br>
     * <b>Description:</b>Reads an input stream a character at a time until a
     * header can be constructed.<br>
     * 
     * @author raymond@thinkparity.com
     * @version 1.1.2.1
     */
    private final class HeaderReader {

        /** An apache logger wrapper. */
        private final Log4JWrapper logger;

        /** An <code>InputStream</code>. */
        private final InputStream stream;

        /**
         * Create HeaderReader.
         * 
         */
        private HeaderReader() throws IOException {
            super();
            this.logger = new Log4JWrapper();
            this.stream = socket.getInputStream();
        }

        /**
         * Read the next byte of the stream.
         * 
         * @return The next byte of the stream; or -1 if the stream is at an
         *         end.
         * @throws IOException
         */
        private final int read() throws IOException {
            return stream.read();
        }

        /**
         * Read the next stream header.
         * 
         * @return A stream header; or null if the end of stream is reached.
         * @throws IOException
         */
        private final StreamHeader readNext() throws IOException {
            int nextByte;
            final StringBuffer headerBuffer = new StringBuffer();
            while (-1 != (nextByte = read())) {
                final char c = new String(new byte[] { (byte) nextByte },
                        streamServer.getCharset().name()).charAt(0);
                headerBuffer.append(c);
                if (';' == c) {
                    /*
                     * here we have just completed what looks like a
                     * header declaration - we analyze the header and
                     * take appropriate action
                     */
                    logger.logVariable("header", headerBuffer);
                    return StreamHeader.valueOf(headerBuffer.toString());
                }
            }
            return null;
        }
    }
}

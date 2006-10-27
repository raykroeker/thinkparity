/*
 * Created On:  26-Oct-06 6:30:30 PM
 */
package com.thinkparity.desdemona.model.stream;

import java.io.IOException;
import java.net.Socket;

import com.thinkparity.codebase.assertion.Assert;

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
final class StreamSocketDelegate {

    private final StreamServer streamServer;

    private final Socket socket;

    /**
     * Create StreamSocketDelegate.
     *
     */
    StreamSocketDelegate(final StreamServer streamServer,
            final Socket socket) throws IOException {
        super();
        this.socket = socket;
        this.streamServer = streamServer;
    }

    void run() throws IOException {
        final StreamReader streamReader = new StreamReader(
                streamServer.getCharset(), socket.getInputStream());
        final StreamHeader sessionId = streamReader.readNextHeader();
        final StreamSession streamSession = streamServer.authenticate(sessionId.getValue());
        final StreamHeader sessionType = streamReader.readNextHeader();
        final StreamHeader streamId = streamReader.readNextHeader();
        if(null != streamSession) {
            if ("UPSTREAM".equals(sessionType.getValue())) {
                new UpstreamHandler(streamServer, streamSession,
                        streamId.getValue(), socket.getInputStream()).run();
            } else if ("DOWNSTREAM".equals(sessionType.getValue())) {
                new DownstreamHandler(streamServer, streamSession,
                        streamId.getValue(), socket.getOutputStream()).run();
            } else {
                Assert.assertUnreachable("Unkown stream transfer.");
            }
        }
        socket.close();
    }
}

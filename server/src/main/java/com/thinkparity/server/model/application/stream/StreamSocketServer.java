/*
 * Created On: Sun Oct 22 2006 11:50 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamSocketServer implements Runnable {

    private final ExecutorService executorService;

    private final Log4JWrapper logger;

    private boolean run;

    private ServerSocket serverSocket;

    private final SocketAddress serverSocketAddress;

    private boolean started;

    private final StreamServer streamServer;

    StreamSocketServer(final StreamServer streamServer, final String host,
            final Integer port) {
        super();
        this.executorService = Executors.newSingleThreadExecutor();
        this.logger = new Log4JWrapper();
        this.serverSocketAddress = new InetSocketAddress(host, port);
        this.streamServer = streamServer;
    }

    /**
     * Run the stream socket server.
     * 
     */
    public void run() {
        logger.logApiId();
        logger.logVariable("run", run);
        try {
            while (run) {
                started = true;
                synchronized (this) {
                    notifyAll();
                }

                final Socket clientSocket = serverSocket.accept();
                logger.logTrace("Socket connected.");
                new StreamSocketDelegate(streamServer, clientSocket).run();
                logger.logTrace("Socket handler executed.");
            }
        } catch (final Throwable t) {
            logger.logFatal(t, "Fatal stream socket server error.");
        }
    }

    /**
     * Destroy a session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    void destroy(final StreamSession session) {
    }

    /**
     * Destroy a stream.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void destroy(final StreamSession streamSession, final String streamId) {
    }

    /**
     * Initialize a stream session.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     */
    void initialize(final StreamSession streamSession) {
    }

    /**
     * Initialize a stream.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void initialize(final StreamSession streamSession, final String streamId) {
    }

    void start() {
        logger.logApiId();
        try {
            doStart();
        } catch (final Exception x) {
            throw new StreamException(x);
        }
    }

    void stop(final Boolean wait) {
        try {
            doStop(wait);
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }

    private void doStart() throws IOException, InterruptedException {
        run = true;
        started = false;
        serverSocket = new ServerSocket();
        serverSocket.bind(serverSocketAddress);
        logger.logTrace("Server socket bound.");
        
        executorService.execute(this);
        while (!started) {
            synchronized (this) {
                wait();
            }
        }
        logger.logTrace("Socket server started.");
    }

    private void doStop(final Boolean wait) throws IOException {
        run = false;
        serverSocket.close();
        serverSocket = null;
    }
}

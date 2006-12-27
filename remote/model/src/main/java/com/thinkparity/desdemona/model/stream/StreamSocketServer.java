/*
 * Created On: Sun Oct 22 2006 11:50 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class StreamSocketServer implements Runnable {

    /** A stack of the connected client <code>Socket</code>. */
    private final Stack<Socket> clientSockets;

    /** A <code>ExecutorServer</code> to create threads within. */
    private final ExecutorService executorService;

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    /** A <code>Boolean</code> flag indicating whether or not to keep running. */
    private Boolean run;

    /** The <code>ServerSocket</code>. */
    private ServerSocket serverSocket;

    /** The <code>InetSocketAddress</code> to bind to. */
    private final InetSocketAddress serverSocketAddress;

    /** An <code>Integer</code> backlog of pending socket connections. */
    private final Integer serverSocketBacklog;

    /** A <code>ServerSocketFactory</code>. */
    private final ServerSocketFactory serverSocketFactory;

    /** A <code>Boolean</code> flag indicating whether or not the stream socket server has been started. */
    private Boolean started;

    /** A <code>StreamServer</code>. */
    private final StreamServer streamServer;

    /**
     * Create StreamSocketServer.
     * 
     * @param streamServer
     *            A <code>StreamServer</code>.
     * @param host
     *            A host <code>String</code>.
     * @param port
     *            A port <code>Integer</code>.
     */
    protected StreamSocketServer(final StreamServer streamServer,
            final String host, final Integer port) {
        this(streamServer, host, port,
                com.thinkparity.codebase.net.ServerSocketFactory.getInstance());
    }

    /**
     * Create StreamSocketServer.
     * 
     * @param streamServer
     *            A <code>StreamServer</code>.
     * @param host
     *            A host <code>String</code>.
     * @param port
     *            A port <code>Integer</code>.
     * @param factory
     *            A <code>ServerSocketFactory</code>.
     */
    protected StreamSocketServer(final StreamServer streamServer,
            final String host, final Integer port,
            final ServerSocketFactory factory) {
        super();
        this.clientSockets = new Stack<Socket>();
        this.executorService = Executors.newSingleThreadExecutor();
        this.logger = new Log4JWrapper(getClass());
        this.serverSocketAddress = new InetSocketAddress(host, port);
        this.serverSocketBacklog = 75;
        this.serverSocketFactory = factory;
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

                // attempt to establish a socket connection
                try {
                    clientSockets.push(serverSocket.accept());
                } catch (final SocketException sx) {
                    if ("Socket closed".equals(sx.getMessage())) {
                        logger.logInfo("Stream socket server shutdown.");
                        synchronized (this) {
                            notifyAll();
                        }
                        return;
                    } else {
                        throw sx;
                    }
                }

                // use the socket delegate to handle the streaming
                try {
                    final StreamSocketDelegate delegate = new StreamSocketDelegate(streamServer, clientSockets.peek());
                    new Thread(delegate, delegate.getName()).start();
                } catch (final Throwable t) {
                    logger.logError(t, "Failed to negotiate stream {0}.",
                            clientSockets.peek().getRemoteSocketAddress());
                } finally {
                    clientSockets.pop();
                }
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

    /**
     * Log some statistics.
     *
     */
    void logStatistics() {
        logger.logInfo("Streaming socket server socket address:{0}", serverSocketAddress.getAddress());
        logger.logInfo("Streaming socket server socket backlog:{0}", serverSocketBacklog);
    }

    void start() {
        logger.logApiId();
        try {
            doStart();
        } catch (final Exception x) {
            logger.logFatal(x, "Could not start stream socket server.");
            throw new StreamException(x);
        }
    }

    void stop(final Boolean wait) {
        try {
            doStop(wait);
        } catch (final InterruptedException ix) {
            throw new StreamException(ix);
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }

    private void doStart() throws IOException, InterruptedException,
            CertificateException, KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException,
            UnrecoverableKeyException, NoSuchProviderException {
        run = true;
        started = false;
        serverSocket = serverSocketFactory.createServerSocket(
                serverSocketAddress.getPort(), serverSocketBacklog,
                serverSocketAddress.getAddress());
        logger.logTrace("Server socket bound.");

        executorService.execute(this);
        while (!started) {
            synchronized (this) {
                wait();
            }
        }
        logger.logTrace("Socket server started.");
    }

    private void doStop(final Boolean wait) throws IOException,
            InterruptedException {
        run = false;
        try {
            if (wait.booleanValue() && 0 < clientSockets.size()) {
                synchronized (this) {
                    wait();
                }
            }
        } finally {
            try {
                serverSocket.close();
            } finally {
                serverSocket = null;
            }
        }
    }
}

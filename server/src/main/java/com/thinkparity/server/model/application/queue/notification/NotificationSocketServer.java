/*
 * Created On:  4-Jun-07 9:12:05 AM
 */
package com.thinkparity.desdemona.model.queue.notification;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class NotificationSocketServer implements Runnable {

    /** The secure <code>javax.net.ServerSocketFactory</code>. */
    private static final ServerSocketFactory SERVER_SOCKET_FACTORY;

    static {
        SERVER_SOCKET_FACTORY = com.thinkparity.codebase.net.ServerSocketFactory.getInstance();
    }

    /** A stack of client sockets used when accepting the connection. */
    private final Stack<Socket> clientSockets;

    /** An executor service used to run the socket server. */
    private final ExecutorService executorService;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** A run indicator. */
    private boolean run;

    /** The notification server. */
    private final NotificationServer server;

    /** The server socket that is established when starting. */
    private ServerSocket serverSocket;

    /** The server socket address. */
    private final InetSocketAddress serverSocketAddress;

    /** The server socket backlog. */
    private final Integer serverSocketBacklog;

    /** The server socket factory. */
    private final ServerSocketFactory serverSocketFactory;

    /** A started indicator. */
    private boolean started;

    /**
     * Create NotificationSocketServer.
     *
     */
    protected NotificationSocketServer(final NotificationServer server,
            final String host, final Integer port,
            final ServerSocketFactory serverSocketFactory) {
        super();
        this.clientSockets = new Stack<Socket>();
        this.executorService = Executors.newSingleThreadExecutor();
        this.logger = new Log4JWrapper("NotificationService");
        this.server = server;
        this.serverSocketAddress = new InetSocketAddress(host, port);
        this.serverSocketBacklog = 75;
        this.serverSocketFactory = serverSocketFactory;
    }

    
    /**
     * Create SecureNotificationSocketServer.
     * 
     * @param server
     *            The notification server.
     * @param host
     *            The host to listen on.
     * @param port
     *            The port to listen on.
     */
    NotificationSocketServer(final NotificationServer server,
            final String host, final Integer port) {
        this(server, host, port, SERVER_SOCKET_FACTORY);
    }

    /**
     * @see java.lang.Runnable#run()
     *
     */
    public void run() {
        logger.logTraceId();
        logger.logInfo("Running notification socket server.");
        try {
            while (run) {
                started = true;
                synchronized (this) {
                    notifyAll();
                }
                // establish socket connection
                try {
                    clientSockets.push(serverSocket.accept());
                } catch (final SocketException sx) {
                    if ("Socket closed".equals(sx.getMessage())) {
                        logger.logInfo("Notification socket server shutdown.");
                        synchronized (this) {
                            notifyAll();
                        }
                        return;
                    } else {
                        throw sx;
                    }
                }
                // start delegate
                try {
                    final NotificationSocketDelegate delegate = newDelegate();
                    // THREAD - NotificationSocketServer#run()
                    new Thread(delegate, "TPS-DesdemonaModel-NSDelegate-" + delegate.getRemoteAddress()).start();
                } catch (final Throwable t) {
                    logger.logError(t, "Failed to negotiate notification stream {0}.",
                            clientSockets.peek().getRemoteSocketAddress());
                } finally {
                    clientSockets.pop();
                }
            }
        } catch (final Throwable t) {
            logger.logFatal(t, "Notification socket server error.");
        }
    }

    /**
     * Log some socket server statistics.
     *
     */
    void logStatistics() {
        logger.logInfo("Notification socket server address:{0}", serverSocketAddress);
        logger.logInfo("Notification socket server backlog:{0}", serverSocketBacklog);
        logger.logInfo("Notification socket server client sockets:{0}", clientSockets.size());
    }

    /**
     * Start the notification socket server.
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    void start() throws IOException, InterruptedException {
        logger.logTraceId();
        logger.logInfo("Starting notification socket server.");
        run = true;
        started = false;
        serverSocket = serverSocketFactory.createServerSocket(
                serverSocketAddress.getPort(), serverSocketBacklog,
                serverSocketAddress.getAddress());
        logger.logTraceId();
        executorService.execute(this);
        while (!started) {
            synchronized (this) {
                wait();
            }
        }
        logger.logInfo("Notification socket server started.");
    }

    /**
     * Stop the notification socket server.
     * 
     * @param wait
     *            Whether or not to wait for the client sockets to disconnect.
     * @throws InterruptedException
     * @throws IOException
     */
    void stop(final Boolean wait) throws InterruptedException, IOException {
        logger.logTraceId();
        logger.logInfo("Stopping notification socket server.");
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
        logger.logInfo("Notification socket server stopped.");
    }

    /**
     * Create a notification socket delegate.
     * 
     * @return A <code>NotificationSocketDelegate</code>.
     * @throws SocketException
     */
    private NotificationSocketDelegate newDelegate() throws SocketException {
        final Socket clientSocket = clientSockets.peek();
        clientSocket.setKeepAlive(true);
        return new NotificationSocketDelegate(server, clientSocket);
    }
}

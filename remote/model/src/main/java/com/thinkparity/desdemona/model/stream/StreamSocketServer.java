/*
 * Created On: Sun Oct 22 2006 11:50 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import coconut.aio.AcceptPolicy;
import coconut.aio.AsyncServerSocket;
import coconut.aio.AsyncSocket;
import coconut.core.Callback;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamSocketServer {

    /** The stream socket <code>SocketAddress</code>. */
    private final SocketAddress socketAddress;

    /** The stream socket <code>Callback&lt;AsyncSocket&gt;</code>. */
    private final Callback<AsyncSocket> socketCallback;

    /** The stream socket <code>Executor</code>. */
    private final Executor socketExecutor;

    /** The stream socket <code>AcceptPolicy</code>. */
    private final AcceptPolicy socketPolicy;

    /**
     * Create StreamSocketServer.
     * 
     * @param host
     *            The socket address host to bind to.
     * @param port
     *            The socket port to listen on.
     */
    StreamSocketServer(final String host, final Integer port) {
        super();
        this.socketAddress = new InetSocketAddress(host, port);
        this.socketCallback = new Callback<AsyncSocket>() {
            public void completed(final AsyncSocket asyncSocket) {
            }
            public void failed(final Throwable t) {
            }
        };
        this.socketExecutor = Executors.newCachedThreadPool();
        this.socketPolicy = new AcceptPolicy() {
            public int acceptNext(final AsyncServerSocket asyncServerSocket) {
                return 1;
            }
        };
    }

    /**
     * Start the stream socket server.
     * 
     * @throws IOException
     */
    void start() throws IOException {
        final Executor executor = Executors.newCachedThreadPool();
        final AsyncServerSocket asyncServerSocket = AsyncServerSocket.open(executor);
        asyncServerSocket.bind(socketAddress);
        asyncServerSocket.startAccepting(socketExecutor, socketCallback, socketPolicy);
    }

    /**
     * Stop the stream socket server.
     *
     */
    void stop() {
    }
}

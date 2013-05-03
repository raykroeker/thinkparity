/*
 * Created On:  30-Oct-06 8:54:07 AM
 */
package com.thinkparity.codebase.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * <b>Title:</b>thinkParity Common Server Socket Factory<br>
 * <b>Description:</b>Used to instantiate server socket factories..<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServerSocketFactory extends javax.net.ServerSocketFactory {

    /**
     * Obtain a server socket factory.
     * 
     * @return A <code>javax.net.ServerSocketFactory</code>.
     */
    public final static javax.net.ServerSocketFactory getInstance() {
        return new ServerSocketFactory(javax.net.ServerSocketFactory.getDefault());
    }

    /** The wrapped instance of <code>SSLServerSocketFactory</code>. */
    private final javax.net.ServerSocketFactory factory;

    /**
     * Create SSLServerSocketFactory.
     *
     */
    private ServerSocketFactory(final javax.net.ServerSocketFactory factory) {
        super();
        this.factory = factory;
    }

    /**
     * @see javax.net.ServerSocketFactory#createServerSocket(int)
     *
     */
    @Override
    public ServerSocket createServerSocket(final int port) throws IOException {
        return factory.createServerSocket(port);
    }

    /**
     * @see javax.net.ServerSocketFactory#createServerSocket(int, int)
     *
     */
    @Override
    public ServerSocket createServerSocket(final int port, final int backlog)
            throws IOException {
        return factory.createServerSocket(port, backlog);
    }

    /**
     * @see javax.net.ServerSocketFactory#createServerSocket(int, int, java.net.InetAddress)
     *
     */
    @Override
    public ServerSocket createServerSocket(final int port, final int backlog,
            final InetAddress ifAddress) throws IOException {
        return factory.createServerSocket(port, backlog, ifAddress);
    }
}
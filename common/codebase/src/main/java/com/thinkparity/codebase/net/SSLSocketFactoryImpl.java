/*
 * Created On:  14-May-07 4:59:04 PM
 */
package com.thinkparity.codebase.net;

import java.io.IOException;
import java.net.*;
import java.text.MessageFormat;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

/**
 * <b>Title:</b>thinkParity CommonCodebase Socket Factory Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class SSLSocketFactoryImpl extends javax.net.SocketFactory {

    /** A <code>ProxySelector</code>. */
    private final ProxySelector proxySelector;

    /** A <code>javax.net.SocketFactory</code>. */
    private final javax.net.SocketFactory socketFactory;

    /** The select proxies uri pattern <code>String</code>. */
    private final String uriPattern;

    /**
     * Create SocketFactory.
     *
     */
    SSLSocketFactoryImpl(final javax.net.SocketFactory socketFactory) {
        super();
        this.proxySelector = ProxySelector.getDefault();
        this.socketFactory = socketFactory;
        this.uriPattern = "socket://{0}";
    }

    /**
     * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int)
     *
     */
    @Override
    public Socket createSocket(final InetAddress host, final int port)
            throws IOException {
        final SocketAddress socketAddress = new InetSocketAddress(host, port);
        /* attempt to tunnel an SSL socket through a proxied socket - the first
         * connection wins */
        final List<Proxy> proxies = selectProxies(host);
        Socket proxySocket = null, socket = null;
        for (final Proxy proxy : proxies) {
            proxySocket = new Socket(proxy);
            proxySocket.connect(socketAddress);
            socket = ((SSLSocketFactory) socketFactory).createSocket(
                    proxySocket, host.getHostName(), port, false);
            SocketFactory.LOGGER.logInfo(
                    "Socket endpoint to {0}:{1} has been established via {2}.",
                    host, port, proxy);

            break;
        }
        return socket;
    }

    /**
     * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int,
     *      java.net.InetAddress, int)
     * 
     */
    @Override
    public Socket createSocket(final InetAddress address, final int port,
            final InetAddress localAddress, final int localPort)
            throws IOException {
        /* because we are always using a proxy, the local address/port are
         * always already bound */
        return createSocket(address, port);
    }

    /**
     * @see javax.net.SocketFactory#createSocket(java.lang.String, int)
     *
     */
    @Override
    public Socket createSocket(final String host, final int port) throws IOException,
            UnknownHostException {
        return createSocket(InetAddress.getByName(host), port);
    }

    /**
     * @see javax.net.SocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int)
     *
     */
    @Override
    public Socket createSocket(final String host, final int port,
            final InetAddress localHost, final int localPort)
            throws IOException, UnknownHostException {
        return createSocket(InetAddress.getByName(host), port, localHost,
                localPort);
    }

    /**
     * Select a list of proxies for a host address and port.
     * 
     * @param host
     *            An <code>InetAddress</code>.
     * @param port
     *            A port <code>int</code>.
     * @return A <code>Proxy</code> <code>List</code>.
     */
    private List<Proxy> selectProxies(final InetAddress host) {
        try {
            return proxySelector.select(new URI(MessageFormat.format(
                    uriPattern, host.getHostName())));
        } catch (final URISyntaxException urisx) {
            throw new IllegalArgumentException(urisx);
        }
    }
}

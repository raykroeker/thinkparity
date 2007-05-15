/*
 * Created On:  14-May-07 4:59:04 PM
 */
package com.thinkparity.codebase.net;

import java.io.IOException;
import java.net.*;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity CommonCodebase Socket Factory Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class SSLSocketFactoryImpl extends SocketFactory {

    /** A <code>ProxySelector</code>. */
    private final ProxySelector proxySelector;

    /** A <code>javax.net.SocketFactory</code>. */
    private final javax.net.SocketFactory socketFactory;

    /**
     * Create SocketFactory.
     *
     */
    SSLSocketFactoryImpl(final javax.net.SocketFactory socketFactory) {
        super();
        this.proxySelector = ProxySelector.getDefault();
        this.socketFactory = socketFactory;
    }

    /**
     * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int)
     *
     */
    @Override
    public Socket createSocket(final InetAddress host, final int port)
            throws IOException {
        final List<Proxy> proxies = selectProxies(host, port);
        Socket socket = null;
        for (final Proxy proxy : proxies) {
            switch (proxy.type()) {
            case DIRECT:
                socket = socketFactory.createSocket(host, port);
                break;
            case SOCKS:
                final Socket proxySocket = new Socket(
                        ((InetSocketAddress) proxy.address()).getHostName(),
                        ((InetSocketAddress) proxy.address()).getPort());
                socket = ((SSLSocketFactory) socketFactory).createSocket(
                        proxySocket, host.getHostName(), port, true);
                break;
            case HTTP:
            default:
                Assert.assertUnreachable("Unsupported proxy type.");
            }
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
        throw Assert.createUnreachable("Cannot create socket.");
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
    private List<Proxy> selectProxies(final InetAddress host, final int port) {
        final String uri = new StringBuilder(32)
            .append("socket://")
            .append(host.getHostName())
            .append(":")
            .append(port)
            .toString();
        try {
            return proxySelector.select(new URI(uri));
        } catch (final URISyntaxException urisx) {
            throw new IllegalArgumentException(urisx);
        }
    }
}

/*
 * Created On:  19-Aug-07 2:05:14 PM
 */
package com.thinkparity.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.net.SocketFactory;

import com.thinkparity.network.io.NetworkOutputStream;

/**
 * <b>Title:</b>thinkParity Network Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class NetworkImpl {

    /** A message format pattern for uri creation. */
    private static final String URI_PATTERN;

    static {
        URI_PATTERN = "{0}://{1}";
    }

    /**
     * Create a uri from a protocol; and address.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>URI</code>.
     * @throws URISyntaxException
     */
    private static URI newURI(final NetworkProtocol protocol,
            final NetworkAddress address) throws URISyntaxException {
        return new URI(MessageFormat.format(URI_PATTERN, protocol.getName(),
                address.getHost()));
    }

    /** The network configuration. */
    private NetworkConfiguration configuration;

    /** A connection log4j wrapper. */
    private final Log4JWrapper connectionLogger;

    /** The network connections. */
    private final NetworkConnections connections;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** A network id generator. */
    private final NetworkId networkId;

    /** The proxy selector. */
    private ProxySelector proxySelector;

    /** A secure socket factory. */
    private SSLSocketFactory secureSocketFactory;

    /**
     * Create NetworkImpl.
     *
     */
    NetworkImpl(final Log4JWrapper logger) {
        super();
        this.connectionLogger = new Log4JWrapper("com.thinkparity.network.connection");
        this.connections = new NetworkConnections();
        this.logger = logger;
        this.networkId = new NetworkId();
    }

    /**
     * Obtain the configuration.
     * 
     * @return A <code>NetworkConfiguration</code>.
     */
    NetworkConfiguration getConfiguration() {
        logger.logTraceId();
        if (null == configuration) {
            configuration = new NetworkConfiguration();
        }
        return configuration;
    }

    /**
     * Find the network connection associated with the output stream.
     * 
     * @param outputStream
     *            An <code>OutputStream</code>.
     * @return A <code>NetworkConnection</code>.
     */
    NetworkConnection getConnection(final OutputStream outputStream) {
        logger.logTraceId();
        logger.logVariable("outputStream", outputStream);
        final String connectionId = getConnectionId(outputStream);
        for (final NetworkConnection connection : connections.get()) {
            if (connectionId.equals(connection.getId())) {
                return connection;
            }
        }
        return null;
    }

    /**
     * Create a network connection.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>NetworkConnection</code>.
     */
    NetworkConnection newConnnection(final NetworkProtocol protocol,
            final NetworkAddress address) {
        logger.logTraceId();
        logger.logVariable("protocol", protocol);
        logger.logVariable("address", address);
        final NetworkConnectionImpl connection;
        try {
            connection = newConnection(protocol, address);
            connections.add(connection);
        } catch (final URISyntaxException urisx) {
            throw new IllegalArgumentException("Cannot create new connection.",
                    urisx);
        }
        return connection;
    }

    /**
     * Create a secure socket routed through a proxy to the address.
     * 
     * @param connectionId
     *            A <code>String</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>Socket</code>.
     */
    Socket newSecureSocket(final String connectionId, final NetworkProxy proxy,
            final NetworkAddress address) throws NetworkException {
        logger.logTraceId();
        logger.logVariable("connectionId", connectionId);
        logger.logVariable("address", proxy);
        logger.logVariable("address", address);
        final Socket proxySocket = newSocket(connectionId, proxy);
        try {
            proxySocket.connect(NetworkUtil.newSocketAddress(address));
            return new SecureNetworkSocket(connectionId,
                    (SSLSocket) getSecureSocketFactory().createSocket(
                            proxySocket, address.getHost(), address.getPort(),
                            true));
        } catch (final IOException iox) {
            throw new NetworkException(iox);
        }
    }

    /**
     * Create a socket routed through a proxy.
     * 
     * @param connectionId
     *            A <code>String</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @return A <code>Socket</code>.
     */
    Socket newSocket(final String connectionId, final NetworkProxy proxy) {
        logger.logTraceId();
        logger.logVariable("connectionId", connectionId);
        logger.logVariable("proxy", proxy);
        return new NetworkSocket(connectionId, NetworkUtil.newProxy(proxy));
    }

    /**
     * Obtain the next connection id.
     * 
     * @param connection
     *            A <code>NetworkConnectionImpl</code>.
     * @return A connection id <code>String</code>.
     */
    String nextId(final NetworkConnectionImpl connection) {
        logger.logTraceId();
        return networkId.nextId();
    }

    /**
     * Ensure the output stream is a network output stream.
     * 
     * @param outputStream
     *            An <code>OutputStream</code>.
     */
    private void ensureNetworkOutputStream(final OutputStream outputStream) {
        if (NetworkOutputStream.class.isAssignableFrom(outputStream.getClass())) {
            return;
        } else {
            throw new IllegalArgumentException("Illegal output stream.");
        }
    }

    /**
     * Obtain the connection id for the output stream.
     * 
     * @param outputStream
     *            A <code>OutputStream</code>.
     * @return A connection id.
     */
    private String getConnectionId(final OutputStream outputStream) {
        ensureNetworkOutputStream(outputStream);
        return ((NetworkOutputStream) outputStream).getConnectionId();
    }

    /**
     * Obtain a proxy selector.
     * 
     * @return A <code>ProxySelector</code>.
     */
    private ProxySelector getProxySelector() {
        if (null == proxySelector) {
            proxySelector = ProxySelector.getDefault();
        }
        return proxySelector;
    }

    /**
     * Obtain the secure socket factory.
     * 
     * @return A <code>SSLSocketFactory</code>.
     */
    private SSLSocketFactory getSecureSocketFactory() {
        if (null == secureSocketFactory) {
            secureSocketFactory = (SSLSocketFactory) SocketFactory.getSecureInstance();
        }
        return secureSocketFactory;
    }

    /**
     * Create a connection.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>NetworkConnectionImpl</code>.
     * @throws URISyntaxException
     */
    private NetworkConnectionImpl newConnection(final NetworkProtocol protocol,
            final NetworkAddress address) throws URISyntaxException {
        return new NetworkConnectionImpl(this, connectionLogger, protocol,
                address, selectProxies(protocol, address));
    }

    /**
     * Select an appropriate list of proxies for the protocol; and address.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>List<NetworkProxy</code>.
     * @throws URISyntaxException
     */
    private List<NetworkProxy> selectProxies(final NetworkProtocol protocol,
            final NetworkAddress address) throws URISyntaxException {
        final ProxySelector selector = getProxySelector();
        final List<Proxy> javaProxies = selector.select(newURI(protocol, address));
        final List<NetworkProxy> proxies = new ArrayList<NetworkProxy>(javaProxies.size());
        for (final Proxy javaProxy : javaProxies) {
            proxies.add(NetworkUtil.newProxy(javaProxy));
        }
        return proxies;
    }
}

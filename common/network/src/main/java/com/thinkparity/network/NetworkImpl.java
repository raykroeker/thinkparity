/*
 * Created On:  19-Aug-07 2:05:14 PM
 */
package com.thinkparity.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
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

        System.setProperty("networkaddress.cache.ttl", String.valueOf(1));
        System.setProperty("networkaddress.cache.negative.ttl", String.valueOf(1));
    }

    /**
     * Create an address.
     * 
     * @param host
     *            A <code>String</code>.
     * @param port
     *            A <code>Integer</code>.
     * @return A <code>NetworkAddress</code>.
     */
    private static NetworkAddress newAddress(final String host,
            final Integer port) {
        return new NetworkAddress(host, port);
    }

    /**
     * Create a proxy.
     * 
     * @param proxy
     *            A <code>Proxy</code>.
     * @return A <code>NetworkProxy</code>.
     */
    private static NetworkProxy newProxy(final Proxy proxy) {
        if (Proxy.NO_PROXY == proxy) {
            return NetworkProxy.NO_PROXY;
        } else {
            final NetworkProxy.Type type;
            switch (proxy.type()) {
            case HTTP:
                type = NetworkProxy.Type.HTTP;
                break;
            case SOCKS:
                type = NetworkProxy.Type.SOCKS;
                break;
            default:
                throw new IllegalArgumentException("Unexpected proxy type.");
            }
            return new NetworkProxy(type, newAddress(
                    ((InetSocketAddress) proxy.address()).getHostName(),
                    ((InetSocketAddress) proxy.address()).getPort()));
        }
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

    /** An address lookup. */
    private NetworkAddressLookup addressLookup;

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
     * Lookup a socket address for a network address.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>SocketAddress</code>.
     * @throws UnknownHostException
     *             if the network address cannot be resolved
     */
    SocketAddress lookupSocketAddress(final NetworkAddress address)
            throws UnknownHostException {
        if (null == addressLookup) {
            addressLookup = new NetworkAddressLookup(getConfiguration());
        }
        SocketAddress socketAddress = addressLookup.lookup(address);
        if (null == socketAddress) {
            socketAddress = addressLookup.resolve(address);
            if (null == socketAddress) {
                throw new UnknownHostException(address.getHost());
            } else {
                return socketAddress;
            }
        } else {
            return socketAddress;
        }
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
        try {
            final Socket proxySocket = newSocket(connectionId, proxy);
            try {
                proxySocket.connect(lookupSocketAddress(address));
                return new SecureNetworkSocket(connectionId,
                        (SSLSocket) getSecureSocketFactory().createSocket(
                                proxySocket, address.getHost(), address.getPort(),
                                true));
            } catch (final UnknownHostException uhx) {
                throw new NetworkException(uhx);
            } catch (final IOException iox) {
                throw new NetworkException(iox);
            }
        } catch (final UnknownHostException uhx) {
            throw new NetworkException(uhx);
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
    Socket newSocket(final String connectionId, final NetworkProxy proxy)
            throws UnknownHostException {
        logger.logTraceId();
        logger.logVariable("connectionId", connectionId);
        logger.logVariable("proxy", proxy);
        return new NetworkSocket(connectionId, newProxy(proxy));
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
     * Create a proxy.
     * 
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @return A <code>Proxy</code>.
     */
    private Proxy newProxy(final NetworkProxy proxy)
            throws UnknownHostException {
        if (NetworkProxy.NO_PROXY == proxy) {
            return Proxy.NO_PROXY;
        } else {
            final Proxy.Type type;
            switch (proxy.getType()) {
            case HTTP:
                type = Proxy.Type.HTTP;
                break;
            case SOCKS:
                type = Proxy.Type.SOCKS;
                break;
            default:
                throw new IllegalArgumentException("Cannot determine proxy type.");
            }            
            return new Proxy(type, lookupSocketAddress(proxy.getAddress()));
        }
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
            proxies.add(newProxy(javaProxy));
        }
        return proxies;
    }
}

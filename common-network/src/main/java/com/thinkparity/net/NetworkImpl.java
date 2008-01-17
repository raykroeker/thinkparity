/*
 * Created On:  19-Aug-07 2:05:14 PM
 */
package com.thinkparity.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.net.SocketFactory;

import com.thinkparity.net.io.NetworkOutputStream;

/**
 * <b>Title:</b>thinkParity Network Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class NetworkImpl {

    static {
        System.setProperty("networkaddress.cache.ttl", String.valueOf(1));
        System.setProperty("networkaddress.cache.negative.ttl", String.valueOf(1));
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

    /** A secure socket factory. */
    private SSLSocketFactory secureSocketFactory;

    /**
     * Create NetworkImpl.
     *
     */
    NetworkImpl(final Log4JWrapper logger) {
        super();
        this.connectionLogger = new Log4JWrapper("com.thinkparity.net.connection");
        this.connections = new NetworkConnections();
        this.logger = logger;
        this.networkId = new NetworkId();
    }

    /**
     * Disconnect all connections.
     * 
     */
    void disconnect() {
        logger.logTraceId();
        final List<NetworkConnection> connectionList = connections.get();
        for (final NetworkConnection connection : connectionList) {
            connection.disconnect();
        }
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
     * Create a secure socket to the address.
     * 
     * @param connectionId
     *            A <code>String</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>Socket</code>.
     */
    Socket newSecureSocket(final String connectionId,
            final NetworkAddress address) throws NetworkException {
        logger.logTraceId();
        logger.logVariable("connectionId", connectionId);
        logger.logVariable("address", address);
        final Socket proxySocket = newSocket(connectionId);
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
    }

    /**
     * Create a secure socket routed through a tunnel to the address.
     * 
     * @param connectionId
     *            A <code>String</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @param tunnel
     *            A <code>NetworkTunnel</code>.
     * @return A <code>Socket</code>.
     */
    Socket newSecureSocket(final String connectionId,
            final NetworkAddress address, final NetworkTunnel tunnel)
            throws NetworkException {
        logger.logTraceId();
        logger.logVariable("connectionId", connectionId);
        logger.logVariable("address", address);
        logger.logVariable("tunnel", tunnel);
        try {
            return new SecureNetworkSocket(connectionId,
                    (SSLSocket) getSecureSocketFactory().createSocket(
                            tunnel.getSocket(), address.getHost(),
                            address.getPort(), tunnel.isAutoClose()));
        } catch (final UnknownHostException uhx) {
            throw new NetworkException(uhx);
        } catch (final IOException iox) {
            throw new NetworkException(iox);
        }
    }

    /**
     * Create a socket.
     * 
     * @param connectionId
     *            A <code>String</code>.
     * @return A <code>Socket</code>.
     */
    Socket newSocket(final String connectionId) {
        logger.logTraceId();
        logger.logVariable("connectionId", connectionId);
        return new NetworkSocket(connectionId);
    }

    /**
     * Create a socket through a proxy.
     * 
     * @param connectionId
     *            A <code>String</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @return A <code>Socket</code>.
     * @throws UnknownHostException
     *             if the proxy address cannot be resolved
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
                address);
    }

    /**
     * Instantiate a proxy.
     * 
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @return A <code>Proxy</code>.
     * @throws UnknownHostException
     */
    private Proxy newProxy(final NetworkProxy proxy)
            throws UnknownHostException {
        return new Proxy(Proxy.Type.SOCKS, lookupSocketAddress(proxy.getAddress()));
    }
}

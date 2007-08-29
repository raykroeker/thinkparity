/*
 * Created On:  19-Aug-07 2:19:45 PM
 */
package com.thinkparity.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.List;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity Network Connection Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class NetworkConnectionImpl implements NetworkConnection {

    /** The network address. */
    private final NetworkAddress address;

    /** A connected flag. */
    private boolean connected;

    /** A connection id. */
    private final String id;

    /** The input. */
    private InputStream input;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** The network. */
    private final NetworkImpl networkImpl;

    /** The output. */
    private OutputStream output;

    /** The network protocol. */
    private final NetworkProtocol protocol;

    /** The network proxies. */
    private final List<NetworkProxy> proxies;

    /** The network proxy. */
    private NetworkProxy proxy;

    /** The underlying socket connection. */
    private Socket socket;

    /**
     * Create NetworkConnectionImpl.
     * 
     * @param networkImpl
     *            A <code>NetworkImpl</code>.
     * @param logger
     *            A <code>Log4JWrapper</code>.
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @param proxies
     *            A <code>List<NetworkProxy></code>.
     */
    NetworkConnectionImpl(final NetworkImpl networkImpl,
            final Log4JWrapper logger, final NetworkProtocol protocol,
            final NetworkAddress address, final List<NetworkProxy> proxies) {
        super();
        this.id = networkImpl.nextId(this);
        this.logger = logger;
        this.networkImpl = networkImpl;
        this.protocol = protocol;
        this.address = address;
        this.proxies = proxies;
    }

    
    /**
     * @see com.thinkparity.network.NetworkConnection#connect()
     *
     */
    @Override
    public void connect() throws NetworkException {
        logger.logTraceId();
        logger.logInfo("{0} - Connect", getId());
        Exception lastX = null;
        connected = false;
        for (final NetworkProxy proxy : proxies) {
            this.proxy = proxy;
            try {
                connectViaProxy();
                setSocketOptions();
                setSocketStreams();
                logger.logInfo("{0} - Connected", getId());
                connected = true;
                break;
            } catch (final SocketException sx) {
                lastX = sx;
            } catch (final IOException iox) {
                lastX = iox;
            }
        }
        if (false == connected) {
            throw new NetworkException(lastX);
        }        
    }

    /**
     * @see com.thinkparity.network.NetworkConnection#disconnect()
     *
     */
    @Override
    public void disconnect() {
        logger.logTraceId();
        logger.logInfo("{0} - Disconnect", getId());
        ensureConnected();
        try {
            if (Boolean.FALSE == protocol.isSecure()) {
                socket.shutdownInput();
            }
        } catch (final IOException iox) {
            logger.logWarning(iox, "{0} - Error disconnecting.", getId());
        } finally {
            try {
                if (Boolean.FALSE == protocol.isSecure()) {
                    socket.shutdownOutput();
                }
            } catch (final IOException iox) {
                logger.logWarning(iox, "{0} - Error disconnecting.", getId());
            } finally {
                try {
                    socket.close();
                } catch (final IOException iox) {
                    logger.logWarning(iox, "{0} - Error disconnecting.", getId());
                } finally {
                    socket = null;
                    input = null;
                    output = null;
                    logger.logInfo("{0} - Disconnected", getId());
                    connected = false;
                }
            }
        }
    }

    /**
     * @see com.thinkparity.network.NetworkConnection#getAddress()
     *
     */
    public NetworkAddress getAddress() {
        logger.logTraceId();
        return address;
    }

    /**
     * @see com.thinkparity.network.NetworkConnection#getId()
     *
     */
    @Override
    public String getId() {
        logger.logTraceId();
        return id;
    }

    /**
     * @see com.thinkparity.network.NetworkConnection#getSocket()
     *
     */
    @Override
    public Socket getSocket() {
        logger.logTraceId();
        return socket;
    }

    /**
     * @see com.thinkparity.network.NetworkConnection#isConnected()
     *
     */
    @Override
    public Boolean isConnected() {
        logger.logTraceId();
        return connected && socket.isConnected();
    }

    /**
     * @see com.thinkparity.network.NetworkConnection#read(byte[])
     *
     */
    @Override
    public int read(final byte[] buffer) throws NetworkException {
        logger.logTraceId();
        logger.logVariable("buffer", buffer);
        ensureConnected();
        try {
            return input.read(buffer);
        } catch (final IOException iox) {
            throw new NetworkException(iox);
        }
    }

    /**
     * @see com.thinkparity.network.NetworkConnection#write(byte[])
     *
     */
    @Override
    public void write(final byte[] buffer) throws NetworkException {
        logger.logTraceId();
        logger.logVariable("buffer", buffer);
        ensureConnected();
        try {
            write(buffer, true);
        } catch (final IOException iox) {
            throw new NetworkException(iox);
        }
    }

    /**
     * Obtain the protocol.
     * 
     * @return A <code>NetworkProtocol</code>.
     */
    NetworkProtocol getProtocol() {
        logger.logTraceId();
        return protocol;
    }

    /**
     * Obtain the proxy.
     * 
     * @return A <code>NetworkProxy</code>.
     */
    NetworkProxy getProxy() {
        logger.logTraceId();
        return proxy;
    }

    /**
     * Connect via a proxy.
     * 
     * @throws SocketException
     * @throws IOException
     */
    private void connectViaProxy() throws NetworkException, SocketException,
            IOException {
        socket = newSocket();
        if (Boolean.FALSE == protocol.isSecure()) {
            /* secure sockets must already be connected */
            socket.connect(newSocketAddress(), getConnectTimeout());
        }
    }

    /**
     * Ensure the connection is connected; throwing an illegal state error if
     * not.
     * 
     */
    private void ensureConnected() {
        if (false == connected) {
            throw new IllegalStateException(getId() + " - Network connection is not connected.");
        }
    }

    /**
     * Obtain the network configuration.
     * 
     * @return A <code>NetworkConfiguration</code>.
     */
    private NetworkConfiguration getConfiguration() {
        return networkImpl.getConfiguration();
    }

    /**
     * Obtain the connect timeout.
     * 
     * @return An <code>Integer</code>.
     */
    private Integer getConnectTimeout() {
        return getConfiguration().getConnectTimeout(protocol, address, proxy, this);
    }

    /**
     * Create a new instance of an input stream.
     * 
     */
    private void newInput() throws NetworkException {
        try {
            input = socket.getInputStream();
        } catch (final IOException iox) {
            throw new NetworkException(iox);
        }
    }

    /**
     * Create a new instance of an output stream.
     * 
     */
    private void newOutput() throws NetworkException {
        try {
            output = socket.getOutputStream();
        } catch (final IOException iox) {
            throw new NetworkException(iox);
        }
    }

    /**
     * Create a new socket.
     * 
     * @return A <code>Socket</code>.
     */
    private Socket newSocket() throws NetworkException {
        if (protocol.isSecure()) {
            return networkImpl.newSecureSocket(id, proxy, address);
        } else {
            return networkImpl.newSocket(id, proxy);
        }
    }

    /**
     * Create a socket address.
     * 
     * @return A <code>SocketAddress</code>.
     */
    private SocketAddress newSocketAddress() {
        return NetworkUtil.newSocketAddress(address);
    }

    /**
     * Set socket options pulled from the network configuration.
     * 
     * @throws SocketException
     */
    private void setSocketOptions() throws SocketException {
        final NetworkConfiguration configuration = networkImpl.getConfiguration();
        final Integer soLinger = configuration.getSoLinger(protocol, address, proxy, this);
        final boolean linger = -1 < soLinger ? true : false;
        logger.logDebug("{0} - linger:{1}", getId(), linger);
        logger.logDebug("{0} - soLinger:{1}", getId(), soLinger);
        final Integer soTimeout = configuration.getSoTimeout(protocol, address, proxy, this);
        logger.logDebug("{0} - soTimeout:{1}", getId(), soTimeout);
        final Boolean tcpNoDelay = configuration.getTcpNoDelay(protocol, address, proxy, this);
        logger.logDebug("{0} - tcpNoDelay:{1}", getId(), tcpNoDelay);

        socket.setSoLinger(linger, linger ? soLinger : -1);
        socket.setSoTimeout(soTimeout);
        socket.setTcpNoDelay(tcpNoDelay);
    }


    /**
     * Set local references to the socket streams.
     * 
     */
    private void setSocketStreams() throws NetworkException {
        newOutput();
        newInput();
    }

    /**
     * Write to the connection.
     * 
     * @param buffer
     *            A buffer <code>byte[]</code>.
     * @param autoFlush
     *            Whether or not to flush.
     * @throws IOException
     *             if the output cannot be written to
     */
    private void write(final byte[] buffer, final boolean autoFlush)
            throws IOException {
        output.write(buffer);
        if (autoFlush) {
            output.flush();
        }
    }
}

/*
 * Created On:  4-Jun-07 6:30:29 PM
 */
package com.thinkparity.network.protocol.http.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import com.thinkparity.network.Network;
import com.thinkparity.network.NetworkAddress;
import com.thinkparity.network.NetworkConnection;
import com.thinkparity.network.NetworkException;
import com.thinkparity.network.NetworkProtocol;

/**
 * <b>Title:</b>thinkParity Codebase Http Socket Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class SocketFactory implements ProtocolSocketFactory {

    /**
     * Obtain a custom instance of the http socket factory.
     * 
     * @return A <code>HttpSocketFactory</code>.
     */
    public static SocketFactory getInstance() {
        return new SocketFactory(NetworkProtocol.getProtocol("http"));
    }

    /**
     * Create a network address for a host/port pair.
     * 
     * @param host
     *            A <code>String</code>.
     * @param port
     *            A <code>int</code>.
     * @return A <code>NetworkAddress</code>.
     */
    protected static final NetworkAddress newAddress(final String host, final int port) {
        return new NetworkAddress(host, port);
    }

    /** A network. */
    protected final Network network;

    /** A network protocol. */
    protected final NetworkProtocol protocol;

    /**
     * Create SocketFactory.
     *
     */
    protected SocketFactory(final NetworkProtocol protocol) {
        super();
        this.network = new Network();
        this.protocol = protocol;
    }

    /**
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int)
     *
     */
    public Socket createSocket(final String host, final int port)
            throws IOException, UnknownHostException {
        return createSocket(newAddress(host, port));
    }

    /**
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int)
     *
     */
    public Socket createSocket(final String host, final int port,
            final InetAddress localAddress, final int localPort)
            throws IOException, UnknownHostException {
        /* the local address/port are deliberately ignored */
        return createSocket(host, port);
    }

    /**
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int, org.apache.commons.httpclient.params.HttpConnectionParams)
     *
     */
    public Socket createSocket(final String host, final int port,
            final InetAddress localAddress, final int localPort,
            final HttpConnectionParams params) throws IOException,
            UnknownHostException, ConnectTimeoutException {
        /* the local address/port are deliberately ignored */
        /* the params are deliberately ignored */
        return createSocket(host, port);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return getClass() == obj.getClass();
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Obtain a socket connected to the network address.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>Socket</code>.
     */
    private Socket createSocket(final NetworkAddress address) throws SocketException {
        final NetworkConnection connection = newConnection(address);
        try {
            connection.connect();
        } catch (final NetworkException nx) {
            final SocketException sx = new SocketException(nx.getMessage());
            sx.initCause(nx);
            throw sx;
        }
        return connection.getSocket();
    }

    /**
     * Create a new network connection.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>NetworkConnection</code>.
     */
    private NetworkConnection newConnection(final NetworkAddress address) {
        return network.newConnection(protocol, address);
    }
}

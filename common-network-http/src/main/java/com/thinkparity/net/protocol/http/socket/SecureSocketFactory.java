/*
 * Created On:  4-Jun-07 6:30:29 PM
 */
package com.thinkparity.net.protocol.http.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import com.thinkparity.net.NetworkAddress;
import com.thinkparity.net.NetworkConnection;
import com.thinkparity.net.NetworkException;
import com.thinkparity.net.NetworkProtocol;
import com.thinkparity.net.NetworkTunnel;

/**
 * <b>Title:</b>thinkParity Codebase Https Socket Factory<br>
 * <b>Description:</b>A custom https socket factory for use with apache's http
 * client.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SecureSocketFactory extends SocketFactory implements
        SecureProtocolSocketFactory {

    /**
     * Obtain a custom instance of the http socket factory.
     * 
     * @return A <code>HttpSocketFactory</code>.
     */
    public static SecureSocketFactory getInstance() {
        return new SecureSocketFactory(NetworkProtocol.getSecureProtocol("https"));
    }

    /**
     * Create HttpsSocketFactory.
     *
     */
    private SecureSocketFactory(final NetworkProtocol protocol) {
        super(protocol);
    }

    /**
     * @see org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory#createSocket(java.net.Socket, java.lang.String, int, boolean)
     *
     */
    @Override
    public Socket createSocket(final Socket socket, final String host,
            final int port, final boolean autoClose) throws IOException,
            UnknownHostException {
        return newSocket(newTunnel(socket, autoClose), newAddress(host, port));
    }

    /**
     * Instantiate a socket through a tunnel.
     * 
     * @param tunnel
     *            A <code>NetworkTunnel</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>Socket</code>.
     * @throws SocketException
     */
    private Socket newSocket(final NetworkTunnel tunnel,
            final NetworkAddress address) throws SocketException {
        final NetworkConnection connection = network.newConnection(protocol, address);
        try {
            connection.connect(tunnel);
        } catch (final NetworkException nx) {
            final SocketException sx = new SocketException(nx.getMessage());
            sx.initCause(nx);
            throw sx;
        }
        return connection.getSocket();
    }

    /**
     * Instantiate a network tunnel.
     * 
     * @param socket
     *            A <code>Socket</code>.
     * @param autoClose
     *            A <code>boolean</code>.
     * @return A <code>NetworkTunnel</code>.
     */
    private NetworkTunnel newTunnel(final Socket socket, final boolean autoClose) {
        final NetworkTunnel tunnel = new NetworkTunnel();
        tunnel.setAutoClose(autoClose);
        tunnel.setSocket(socket);
        return tunnel;
    }
}

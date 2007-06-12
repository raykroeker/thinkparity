/*
 * Created On:  4-Jun-07 6:30:29 PM
 */
package com.thinkparity.service.client.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * <b>Title:</b>thinkParity Ophelia Web Service Protocol Socket Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class HttpSocketFactory implements ProtocolSocketFactory {

    /** The underlying socket factory. */
    private static final javax.net.SocketFactory SOCKET_FACTORY;

    static {
        final char[] keyStorePassword = "password".toCharArray();
        final String keyStorePath = "security/client_keystore";
        try {
            SOCKET_FACTORY = com.thinkparity.codebase.net.SocketFactory.getSecureInstance(
                    keyStorePath, keyStorePassword, keyStorePath, keyStorePassword);
        } catch (final Exception x) {
            throw new RuntimeException(x);
        }
    }

    /**
     * Create SocketFactory.
     *
     */
    public HttpSocketFactory() {
        super();
    }

    /**
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int)
     *
     */
    public Socket createSocket(final String host, final int port) throws IOException,
            UnknownHostException {
        return SOCKET_FACTORY.createSocket(host, port);
    }

    /**
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int)
     *
     */
    public Socket createSocket(final String host, final int port,
            final InetAddress localAddress, final int localPort)
            throws IOException, UnknownHostException {
        return SOCKET_FACTORY.createSocket(host, port, localAddress, localPort);
    }

    /**
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int, org.apache.commons.httpclient.params.HttpConnectionParams)
     *
     */
    public Socket createSocket(final String host, final int port,
            final InetAddress localAddress, final int localPort,
            final HttpConnectionParams params) throws IOException,
            UnknownHostException, ConnectTimeoutException {
        final Socket socket = createSocket(host, port, localAddress, localPort);
        if (params.isParameterSet(HttpConnectionParams.SO_TIMEOUT)) {
            socket.setSoTimeout(params.getIntParameter(HttpConnectionParams.SO_TIMEOUT, 0));
        }
        if (params.isParameterSet(HttpConnectionParams.TCP_NODELAY)) {
            socket.setTcpNoDelay(params.getBooleanParameter(HttpConnectionParams.TCP_NODELAY, false));
        }
        if (params.isParameterSet(HttpConnectionParams.SO_SNDBUF)) {
            socket.setSendBufferSize(params.getIntParameter(HttpConnectionParams.SO_SNDBUF, 0));
        }
        if (params.isParameterSet(HttpConnectionParams.SO_RCVBUF)) {
            socket.setReceiveBufferSize(params.getIntParameter(HttpConnectionParams.SO_RCVBUF, 0));
        }
        if (params.isParameterSet(HttpConnectionParams.SO_LINGER)) {
            socket.setSoLinger(true, params.getIntParameter(HttpConnectionParams.SO_LINGER, 0));
        }
        return socket;
    }
}

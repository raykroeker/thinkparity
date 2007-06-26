/*
 * Created On:  4-Jun-07 6:30:29 PM
 */
package com.thinkparity.codebase.model.util.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.thinkparity.codebase.net.SocketFactory;

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
public final class HttpSocketFactory implements ProtocolSocketFactory {

    /**
     * Obtain a custom instance of the http socket factory.
     * 
     * @return A <code>HttpSocketFactory</code>.
     */
    public static HttpSocketFactory getInstance() {
        return new HttpSocketFactory(SocketFactory.getSecureInstance());
    }

    /** A socket factory. */
    private final javax.net.SocketFactory socketFactory;

    /**
     * Create SocketFactory.
     *
     */
    private HttpSocketFactory(final javax.net.SocketFactory socketFactory) {
        super();
        this.socketFactory = socketFactory;
    }

    /**
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int)
     *
     */
    public Socket createSocket(final String host, final int port) throws IOException,
            UnknownHostException {
        return socketFactory.createSocket(host, port);
    }

    /**
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int)
     *
     */
    public Socket createSocket(final String host, final int port,
            final InetAddress localAddress, final int localPort)
            throws IOException, UnknownHostException {
        return socketFactory.createSocket(host, port, localAddress, localPort);
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

/*
 * Created On:  22-Aug-07 1:44:26 PM
 */
package com.thinkparity.codebase.model.stream.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpParser;

import com.thinkparity.network.Network;
import com.thinkparity.network.NetworkAddress;
import com.thinkparity.network.NetworkConnection;
import com.thinkparity.network.NetworkException;
import com.thinkparity.network.NetworkProtocol;

/**
 * <b>Title:</b>thinkParity Common Model Stream Http Client Connection<br>
 * <b>Description:</b>An extension of the http connection for the stream
 * component that uses the newtork layer to connect.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class HttpConnection extends
        org.apache.commons.httpclient.HttpConnection {

    /** The network address. */
    private NetworkAddress address;

    /** The network connection. */
    private NetworkConnection connection;

    /** The socket input stream. */
    private InputStream inputStream;

    /** The network. */
    private final Network network;

    /** The socket output stream. */
    private OutputStream outputStream;

    /** The network protocol. */
    private final NetworkProtocol protocol;

    /** The connection socket. */
    private Socket socket;

    /**
     * Create HttpConnection.
     * 
     * @param hostConfiguration
     *            A <code>HostConfiguration</code>.
     */
    public HttpConnection(final HostConfiguration hostConfiguration) {
        super(hostConfiguration);
        this.network = new Network();
        this.protocol = HttpClientUtil.getProtocol(hostConfiguration.getProtocol());
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#close()
     *
     */
    @Override
    public void close() {
        connection.disconnect();
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#flushRequestOutputStream()
     *
     */
    @Override
    public void flushRequestOutputStream() throws IOException {
        assertOpen();
        outputStream.flush();
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#getRequestOutputStream()
     *
     */
    @Override
    public OutputStream getRequestOutputStream() throws IOException,
            IllegalStateException {
        assertOpen();
        return outputStream;
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#getResponseInputStream()
     *
     */
    @Override
    public InputStream getResponseInputStream() throws IOException,
            IllegalStateException {
        assertOpen();
        return inputStream;
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#isResponseAvailable()
     *
     */
    @Override
    public boolean isResponseAvailable() throws IOException {
        if (isOpen) {
            return inputStream.available() > 0;
        } else {
            return false;
        }
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#isResponseAvailable(int)
     *
     */
    @Override
    public boolean isResponseAvailable(final int timeout) throws IOException {
        assertOpen();
        if (inputStream.available() > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @see org.apache.commons.httpclient.HttpConnection#open()
     *
     */
    @Override
    public void open() throws IOException {
        if (!isConnected()) {
            try {
                connect();
            } catch (final NetworkException nx) {
                throw new IOException(nx);
            }
        }
        socket = connection.getSocket();
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        isOpen = true;
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#tunnelCreated()
     *
     */
    @Override
    public void tunnelCreated() throws IllegalStateException, IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#write(byte[], int, int)
     *
     */
    @Override
    public void write(final byte[] data, final int offset, final int length)
            throws IOException, IllegalStateException {
        if (offset < 0) {
            throw new IllegalArgumentException("Array offset may not be negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Array length may not be negative");
        }
        if (offset + length > data.length) {
            throw new IllegalArgumentException("Given offset and length exceed the array length");
        }
        assertOpen();
        outputStream.write(data, offset, length);
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#closeSocketAndStreams()
     *
     */
    @Override
    protected void closeSocketAndStreams() {
        isOpen = false;
        connection.disconnect();
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#isStale()
     *
     */
    @Override
    protected boolean isStale() throws IOException {
        return true;
    }

    /**
     * Establish a network connection.
     * 
     * @throws NetworkException
     */
    private void connect() throws NetworkException {
        connection = network.newConnection(protocol, getAddress());
        connection.connect();
    }

    /**
     * Obtain the network address.
     * 
     * @return A <code>NetworkAddress</code>.
     */
    private NetworkAddress getAddress() {
        if (null == address
                || !getHost().equals(address.getHost())
                || getPort() != address.getPort().intValue()) {
            address = new NetworkAddress(getHost(), getPort());
        }
        return address;
    }

    /**
     * Determine if we are connected to the address.
     * 
     * @return True if we are connected.
     */
    private boolean isConnected() {
        if (null == connection) {
            return false;
        } else {
            if (connection.isConnected()) {
                return connection.getAddress().equals(getAddress());
            } else {
                return false;
            }
        }
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#readLine()
     * @deprecated
     *
     */
    @Override
    @Deprecated
    public String readLine() throws IOException, IllegalStateException {
        assertOpen();
        return HttpParser.readLine(inputStream);
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#readLine(java.lang.String)
     *
     */
    @Override
    public String readLine(final String charset) throws IOException,
            IllegalStateException {
        assertOpen();
        return HttpParser.readLine(inputStream, charset);
    }
}

/*
 * Created On:  16-Aug-07 11:48:57 AM
 */
package com.thinkparity.network;

import java.net.Socket;

/**
 * <b>Title:</b>thinkParity Network Connection<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface NetworkConnection {

    /**
     * Connect.
     * 
     */
    void connect() throws NetworkException;

    /**
     * Disconnect.
     * 
     */
    void disconnect();

    /**
     * Obtain the address.
     * 
     * @return A <code>NetworkAddress</code>.
     */
    NetworkAddress getAddress();

    /**
     * A connection id.
     * 
     * @return A <code>String</code>.
     */
    String getId();

    /**
     * Obtain the underlying socket.
     * 
     * @return A <code>Socket</code>.
     */
    Socket getSocket();

    /**
     * Determine if connected.
     * 
     * @return True if connected.
     */
    Boolean isConnected();

    /**
     * Read from the connection into the buffer.
     * 
     * @param buffer
     *            A buffer <code>byte[]</code>.
     * @return The number of bytes read.
     * @throws NetworkException
     *             if the connection cannot be read from
     */
    int read(byte[] buffer) throws NetworkException;

    /**
     * Write to the connection from the buffer.
     * 
     * @param buffer
     *            A buffer <code>byte[]</code>.
     * @throws NetworkException
     *             if the connection cannot be written to
     */
    void write(byte[] buffer) throws NetworkException;
}

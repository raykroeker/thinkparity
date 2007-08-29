/*
 * Created On:  22-Aug-07 9:50:38 AM
 */
package com.thinkparity.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.Socket;

import com.thinkparity.network.io.NetworkInputStream;
import com.thinkparity.network.io.NetworkOutputStream;

/**
 * <b>Title:</b>thinkParity Network Socket<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class NetworkSocket extends Socket {

    /** The connection id. */
    private final String connectionId;

    /**
     * Create NetworkSocket.
     * 
     * @param connectionId
     *            A connection id <code>String</code>.
     * @param proxy
     *            A <code>Proxy</code>.
     */
    public NetworkSocket(final String connectionId, final Proxy proxy) {
        super(proxy);
        this.connectionId = connectionId;
    }

    /**
     * Obtain the connectionId.
     *
     * @return A <code>String</code>.
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * @see java.net.Socket#getInputStream()
     *
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new NetworkInputStream(connectionId, super.getInputStream());
    }

    /**
     * @see java.net.Socket#getOutputStream()
     *
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        return new NetworkOutputStream(connectionId, super.getOutputStream());
    }
}

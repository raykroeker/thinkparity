/*
 * Created On:  12-Dec-07 6:56:10 PM
 */
package com.thinkparity.net;

import java.net.Socket;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkTunnel {

    /** An auto-close flag. */
    private Boolean autoClose;

    /** A tunnel socket. */
    private Socket socket;

    /**
     * Create NetworkTunnel.
     *
     */
    public NetworkTunnel() {
        this(null, null);
    }

    /**
     * Create NetworkTunnel.
     * 
     * @param socket
     *            A <code>Socket</code>.
     * @param autoClose
     *            A <code>Boolean</code>.
     */
    private NetworkTunnel(final Socket socket, final Boolean autoClose) {
        super();
        this.socket = socket;
        this.autoClose = autoClose;
    }

    /**
     * Obtain the tunnel.
     *
     * @return A <code>Socket</code>.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Obtain the autoClose.
     *
     * @return A <code>Boolean</code>.
     */
    public Boolean isAutoClose() {
        return autoClose;
    }

    /**
     * Set the autoClose.
     *
     * @param autoClose
     *		A <code>Boolean</code>.
     */
    public void setAutoClose(final Boolean autoClose) {
        this.autoClose = autoClose;
    }

    /**
     * Set the socket.
     *
     * @param socket
     *		A <code>Socket</code>.
     */
    public void setSocket(final Socket socket) {
        this.socket = socket;
    }
}

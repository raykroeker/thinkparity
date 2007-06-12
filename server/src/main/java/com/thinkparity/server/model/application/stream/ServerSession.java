/**
 * 
 */
package com.thinkparity.desdemona.model.stream;

import java.net.InetAddress;
import java.nio.charset.Charset;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * <b>Title:</b>thinkParity Desdemona Stream Server Session<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServerSession {

    /** The bandwidth consumed for this session. */
    private Long bandwidth;

    /** The <code>InetAddress</code> used to create the session. */
    private InetAddress inetAddress;

    /** A shared client/server session. */
    private final StreamSession sharedSession;

    /**
     * Create ServerSession.
     * 
     */
    public ServerSession() {
        super();
        this.bandwidth = 0L;
        this.sharedSession = new StreamSession();
    }

    /**
     * @return the bandwidth
     */
    public Long getBandwidth() {
        return bandwidth;
    }

    /**
     * Obtain the session buffer size.
     * 
     * @return The buffer size <code>Integer</code>.
     */
    public Integer getBufferSize() {
        return sharedSession.getBufferSize();
    }

    /**
     * Obtain the session charset (encoding).
     * 
     * @return A <code>Charset</code>.
     */
    public Charset getCharset() {
        return sharedSession.getCharset();
    }

    /**
     * Obtain the client stream session.
     * 
     * @return A <code>StreamSession</code>.
     */
    public StreamSession getClientSession() {
        final StreamSession clientSession = new StreamSession();
        clientSession.setBufferSize(sharedSession.getBufferSize());
        clientSession.setCharset(sharedSession.getCharset());
        clientSession.setId(sharedSession.getId());
        clientSession.setServerHost(sharedSession.getServerHost());
        clientSession.setServerPort(sharedSession.getServerPort());
        return clientSession;
    }

    /**
     * Obtain the session id.
     * 
     * @return A session id <code>String</code>.
     */
    public String getId() {
        return sharedSession.getId();
    }

    /**
     * Obtain the inet address.
     * 
     * @return An <code>InetAddress</code>.
     */
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    /**
     * Obtain the server host.
     * 
     * @return The server host <code>String</code>.
     */
    public String getServerHost() {
        return sharedSession.getServerHost();
    }

    /**
     * Obtain the server port.
     * 
     * @return The server port <code>Integer</code>.
     */
    public Integer getServerPort() {
        return sharedSession.getServerPort();
    }

    /**
     * @param bandwidth the bandwidth to set
     */
    public void setBandwidth(final Long bandwidth) {
        this.bandwidth = bandwidth;
    }

    /**
     * Set the session charset (encoding).
     * 
     * @param bufferSize
     *            A buffer size <code>Integer</code>.
     */
    public void setBufferSize(final Integer bufferSize) {
        sharedSession.setBufferSize(bufferSize);
    }

    /**
     * Set the session charset (encoding).
     * 
     * @param charset
     *            A <code>Charset</code>.
     */
    public void setCharset(final Charset charset) {
        sharedSession.setCharset(charset);
    }

    /**
     * Set the session id.
     * 
     * @param id
     *            A session id <code>String</code>.
     */
    public void setId(final String id) {
        sharedSession.setId(id);
    }

    /**
     * Set the inet address.
     *
     * @param inetAddress
     *		An <code>InetAddress</code>.
     */
    public void setInetAddress(final InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    /**
     * Set the session server host.
     * 
     * @param serverHost
     *            A server host <code>String</code>.
     */
    public void setServerHost(final String serverHost) {
        this.sharedSession.setServerHost(serverHost);
    }

    /**
     * Set the session server port.
     * 
     * @param serverHost
     *            A server port <code>Integer</code>.
     */
    public void setServerPort(final Integer serverPort) {
        this.sharedSession.setServerPort(serverPort);
    }
}

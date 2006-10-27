/**
 * 
 */
package com.thinkparity.desdemona.model.stream;

import java.net.InetAddress;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * @author raymond
 *
 */
public final class ServerSession extends StreamSession {

    /** The bandwidth consumed for this session. */
    private Long bandwidth;

    /** The <code>InetAddress</code> used to create the session. */
    private InetAddress inetAddress;

    /**
     * Create ServerSession.
     * 
     */
    public ServerSession() {
        super();
        this.bandwidth = 0L;
    }

    /**
     * @return the bandwidth
     */
    public Long getBandwidth() {
        return bandwidth;
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
     * @param bandwidth the bandwidth to set
     */
    public void setBandwidth(final Long bandwidth) {
        this.bandwidth = bandwidth;
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
}

/**
 * 
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * @author raymond
 *
 */
public final class ServerSession extends StreamSession {

    /** The bandwidth consumed for this session. */
    private Long bandwidth;

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
     * @param bandwidth the bandwidth to set
     */
    public void setBandwidth(final Long bandwidth) {
        this.bandwidth = bandwidth;
    }
}

/*
 * Created On:  17-Aug-07 9:04:05 AM
 */
package com.thinkparity.desdemona.util;

import java.net.URISyntaxException;

import com.thinkparity.stream.StreamRetryHandler;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * <b>Title:</b>thinkParity Desdemona Util Default Stream Retry Handler<br>
 * <b>Description:</b>Attempt to open a socket connection to the stream
 * host/port. If one cannot be attempted within a finite number of retries; we
 * give up.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultRetryHandler implements StreamRetryHandler {

    /** A period of time to wait between retries. */
    private static final Long PERIOD;

    static {
        // PERIOD - DefaultRetryHandler#<cinit> - Validate - 1s
        PERIOD = Long.valueOf(1L * 1000L);
    }

    /** The current retry attempt. */
    private int retryAttempt;

    /** The maximum number of retry attempts. */
    private final int retryAttempts;

    /**
     * Create DefaultRetryHandler.
     *
     */
    public DefaultRetryHandler(final StreamSession session)
            throws URISyntaxException {
        super();
        this.retryAttempts = 4;
        this.retryAttempt = 0;
    }

    /**
     * @see com.thinkparity.stream.StreamRetryHandler#retry()
     *
     */
    @Override
    public Boolean retry() {
        if (++retryAttempt <= retryAttempts) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @see com.thinkparity.stream.StreamRetryHandler#period()
     *
     */
    @Override
    public Long waitPeriod() {
        return PERIOD;
    }
}

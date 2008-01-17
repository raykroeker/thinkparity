/*
 * Created On:  29-Aug-07 4:07:46 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;


import com.thinkparity.ophelia.model.util.service.ServiceRetryHandler;

import com.thinkparity.stream.StreamRetryHandler;

/**
 * <b>Title:</b>thinKParity Ophelia Model Finite Retry Handler<br>
 * <b>Description:</b>A retry handler that will attempt a retry a finite number
 * of times before giving up.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class FiniteRetryHandler implements ServiceRetryHandler,
        StreamRetryHandler {

    /** A retry wait period. */
    private static final Long PERIOD;

    static {
        // PERIOD - FiniteRetryHandler#<cinit> - Retry - 1s
        PERIOD = Long.valueOf(1L * 1000L);
    }

    /** The current attempt. */
    private int retryAttempt;

    /** The number of attempts. */
    private final int retryAttempts;

    /**
     * Create FiniteRetryHandler.
     *
     */
    public FiniteRetryHandler() {
        super();
        this.retryAttempts = 2;
        this.retryAttempt = 0;
    }

    /**
     * @see com.thinkparity.ophelia.model.util.service.ServiceRetryHandler#retry()
     *
     */
    @Override
    public Boolean retry() {
        if (retryAttempt++ < retryAttempts) {
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

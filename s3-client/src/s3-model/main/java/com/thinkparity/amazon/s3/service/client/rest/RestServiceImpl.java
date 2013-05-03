/*
 * Created On:  19-Jun-07 8:12:35 PM
 */
package com.thinkparity.amazon.s3.service.client.rest;

import com.thinkparity.amazon.s3.service.S3Context;
import com.thinkparity.amazon.s3.service.client.ServiceException;

/**
 * <b>Title:</b>thinkParity Amazon S3 Service Implementation Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class RestServiceImpl {

    /** A service context. */
    protected S3Context context;

    /**
     * Create RestServiceImpl.
     *
     */
    protected RestServiceImpl() {
        super();
    }

    /**
     * Initialize the service.
     * 
     * @param context
     *            A <code>S3Context</code>.
     */
    public void initialize(final S3Context context) {
        this.context = context;
    }

    /**
     * Panic. Throw a runtime error initialized to the given cause; or the cause
     * itself if it is itself a service error.
     * 
     * @param throwable
     *            A <code>Throwable</code>.
     * @return A <code>RuntimeException</code>.
     */
    protected RuntimeException panic(final Throwable throwable) {
        if (throwable.getClass().isAssignableFrom(ServiceException.class)) {
            return (ServiceException) throwable;
        } else {
            return new ServiceException("Could not execute service.", throwable);
        }
    }
}

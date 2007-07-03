/*
 * Created On:  20-Jun-07 10:05:58 AM
 */
package com.thinkparity.amazon.s3.client;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.client.ServiceFactory;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3CommandContext {

    /** A buffer array. */
    private static final byte[] BUFFER_ARRAY;

    /** A buffer lock. */
    private static final Object BUFFER_LOCK;

    static {
        BUFFER_ARRAY = new byte[1024 * 1024 * 2];   // 2MB
        BUFFER_LOCK = new Object();
    }

    /** The amazon s3 authentication. */
    private S3Authentication authentication;

    /**
     * Create S3CommandContext.
     *
     */
    public S3CommandContext() {
        super();
    }

    /**
     * Obtain the authentication.
     * 
     * @return A <code>S3Authentication</code>.
     */
    public S3Authentication getAuthentication() {
        return authentication;
    }

    /**
     * Obtain a buffer array.
     * 
     * @return A <code>byte[]</code>.
     */
    public byte[] getBufferArray(final Object lock) {
        if (BUFFER_LOCK.equals(lock)) {
            return BUFFER_ARRAY;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Obtain a buffer lock.
     * 
     * @return An <code>Object</code>.
     */
    public Object getBufferLock() {
        return BUFFER_LOCK;
    }

    /**
     * Obtain the console.
     * 
     * @return A <code>Console</code>.
     */
    public S3ClientConsole getConsole() {
        return new S3ClientConsole();
    }

    /**
     * Obtain a logger.
     * 
     * @return A <code>Log4JWrapper</code>.
     */
    public Log4JWrapper getLogger() {
        return new Log4JWrapper(StackUtil.getCallerClassName());
    }

    /**
     * Obtain the service factory.
     * 
     * @return A <code>ServiceFactory</code>.
     */
    public ServiceFactory getServiceFactory() {
        return ServiceFactory.getInstance();
    }

    /**
     * Set authentication.
     *
     * @param authentication
     *		A S3Authentication.
     */
    public void setAuthentication(final S3Authentication authentication) {
        this.authentication = authentication;
    }
}

/*
 * Created On:  19-Jun-07 4:03:18 PM
 */
package com.thinkparity.amazon.s3.service.client.rest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RestServiceProxy implements InvocationHandler {

    /** The service implementation. */
    private final RestServiceImpl impl;

    /**
     * Create RestServiceProxy.
     * 
     * @param impl
     *            A <code>RestServiceImpl</code>.
     */
    RestServiceProxy(final RestServiceImpl impl) {
        super();
        this.impl = impl;
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     *
     */
    public Object invoke(final Object proxy, final Method method,
            final Object[] args) throws Throwable {
        try {
            return method.invoke(impl, args);
        } catch (final InvocationTargetException itx) {
            throw itx.getTargetException();
        }
    }
}

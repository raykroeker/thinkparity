/*
 * Created On:  29-Sep-07 5:20:10 PM
 */
package com.thinkparity.adriana.backup.model.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <b>Title:</b>thinkParity Adriana Backup Model Invocation Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ModelInvocationHandler implements InvocationHandler {

    /** The invocation object. */
    private final Object obj;

    /**
     * Create ModelInvocationHandler.
     *
     */
    ModelInvocationHandler(final Object obj) {
        super();
        this.obj = obj;
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     *
     */
    @Override
    public Object invoke(final Object proxy, final Method method,
            final Object[] args) throws Throwable {
        return method.invoke(obj, args);
    }
}

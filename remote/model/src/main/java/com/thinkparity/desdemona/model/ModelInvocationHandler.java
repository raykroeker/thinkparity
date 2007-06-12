/*
 * Created On:  9-Jan-07 1:20:00 PM
 */
package com.thinkparity.desdemona.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Invocation Handler<br>
 * <b>Description:</b>The invocation handler for the dynamic proxies generated
 * by the model factories. The invocation handler will control all transactions
 * as well as interface and parameter logging.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ModelInvocationHandler implements InvocationHandler {

    /** A <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    static {
        LOGGER = new Log4JWrapper(ModelInvocationHandler.class);
    }

    /** The model synchronization lock <code>Object</code>. */
    private final Object lock;

    /** The target <code>AbstractModelImpl</code>. */
    private final AbstractModelImpl model;

    /**
     * Create ModelInvocationHandler.
     *
     */
    ModelInvocationHandler(final Object lock, final AbstractModelImpl model) {
        super();
        this.lock = lock;
        this.model = model;
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[])
     * 
     */
    public Object invoke(final Object proxy, final Method method,
            final Object[] args) throws Throwable {
        LOGGER.logTrace("Invoking method {0} on {1}.", method, model);
        if (null != args && 0 < args.length && LOGGER.isDebugEnabled()) {
            for (int i = 0; i < args.length; i++)
                LOGGER.logDebug("args[{0}]:{1}", i, args[i]);
        }
        ModelInvocationMetrics.begin(method);
        synchronized (lock) {
            try {
                return LOGGER.logVariable("result", method.invoke(model, args));
            } catch (final InvocationTargetException itx) {
                throw itx.getTargetException();
            } catch (final Throwable t) {
                throw t;
            } finally {
                ModelInvocationMetrics.end(method);
            }
        }
    }
}

/*
 * Created On:  16-Aug-07 9:17:27 AM
 */
package com.thinkparity.ophelia.model.util.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.util.service.ServiceProxyMetrics.Context;

import com.thinkparity.network.NetworkException;

/**
 * <b>Title:</b>thinkParity Ophelia Service Client Proxy<br>
 * <b>Description:</b>A proxy invocation handler that implements retry.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceProxy implements InvocationHandler {

    /** The duration to wait between retry attempts. */
    private static final long RETRY_PERIOD;

    static {
        RETRY_PERIOD = 1 * 1000;
    }

    /** An invocation id. */
    private final String id;

    /** The invocation count. */
    private int invocation;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** A service retry handler. */
    private final ServiceRetryHandler retryHandler;

    /** An instance implementing the service method.*/
    private final Object service;

    /**
     * Create ServiceProxy.
     * 
     * @param callback
     *            A <code>ServiceCallback</code>.
     * @param service
     *            An <code>Object</code>.
     */
    ServiceProxy(final ServiceRetryHandler retryHandler, final Object service) {
        super();
        this.retryHandler = retryHandler;
        this.id = String.valueOf(System.currentTimeMillis());
        this.invocation = 0;
        this.logger = new Log4JWrapper(service.getClass());
        this.service = service;
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     *
     */
    @Override
    public Object invoke(final Object proxy, final Method method,
            final Object[] args) throws Throwable {
        while (true) {
            logger.logInfo("{0}:{1} - Begin service proxy invocation.", id,
                    ++invocation);
            final Context context = ServiceProxyMetrics.begin(method, Integer.valueOf(invocation));
            try {
                return method.invoke(service, args);
            } catch (final InvocationTargetException itx) {
                if (NetworkException.isAssignableFrom(itx)) {
                    final NetworkException nx = NetworkException.getNetworkException(itx);
                    logger.logWarning(
                            "{0}:{1} - Service proxy invocation has encountered a network error.  {2}",
                            id, invocation, nx.getMessage());
                    if (retry()) {
                        try {
                            Thread.sleep(RETRY_PERIOD);
                        } catch (final InterruptedException ix) {
                            logger.logWarning("{0}:{1} - Service proxy retry interruped.  {2}",
                                    id, invocation, ix.getMessage());
                        }
                    } else {
                        throw nx;
                    }
                } else {
                    throw itx.getTargetException();
                }
            } catch (final Exception x) {
                throw x;
            } finally {
                ServiceProxyMetrics.end(context);
            }
        }
    }

    /**
     * Determine whether or not we should retry.
     * 
     * @return True if we are going to retry.
     */
    private boolean retry() {
        return retryHandler.retry().booleanValue();
    }
}

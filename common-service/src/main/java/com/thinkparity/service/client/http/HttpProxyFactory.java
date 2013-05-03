/*
 * Created On:  7-Jun-07 10:09:10 AM
 */
package com.thinkparity.service.client.http;

import java.lang.reflect.Proxy;

import com.thinkparity.service.client.Service;
import com.thinkparity.service.client.ServiceProxyFactory;

import com.thinkparity.net.protocol.http.HttpException;

/**
 * <b>Title:</b>thinkParity Service Http Proxy Factory<br>
 * <b>Description:</b>A proxy factory for invoking services over http.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HttpProxyFactory implements ServiceProxyFactory {

    /** A singleton http proxy factory instance. */
    private static HttpProxyFactory instance;

    /**
     * Obtain an http service proxy factory.
     * 
     * @return A <code>ServiceProxyFactory</code>.
     * @throws HttpException
     *             if the http proxy factory cannot be instantiated
     */
    public static ServiceProxyFactory getInstance() throws HttpException {
        if (null == instance) {
            instance = new HttpProxyFactory();
        }
        return instance;
    }

    /** An http service context. */
    private final HttpServiceContext context;

    /**
     * Create HttpProxyFactory.
     *
     */
    private HttpProxyFactory() throws HttpException {
        super();
        this.context = new HttpServiceContext();
        this.context.setContentType("text/xml");
    }

    /**
     * @see com.thinkparity.service.client.ServiceProxyFactory#createProxy(com.thinkparity.service.client.Service)
     *
     */
    public Object createProxy(final Class<?> serviceClass, final Service service) {
        return Proxy.newProxyInstance(getLoader(),
                new Class<?>[] { serviceClass },
                new HttpServiceProxy(context, service));
    }

    /**
     * Obtain the current thread's context class loader.
     * 
     * @return A <code>ClassLoader</code>.
     */
    private ClassLoader getLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}

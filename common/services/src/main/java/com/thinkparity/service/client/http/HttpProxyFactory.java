/*
 * Created On:  7-Jun-07 10:09:10 AM
 */
package com.thinkparity.service.client.http;

import java.lang.reflect.Proxy;

import com.thinkparity.codebase.model.util.http.HttpUtils;

import org.apache.commons.httpclient.HttpClient;

import com.thinkparity.service.client.Constants;
import com.thinkparity.service.client.Service;
import com.thinkparity.service.client.ServiceProxyFactory;

/**
 * <b>Title:</b>thinkParity Service Http Proxy Factory<br>
 * <b>Description:</b>A proxy factory for invoking services over http.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HttpProxyFactory implements ServiceProxyFactory {

    /** A singleton http proxy factory. */
    private static final HttpProxyFactory SINGLETON;

    static {
        SINGLETON = new HttpProxyFactory();
    }

    /**
     * Obtain an http service proxy factory.
     * 
     * @return A <code>ServiceProxyFactory</code>.
     */
    public static ServiceProxyFactory getInstance() {
        return SINGLETON;
    }

    /**
     * Create a new instance of an apache http client.
     * 
     * @return An <code>HtttpClient</code>.
     */
    private static HttpClient newHttpClient() {
        final HttpClient httpClient = HttpUtils.newClient();

        httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(
                Constants.HttpClient.ConnectionManager.MAX_TOTAL_CONNECTIONS);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(
                Constants.HttpClient.ConnectionManager.SO_TIMEOUT);
        httpClient.getHttpConnectionManager().getParams().setTcpNoDelay(
                Constants.HttpClient.ConnectionManager.TCP_NODELAY);

        return httpClient;
    }

    /** An http service context. */
    private final HttpServiceContext context;

    /**
     * Create HttpProxyFactory.
     *
     */
    private HttpProxyFactory() {
        super();
        this.context = new HttpServiceContext();
        this.context.setContentType("text/xml");
        this.context.setHttpClient(newHttpClient());
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

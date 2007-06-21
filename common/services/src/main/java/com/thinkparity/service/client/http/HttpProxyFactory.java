/*
 * Created On:  7-Jun-07 10:09:10 AM
 */
package com.thinkparity.service.client.http;

import java.lang.reflect.Proxy;

import com.thinkparity.codebase.model.session.Environment;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;

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
     * Create a new instance of a http client. Set the connection manager
     * parameters not to perform a stale connection check.
     * 
     * @return An instance of <code>HttpClient</code>.
     */
    private static HttpClient newHttpClient() {
        final HttpClient httpClient = new HttpClient();
        final HttpConnectionManager httpConnectionManager = httpClient.getHttpConnectionManager();
        final HttpConnectionManagerParams httpConnectionManagerParams = httpConnectionManager.getParams();
        httpConnectionManagerParams.setStaleCheckingEnabled(false);
        httpConnectionManager.setParams(httpConnectionManagerParams);
        httpClient.setHttpConnectionManager(httpConnectionManager);
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
        final Environment environment = Environment.valueOf(System.getProperty("thinkparity.environment"));
        Protocol.registerProtocol("https", new Protocol("https",
                new HttpSocketFactory(), environment.getServicePort()));
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

/*
 * Created On:  7-Jun-07 10:09:10 AM
 */
package com.thinkparity.service.client.http;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

import com.thinkparity.network.protocol.http.Http;
import com.thinkparity.network.protocol.http.HttpException;

import com.thinkparity.service.client.Constants;
import com.thinkparity.service.client.Service;
import com.thinkparity.service.client.ServiceProxyFactory;

import org.apache.commons.httpclient.ProxyHost;

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

        final Http http = new Http();
        http.getConfiguration().setMaxTotalConnections(http, Constants.Http.MAX_TOTAL_CONNECTIONS);
        http.getConfiguration().setSoTimeout(http, Constants.Http.SO_TIMEOUT);
        http.getConfiguration().setTcpNoDelay(http, Constants.Http.TCP_NO_DELAY);
        http.getConfiguration().setProxyHost(http, newProxyHost());
        this.context.setHttpClient(http.newClient());
    }

    /**
     * Instantiate a proxy host for a uri.
     * 
     * @param uri
     *            A <code>URI</code>.
     * @return A <code>ProxyHost</code>.
     */
    private static ProxyHost newProxyHost() {
        final URI uri = URI.create(HttpServiceContext.getBaseURI());
        final List<java.net.Proxy> proxyList = ProxySelector.getDefault().select(uri);
        if (proxyList.isEmpty()) {
            return null;
        } else {
            final java.net.Proxy proxy = proxyList.get(0);
            if (java.net.Proxy.NO_PROXY == proxy || java.net.Proxy.Type.DIRECT == proxy.type()) {
                return null;
            } else {
                final InetSocketAddress address = (InetSocketAddress) proxy.address();
                return new ProxyHost(address.getHostName(), address.getPort());
            }
        }
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

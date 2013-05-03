/*
 * Created On:  19-Jun-07 3:48:21 PM
 */
package com.thinkparity.amazon.s3.service.client.rest;

import java.lang.reflect.Proxy;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import com.thinkparity.amazon.s3.service.S3Context;
import com.thinkparity.amazon.s3.service.client.ServiceException;
import com.thinkparity.amazon.s3.service.client.ServiceProxyFactory;


/**
 * <b>Title:</b>thinkParity Amazon S3 Service Rest Proxy Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RestProxyFactory implements ServiceProxyFactory {

    /** A singleton rest proxy factory. */
    private static final RestProxyFactory SINGLETON;

    static {
        SINGLETON = new RestProxyFactory();
    }

    /**
     * Obtain a rest service proxy factory.
     * 
     * @return A <code>ServiceProxyFactory</code>.
     */
    public static ServiceProxyFactory getInstance() {
        return SINGLETON;
    }

    /** An amazon s3 context. */
    private final S3Context context;

    /**
     * Create RestProxyFactory.
     *
     */
    private RestProxyFactory() {
        super();
        this.context = new S3Context();
        this.context.setHttpClient(newHttpClient());
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

    /**
     * @see com.thinkparity.amazon.s3.service.client.ServiceProxyFactory#createProxy(java.lang.Class, com.thinkparity.amazon.s3.service.client.Service)
     *
     */
    public Object createProxy(final Class<?> serviceClass, final Class<?> implClass) {
        try {
            final RestServiceImpl impl = (RestServiceImpl) implClass.newInstance();
            impl.initialize(context);
            return Proxy.newProxyInstance(getLoader(),
                    new Class<?>[] { serviceClass },
                    new RestServiceProxy(impl));
        } catch (final InstantiationException ix) {
            throw new ServiceException("Could not create proxy.", ix);
        } catch (final IllegalAccessException iax) {
            throw new ServiceException("Could not create proxy.", iax);
        }
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

/*
 * Created On:  18-Oct-07 12:49:37 PM
 */
package com.thinkparity.stream;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

import com.thinkparity.network.protocol.http.Http;
import com.thinkparity.network.protocol.http.HttpException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.ProxyHost;

import com.thinkparity.stream.httpclient.HttpConnectionManager;

/**
 * <b>Title:</b>thinkParity Stream Configuration<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamConfiguration {

    /**
     * Instantiate a proxy host for a uri.
     * 
     * @param uri
     *            A <code>URI</code>.
     * @return A <code>ProxyHost</code>.
     */
    private static ProxyHost newProxyHost(final URI uri) {
        final List<Proxy> proxyList = ProxySelector.getDefault().select(uri);
        if (proxyList.isEmpty()) {
            return null;
        } else {
            final Proxy proxy = proxyList.get(0);
            if (Proxy.NO_PROXY == proxy || Proxy.Type.DIRECT == proxy.type()) {
                return null;
            } else {
                final InetSocketAddress address = (InetSocketAddress) proxy.address();
                return new ProxyHost(address.getHostName(), address.getPort());
            }
        }
    }

    /** An http client. */
    private HttpClient httpClient;

    /**
     * Create StreamConfiguration.
     *
     */
    public StreamConfiguration() {
        super();
    }

    /**
     * Obtain the http client.
     * 
     * @return An <code>HttpClient</code>.
     * @throws HttpException
     *             if the client cannot be instantiated
     */
    public HttpClient getHttpClient(final URI uri) throws HttpException {
        if (null == httpClient) {
            final Http http = new Http();
            http.getConfiguration().setConnectionManager(http, new HttpConnectionManager());
            http.getConfiguration().setMaxTotalConnections(http, 3);
            http.getConfiguration().setSoTimeout(http, 7 * 1000);
            http.getConfiguration().setProxyHost(http, newProxyHost(uri));
            httpClient = http.newClient();
        }
        return httpClient;
    }

    /**
     * Set the http client.
     * 
     * @param httpClient
     *            A <code>HttpClient</code>.
     */
    public void setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}

/*
 * Created On:  18-Oct-07 12:49:37 PM
 */
package com.thinkparity.stream;

import org.apache.commons.httpclient.HttpClient;

import com.thinkparity.net.protocol.http.Http;
import com.thinkparity.net.protocol.http.HttpException;
import com.thinkparity.stream.httpclient.HttpConnectionManager;

/**
 * <b>Title:</b>thinkParity Stream Configuration<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamConfiguration {

    /** An instance of http. */
    private final Http http;

    /** An http client. */
    private HttpClient httpClient;

    /**
     * Create StreamConfiguration.
     *
     */
    public StreamConfiguration() {
        super();
        this.http = new Http();
        this.http.getConfiguration().setConnectionManager(http, new HttpConnectionManager());
        this.http.getConfiguration().setMaxTotalConnections(http, 3);
        this.http.getConfiguration().setSoTimeout(http, 7 * 1000);
    }

    /**
     * Obtain the http client.
     * 
     * @return An <code>HttpClient</code>.
     * @throws HttpException
     *             if the client cannot be instantiated
     */
    public HttpClient getHttpClient() throws HttpException {
        if (null == httpClient) {
            /* first call to get client */
            httpClient = newHttpClient();
        } else {
            /* check for dirty http configuration; and recreate as needed */
            if (http.getConfiguration().isDirty(http)) {
                httpClient = newHttpClient();
            }
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

    /**
     * Instantiate an http client.
     * @return An <code>HttpClient</code>.
     */
    private HttpClient newHttpClient() throws HttpException {
        return http.newClient();
    }
}

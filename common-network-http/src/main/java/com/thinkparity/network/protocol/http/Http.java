/*
 * Created On:  14-Dec-07 1:08:44 PM
 */
package com.thinkparity.network.protocol.http;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.apache.commons.httpclient.HttpClient;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Http {

    /** An implementation. */
    private final HttpImpl impl;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /**
     * Create Http.
     *
     */
    public Http() {
        super();
        this.logger = new Log4JWrapper("com.thinkparity.network.protocol.http");
        this.impl = new HttpImpl(this, logger);
    }

    /**
     * Obtain the http configuration.
     * 
     * @return A <code>HttpConfiguration</code>.
     */
    public HttpConfiguration getConfiguration() {
        return impl.getConfiguration();
    }
    
    /**
     * Instantiate an http client.
     * 
     * @return A <code>HttpClient</code>.
     * @throws HttpException
     *             if the http client cannot be instantiated
     */
    public HttpClient newClient() throws HttpException {
        return impl.newClient();
    }
}

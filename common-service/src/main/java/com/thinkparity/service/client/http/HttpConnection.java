/*
 * Created On:  2-Jul-07 10:22:04 PM
 */
package com.thinkparity.service.client.http;

import org.apache.commons.httpclient.HostConfiguration;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HttpConnection extends
        org.apache.commons.httpclient.HttpConnection {

    /**
     * Create HttpConnection.
     *
     * @param hostConfiguration
     */
    public HttpConnection(final HostConfiguration hostConfiguration) {
        super(hostConfiguration);
    }

    /**
     * @see org.apache.commons.httpclient.HttpConnection#close()
     *
     */
    @Override
    public void close() {
        isOpen = false;
    }
}

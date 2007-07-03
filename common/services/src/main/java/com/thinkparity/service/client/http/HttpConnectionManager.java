/*
 * Created On:  2-Jul-07 10:20:33 PM
 */
package com.thinkparity.service.client.http;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HttpConnectionManager extends SimpleHttpConnectionManager {

    /**
     * Create HttpConnectionManager.
     *
     */
    public HttpConnectionManager() {
        super();
    }

    /**
     * @see org.apache.commons.httpclient.SimpleHttpConnectionManager#getConnectionWithTimeout(org.apache.commons.httpclient.HostConfiguration, long)
     *
     */
    @Override
    public org.apache.commons.httpclient.HttpConnection getConnectionWithTimeout(
            final HostConfiguration hostConfiguration, final long timeout) {
        if (null == httpConnection || !httpConnection.isOpen()) {
            httpConnection = new HttpConnection(hostConfiguration);
            httpConnection.setHttpConnectionManager(this);
            httpConnection.getParams().setDefaults(getParams());
        }
        return super.getConnectionWithTimeout(hostConfiguration, timeout);
    }
}

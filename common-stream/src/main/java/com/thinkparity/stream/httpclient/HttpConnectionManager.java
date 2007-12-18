/*
 * Created On:  22-Aug-07 1:34:36 PM
 */
package com.thinkparity.stream.httpclient;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;

/**
 * <b>Title:</b>thinkParity Common Model Stream Http Client Connection Manager<br>
 * <b>Description:</b>A stream client http connection manager.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class HttpConnectionManager extends SimpleHttpConnectionManager {

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
    public HttpConnection getConnectionWithTimeout(
            final HostConfiguration hostConfiguration, final long timeout) {
        if (null == httpConnection || !httpConnection.isOpen()) {
            httpConnection = new com.thinkparity.stream.httpclient.HttpConnection(hostConfiguration);
            httpConnection.setHttpConnectionManager(this);
            httpConnection.getParams().setDefaults(getParams());
        }
        return super.getConnectionWithTimeout(hostConfiguration, timeout);
    }
}
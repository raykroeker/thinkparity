/*
 * Created On:  22-Aug-07 1:49:12 PM
 */
package com.thinkparity.stream.httpclient;

import org.apache.commons.httpclient.protocol.Protocol;

import com.thinkparity.net.NetworkProtocol;


/**
 * <b>Title:</b>thinkParity Common Model Stream Http Client Util<br>
 * <b>Description:</b>An http client utility for the stream model code.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class HttpClientUtil {

    /**
     * Obtain the network protocol for the http client protocol.
     * 
     * @param protocol
     *            A <code>Protocol</code>.
     * @return A <code>NetworkProtocol</code>.
     */
    static NetworkProtocol getProtocol(final Protocol protocol) {
        if ("https".equals(protocol.getScheme())) {
            return NetworkProtocol.getSecureProtocol(protocol.getScheme());
        } else {
            return NetworkProtocol.getProtocol(protocol.getScheme());
        }
    }

    /**
     * Create HttpClientUtil.
     *
     */
    private HttpClientUtil() {
        super();
    }
}

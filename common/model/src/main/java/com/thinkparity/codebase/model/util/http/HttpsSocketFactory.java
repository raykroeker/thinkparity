/*
 * Created On:  4-Jun-07 6:30:29 PM
 */
package com.thinkparity.codebase.model.util.http;

import com.thinkparity.network.NetworkProtocol;

/**
 * <b>Title:</b>thinkParity Codebase Https Socket Factory<br>
 * <b>Description:</b>A custom https socket factory for use with apache's http
 * client.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HttpsSocketFactory extends HttpSocketFactory {

    /**
     * Obtain a custom instance of the http socket factory.
     * 
     * @return A <code>HttpSocketFactory</code>.
     */
    public static HttpsSocketFactory getInstance() {
        return new HttpsSocketFactory(NetworkProtocol.getSecureProtocol("https"));
    }

    /**
     * Create HttpsSocketFactory.
     *
     */
    private HttpsSocketFactory(final NetworkProtocol protocol) {
        super(protocol);
    }
}

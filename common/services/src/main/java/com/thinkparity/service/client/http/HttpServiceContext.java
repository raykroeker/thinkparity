/*
 * Created On:  7-Jun-07 10:01:51 AM
 */
package com.thinkparity.service.client.http;

import com.thinkparity.codebase.model.session.Environment;

import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.protocol.Protocol;

import com.thinkparity.service.client.ServiceContext;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class HttpServiceContext implements ServiceContext {

    /** The message format pattern for the service urls. */
    private static final String URL_PATTERN;

    static {
        final Environment environment = Environment.valueOf(System.getProperty("thinkparity.environment"));
        Protocol.registerProtocol("https", new Protocol("https",
                new HttpSocketFactory(), environment.getServicePort()));
        URL_PATTERN = new StringBuilder(32)
            .append("https://").append(environment.getServiceHost()).append(':')
            .append(environment.getServicePort()).append("/services/{0}")
            .toString();
    }

    /** The http content type. */
    private String contentType;

    /** The http connection. */
    private HttpConnection httpConnection;

    /** The http state. */
    private HttpState httpState;

    /**
     * Create HttpServiceContext.
     *
     */
    HttpServiceContext() {
        super();
    }

    /**
     * Obtain contentType.
     *
     * @return A String.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Obtain httpConnection.
     *
     * @return A HttpConnection.
     */
    public HttpConnection getHttpConnection() {
        return httpConnection;
    }

    /**
     * Obtain httpState.
     *
     * @return A HttpState.
     */
    public HttpState getHttpState() {
        return httpState;
    }

    /**
     * Obtain urlPattern.
     *
     * @return A String.
     */
    public String getURLPattern() {
        return URL_PATTERN;
    }

    /**
     * Set contentType.
     *
     * @param contentType
     *		A String.
     */
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    /**
     * Set httpConnection.
     *
     * @param httpConnection
     *		A HttpConnection.
     */
    public void setHttpConnection(final HttpConnection httpConnection) {
        this.httpConnection = httpConnection;
    }

    /**
     * Set httpState.
     *
     * @param httpState
     *		A HttpState.
     */
    public void setHttpState(final HttpState httpState) {
        this.httpState = httpState;
    }
}

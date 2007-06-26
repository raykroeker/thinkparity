/*
 * Created On:  7-Jun-07 10:01:51 AM
 */
package com.thinkparity.service.client.http;

import com.thinkparity.codebase.model.session.Environment;

import org.apache.commons.httpclient.HttpClient;

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
        final Environment environment = Environment.valueOf(
                System.getProperty("thinkparity.environment"));
        URL_PATTERN = new StringBuilder(32)
            .append("https://").append(environment.getServiceHost()).append(':')
            .append(environment.getServicePort()).append("/tps/service/{0}")
            .toString();
    }

    /** The http content type. */
    private String contentType;

    /** The http client. */
    private HttpClient httpClient;

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
     * Obtain httpClient.
     *
     * @return A HttpClient.
     */
    public HttpClient getHttpClient() {
        return httpClient;
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
     * Set httpClient.
     *
     * @param httpClient
     *		A HttpClient.
     */
    public void setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}

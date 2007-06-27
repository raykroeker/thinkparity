/*
 * Created On:  7-Jun-07 10:01:51 AM
 */
package com.thinkparity.service.client.http;

import java.io.IOException;
import java.io.Writer;

import com.thinkparity.codebase.log4j.Log4JWrapper;

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

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** A log4j writer. */
    private final Log4JWriter logWriter;

    /**
     * Create HttpServiceContext.
     *
     */
    HttpServiceContext() {
        super();
        this.logger = new Log4JWrapper("SERVICE_DEBUGGER");
        this.logWriter = new Log4JWriter();
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
     * Obtain logWriter.
     *
     * @return A Log4JWriter.
     */
    public Log4JWriter getLogWriter() {
        return logWriter;
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

    /** <b>Title:</b>Http Service Context Log4J Writer<br> */
    private class Log4JWriter extends Writer {

        /** A buffer to log. */
        private final StringBuilder buffer = new StringBuilder();

        /**
         * @see java.io.Writer#close()
         *
         */
        @Override
        public void close() throws IOException {
            /* nothing to do */
        }

        /**
         * @see java.io.Writer#flush()
         *
         */
        @Override
        public void flush() throws IOException {
            if (logger.isDebugEnabled()) {
                logger.logDebug(buffer.toString());
                buffer.setLength(0);
            }
        }

        /**
         * @see java.io.Writer#write(char[], int, int)
         *
         */
        @Override
        public void write(final char[] cbuf, final int off, final int len)
                throws IOException {
            if (logger.isDebugEnabled()) {
                for (int i = 0; i < len; i++)
                    buffer.append(cbuf[off + i]);
            }
        }
    }
}

/*
 * Created On:  25-Jun-07 9:56:30 AM
 */
package com.thinkparity.codebase.model.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.http.HttpUtils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamUtils {

    /** A bytes format. */
    private static final BytesFormat BYTES_FORMAT;

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    static {
        BYTES_FORMAT = new BytesFormat();
        LOGGER = new Log4JWrapper(StreamUtils.class);
    }

    /** An http client. */
    private final HttpClient httpClient;

    /**
     * Create StreamUtils.
     *
     */
    StreamUtils() {
        super();
        this.httpClient = HttpUtils.newClient();
    }

    /**
     * Execute the method.
     * 
     * @param method
     *            A <code>HttpMethod</code>.
     * @return An http status code.
     */
    int execute(final HttpMethod method) throws IOException {
        httpClient.executeMethod(method);
        return method.getStatusCode();
    }

    /**
     * Log the receipt of a chunk by the monitor.
     * 
     * @param monitor
     *            The <code>StreamMonitor</code>.
     * @param chunkSize
     *            A number of bytes.
     */
    void logMonitorChunkReceived(final StreamMonitor monitor,
            final int chunkSize) {
        LOGGER.logDebug("Stream monitor \"{0}\" sent {1}.",
                null == monitor ? "unknown" : monitor.getName(),
                BYTES_FORMAT.format(new Long(chunkSize)));
    }

    /**
     * Log a monitor error.
     * 
     * @param monitor
     *            The <code>StreamMonitor</code>.
     * @param chunkSize
     *            A number of bytes.
     */
    void logMonitorChunkSent(final StreamMonitor monitor, final int chunkSize) {
        LOGGER.logDebug("Stream monitor \"{0}\" sent {1}.",
                null == monitor ? "unknown" : monitor.getName(),
                BYTES_FORMAT.format(new Long(chunkSize)));
    }

    /**
     * Log a monitor error.
     * 
     * @param cause
     *            The error cause <code>Throwable</code>.
     * @param monitor
     *            The <code>StreamMonitor</code>.
     */
    void logMonitorError(final StreamMonitor monitor, final Throwable cause) {
        LOGGER.logWarning(cause,
                "An unexpected error has occured in stream monitor \"{0}\".",
                null == monitor ? "unknown" : monitor.getName());
    }

    /**
     * Set headers within the method.
     * 
     * @param method
     *            A <code>HttpMethod</code>.
     * @param headers
     *            A <code>Map<String, String></code>.
     */
    void setHeaders(final HttpMethod method, final Map<String, String> headers) {
        for (final Entry<String, String> header : headers.entrySet()) {
            method.setRequestHeader(header.getKey(), header.getValue());
        }
    }
    
    /**
     * Write the error to the system error stream.
     * 
     * @param method
     *            A <code>HttpMethod</code>.
     */
    void writeError(final HttpMethod method) throws IOException {
        final InputStream errorStream = method.getResponseBodyAsStream();
        StreamUtil.copy(errorStream, System.err);
    }
}

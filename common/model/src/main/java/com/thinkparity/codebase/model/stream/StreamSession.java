/*
 * Created On: Sun Oct 22 2006 10:28 PDT
 */
package com.thinkparity.codebase.model.stream;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>Title:</b>thinkParity Stream Session<br>
 * <b>Description:</b>Provides per-stream meta information for
 * uploading/downloading thinkParity streams via http.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamSession {

    /** The transfer buffer size. */
    private Integer bufferSize;

    /** The http headers. */
    private final Map<String, String> headers;

    /** The http port. */
    private Integer port;

    /** Number of times to retry upon failure. */
    private Integer retryAttempts;

    /** The http uri. */
    private String uri;

    /**
     * Create StreamSession.
     *
     */
    public StreamSession() {
        super();
        this.headers = new HashMap<String, String>();
    }

    /**
     * Obtain the buffer size.
     * 
     * @return A size <code>Integer</code>.
     */
    public Integer getBufferSize() {
        return bufferSize;
    }

    /**
     * Obtain headers.
     *
     * @return A Map<String,String>.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Obtain port.
     *
     * @return A Integer.
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Obtain retryAttempts.
     *
     * @return A Integer.
     */
    public Integer getRetryAttempts() {
        return retryAttempts;
    }

    /**
     * Obtain the http uri.
     * 
     * @return A uri <code>String</code>.
     */
    public String getURI() {
        return uri;
    }

    /**
     * Set the buffer size.
     * 
     * @param bufferSize
     *            A size <code>Integer</code>.
     */
    public void setBufferSize(final Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * Set the http headers.
     *
     * @param headers
     *		A <code>Map<String,String></code>.
     */
    public void setHeaders(final Map<String, String> headers) {
        this.headers.clear();
        this.headers.putAll(headers);
    }

    /**
     * Set port.
     *
     * @param port
     *		A Integer.
     */
    public void setPort(final Integer port) {
        this.port = port;
    }

    /**
     * Set retryAttempts.
     *
     * @param retryAttempts
     *		A Integer.
     */
    public void setRetryAttempts(final Integer retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    /**
     * Set the http uri.
     *
     * @param uri
     *		A <code>String</code>.
     */
    public void setURI(final String uri) {
        this.uri = uri;
    }
}
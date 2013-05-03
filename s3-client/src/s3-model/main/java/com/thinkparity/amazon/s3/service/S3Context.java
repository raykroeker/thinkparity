/*
 * Created On:  20-Jun-07 8:37:39 AM
 */
package com.thinkparity.amazon.s3.service;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.thinkparity.codebase.StringUtil;

import org.apache.commons.httpclient.HttpClient;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3Context {

    /** A request uri pattern. */
    private static final String BASE_URI;

    /** The xml charset (encoding). */
    private static final Charset CHARSET;

    /** The http content type. */
    private static final String REQUEST_CONTENT_TYPE;

    static {
        BASE_URI = "https://s3.amazonaws.com";

        CHARSET = StringUtil.Charset.UTF_8.getCharset();

        REQUEST_CONTENT_TYPE = "text/xml";
    }

    /** A buffer. */
    private ByteBuffer buffer;

    /** A buffer array. */
    private byte[] bufferArray;

    /** A buffer lock. */
    private Object bufferLock;

    /** An apache http client. */
    private HttpClient httpClient;

    /**
     * Create S3Context.
     *
     */
    public S3Context() {
        super();
    }

    /**
     * Obtain the base uri.
     * 
     * @return A uri <code>String</code>.
     */
    public String getBaseURI() {
        return BASE_URI;
    }

    /**
     * Obtain a buffer.
     * 
     * @param lock
     *            A buffer lock <code>Object</code>.
     * @return A <code>ByteBuffer</code>.
     */
    public ByteBuffer getBuffer(final Object lock) {
        validateBufferLock(lock);
        if (null == buffer) {
            buffer = ByteBuffer.allocateDirect(1024 * 1024 * 2);
        }
        return buffer;
    }

    /**
     * Obtain a buffer array.
     * 
     * @param lock
     *            A buffer lock <code>Object</code>.
     * @return A <code>byte[]</code>.
     */
    public byte[] getBufferArray(final Object lock) {
        validateBufferLock(lock);
        if (null == bufferArray) {
            bufferArray = new byte[1024 * 1024 * 2];
        }
        return bufferArray;
    }

    /**
     * Obtain the xml charset (encoding).
     * 
     * @return A <code>Charset</code>.
     */
    public Charset getCharset() {
        return CHARSET;
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
     * Obtain a request content type.
     * 
     * @return A content type <code>String</code>.
     */
    public String getRequestContentType() {
        return REQUEST_CONTENT_TYPE;
    }

    /**
     * Obtain the buffer lock.
     * 
     * @return A buffer lock <code>Object</code>.
     */
    public Object lockBuffer() {
        if (null != bufferLock)
            throw new RuntimeException("Buffer has already been locked.");
        this.bufferLock = new Object();
        return this.bufferLock;
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

    /**
     * Unlock the buffer.
     * @param lock A buffer lock <code>Object</code>.
     */
    public void unlockBuffer(final Object lock) {
        if (null == bufferLock)
            throw new RuntimeException("Buffer has not been locked.");
        if (!bufferLock.equals(lock))
            throw new IllegalArgumentException("Illegal buffer lock.");
        this.bufferLock = null;
    }

    /**
     * Validate a buffer lock.
     * 
     * @param lock
     *            A buffer lock <code>Object</code>.
     */
    private void validateBufferLock(final Object lock) {
        if (null == lock)
            throw new IllegalArgumentException("Cannot use null lock.");
        if (null == bufferLock)
            throw new IllegalArgumentException("Buffer has not been locked.");
        if (!bufferLock.equals(lock))
            throw new IllegalArgumentException("Invalid buffer lock.");
    }
}

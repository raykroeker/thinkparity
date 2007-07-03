/*
 * Created On:  22-Jun-07 3:09:30 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.http;

import java.text.ParseException;

import org.apache.commons.httpclient.Header;

import com.thinkparity.amazon.s3.service.object.S3Object;
import com.thinkparity.amazon.s3.service.object.S3ObjectContentType;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3ObjectHeaderReader implements HeaderReader<S3Object> {

    /** The headers to read. */
    private Header[] headers;

    /** A result. */
    private S3Object result;

    /** A header reader utils. */
    private final HeaderReaderUtils utils;

    /**
     * Create S3ObjectHeaderReader.
     *
     */
    public S3ObjectHeaderReader() {
        super();
        this.utils = new HeaderReaderUtils();
    }

    /**
     * @see com.thinkparity.amazon.s3.service.client.rest.http.HeaderReader#read(org.apache.commons.httpclient.Header[], java.lang.Object)
     *
     */
    public void read(final Header[] headers, final S3Object result) {
        this.headers = headers;
        this.result = result;
        read();
    }

    /**
     * Read the headers.
     *
     */
    private void read() {
        result.setETag(utils.find(headers, "ETag").getValue());
        try {
            result.setLastModified(utils.parseDate(utils.find(headers, "Last-Modified")));
        } catch (final ParseException px) {
            throw new RuntimeException("Could not parse last-modified.", px);
        }
        result.setSize(Long.valueOf(utils.find(headers, "Content-Length").getValue()));
        result.setType(S3ObjectContentType.fromValue(utils.find(headers, "Content-Type").getValue()));
    }
}

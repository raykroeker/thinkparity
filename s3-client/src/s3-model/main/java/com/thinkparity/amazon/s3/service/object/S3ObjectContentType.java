/*
 * Created On:  22-Jun-07 11:03:21 AM
 */
package com.thinkparity.amazon.s3.service.object;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity Amazon S3 Service Object Content Type<br>
 * <b>Description:</b> <br>
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.17">RFC
 * 2616 - Section 14.17 - Content Type</a><br>
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7">RFC
 * 2616 - Section 3.7 - Media Types</a><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum S3ObjectContentType {

    /** binary/octet-stream */
    BINARY_OCTET_STREAM("binary/octet-stream"),

    /** text/plain */
    TEXT_PLAIN("text/plain");

    /**
     * Obtain the s3 object content type from it's value.
     * 
     * @param value
     *            A value <code>String</code>.
     * @return A <code>S3ObjectContentType</code>.
     */
    public static S3ObjectContentType fromValue(final String value) {
        if (BINARY_OCTET_STREAM.getValue().equals(value)) {
            return BINARY_OCTET_STREAM;
        } else if (TEXT_PLAIN.getValue().equals(value)) {
            return TEXT_PLAIN;
        } else {
            throw Assert.createUnreachable("Unknown s3 object content type.");
        }
    }

    /** The content type header value. */
    private String value;

    /**
     * Create S3ObjectContentType.
     * 
     * @param value
     *            The content type header value.
     */
    private S3ObjectContentType(final String value) {
        this.value = value;
    }

    /**
     * Obtain the content type header value.
     * 
     * @return The header value <code>String</code>.
     */
    public String getValue() {
        return value;
    }
}

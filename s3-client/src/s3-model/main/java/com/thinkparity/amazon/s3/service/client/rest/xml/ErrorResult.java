/*
 * Created On:  21-Jun-07 3:20:24 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.xml;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ErrorResult {

    /** The error code. */
    private String code;

    /** The error message. */
    private String message;

    /** The request id. */
    private String requestId;

    /** The resource. */
    private String resource;

    /**
     * Create ErrorResult.
     *
     */
    public ErrorResult() {
        super();
    }

    /**
     * Obtain code.
     *
     * @return A String.
     */
    public String getCode() {
        return code;
    }

    /**
     * Obtain message.
     *
     * @return A String.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Obtain resourceId.
     *
     * @return A String.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Obtain resource.
     *
     * @return A String.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Set code.
     *
     * @param code
     *		A String.
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * Set message.
     *
     * @param message
     *		A String.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Set resourceId.
     *
     * @param resourceId
     *		A String.
     */
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }

    /**
     * Set resource.
     *
     * @param resource
     *		A String.
     */
    public void setResource(final String resource) {
        this.resource = resource;
    }
}

/*
 * Created On:  20-Jun-07 8:47:16 AM
 */
package com.thinkparity.amazon.s3.service.client.rest;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RestResponse<T extends Object> {

    /** The parse result. */
    private T result;

    /**
     * Create RestResponse.
     *
     */
    public RestResponse() {
        super();
    }

    /**
     * Obtain result.
     *
     * @return A T.
     */
    public T getResult() {
        return result;
    }

    /**
     * Set result.
     *
     * @param result
     *		A T.
     */
    public void setResult(T result) {
        this.result = result;
    }
}

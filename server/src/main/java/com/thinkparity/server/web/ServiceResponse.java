/*
 * Created On:  6-Jun-07 10:14:04 AM
 */
package com.thinkparity.desdemona.web;

import com.thinkparity.desdemona.web.service.Result;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceResponse {

    /** A service error message. */
    private String errorMessage;

    /** A stack trace of a service error. */
    private StackTraceElement[] errorStackTrace;

    /** A return response. */
    private Result result;

    /**
     * Create ServiceResponse.
     *
     */
    public ServiceResponse() {
        super();
    }

    /**
     * Obtain errorMessage.
     *
     * @return A String.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public Boolean isSetErrorMessage() {
        return null != errorMessage;
    }

    /**
     * Obtain stackTrace.
     *
     * @return A StackTraceElement[].
     */
    public StackTraceElement[] getErrorStackTrace() {
        return errorStackTrace;
    }

    /**
     * Obtain result.
     *
     * @return A Object.
     */
    public Result getResult() {
        return result;
    }

    /**
     * Determine whether or not the service response is an error response.
     * 
     * @return True if the service response is an error.
     */
    public Boolean isErrorResponse() {
        return null != errorMessage || null != errorStackTrace;
    }

    /**
     * Determine if the result is set.
     * 
     * @return True if the result is set.
     */
    public Boolean isSetResult() {
        return null != result;
    }

    /**
     * Set errorMessage.
     *
     * @param errorMessage
     *		A String.
     */
    public void setError(final Throwable error) {
        this.errorMessage = error.getMessage();
        this.errorStackTrace = error.getStackTrace();
    }

    /**
     * Set result.
     *
     * @param result
     *		A Object.
     */
    public void setResult(final Result result) {
        this.result = result;
    }
}
